package com.apu.example.springsecurityjwt.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
//@Slf4j
public class LoggingAdvice {
    Logger log = LoggerFactory.getLogger(LoggingAdvice.class);

    //this is not workable--> deadlock might occurred and the embedded tomcat  could not be started
    /*@Pointcut(value = "execution(* com.apu.example.springsecurityjwt.*.*.*(..) )")//star first = any package, 2nd = class, 3rd = method
    public void myPointcut(){

    }*/

    //If we define the Controller then no deadlock and works fine
    @Pointcut(value = "execution(* com.apu.example.springsecurityjwt.controller.UserController.*(..) )")//star first = any package, 2nd = class, 3rd = method
    public void myPointcut(){

    }

    @Around("myPointcut()")
    public Object applicationLogger(ProceedingJoinPoint point) throws Throwable{
        ObjectMapper mapper = new ObjectMapper();

        String methodName = point.getSignature().getName();
        String className = point.getTarget().getClass().toString();
        Object[] array = point.getArgs();

        log.info("Method Invoked: "+className+ "-->"+methodName+"() arguments: "+mapper.writeValueAsString(array));

        Object object = point.proceed();

        log.info("Method end: "+className+ "-->"+methodName+"() Response: "+mapper.writeValueAsString(object));

        return object;
    }
}
