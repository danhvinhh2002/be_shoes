package com.shoescms.domain.user;

import com.google.gson.Gson;
import com.shoescms.common.config.CommonConfig;
import com.shoescms.common.enums.RoleEnum;
import com.shoescms.common.exception.*;
import com.shoescms.common.model.response.CommonIdResult;
import com.shoescms.common.security.JwtTokenProvider;
import com.shoescms.common.service.MailService;
import com.shoescms.domain.auth.entity.VaiTroEntity;
import com.shoescms.domain.auth.repository.IVaiTroRepository;
import com.shoescms.domain.user.dto.*;
import com.shoescms.domain.user.entity.NguoiDungEntity;
import com.shoescms.domain.user.repository.INguoiDungRepository;
import com.shoescms.domain.user.repository.UserQueryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
@Transactional
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final INguoiDungRepository userRepository;
    private final UserQueryRepository userQueryRepository;
    private final IVaiTroRepository roleRepository;
    private final CommonConfig config;
    private final MailService mailService;

    private final JwtTokenProvider jwtTokenProvider;

    public UserService(PasswordEncoder passwordEncoder, INguoiDungRepository userRepository, UserQueryRepository userQueryRepository, IVaiTroRepository roleRepository, CommonConfig config, MailService mailService, JwtTokenProvider jwtTokenProvider) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.userQueryRepository = userQueryRepository;
        this.roleRepository = roleRepository;
        this.config = config;
        this.mailService = mailService;
        this.jwtTokenProvider = jwtTokenProvider;
        initRole();
        NguoiDungEntity adminUser =        this.userRepository.findByUserNameAndDel("admin", false).orElse(
         NguoiDungEntity.builder()
                 .userName("admin")
                 .password(this.passwordEncoder.encode("123456"))
                 .name("admin")
                 .approved(true)
                 .email("admin@email.com")
                 .phone("0958572838")
                 .role(this.roleRepository.findByRoleCd(RoleEnum.ROLE_ADMIN.getTitle()))
                 .del(false)
                 .build()
 );
 this.userRepository.saveAndFlush(adminUser);
    }

