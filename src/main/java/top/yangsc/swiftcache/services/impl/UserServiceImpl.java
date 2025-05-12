package top.yangsc.swiftcache.services.impl;

import cn.hutool.crypto.digest.MD5;
import cn.hutool.jwt.JWTUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.yangsc.swiftcache.base.ResultData;
import top.yangsc.swiftcache.base.field.RedisPreFix;
import top.yangsc.swiftcache.base.mapper.UserMapper;
import top.yangsc.swiftcache.base.pojo.User;
import top.yangsc.swiftcache.controller.bean.vo.LoginVO;
import top.yangsc.swiftcache.controller.bean.vo.RegisterVO;
import top.yangsc.swiftcache.controller.bean.vo.resp.LoginRespVO;
import top.yangsc.swiftcache.services.IUserService;
import top.yangsc.swiftcache.tools.RedisUtil;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author yang
 * @since 2023-06-19
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Resource
    private UserMapper userMapper;


    @Override

    public ResultData<LoginRespVO> login(LoginVO loginVO) {

        LambdaQueryWrapper<User> wrapper=new LambdaQueryWrapper<>();
        LambdaQueryWrapper<User> eq = wrapper.eq(User::getPhone, loginVO.getPhone())
                .eq(User::getUsed, 1);
        List<User> list = userMapper.selectList(eq);
        if (list.isEmpty()){
            throw new RuntimeException("登录失败");
        }
        User user=list.get(0);
        String token ="";
        if (MD5.create().digestHex(loginVO.getPasswd()).equals(user.getVoucher())){
            token = JWTUtil.createToken(new HashMap<>(),"1999".getBytes(StandardCharsets.UTF_8));
            RedisUtil.setValue(RedisPreFix.USERLONGIN+token,user);
        }else {
            throw new RuntimeException("登录失败");
        }
        LoginRespVO respVO=new LoginRespVO();
        user.setVoucher("");
        respVO.setUser(user);
        respVO.setToken(token);

        return ResultData.ok("登录", respVO);
    }

    // todo 暂时使用
    @Override
    public ResultData<String> register(RegisterVO registerVO) {
        User user=new User();
        user.setUserName(registerVO.getUserName());
        user.setPhone(registerVO.getPhone());
        user.setVoucher(MD5.create().digestHex(registerVO.getPassword()));
        userMapper.insert(user);
        return ResultData.ok("注册成功",null);
    }
}
