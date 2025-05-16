package top.yangsc.swiftcache.base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.yangsc.swiftcache.base.pojo.ClipboardHistory;
import top.yangsc.swiftcache.controller.bean.vo.ClipboardPageVO;
import top.yangsc.swiftcache.controller.bean.vo.resp.ClipboardRespVO;

import java.util.List;

@Mapper
public interface ClipboardHistoryMapper extends BaseMapper<ClipboardHistory> {
    List<ClipboardRespVO> getClipboard(ClipboardPageVO clipboardPageVO);

}
