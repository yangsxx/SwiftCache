package top.yangsc.swiftcache.controller.bean.vo;

import lombok.Data;
import top.yangsc.swiftcache.controller.annotation.NotNull;
import top.yangsc.swiftcache.controller.annotation.RegexValidator;


@Data
public class LoginVO  {

    @NotNull(value = true)
    @RegexValidator(regex = "^1[3-9]\\d{9}$")
    private String phone;
    @NotNull(value = true)
    private String passwd;
}
