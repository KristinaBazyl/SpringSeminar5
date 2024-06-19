package ru.gb.springdemo.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Aspect
@Component
public class ExceptionHandlingAspect {
    @Around("@annotation(recoverException)")
    public Object handleRecoverException(ProceedingJoinPoint joinPoint, RecoverException recoverException) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (RuntimeException ex) {
            for (Class<? extends RuntimeException> nonRecoverable : recoverException.noRecoverFor()) {
                if (nonRecoverable.isInstance(ex)) {
                    throw ex;
                }
            }
            return getDefaultReturnValue(joinPoint.getSignature().getClass());
        }
    }

    public Object getDefaultReturnValue(Class<?> returnType) {
        if (returnType.isPrimitive()) {
            if (returnType == boolean.class)
                return false;
            if (returnType == char.class)
                return '\u0000';
            if (returnType == byte.class || returnType == short.class || returnType == int.class || returnType == long.class)
                 return 0;
            if (returnType == float.class)
                return 0f;
            if (returnType == double.class)
                return 0d;
        }
        return null;
    }
}
