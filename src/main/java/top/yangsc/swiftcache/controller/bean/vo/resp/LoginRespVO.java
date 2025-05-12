package top.yangsc.swiftcache.controller.bean.vo.resp;

import lombok.Data;
import top.yangsc.swiftcache.base.pojo.Users;
import top.yangsc.swiftcache.controller.ParameterValidation.BaseVO;


@Data
public class LoginRespVO implements BaseVO {
    private Users user;
    private String token;
    private int role;
}
