package top.yangsc.swiftcache.controller.annotation;

import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 描述： 标记为实体类，属性上使用后将会在参数自动校验时，对下级类中属性继续进行校验.
 * @author yang
 * @date 2023/11/27 22:05
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Entity {
    Class<? extends Serializable> clz() ;
}

