package com.shoescms.domain.auth;

import com.google.gson.Gson;
import com.shoescms.common.exception.*;
import com.shoescms.common.model.response.CommonIdResult;
import com.shoescms.common.security.JwtTokenProvider;
import com.shoescms.domain.auth.dto.AuthChangePassDto;
import com.shoescms.domain.auth.dto.SignInReqDto;
import com.shoescms.domain.auth.dto.SignInResDto;
import com.shoescms.domain.auth.entity.AuthenticationEntity;
import com.shoescms.domain.auth.repository.AuthenticationRepository;
import com.shoescms.domain.user.entity.NguoiDungEntity;
import com.shoescms.domain.user.repository.INguoiDungRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {
    private final INguoiDungRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationRepository authenticationRepository;

    @Transactional
    public SignInResDto signIn(SignInReqDto reqDto) {
        System.out.println("password: "+ reqDto.getPassword());
        NguoiDungEntity nguoiDungEntity = userRepository.findByUserNameAndDel(reqDto.getUsername(), false).orElseThrow(() -> new SigninFailedException("ID/PW"));
        if (nguoiDungEntity.getApproved().equals(false))
            throw new SigninFailedException("Not approved.");

        if (nguoiDungEntity.getPassword() != null) {
            // check password
            if (!passwordEncoder.matches(reqDto.getPassword(), nguoiDungEntity.getPassword()))
                throw new SigninFailedException("ID/PW");

            // create token
            StringBuilder expire = new StringBuilder();
            String accessToken = jwtTokenProvider.createToken(String.valueOf(nguoiDungEntity.getId()), List.of(nguoiDungEntity.getRole().getRoleCd()), expire);
            StringBuilder refreshExpire = new StringBuilder();
            String refreshToken = jwtTokenProvider.createRefreshToken(String.valueOf(nguoiDungEntity.getId()), List.of(nguoiDungEntity.getRole().getRoleCd()), refreshExpire);

            // save authentication information
            authenticationRepository.save(AuthenticationEntity.builder()
                    .id(UUID.randomUUID().toString())
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .userId(nguoiDungEntity.getId())
                    .hasRevoked(false)
                    .build());

            return SignInResDto.builder()
                    .userName(nguoiDungEntity.getUsername())
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .accessExpireIn(expire.toString())
                    .refreshExpireIn(refreshExpire.toString())
                    .roles(List.of(nguoiDungEntity.getRole().getRoleCd()))
                    .setPassword(true)
                    .build();
        } else {
            // check tmpPassword
            if (!passwordEncoder.matches(reqDto.getPassword(), nguoiDungEntity.getTmpPassword()))
                throw new SigninFailedException("ID/PW");

            return SignInResDto.builder()
                    .userName(nguoiDungEntity.getUsername())
                    .roles(List.of(nguoiDungEntity.getRole().getRoleCd()))
                    .setPassword(false)
                    .build();
        }
    }

    @Transactional
    public void logout(Long id) {
        AuthenticationEntity authenticationEntity = authenticationRepository.findFirstByUserIdAndHasRevokedOrderByNgayTaoDesc(id, false).orElseThrow(() -> new ObjectNotFoundException(0));
//        authenticationEntity.setLogout(); // task: need update
    }

    @Transactional
    public SignInResDto refreshToken(Long id, String accessToken, String refreshToken){
        if (jwtTokenProvider.validateToken(refreshToken)) {
            if (jwtTokenProvider.getUserPk(refreshToken).equals(Long.toString(id))) {
                AuthenticationEntity authenticationEntity = authenticationRepository.findByRefreshToken(refreshToken).orElseThrow(IllegalArgumentException::new);
                NguoiDungEntity nguoiDungEntity = userRepository.findById(authenticationEntity.getUserId()).orElseThrow(UserNotFoundException::new);
                if (nguoiDungEntity.getId().equals(id) && authenticationEntity.getAccessToken().equals(accessToken)){
                    StringBuilder expire = new StringBuilder();
                    String newAccessToken = jwtTokenProvider.createToken(String.valueOf(nguoiDungEntity.getId()), List.of(nguoiDungEntity.getRole().getRoleCd()), expire);
                    StringBuilder refreshExpire = new StringBuilder();
                    String newRefreshToken = jwtTokenProvider.createRefreshToken(String.valueOf(nguoiDungEntity.getId()), List.of(nguoiDungEntity.getRole().getRoleCd()), refreshExpire);

                    // save authentication information
                    authenticationRepository.save(AuthenticationEntity.builder()
                            .id(UUID.randomUUID().toString())
                            .accessToken(newAccessToken)
                            .refreshToken(newRefreshToken)
                            .userId(nguoiDungEntity.getId())
                            .hasRevoked(false)
                            .build());

                    return SignInResDto.builder()
                            .userName(nguoiDungEntity.getUsername())
                            .accessToken(newAccessToken)
                            .refreshToken(newRefreshToken)
                            .accessExpireIn(expire.toString())
                            .refreshExpireIn(refreshExpire.toString())
                            .roles(List.of(nguoiDungEntity.getRole().getRoleCd()))
                            .build();
                }
            }
        }
        throw new IllegalArgumentException();
    }

    @Transactional
    public CommonIdResult resetPw(AuthChangePassDto dto) {
        // code check (id, time), base64 decoding and decript
        // {"id":1,"expire":"2021060101"}

        String decCode;
        try {
//            decCode = encryptProvider.decAES(dto.getCode());
        } catch (Exception e) {
            throw new InvalidRequstException();
        }

        Gson gson = new Gson();
//        AuthPasswordCodeDto codedto = gson.fromJson(decCode, AuthPasswordCodeDto.class);

        // check expire time
//        LocalDateTime dateTime = LocalDateTime.parse(codedto.getExpire(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//        if (LocalDateTime.now().isAfter(dateTime)) {
//            throw new AuthExpireException();
//        }

//        // check user
//        nguoiDungEntity nguoiDungEntity = userRepository.findById(codedto.getId()).orElseThrow(UserNotFoundException::new);
//        // encryption password and save password
//        nguoiDungEntity.setPassword(passwordEncoder.encode(dto.getNewPassword()));
//        return new CommonIdResult(nguoiDungEntity.getId());
        return null;
    }
}
