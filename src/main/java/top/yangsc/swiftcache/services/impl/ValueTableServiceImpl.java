package top.yangsc.swiftcache.services.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.yangsc.swiftcache.base.mapper.ValueTableMapper;
import top.yangsc.swiftcache.base.pojo.ValueTable;
import top.yangsc.swiftcache.services.ValueTableService;

@Service
public class ValueTableServiceImpl extends ServiceImpl<ValueTableMapper, ValueTable> implements ValueTableService {
}
