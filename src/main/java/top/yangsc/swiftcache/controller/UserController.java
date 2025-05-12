package top.yangsc.swiftcache.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.yangsc.swiftcache.base.ResultData;
import top.yangsc.swiftcache.controller.bean.vo.LoginVO;
import top.yangsc.swiftcache.controller.bean.vo.RegisterVO;
import top.yangsc.swiftcache.controller.bean.vo.resp.LoginRespVO;
import top.yangsc.swiftcache.services.IUserService;


import javax.annotation.Resource;


@RestController
@RequestMapping("/user")
@Tag(name = "用户管理")
public class UserController {

    @Resource
    private IUserService userService;

    @PostMapping("/login")
    @Operation(summary = "用户登录")
    public ResultData<LoginRespVO> login(@RequestBody LoginVO loginVO){

        return userService.login(loginVO);
    }
    @PostMapping("/register")
    @Operation(summary = "用户注册")
    public ResultData register(@RequestBody RegisterVO registerVO){

        return userService.register(registerVO);
    }

}
