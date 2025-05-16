package top.yangsc.swiftcache.base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.yangsc.swiftcache.base.pojo.KeyTable;
import top.yangsc.swiftcache.controller.bean.vo.KeyTablePageVO;
import top.yangsc.swiftcache.controller.bean.vo.PageBaseVO;
import top.yangsc.swiftcache.controller.bean.vo.resp.KeyTableRespVO;

import java.util.List;

@Mapper
public interface KeyTableMapper extends BaseMapper<KeyTable> {
    int findMaxVersion(Long id);

    List<KeyTable> selectByPage(PageBaseVO pageBaseVO);

    Long selectCountByPage(KeyTablePageVO pageBaseVO);
}
