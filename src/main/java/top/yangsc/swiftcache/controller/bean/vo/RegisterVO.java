package top.yangsc.swiftcache.controller.bean.vo;

import lombok.Data;
import top.yangsc.swiftcache.controller.annotation.Condition;
import top.yangsc.swiftcache.controller.annotation.NotNull;
import top.yangsc.swiftcache.controller.annotation.RegexValidator;


@Data
public class RegisterVO {

    @NotNull
    @Condition(clz = String.class,unique = true, table = "users")
    private String userName;
    /**
     * 手机号
     */
    @NotNull
    @Condition(clz = String.class,unique = true, table = "users")
    @RegexValidator(regex = "^1[3-9]\\d{9}$")
    private String phone;

    @NotNull
    private String password;

    @NotNull
    private String email;

    /**
     * 用户头像
     */
    private String pic;
}
