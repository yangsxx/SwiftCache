package top.yangsc.swiftcache.services;

import com.baomidou.mybatisplus.extension.service.IService;
import top.yangsc.swiftcache.base.ResultData;
import top.yangsc.swiftcache.base.pojo.ClipboardHistory;
import top.yangsc.swiftcache.controller.bean.vo.CreateClipboardVO;
import top.yangsc.swiftcache.controller.bean.vo.resp.ClipboardRespVO;

public interface ClipboardHistoryService extends IService<ClipboardHistory> {
    ResultData<String> createClipboard(CreateClipboardVO createClipboardVO);

    ResultData<ClipboardRespVO> getClipboard();
}
