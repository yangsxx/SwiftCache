package top.yangsc.swiftcache.services.impl;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.yangsc.swiftcache.base.Exception.ParameterValidationException;
import top.yangsc.swiftcache.base.Exception.PermissionException;
import top.yangsc.swiftcache.base.cache.FindUserWithCache;
import top.yangsc.swiftcache.base.mapper.KeyTableMapper;
import top.yangsc.swiftcache.base.mapper.ValueTableMapper;
import top.yangsc.swiftcache.base.pojo.KeyTable;
import top.yangsc.swiftcache.base.pojo.ValueTable;
import top.yangsc.swiftcache.config.CurrentContext;
import top.yangsc.swiftcache.controller.bean.vo.KeyTablePageVO;
import top.yangsc.swiftcache.controller.bean.vo.UpdateKeyValueVO;
import top.yangsc.swiftcache.controller.bean.vo.resp.ForKeyValue;
import top.yangsc.swiftcache.controller.bean.vo.resp.KeyTableRespVO;
import top.yangsc.swiftcache.services.KeyTableService;
import top.yangsc.swiftcache.tools.TimestampUtil;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class KeyTableServiceImpl extends ServiceImpl<KeyTableMapper, KeyTable> implements KeyTableService {

    @Resource
    private KeyTableMapper keyTableMapper;
    @Resource
    private ValueTableMapper valueTableMapper;

    @Override
    @Transactional
    public boolean createKeyTable(UpdateKeyValueVO createKeyTableVO) {
        KeyTable  keyTable = new KeyTable();
        keyTable.setKeyName(createKeyTableVO.getKey());
        ForKeyValue forKeyValue = createKeyTableVO.getValues().get(0);
        if ( forKeyValue.getValues().length == 1){
            keyTable.setIsMultiple(false);
            keyTable.setCurrentValue(forKeyValue.getValues()[0]);
        }
        else {
            keyTable.setIsMultiple(true);
            keyTable.setCurrentValue(forKeyValue.getValues()[0]);
        }
        keyTable.setPermissionLevel((short)createKeyTableVO.getPermission());
        keyTable.setIsDeleted(false);
        keyTableMapper.insert(keyTable);

        ValueTable valueTable;
        for (String value : forKeyValue.getValues()) {
            valueTable = new ValueTable();
            valueTable.setKeyId(keyTable.getId());
            valueTable.setValueData(value);
            valueTable.setVersion(1);
            valueTableMapper.insert(valueTable);
        }
        return true;
    }

    @Override
    @Transactional
    public boolean updateKeyTable(UpdateKeyValueVO createKeyTableVO) {
        // 检查key是否存在
        KeyTable keyTable = keyTableMapper.selectById(createKeyTableVO.getId());
        if (keyTable == null || keyTable.getIsDeleted()){
            throw new ParameterValidationException("id不存在");
        }
        validatorUpdateKey(keyTable);
        // 获取传入的所有值
        List<ForKeyValue> values = createKeyTableVO.getValues();

        // 找出版本号最高的ForKeyValue
        ForKeyValue maxVersionValue = values.stream()
            .max((v1, v2) -> Integer.compare(v1.getVersion(), v2.getVersion()))
            .orElseThrow(() -> new ParameterValidationException("没有有效的值"));

        // 获取当前最大版本号
        int maxVersion = keyTableMapper.findMaxVersion(createKeyTableVO.getId());

        // 检查版本号是否连续
        if(maxVersionValue.getVersion() != maxVersion + 1) {
            throw new ParameterValidationException("版本号不连续");
        }

        // 更新key表的当前值
        keyTable.setCurrentValue(maxVersionValue.getValues()[0]);
        keyTableMapper.updateById(keyTable);

        // 插入新的value记录
        for (String value : maxVersionValue.getValues()) {
            ValueTable valueTable = new ValueTable();
            valueTable.setKeyId(keyTable.getId());
            valueTable.setValueData(value);
            valueTable.setVersion(maxVersionValue.getVersion());
            valueTableMapper.insert(valueTable);
        }

        return true;
    }

    @Override
    public KeyTableRespVO getValue(Long id) {

        //通过id获取keyTable数据
        KeyTable keyTable = keyTableMapper.selectById(id);
        if (keyTable == null || keyTable.getIsDeleted()){
            throw new ParameterValidationException("id不存在");
        }
        validatorReadKey(keyTable);

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


        List<ForKeyValue> historyValues = new ArrayList<>();
        ForKeyValue historyValue ;
        for(int i = latestVersion ; i>=1 ; i--){
            List<ValueTable> valueTables2 = versionMap.get(i);
            String[] values = new String[valueTables2.size()];

            for (int j=0;j<valueTables2.size();j++) {
                values[j] = valueTables2.get(j).getValueData();
            }
            historyValue = new ForKeyValue();
            historyValue.setValues(values);
            historyValue.setCreateTime(TimestampUtil.format(valueTables2.get(0).getCreatedAt()));
            historyValue.setId(valueTables2.get(0).getId());
            historyValue.setVersion(valueTables2.get(0).getVersion());

            historyValues.add(historyValue);
        }

        KeyTableRespVO keyTableRespVO = new KeyTableRespVO();
        keyTableRespVO.setValues(historyValues);
        keyTableRespVO.setCreateTime(TimestampUtil.format(keyTable.getCreatedAt()));
        keyTableRespVO.setUpdateTime(TimestampUtil.format(keyTable.getUpdatedAt()));
        keyTableRespVO.setCreateBy(FindUserWithCache.findUserById(keyTable.getUserId()).getUserName());
        keyTableRespVO.setId(keyTable.getId());
        keyTableRespVO.setPermission(keyTable.getPermissionLevel());
        keyTableRespVO.setKey(keyTable.getKeyName());


        return keyTableRespVO;
    }

    @Override
    public boolean delete(Long id) {
        KeyTable keyTable = keyTableMapper.selectById(id);
        if (keyTable == null || keyTable.getIsDeleted()){
            throw new ParameterValidationException("id不存在");
        }
        validatorDeleteKey(keyTable);
        if (keyTable.getUserId() == CurrentContext.getCurrentUser().getId()){
            throw new PermissionException("无权限!");
        }
        keyTable.setIsDeleted(true);
        keyTableMapper.updateById(keyTable);
        return true;
    }

    @Override
    public boolean updatePermission(Long id, int permission) {
        KeyTable keyTable = keyTableMapper.selectById(id);
        validatorUpdateKey(keyTable);
        if (!keyTable.getIsDeleted()) {
            keyTable.setPermissionLevel((short)permission);
            keyTableMapper.updateById(keyTable);
            return true;
        }
        else {
            throw new ParameterValidationException("id不存在");
        }

    }

    @Override
    public List<KeyTableRespVO> getKeyTableByPage(KeyTablePageVO pageBaseVO) {
        if (!StringUtils.isEmpty(pageBaseVO.getKey())){
            return findByEs(pageBaseVO);
        }
        pageBaseVO.setOffset((pageBaseVO.getPageNum()-1) * pageBaseVO.getPageSize());
        pageBaseVO.setUserId(CurrentContext.getCurrentUser().getId());
        List<KeyTable> keyTable = keyTableMapper.selectByPage(pageBaseVO);

        List<KeyTableRespVO> keyTableRespVOS = new ArrayList<>();

//        for (KeyTable keyTable1 : keyTable){
//            KeyTableRespVO value = getValue(keyTable1.getId());
//            keyTableRespVOS.add(value);
//        }

        KeyTableRespVO  keyTablePageVO;

        for (KeyTable keyTable1 : keyTable) {
            keyTablePageVO = new KeyTableRespVO();
            keyTablePageVO.setId(keyTable1.getId());
            keyTablePageVO.setKey(keyTable1.getKeyName());
            keyTablePageVO.setCreateTime(TimestampUtil.format(keyTable1.getCreatedAt()));
            keyTablePageVO.setUpdateTime(TimestampUtil.format(keyTable1.getUpdatedAt()));
            keyTablePageVO.setCreateBy(FindUserWithCache.findUserById(keyTable1.getUserId()).getUserName());
            keyTablePageVO.setPermission(keyTable1.getPermissionLevel());

            List<ForKeyValue> values = new ArrayList<>();
            ForKeyValue  forKeyValue ;
            List<ValueTable> valueTableList = valueTableMapper.selectList(new LambdaQueryWrapper<ValueTable>()
                            .eq(ValueTable::getKeyId, keyTable1.getId())
                            .orderByDesc(ValueTable::getVersion));
            Integer version = valueTableList.get(0).getVersion();
            Map<Integer, List<ValueTable>> collect = valueTableList
                    .stream()
                    .collect(Collectors.groupingBy(ValueTable::getVersion));

            for (int i = version ; i >= 1; i--) {
                List<ValueTable> valueTables = collect.get(i);
                forKeyValue = new ForKeyValue();
                forKeyValue.setVersion(valueTables.get(0).getVersion());
                forKeyValue.setCreateTime(TimestampUtil.format(valueTables.get(0).getCreatedAt()));
                forKeyValue.setId(valueTables.get(0).getId());
                forKeyValue.setValues(valueTables.stream().map(ValueTable::getValueData).toArray(String[]::new));
                forKeyValue.setCreateBy(FindUserWithCache.findUserById(keyTable1.getUserId()).getUserName());

                values.add(forKeyValue);
            }

            keyTablePageVO.setValues(values);


            keyTableRespVOS.add(keyTablePageVO);

        }
        return keyTableRespVOS;
    }

    //todo 待实现
    private List<KeyTableRespVO> findByEs(KeyTablePageVO pageBaseVO) {
        return null;
    }

    private void validatorReadKey(KeyTable keyTable){
        if (keyTable.getPermissionLevel() == -1 ){
            throw new PermissionException("无权限！");
        }
    }

    private void validatorUpdateKey(KeyTable keyTable){
        if (keyTable.getPermissionLevel() == -1 || keyTable.getPermissionLevel() == 0){
            if (keyTable.getUserId() != CurrentContext.getCurrentUser().getId()){
                throw new PermissionException("无权限！");
            }
        }
    }

    private void validatorDeleteKey(KeyTable keyTable){
        if (Objects.equals(keyTable.getUserId(), CurrentContext.getCurrentUser().getId())){
            throw new PermissionException("无权限！");
        }
    }
}
