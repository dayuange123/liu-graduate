package com.ljy.graduate.aspect;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * Author: liuzhiyuan
 * Date: 2020/4/27
 * Description:
 */
@Slf4j
@Aspect
@Component
public class LogAop {
    // 定义切点Pointcut
    @Pointcut("within(com.ljy.graduate.controller.*)")
    public void excludeService() {
    }

    @Around("excludeService()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs();
        try {
            log.info("invoking interface: [{}] params==> {}",
                pjp.getSignature().getDeclaringTypeName() + "." + pjp.getSignature().getName(),
                JSON.toJSONString(args, SerializerFeature.IgnoreNonFieldGetter));
        } catch (Throwable e) {
            log.error(e.getMessage());
        }
        // result的值是被拦截方法的返回值
        return pjp.proceed();
    }

}
