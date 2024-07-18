package com.shoescms.common.exception;

import com.shoescms.common.model.response.BaseResponse;
import com.shoescms.common.model.response.CommonBaseResult;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice(basePackages = "com.shoescms")
public class ExceptionAdvice {
    private final BaseResponse baseResponse;
    private final MessageSource messageSource;

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonBaseResult defaultException(HttpServletRequest request, Exception e) {
        log.error("[Internel Exception] ", e);
        return baseResponse.getFailResult(Integer.parseInt(getMessage("unKnown.code")), getMessage("unKnown.message"));
    }

    // arg validation exception
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected CommonBaseResult argInvalidException(HttpServletRequest request, MethodArgumentNotValidException e) {
        String message = getMessage("argValidationFailed.message");
        message = message + "(" + e.getBindingResult() +")";
        return baseResponse.getFailResult(Integer.parseInt(getMessage("argValidationFailed.code")), message);
    }

    // json parsing exception
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected CommonBaseResult invalidRequestException(HttpServletRequest request, HttpMessageNotReadableException e) {
        String message = getMessage("argValidationFailed.message");
        if (e.getMessage() != null) {
            message = message + "(" + e.getMessage() +")";
        }
        return baseResponse.getFailResult(Integer.parseInt(getMessage("argValidationFailed.code")), message);
    }

    @ExceptionHandler(AuthExpireException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    protected CommonBaseResult authExpire(HttpServletRequest request, AuthExpireException e) {
        return baseResponse.getFailResult(Integer.parseInt(getMessage("authExpire.code")), getMessage("authExpire.message"));
    }

    @ExceptionHandler(InvalidRequstException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected CommonBaseResult invalidRequest(HttpServletRequest request, InvalidRequstException e) {
        String message = getMessage("invalidRequest.message");
        if (e.getMessage() != null) {
            message = message + "(" + e.getMessage() +")";
        }
        return baseResponse.getFailResult(Integer.parseInt(getMessage("invalidRequest.code")), message);
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected CommonBaseResult userNotFountException(HttpServletRequest request, Exception e) {
        String message = getMessage("userNotFound.message");
        if (e.getMessage() != null) {
            message = message + "(" + e.getMessage() +")";
        }
        return baseResponse.getFailResult(Integer.parseInt(getMessage("userNotFound.code")), message);
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected CommonBaseResult objectNotFoundSuccessException(HttpServletRequest request, ObjectNotFoundException e) {
        String message = getMessage("validate.code" + e.getCode());
        if (e.getMessage() != null)
            message = message + "(" + e.getMessage() +")";
        return baseResponse.getFailResult(e.getCode(), message);
    }

    @ExceptionHandler(SigninFailedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    protected CommonBaseResult signInFailed(HttpServletRequest request, SigninFailedException e) {
        String message = getMessage("signinFailed.message");
        if (e.getMessage() != null) {
            message = message + "(" + e.getMessage() +")";
        }
        return baseResponse.getFailResult(Integer.parseInt(getMessage("signinFailed.code")), message);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public CommonBaseResult authAccessDeniedException(HttpServletRequest request, AccessDeniedException e) {
        return baseResponse.getFailResult(Integer.parseInt(getMessage("accessDenied.code")), getMessage("accessDenied.message"));
    }

    @ExceptionHandler(AuthAccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public CommonBaseResult authAccessDeniedException(HttpServletRequest request, AuthAccessDeniedException e) {
        return baseResponse.getFailResult(Integer.parseInt(getMessage("accessDenied.code")), getMessage("accessDenied.message"));
    }

    @ExceptionHandler(AuthEntryPointException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public CommonBaseResult authEntryPointException(HttpServletRequest request, AuthEntryPointException e) {
        return baseResponse.getFailResult(Integer.parseInt(getMessage("entryPointException.code")), getMessage("entryPointException.message"));
    }

    @ExceptionHandler(AuthTokenExpiredException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public CommonBaseResult authTokenExpiredException(HttpServletRequest request, AuthTokenExpiredException e) {
        return baseResponse.getFailResult(Integer.parseInt(getMessage("tokenExpired.code")), getMessage("tokenExpired.message"));
    }

    @ExceptionHandler(AuthFailedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    protected CommonBaseResult valifyFailedException(HttpServletRequest request, Exception e) {
        log.error("[AuthFailedException Exception] " + e);
        return baseResponse.getFailResult(Integer.parseInt(getMessage("authfailed.code")), getMessage("authfailed.message"));
    }

    @ExceptionHandler(ProcessFailedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonBaseResult processFailedException(HttpServletRequest request, ProcessFailedException e) {
        String message = getMessage("processFailed.message");
        if (e.getMessage() != null) {
            message = message + "(" + e.getMessage() +")";
        }
        return baseResponse.getFailResult(Integer.parseInt(getMessage("processFailed.code")), message);
    }

    @ExceptionHandler(ObjectAlreadExistException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected CommonBaseResult objectAlreadExistException(HttpServletRequest request, Exception e) {
        String message = getMessage("objectAlreadyExist.message");
        if (e.getMessage() != null) {
            message = message + "(" + e.getMessage() +")";
        }
        return baseResponse.getFailResult(Integer.parseInt(getMessage("objectAlreadyExist.code")), message);
    }

    // code정보에 해당하는 메시지를 조회
    private String getMessage(String code) {
        return getMessage(code, null);
    }

    // code정보, 추가 argument로 현재 locale에 맞는 메시지를 조회
    private String getMessage(String code, Object[] args) {
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }
}
