package com.apu.example.springsecurityjwt.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class ExecutionTimeTrackerAdvice {

    @Around("@annotation(com.apu.example.springsecurityjwt.aop.TrackExecutionTime)")
    public Object trackTime(ProceedingJoinPoint point) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object obj = point.proceed();
        long endTime = System.currentTimeMillis();
        log.info("Method name"+point.getSignature()+" time taken to execute: "+ (endTime-startTime));
        return obj;
    }
}
