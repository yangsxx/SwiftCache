package top.yangsc.swiftcache.controller.bean.vo;

import lombok.Data;
import top.yangsc.swiftcache.controller.annotation.NotNull;


@Data
public class LoginVO  {

    @NotNull(value = true)
    private String phone;
    @NotNull(value = true)
    private String passwd;
}
