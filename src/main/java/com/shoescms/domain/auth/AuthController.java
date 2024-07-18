package com.shoescms.domain.auth;

import com.shoescms.common.exception.AuthAccessDeniedException;
import com.shoescms.common.exception.AuthEntryPointException;
import com.shoescms.common.exception.AuthTokenExpiredException;
import com.shoescms.common.model.response.BaseResponse;
import com.shoescms.common.model.response.CommonBaseResult;
import com.shoescms.common.model.response.CommonIdResult;
import com.shoescms.common.model.response.CommonResult;
import com.shoescms.domain.auth.dto.AuthChangePassDto;
import com.shoescms.domain.auth.dto.SignInReqDto;
import com.shoescms.domain.auth.dto.SignInResDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "01. Auth")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1")
public class AuthController {
    private final AuthService authService;
    private final BaseResponse baseResponse;

    @Operation(hidden = true)
    @RequestMapping(value = "/exception/entrypoint")
    public CommonBaseResult entryPointException(){
        throw new AuthEntryPointException();
    }

    @Operation(hidden = true)
    @RequestMapping(value = "/exception/accessdenied")
    public CommonBaseResult accessDeniedException(){
        throw new AuthAccessDeniedException();
    }

    @Operation(hidden = true)
    @RequestMapping(value = "/exception/tokenexpired")
    public CommonBaseResult tokenExpired(){
        throw new AuthTokenExpiredException();
    }

    @Operation(summary = "로그인", description = "로그인")
    @PostMapping(value = "/auth/sign-in")
    public SignInResDto signIn(@Parameter(required = true, name = "reqDto", description = "사용자 로그인 정보") @RequestBody @Valid SignInReqDto reqDto) {
        return authService.signIn(reqDto);
    }

    @Operation(summary = "로그아웃", description = "로그아웃")
    @PostMapping(value = "/auth/log-out/{id}")
    public CommonBaseResult postLogOut(@Parameter(required = true, name = "id", description = "사용자 아이디") @PathVariable Long id) {
        authService.logout(id);
        return baseResponse.getSuccessResult();
    }

    @Operation(summary = "토큰 갱신", description = "토큰 갱신")
    @PostMapping(value = "/auth/refresh/{id}")
    public CommonResult<SignInResDto> refresh(@Parameter(required = true, name = "id", description = "사용자 아이디") @PathVariable Long id,
                                              @RequestHeader(value = "x-api-token") String accessToken,
                                              @RequestHeader(value = "x-refresh-token") String refreshToken) {
        return baseResponse.getContentResult(authService.refreshToken(id, accessToken, refreshToken));
    }

    @Operation(summary = "패스워드 변경(메일)", description = "패스워드 변경(메일)")
    @PostMapping(value = "/auth/reset-pw")
    public CommonResult<CommonIdResult> resetPw(@Parameter(required = true, description = "param code") @RequestBody AuthChangePassDto dto) throws Exception{
        return baseResponse.getContentResult(authService.resetPw(dto));
    }
}