//    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository, UserQueryRepository userQueryRepository, RoleRepository roleRepository, CommonConfig config, MailService mailService) {
//        this.passwordEncoder = passwordEncoder;
//        this.userRepository = userRepository;
//        this.userQueryRepository = userQueryRepository;
//        this.roleRepository = roleRepository;
//        this.config = config;
//        this.mailService = mailService;
//        initRole();
// nguoiDungEntity adminUser =        this.userRepository.findByUserNameAndDel("admin", false).orElse(
//         nguoiDungEntity.builder()
//                 .userName("admin")
//                 .password(this.passwordEncoder.encode("123456"))
//                 .name("admin")
//                 .approved(true)
//                 .email("admin@email.com")
//                 .phone("0958572838")
//                 .role(this.roleRepository.findByRoleCd(RoleEnum.ROLE_ADMIN.getTitle()))
//                 .del(false)
//                 .build()
// );
// this.userRepository.saveAndFlush(adminUser);
//    }

    private void initRole(){
        this.roleRepository.saveAndFlush(VaiTroEntity.builder()
                        .id(RoleEnum.ROLE_ADMIN.getId())
                        .roleCd(RoleEnum.ROLE_ADMIN.getTitle())
                .build());
        this.roleRepository.saveAndFlush(VaiTroEntity.builder()
                .id(RoleEnum.ROLE_STAFF.getId())
                .roleCd(RoleEnum.ROLE_STAFF.getTitle())
                .build());
        this.roleRepository.saveAndFlush(VaiTroEntity.builder()
                .id(RoleEnum.ROLE_USER.getId())
                .roleCd(RoleEnum.ROLE_USER.getTitle())
                .build());
    }
    @Transactional
    public CommonIdResult addUser(UserReqDto reqDto) {
        NguoiDungEntity nguoiDungCheck = null;
        if (userRepository.findByUserNameAndDel(reqDto.getUserName(), false).isPresent())
            throw new ObjectNotFoundException(21);

        if(userRepository.findByEmailAndDel(reqDto.getEmail(), false).isPresent())
            throw new ObjectNotFoundException(22);


        if(userRepository.findByPhoneAndDel(reqDto.getPhone(), false).isPresent())
            throw new ObjectNotFoundException(23);

        NguoiDungEntity nguoiDungEntity = reqDto.toEntity();
        System.out.println("pass = " + reqDto.getPassword());
        nguoiDungEntity.setPassword(passwordEncoder.encode(reqDto.getPassword()));

        nguoiDungEntity.setApproved(true);

        nguoiDungEntity.setRole(this.roleRepository.findByRoleCd(reqDto.getRole().getTitle()));

        return new CommonIdResult(userRepository.save(nguoiDungEntity).getId());
    }

    @Transactional
    public CommonIdResult approval(Long id) {
        NguoiDungEntity nguoiDungEntity = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        nguoiDungEntity.setApproved(true);
        return new CommonIdResult(nguoiDungEntity.getId());
    }

    @Transactional
    public CommonIdResult updateUser(Long id, UserUpdateReqDto reqDto) {
        NguoiDungEntity nguoiDungEntity = userRepository.findById(id).orElseThrow(UserNotFoundException::new);

        NguoiDungEntity check = null;
        if(!ObjectUtils.isEmpty(reqDto.getEmail())) {
            check = userRepository.findByEmailAndDel(reqDto.getEmail(), false).orElse(null);
            if (check != null)
                if (!check.getId().equals(nguoiDungEntity.getId()))
                    throw new ObjectNotFoundException(22);
        }

        check  = userRepository.findByPhoneAndDel(reqDto.getPhone(), false).orElse(null);
        if(check != null)
            if(!check.getId().equals(nguoiDungEntity.getId()))
                throw new ObjectNotFoundException(23);
        nguoiDungEntity.update(reqDto);

        if(reqDto.getPassword() != null)
            nguoiDungEntity.setPassword(passwordEncoder.encode(reqDto.getPassword()));
        userRepository.saveAndFlush(nguoiDungEntity);
        return new CommonIdResult(nguoiDungEntity.getId());
    }
    @Transactional
    public CommonIdResult forgotPass(Long id, UserForgot reqDto) {
        NguoiDungEntity nguoiDungEntity = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        nguoiDungEntity.setPassword(passwordEncoder.encode(reqDto.getPassword()));
        nguoiDungEntity.update(nguoiDungEntity);
        userRepository.saveAndFlush(nguoiDungEntity);
        return new CommonIdResult(nguoiDungEntity.getId());
    }

    @Transactional
    public CommonIdResult deleteUser(Long id) {
        NguoiDungEntity nguoiDungEntity = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        nguoiDungEntity.setDel();
        userRepository.saveAndFlush(nguoiDungEntity);
        return new CommonIdResult(id);
    }

    @Transactional(readOnly = true)
    public UserDetailResDto getDetail(Long id) {
        return userQueryRepository.getDetail(id).orElseThrow(UserNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public List<String> findId(String name, String phone) {
        return userRepository.findByNameAndPhone(name, phone).stream().map(NguoiDungEntity::getUsername).toList();
    }
    @Transactional(readOnly = true)
    public void findPassword(String email) throws Exception {
        NguoiDungEntity nguoiDungEntity = userRepository.findByEmail(email);
        if(nguoiDungEntity == null)
            throw new ObjectNotFoundException(20);
        // make expire time
        LocalDateTime currentTime = LocalDateTime.now().plusDays(1L);
        String expire = currentTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Gson gson = new Gson();
        UserPasswordCodeDto dto = new UserPasswordCodeDto();
        dto.setId(nguoiDungEntity.getId());
        dto.setExpire(expire);
        String param = gson.toJson(dto);


        // encrypt info
        StringBuilder expired = new StringBuilder();
        String token = jwtTokenProvider.createToken(String.valueOf(nguoiDungEntity.getId()), List.of(nguoiDungEntity.getRole().getRoleCd()), expired);
        ClassPathResource resource = new ClassPathResource("templates/html/mail-body.html");
        if (resource.exists()) {
            try {
                log.info("start send mail");
                InputStreamReader inputStreamReader =  new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);
                Stream<String> streamOfString= new BufferedReader(inputStreamReader).lines();
                String content = streamOfString.collect(Collectors.joining());
                content = content.replace("__enc_param", email)
                        .replace("__enc_token", token)
                        .replace("__password_change_url", config.getForgotPass());

                log.info(content);
                if (!mailService.sendMail(nguoiDungEntity.getEmail(), "Quên mật khẩu", content))
                    throw new AuthFailedException();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            throw new ObjectNotFoundException("mail-body.html");
        }
    }

    @Transactional(readOnly = true)
    public void findPassword(String userName, String name, String email) throws Exception {
        NguoiDungEntity nguoiDungEntity = userRepository.findByUserNameAndNameAndEmail(userName, name, email).orElseThrow(UserNotFoundException::new);

        // make expire time
        LocalDateTime currentTime = LocalDateTime.now().plusDays(1L);
        String expire = currentTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Gson gson = new Gson();
        UserPasswordCodeDto dto = new UserPasswordCodeDto();
        dto.setId(nguoiDungEntity.getId());
        dto.setExpire(expire);
        String param = gson.toJson(dto);

        // encrypt info
        String encParam = "www";
        ClassPathResource resource = new ClassPathResource("templates/html/mail-body.html");
        if (resource.exists()) {
            try {
                log.info("start send mail");
                InputStreamReader inputStreamReader =  new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);
                Stream<String> streamOfString= new BufferedReader(inputStreamReader).lines();
                String content = streamOfString.collect(Collectors.joining());
                content = content.replaceAll("__enc_param", encParam);
                content = content.replaceAll("__password_change_url", config.getVnpayRedirectURl());
                log.info(content);
                if (!mailService.sendMail(nguoiDungEntity.getEmail(), "포토이즘 비밀번호 변경", content))
                    throw new AuthFailedException();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            throw new ObjectNotFoundException("mail-body.html");
        }
    }

    @Transactional
    public CommonIdResult changePassword(Long id, ChangePasswordReqDto reqDto) {
        NguoiDungEntity nguoiDungEntity = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        String oldPassword = nguoiDungEntity.getPassword() != null ? nguoiDungEntity.getPassword() : nguoiDungEntity.getTmpPassword();
        if (!passwordEncoder.matches(reqDto.getOldPassword(), oldPassword)) {
            throw new SigninFailedException("ID/PW");
        }

        nguoiDungEntity.setPassword(passwordEncoder.encode(reqDto.getNewPassword()));
        nguoiDungEntity.setTmpPassword(null);
        return new CommonIdResult(nguoiDungEntity.getId());
    }

    @Transactional
    public CommonIdResult resetPassword(Long id) {
        NguoiDungEntity nguoiDungEntity = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        nguoiDungEntity.setPassword(null);
        nguoiDungEntity.setTmpPassword(passwordEncoder.encode(nguoiDungEntity.getUsername()));
        return new CommonIdResult(nguoiDungEntity.getId());
    }

    @Transactional(readOnly = true)
    public Page<UserResDto> getStaffUserList(Long loggedUserId, String userId, String name,  String phone, String email,  Pageable pageable) {
        return userQueryRepository.findStaffUserList(loggedUserId, userId, name, phone, email, pageable);
    }

    @Transactional(readOnly = true)
    public Page<UserResDto> getStoreUserList(Long loggedUserId, String userId, String name, String phone, String email, Pageable pageable) {
        return userQueryRepository.findStoreUserList(loggedUserId, userId, name, phone, email, pageable);
    }

    public void addNewUser(UserUpdateReqDto reqDto) {
        if (userRepository.findByUserNameAndDel(reqDto.getUserId(), false).isPresent())
            throw new ObjectNotFoundException(21);

        if(!ObjectUtils.isEmpty(reqDto.getEmail()))
          if(userRepository.findByEmailAndDel(reqDto.getEmail(), false).isPresent())
            throw new ObjectNotFoundException(22);


        if(userRepository.findByPhoneAndDel(reqDto.getPhone(), false).isPresent())
            throw new ObjectNotFoundException(23);
        NguoiDungEntity nguoiDungEntity = NguoiDungEntity
                .builder()
                .userName(reqDto.getUserId())
                .name(reqDto.getName())
                .password(passwordEncoder.encode(reqDto.getPassword()))
                .phone(reqDto.getPhone())
                .email(reqDto.getEmail())
                .sex(reqDto.getSex())
                .birthDate(reqDto.getBirthDate())
                .approved(true)
                .del(false)
                .build();
        nguoiDungEntity.setRole(this.roleRepository.findByRoleCd(reqDto.getRole().getTitle()));
        userRepository.saveAndFlush(nguoiDungEntity);
    }

    @Transactional(readOnly = true)
    public NguoiDungEntity findById(Long id) {
        return userRepository.findById(id).orElseGet(null);
    }
}
