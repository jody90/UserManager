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
public class LoggingAspect {

	private static final Logger LOG = LoggerFactory.getLogger(LoggingAspect.class);
	

	@Pointcut("@annotation(Loggable)")
	public void k() {}

	//    @Before("k()")
	//    public void loggi(JoinPoint joinPoint) {
	//    	System.out.println("Loggi Fired!!!!!!!!");
	//    }

	@Around("k()")
	public Object TimeLoggi(ProceedingJoinPoint point) {
		
		long start = System.currentTimeMillis();
		Object result = null;
		
		try {
			result = point.proceed();
		} 
		catch (Throwable e) {
			LOG.info("Loggi Around proceed fehlgeschlagen.");
		}
		
		LOG.info(
			"#%s(%s): %s in %[msec]s",
			MethodSignature.class.cast(point.getSignature()).getMethod().getName(),
			point.getArgs(),
			result,
			System.currentTimeMillis() - start
		);
		
		return result;
	}

}
