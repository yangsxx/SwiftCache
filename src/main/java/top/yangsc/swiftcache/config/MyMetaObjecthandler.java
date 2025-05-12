package top.yangsc.swiftcache.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;
import top.yangsc.swiftcache.base.mapper.CommonMapper;
import top.yangsc.swiftcache.tools.SpringContextUtil;


import java.sql.Timestamp;
import java.util.Date;

/**
 * 自定义元数据对象处理器
 */
@Component
@Slf4j
public class MyMetaObjecthandler implements MetaObjectHandler {


    /**
     * 插入操作，自动填充
     *
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        CommonMapper mapper = SpringContextUtil.getBean(CommonMapper.class);
        log.info("公共字段自动填充[insert]...");
        log.info(metaObject.toString());

        // 只填充存在的字段
        if (metaObject.hasGetter("createdAt")) {
            metaObject.setValue("createdAt", new Timestamp(System.currentTimeMillis()));
        }
        if (metaObject.hasGetter("id")) {
            metaObject.setValue("id", mapper.getId());
        }
        if (metaObject.hasGetter("userId")) {
                metaObject.setValue("userId", getUserId());
        }

    }

    /**
     * 更新操作，自动填充
     *
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("公共字段自动填充[update]...");
        log.info(metaObject.toString());

        // 只填充存在的字段
        if (metaObject.hasGetter("updatedAt")) {
            metaObject.setValue("updatedAt", new Timestamp(System.currentTimeMillis()));
        }
        if (metaObject.hasGetter("updatedBy")) {
            metaObject.setValue("updatedBy", getUserId());
        }
    }

    private Long  getUserId(){
        return CurrentContext.getCurrentUser().getId();
    }


}
