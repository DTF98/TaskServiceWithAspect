package ru.DTF98.TaskServiceWithAspect.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class MainAspect {
    private static final Logger logger = LoggerFactory.getLogger(MainAspect.class);

    @Before("@annotation(ru.DTF98.TaskServiceWithAspect.aspect.annotations.LoggerAnnotationBefore)")
    public void beforeAdviceService(JoinPoint joinPoint) {
        logger.info("Before advice [Controller]: Method invoked: {}", joinPoint.getSignature().getName());
        logger.info("Arguments: {}", Arrays.toString(joinPoint.getArgs()));
    }

    @AfterReturning(value = "execution(* ru.DTF98.TaskServiceWithAspect.service.TaskService.updateTask(..))", returning = "result")
    public void afterUpdateReturningAdvice(JoinPoint joinPoint, Object result) {
        logger.info("AfterReturning advice: Method in TaskService completed successfully.");
        logger.info("Method executed: {}", joinPoint.getSignature().getName());
        logger.info("Return value: {}", result);
    }

    @AfterReturning(value = "execution(* ru.DTF98.TaskServiceWithAspect.service.TaskService.getTaskById(..))", returning = "result")
    public void afterGetByIdReturningAdvice(JoinPoint joinPoint, Object result) {
        logger.info("AfterReturning advice: Method in TaskService completed successfully.");
        logger.info("Method executed: {}", joinPoint.getSignature().getName());
        logger.info("Return value: {}", result);
    }

    @AfterReturning(value = "execution(* ru.DTF98.TaskServiceWithAspect.service.TaskService.getAllTasks(..))", returning = "result")
    public void afterGetReturningAdvice(JoinPoint joinPoint, Object result) {
        logger.info("AfterReturning advice: Method in TaskService completed successfully.");
        logger.info("Method executed: {}", joinPoint.getSignature().getName());
        logger.info("Return value: {}", result);
    }

    @AfterThrowing(value = "execution(* ru.DTF98.TaskServiceWithAspect.service.TaskService.*(..))", throwing = "exception")
    public void afterThrowingServiceAdvice(JoinPoint joinPoint, Exception exception) {
        logger.info("AfterThrowing advice: Exception occurred in TaskService. Exception: {} in method: {}",
                exception.getMessage(), joinPoint.getSignature().getName());
    }

    @Around("execution(* ru.DTF98.TaskServiceWithAspect.controller.TaskController.createTask(..))")
    public Object aroundAdvice(ProceedingJoinPoint joinPoint) {
        long startTime = System.currentTimeMillis();

        logger.info("Around advice: Method createTask in TaskController is called");
        logger.info("Executing: {}", joinPoint.getSignature().getName());

        Object result;
        try {
            result = joinPoint.proceed();
            logger.info("Around advice: Method createTask in TaskController executed successfully");
        } catch (Throwable throwable) {
           throw new RuntimeException("Exception in Main Aspect");
        }

        long finishTime = System.currentTimeMillis();
        logger.info("Execution time for method {}: {} ms", joinPoint.getSignature().getName(), finishTime - startTime);

        return result;
    }
}
