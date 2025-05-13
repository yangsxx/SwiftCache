package top.yangsc.swiftcache.controller.annotation;

import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Entity {
    /**
     *
     *
     * @author yang
     * @date 2025/5/13 10:08
     *
     * @return
     */
    Class<? extends Serializable> clz() ;
}

