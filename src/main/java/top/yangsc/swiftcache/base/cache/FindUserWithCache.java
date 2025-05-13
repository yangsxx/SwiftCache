package top.yangsc.swiftcache.base.cache;

import top.yangsc.swiftcache.base.field.RedisPreFix;
import top.yangsc.swiftcache.base.mapper.UserMapper;
import top.yangsc.swiftcache.base.pojo.Users;
import top.yangsc.swiftcache.tools.RedisUtil;
import top.yangsc.swiftcache.tools.SpringContextUtil;

/**
 * 描述：top.yangsc.swiftcache.base.cache
 *
 * @author yang
 * @date 2025/5/13 18:08
 */

public class FindUserWithCache {

    public static Users findUserById(Long idLong) {
        String id = idLong.toString();
        if (!RedisUtil.hasKey(RedisPreFix.USERINFOKEY)
            || !RedisUtil.hasKey(RedisPreFix.USERISUPDATEKEY)
            || !RedisUtil.hasHashKey(RedisPreFix.USERISUPDATEKEY,id)
            || !RedisUtil.hasHashKey(RedisPreFix.USERINFOKEY,id)
            || (boolean)(RedisUtil.getHash(RedisPreFix.USERISUPDATEKEY,id))) {

            Users users = getUserById(idLong);
            RedisUtil.putHash(RedisPreFix.USERINFOKEY,id, users);
            RedisUtil.putHash(RedisPreFix.USERINFOKEY,id, false);
            return users;
        }
        else {
            return RedisUtil.getHash(RedisPreFix.USERINFOKEY, id);
        }

    }

    private static Users getUserById(Long id) {
        UserMapper userMapper = SpringContextUtil.getBean(UserMapper.class);
        Users users = userMapper.selectById(id);
        return users;
    }
}
