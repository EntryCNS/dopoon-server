package com.swcns.dopoonserver.global.utils.csrf;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.Arrays;

@Log4j2
@RequiredArgsConstructor
@Component
@Aspect
public class CsrfAspect {
    private final CsrfService csrfService;

    @Before("@annotation(ValidCsrf)")
    public void validCsrf(JoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Annotation[][] annotations = methodSignature.getMethod().getParameterAnnotations();

        for(int argIndex = 0; argIndex < annotations.length; argIndex++) {
            for(int i = 0; i < annotations[argIndex].length; i++) {
                if(annotations[argIndex][i].annotationType() == CsrfToken.class) {
                    String token = (String) joinPoint.getArgs()[argIndex];

                    if(!csrfService.isValidToken(token)) throw new InvalidCsrfException();
                    return;
                }
            }
        }
        throw new InvalidCsrfException();
    }

}
