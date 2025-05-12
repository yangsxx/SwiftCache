package top.yangsc.swiftcache.services.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.yangsc.swiftcache.base.mapper.ClipboardValuesMapper;
import top.yangsc.swiftcache.base.pojo.ClipboardValues;
import top.yangsc.swiftcache.services.ClipboardValuesService;

@Service
public class ClipboardValuesServiceImpl extends ServiceImpl<ClipboardValuesMapper, ClipboardValues> implements ClipboardValuesService {
}
