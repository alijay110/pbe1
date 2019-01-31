package com.pearson.sam.bridgeapi.config;

import com.pearson.sam.bridgeapi.security.ISessionFacade;
import com.pearson.sam.bridgeapi.util.Utils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.REQUEST_ID;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.CURRENT_QUERY;

@Aspect
public class MethodExecutionCalculationAspect {

  private Logger logger = LoggerFactory.getLogger(this.getClass());

  @Around("execution(* com.pearson.sam.bridgeapi.samservices.*.*(..)) "
      + "||execution(* com.pearson.sam.bridgeapi.samintegeration.*.*(..)) "
      + "||execution(* com.pearson.sam.bridgeapi.samintegeration.impl.*.*(..))"
      + "||execution(* com.pearson.sam.bridgeapi.reposervice.*.*(..)) "
      + "||execution(* com.pearson.sam.bridgeapi.reposervice.*.*(..)) "
      + "||within(com.pearson.sam.bridgeapi.repository.*+)")
  public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
    long startTime = System.currentTimeMillis();
    Object ret = joinPoint.proceed();
    long timeTaken = System.currentTimeMillis() - startTime;
    logger.info("Time Taken by {} with requestId {} for query {} in method {} is {}",
        null, null,
        null, joinPoint.getSignature().getName(), timeTaken);
    return ret;
  }
}