package top.yangsc.swiftcache.services;

import com.baomidou.mybatisplus.extension.service.IService;
import top.yangsc.swiftcache.base.ResultData;
import top.yangsc.swiftcache.base.pojo.Users;
import top.yangsc.swiftcache.controller.bean.vo.LoginVO;
import top.yangsc.swiftcache.controller.bean.vo.RegisterVO;
import top.yangsc.swiftcache.controller.bean.vo.resp.LoginRespVO;


/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author yang
 * @since 2023-06-19
 */
public interface IUserService extends IService<Users> {

    ResultData<LoginRespVO> login(LoginVO loginVO);

    ResultData register(RegisterVO registerVO);
}
