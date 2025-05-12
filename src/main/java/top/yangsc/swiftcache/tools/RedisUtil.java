package top.yangsc.swiftcache.tools;


import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

public class RedisUtil {
    public static RedisTemplate template;
    public static long defineExpireTime = 3600;

    static {
        RedisTemplate bean = SpringContextUtil.getBean("redisTemplate");
        template=bean;
    }

    public static <T extends Object> void setValue(String key,T data){
        setValue(key,data,defineExpireTime);
    }

    // 过期时间单位，分钟
    public static <T extends Object> void  setValue(String key,T data ,Long expireTime){
        ValueOperations ops = template.opsForValue();    // 首先redisTemplate.opsForValue的目的就是表明是以key，value形式储存到Redis数据库中数据的
        ops.set(key,data,expireTime, TimeUnit.MINUTES);
    }

    public static Object getValue(String key){
        ValueOperations ops = template.opsForValue();  // 表明取的是key，value型的数据
        return ops.get(key);
    }


}
