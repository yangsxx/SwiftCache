package top.yangsc.swiftcache.services.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.yangsc.swiftcache.base.mapper.ClipboardHistoryMapper;
import top.yangsc.swiftcache.base.pojo.ClipboardHistory;
import top.yangsc.swiftcache.services.ClipboardHistoryService;

@Service
public class ClipboardHistoryServiceImpl extends ServiceImpl<ClipboardHistoryMapper, ClipboardHistory> implements ClipboardHistoryService {
}
