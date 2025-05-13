package com.springlabs.aspect;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @Before("execution(* com.springlabs.controller..*(..))")
    public void logBefore(JoinPoint joinPoint) {
        log.info("Вызов метода: {}", joinPoint.getSignature().getName());
    }

    @After("execution(* com.springlabs.controller..*(..))")
    public void logAfter(JoinPoint joinPoint) {
        log.info("Метод завершен: {}", joinPoint.getSignature().getName());
    }

    @AfterThrowing(pointcut = "execution(* com.springlabs.controller..*(..))", throwing = "error")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable error) {
  log.error("Ошибка в методе: {}. Сообщение: {}", joinPoint.getSignature().getName(), error.getMessage());
    }
}