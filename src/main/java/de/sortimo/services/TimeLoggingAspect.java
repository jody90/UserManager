package de.sortimo.services;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class TimeLoggingAspect {

	private static final Logger LOGGER = LoggerFactory.getLogger("\033[33mTimeLoggingAspect\033[39m");
	
	@Pointcut("@annotation(Timelog)")
	public void timelogPointcut() {}

	@Around("timelogPointcut()")
	public Object TimeLoggi(ProceedingJoinPoint point) throws Throwable {
		
		long start = System.currentTimeMillis();
		Object result = null;
		
		try {
			LOGGER.info(
					"OnEntry --> Klasse: \33[96m{}\033[39m; Methode: \33[96m{}\033[39m",
					point.getSignature().getDeclaringType().getName(),
					MethodSignature.class.cast(point.getSignature()).getMethod().getName());
		
			result = point.proceed();
		} finally {

			LOGGER.info(
				"OnExit <-- Klasse: \33[96m{}\033[39m; Methode: \33[96m{}\033[39m; Dauer: \033[96m{} ms\033[39m",
				point.getSignature().getDeclaringType().getName(),
				MethodSignature.class.cast(point.getSignature()).getMethod().getName(),
				System.currentTimeMillis() - start
			);	
		}

		return result;
	}
	
}
