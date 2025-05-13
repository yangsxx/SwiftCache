package top.yangsc.swiftcache.services.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.yangsc.swiftcache.base.Exception.ParameterValidationException;
import top.yangsc.swiftcache.base.mapper.KeyTableMapper;
import top.yangsc.swiftcache.base.mapper.UserMapper;
import top.yangsc.swiftcache.base.mapper.ValueTableMapper;
import top.yangsc.swiftcache.base.pojo.KeyTable;
import top.yangsc.swiftcache.base.pojo.ValueTable;
import top.yangsc.swiftcache.controller.bean.vo.CreateKeyTableVO;
import top.yangsc.swiftcache.controller.bean.vo.PageBaseVO;
import top.yangsc.swiftcache.controller.bean.vo.resp.HistoryValue;
import top.yangsc.swiftcache.controller.bean.vo.resp.KeyTableRespVO;
import top.yangsc.swiftcache.services.KeyTableService;
import top.yangsc.swiftcache.tools.TimestampUtil;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class KeyTableServiceImpl extends ServiceImpl<KeyTableMapper, KeyTable> implements KeyTableService {

    @Resource
    private KeyTableMapper keyTableMapper;
    @Resource
    private ValueTableMapper valueTableMapper;

    @Resource
    private UserMapper userMapper;
    @Override
    public boolean createKeyTable(CreateKeyTableVO createKeyTableVO) {
        return false;
    }

    @Override
    public boolean updateKeyTable(CreateKeyTableVO createKeyTableVO) {
        return false;
    }

    @Override
    public KeyTableRespVO getValue(String id) {

        //通过id获取keyTable数据
        KeyTable keyTable = keyTableMapper.selectById(id);

        //通过keyid 获取valueTable数据,并通过版本号排序
        List<ValueTable> valueTables = valueTableMapper.selectList(
                new LambdaQueryWrapper<ValueTable>()
                        .eq(ValueTable::getKeyId, keyTable.getId())
                        .orderByDesc(ValueTable::getVersion)  // 先按版本号降序排序
        );

        // 直接获取第一个元素的版本号作为最高版本
        if(valueTables.isEmpty()) {
            throw new ParameterValidationException("id不存在");
        }
        Integer latestVersion = valueTables.get(0).getVersion();

        //根据版本号分组
        Map<Integer, List<ValueTable>> versionMap = valueTables.stream()
                .collect(Collectors.groupingBy(ValueTable::getVersion));

        //获取最新版本的数据
        List<ValueTable> valueTables1 = versionMap.get(latestVersion);
        //当前值
        String[] currentValues = new String[valueTables1.size()];
        //取值
        for (int i = 0; i < valueTables1.size(); i++) {
            currentValues[i] = valueTables1.get(i).getValueData();
        }


        List<HistoryValue> historyValues = new ArrayList<>();
        HistoryValue historyValue ;
        for(int i = latestVersion ; i>=1 ; i--){
            List<ValueTable> valueTables2 = versionMap.get(i);
            String[] values = new String[valueTables2.size()];

            for (int j=0;j<=valueTables2.size();j++) {
                values[j] = valueTables2.get(j).getValueData();
            }
            historyValue = new HistoryValue();
            historyValue.setValues(values);
            historyValue.setCreateTime(TimestampUtil.format(valueTables2.get(0).getCreatedAt()));
            historyValue.setId(valueTables2.get(0).getId());
            historyValue.setVersion(valueTables2.get(0).getVersion());

            historyValues.add(historyValue);
        }



        KeyTableRespVO keyTableRespVO = new KeyTableRespVO();
        keyTableRespVO.setCurrentValues(currentValues);
        keyTableRespVO.setValues(historyValues);
        keyTableRespVO.setCreateTime(TimestampUtil.format(keyTable.getCreatedAt()));
        keyTableRespVO.setUpdateTime(TimestampUtil.format(keyTable.getUpdatedAt()));
        keyTableRespVO.setCreateBy(userMapper.selectById(keyTable.getId()).getUserName());
        keyTableRespVO.setId(keyTable.getId());
        keyTableRespVO.setPermission(keyTable.getPermissionLevel());
        keyTableRespVO.setKey(keyTable.getKeyName());

        return keyTableRespVO;
    }

    @Override
    public boolean delete(String id) {
        KeyTable keyTable = keyTableMapper.selectById(id);
        keyTable.setIsDeleted(true);
        keyTableMapper.updateById(keyTable);
        return true;
    }

    @Override
    public boolean updatePermission(String id, int permission) {
        KeyTable keyTable = keyTableMapper.selectById(id);
        if (keyTable != null) {
            keyTable.setPermissionLevel((short)permission);
            keyTableMapper.updateById(keyTable);
            return true;
        }
        else {
            throw new ParameterValidationException("id不存在");
        }

    }

    @Override
    public KeyTableRespVO getKeyTableByPage(PageBaseVO pageBaseVO) {
        return null;
    }
}
