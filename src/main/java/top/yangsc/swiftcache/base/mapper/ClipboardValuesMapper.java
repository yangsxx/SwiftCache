package top.yangsc.swiftcache.base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.yangsc.swiftcache.base.pojo.ClipboardValues;

@Mapper
public interface ClipboardValuesMapper extends BaseMapper<ClipboardValues> {
    Long insertOne(String content);
}
