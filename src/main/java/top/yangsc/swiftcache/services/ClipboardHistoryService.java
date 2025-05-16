package top.yangsc.swiftcache.services;

import com.baomidou.mybatisplus.extension.service.IService;
import top.yangsc.swiftcache.base.ResultData;
import top.yangsc.swiftcache.base.pojo.ClipboardHistory;
import top.yangsc.swiftcache.config.PageResult;
import top.yangsc.swiftcache.controller.bean.vo.ClipboardPageVO;
import top.yangsc.swiftcache.controller.bean.vo.CreateClipboardVO;
import top.yangsc.swiftcache.controller.bean.vo.resp.ClipboardRespVO;

import java.util.List;

public interface ClipboardHistoryService extends IService<ClipboardHistory> {
    ResultData<String> createClipboard(CreateClipboardVO createClipboardVO);

    ResultData<PageResult<ClipboardRespVO>> getClipboard(ClipboardPageVO clipboardPageVO);
}
