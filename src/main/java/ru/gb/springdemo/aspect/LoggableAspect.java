package ru.gb.springdemo.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.event.Level;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggableAspect {

    @Pointcut("@annotation(ru.gb.springdemo.aspect.Timer)")
    public void methodsAnnotatedWith() {

    }
    @Around("methodsAnnotatedWith()")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint)throws Throwable {

        Long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        Long endTime = System.currentTimeMillis();
        Long timeElapsed = endTime - startTime;
        log.info("{} - {} #({} mseconds)", joinPoint.getTarget().getClass().getSimpleName(), joinPoint.getSignature().getName(), timeElapsed / 1000.0);
        return result;
    }


}
