package com.shoescms.domain.user;

import com.shoescms.common.model.response.BaseResponse;
import com.shoescms.common.model.response.CommonBaseResult;
import com.shoescms.common.model.response.CommonIdResult;
import com.shoescms.common.model.response.CommonResult;
import com.shoescms.common.security.JwtTokenProvider;
import com.shoescms.domain.user.dto.*;
import com.shoescms.domain.user.entity.NguoiDungEntity;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Tag(name = "02. User")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1/user")
public class UserController {
    private final UserService userService;
    private final BaseResponse baseResponse;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping(value = "/sign-up")
    public CommonIdResult addUser(@Parameter(required = true, name = "reqDto", description = "사용자 등록 정보") @RequestBody @Valid UserReqDto reqDto) {
        return userService.addUser(reqDto);
    }

    @PatchMapping(value = "/{id}")
    public CommonResult<CommonIdResult> approval(@Parameter(required = true, name = "id", description = "아이디") @PathVariable Long id) {
        return baseResponse.getContentResult(userService.approval(id));
    }

    @PostMapping(value = "/add-new")
    public void addNewUser(@Parameter(required = true, name = "reqDto", description = "사용자 수정 정보") @RequestBody @Valid UserUpdateReqDto reqDto) {
        userService.addNewUser(reqDto);
    }
    @PutMapping(value = "/{id}")
    public void updateUser(@Parameter(required = true, name = "id", description = "아이디") @PathVariable Long id,
                                                   @Parameter(required = true, name = "reqDto", description = "사용자 수정 정보") @RequestBody @Valid UserUpdateReqDto reqDto) {
        userService.updateUser(id, reqDto);
    }

    @PutMapping(value = "/forgot-pass/{id}")
    public CommonResult<CommonIdResult> forgotPassword(@Parameter(required = true, name = "id", description = "아이디") @PathVariable Long id,
                                                   @Parameter(required = true, name = "reqDto", description = "사용자 수정 정보") @RequestBody @Valid UserForgot reqDto) {
        return baseResponse.getContentResult(userService.forgotPass(id, reqDto));
    }

    @DeleteMapping(value = "/{id}")
    public CommonResult<CommonIdResult> deleteUser(@Parameter(required = true, name = "id", description = "아이디") @PathVariable Long id) {
        return baseResponse.getContentResult(userService.deleteUser(id));
    }


    @GetMapping(value = "/find-id")
    public CommonResult<List<String>> findId(@Parameter(required = true, name = "name", description = "이름") @RequestParam String name,
                                             @Parameter(required = true, name = "phone", description = "연락처") @RequestParam String phone) {
        return baseResponse.getContentResult(userService.findId(name, phone));
    }

    @PostMapping(value = "/find-pw")
    public CommonBaseResult findPassword(@Parameter(required = true, name = "email", description = "이메일") @RequestParam String email) throws Exception {
        userService.findPassword(email);
        return baseResponse.getSuccessResult();
    }

    @PatchMapping(value = "/password")
    public CommonResult<CommonIdResult> changePassword(@RequestHeader(name ="x-api-token", required = false) String token,
                                                       @Parameter(required = true, name = "reqDto", description = "비밀번호 변경 요청 정보") @RequestBody @Valid ChangePasswordReqDto reqDto) {
        Long userId = Long.valueOf(jwtTokenProvider.getUserPk(token));
        return baseResponse.getContentResult(userService.changePassword(userId, reqDto));
    }

    @Deprecated
    @PatchMapping(value = "/resetPassword/{id}")
    public CommonResult<CommonIdResult> resetPassword(@Parameter(required = true, name = "id", description = "아이디") @PathVariable Long id) {
        return baseResponse.getContentResult(userService.resetPassword(id));
    }

    @GetMapping(value = "/list/staff")
    public Page<UserResDto> getStaffUserList(@RequestHeader(name ="x-api-token") String token,
                                             @Parameter(name = "userId", description = "사용자 아이디") @RequestParam(required = false) String userId,
                                             @Parameter(name = "name", description = "이름") @RequestParam(required = false) String name,
                                             @Parameter(name = "phone", description = "연락처") @RequestParam(required = false) String phone,
                                             @Parameter(name = "email", description = "이메일") @RequestParam(required = false) String email,
                                             @PageableDefault(sort="createDate", direction = Sort.Direction.DESC) @ParameterObject Pageable pageable) {
        Long loggedUserId = Long.valueOf(jwtTokenProvider.getUserPk(token));
        return userService.getStaffUserList(loggedUserId, userId, name, phone, email, pageable);
    }

    @GetMapping(value = "/list/user")
    public Page<UserResDto> getUserList(@RequestHeader(name ="x-api-token") String token,
                                        @Parameter(name = "userId", description = "사용자 아이디") @RequestParam(required = false) String userId,
                                            @Parameter(name = "name", description = "이름") @RequestParam(required = false) String name,
                                            @Parameter(name = "phone", description = "연락처") @RequestParam(required = false) String phone,
                                            @Parameter(name = "email", description = "이메일") @RequestParam(required = false) String email,
                                            @PageableDefault(sort="createDate", direction = Sort.Direction.DESC) @ParameterObject Pageable pageable) {
        Long loggedUserId = Long.valueOf(jwtTokenProvider.getUserPk(token));
        return userService.getStoreUserList(loggedUserId, userId, name, phone, email, pageable);
    }

    @GetMapping("/detail")
    public CommonResult<NguoiDungEntity> getAccountDetail(@RequestHeader(name ="x-api-token", required = false) String token) {
        Long userId = Long.valueOf(jwtTokenProvider.getUserPk(token));
        return baseResponse.getContentResult(userService.findById(userId));
    }
    @PutMapping("/update-profile")
    public CommonResult<CommonIdResult> getAccountDetail(@RequestHeader(name ="x-api-token", required = false) String token, @RequestBody UserUpdateReqDto dto) {
        Long userId = Long.valueOf(jwtTokenProvider.getUserPk(token));
        return baseResponse.getContentResult(userService.updateUser(userId, dto));
    }

    @GetMapping("/token")
    public CommonResult<String> getToken(@RequestParam(name ="token", required = false) String token) {
        Long userId = Long.valueOf(jwtTokenProvider.getUserPk(token));
        return baseResponse.getContentResult(userId.toString());
    }
}
