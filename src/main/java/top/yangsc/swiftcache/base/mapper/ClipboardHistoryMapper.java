package top.yangsc.swiftcache.base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.yangsc.swiftcache.base.pojo.ClipboardHistory;
import top.yangsc.swiftcache.controller.bean.vo.ClipboardPageVO;
import top.yangsc.swiftcache.controller.bean.vo.resp.ClipboardRespVO;

@Mapper
public interface ClipboardHistoryMapper extends BaseMapper<ClipboardHistory> {
    ClipboardRespVO getClipboard(ClipboardPageVO clipboardPageVO);
}
