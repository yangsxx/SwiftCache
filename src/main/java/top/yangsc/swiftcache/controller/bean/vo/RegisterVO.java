package top.yangsc.swiftcache.controller.bean.vo;

import lombok.Data;
import top.yangsc.swiftcache.controller.annotation.Condition;
import top.yangsc.swiftcache.controller.annotation.NotNull;


@Data
public class RegisterVO {

    @NotNull
    @Condition(clz = String.class,unique = true, table = "user")
    private String userName;
    /**
     * 手机号
     */
    @NotNull
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
