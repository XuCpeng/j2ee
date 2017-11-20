package cn.medemede.j2ee.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Aop事务
 */
@Aspect
@Component
public class HttpAspect {

    private final static Logger logger = LoggerFactory.getLogger(HttpAspect.class);

    @Pointcut("execution(public * cn.medemede.j2ee.controller.*.*(..))")
    public void log() {
        logger.info("-->切入");
    }

    @Before("log()")
    public void doBefore(JoinPoint joinPoint) {

        logger.info("-->事务执行前");
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        //url
        logger.info("url={}", request.getRequestURL());

        //method
        logger.info("method={}", request.getMethod());

        //ip
        logger.info("ip={}", request.getRemoteAddr());

        //类方法
        logger.info("class_method={}", joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName() + "()");

        //参数
        logger.info("args={}", joinPoint.getArgs());

    }

    @After("log()")
    public void doAfter() {
        logger.info("-->事务执行后");
    }


    //获取服务端的响应数据
    @AfterReturning(returning = "o", pointcut = "log()")
    public void doAfterReturnint(Object o) {
        if (o!=null)
        logger.info("response={}", o.toString());
    }

}
