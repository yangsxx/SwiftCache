package top.yangsc.swiftcache.controller.ParameterValidation.AOP;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import top.yangsc.swiftcache.tools.ObjectUtil;


@Aspect
@Component
public class ParamAspect {

    @Pointcut("execution(* top.yangsc.swiftcache.controller..*Controller.*(..))")
    public void paramPointCut(){

    }

    @Around("paramPointCut()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        String s="我是一个切片";
        Object[] args = joinPoint.getArgs();
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
        return o;

    }
    private Class getVOClass(Object o) {
        return ObjectUtil.getClassByObject(o,"/controller/bean/vo/*VO.class");
    }

}
