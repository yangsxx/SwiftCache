package top.yangsc.swiftcache.controller.ParameterValidation.AOP;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import top.yangsc.swiftcache.base.mapper.ExecutionLogMapper;
import top.yangsc.swiftcache.base.pojo.ExecutionLog;
import top.yangsc.swiftcache.config.ThreadLocalTools.CurrentContext;
import top.yangsc.swiftcache.config.ThreadLocalTools.sqlCount.SqlCountQuery;
import top.yangsc.swiftcache.config.ThreadLocalTools.sqlCount.SqlCountUpdate;
import top.yangsc.swiftcache.tools.ObjectUtil;
import top.yangsc.swiftcache.tools.SpringContextUtil;

import java.lang.reflect.Method;


@Aspect
@Component
public class ParamAspect {

    @Pointcut("execution(* top.yangsc.swiftcache.controller..*Controller.*(..))")
    public void paramPointCut(){

    }

    @Around("paramPointCut()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        CurrentContext.clearSqlCount();
        Object[] args = joinPoint.getArgs();
        // 获取方法签名
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        // 获取类名 方法名
        String declaringTypeName = signature.getDeclaringTypeName();
        String name = signature.getName();


        Object o = null;
        if (args.length>0){
                for (Object arg : args) {
                        Class voClass = null;
                        try {
                            voClass = getVOClass(arg);
                        }catch (Exception e){
                        }
                        if (voClass!=null){
                        Validator.doValidator(voClass,arg);
                    }
                }
            }

            o = joinPoint.proceed();
        Long endTime = System.currentTimeMillis();
        recordRuntime(endTime-startTime,declaringTypeName,name);
        return o;

    }
    private Class getVOClass(Object o) {
        return ObjectUtil.getClassByObject(o);
    }
    private void recordRuntime(Long time,String clazzName,String methodName) {
        ExecutionLogMapper bean = SpringContextUtil.getBean(ExecutionLogMapper.class);
        ExecutionLog executionLog = new ExecutionLog();
        executionLog.setClassName(clazzName);
        executionLog.setMethodName(methodName);
        executionLog.setExecutionTime(time);
        executionLog.setSqlQueryTime(CurrentContext.getSqlQueryCount());
        executionLog.setSqlUpdateTime(CurrentContext.getSqlUpdateCount());
        bean.insert(executionLog);

    }

}

