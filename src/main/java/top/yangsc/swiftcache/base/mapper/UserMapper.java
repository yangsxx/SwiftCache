package top.yangsc.swiftcache.base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.yangsc.swiftcache.base.pojo.User;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author yang
 * @since 2023-06-19
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
