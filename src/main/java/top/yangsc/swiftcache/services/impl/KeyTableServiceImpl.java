package top.yangsc.swiftcache.services.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.yangsc.swiftcache.base.mapper.KeyTableMapper;
import top.yangsc.swiftcache.base.pojo.KeyTable;
import top.yangsc.swiftcache.services.KeyTableService;

@Service
public class KeyTableServiceImpl extends ServiceImpl<KeyTableMapper, KeyTable> implements KeyTableService {
}
