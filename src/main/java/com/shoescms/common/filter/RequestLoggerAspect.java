package com.shoescms.common.filter;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Component
@Aspect
@Slf4j
public class RequestLoggerAspect {
    @Pointcut("execution(* com.shoescms..*Controller.*(..))") // 이런 패턴이 실행될 경우 수행
    public void loggerPointCut() {
    }

    @Around("loggerPointCut()")
    public Object methodLogger(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest(); // request 정보를 가져온다.

            String controllerName = proceedingJoinPoint.getSignature().getDeclaringType().getSimpleName();
            String methodName = proceedingJoinPoint.getSignature().getName();

            Map<String, Object> params = new HashMap<>();

            try {
                params.put("controller", controllerName);
                params.put("method", methodName);
                params.put("params", getParams(request));
                params.put("log_time", new Date());
                params.put("request_uri", request.getRequestURI());
                params.put("http_method", request.getMethod());
            } catch (Exception e) {
                log.error("LoggerAspect error", e);
            }
            log.info("params : {}", params);

            return  proceedingJoinPoint.proceed();

        } catch (Throwable throwable) {
            throw throwable;
        }
    }

    /**
     * request 에 담긴 정보를 JSONObject 형태로 반환한다.
     * @param request
     * @return
     */
    private static JSONObject getParams(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        Enumeration<String> params = request.getParameterNames();
        while (params.hasMoreElements()) {
            String param = params.nextElement();
            String replaceParam = param.replaceAll("\\.", "-");
            if (replaceParam.equalsIgnoreCase("password"))
                jsonObject.put(replaceParam, "*****");
            else
                jsonObject.put(replaceParam, request.getParameter(param));
        }
        return jsonObject;
    }
}
