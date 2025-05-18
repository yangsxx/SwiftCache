package top.yangsc.swiftcache.Schedule.mq;


import cn.hutool.json.JSONUtil;
import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.yangsc.swiftcache.ai.BaseAiService;
import top.yangsc.swiftcache.ai.ResultDTO;
import top.yangsc.swiftcache.base.mapper.*;
import top.yangsc.swiftcache.base.pojo.*;
import top.yangsc.swiftcache.controller.bean.vo.UpdateKeyValueVO;
import top.yangsc.swiftcache.controller.bean.vo.resp.ForKeyValue;
import top.yangsc.swiftcache.services.KeyTableService;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class Consumer {

    @Resource
    private ClipboardValuesMapper clipboardValuesMapper;

    @Resource
    private KeyTableMapper  keyTableMapper;

    @Resource
    private ValueTableMapper  valueTableMapper;

    // 第一级缓存：快速hash判断（100条）
    private static final int MAX_HASH_ITEMS = 100;
    private static boolean  isInit = false;
    private final BlockingQueue<Integer> hashCache = new LinkedBlockingQueue<>(MAX_HASH_ITEMS);

    // 第二级缓存：精确内容判断（10条）
    private static final int MAX_CONTENT_ITEMS = 10;
    private static final int CONTENT_PREVIEW_LENGTH = 32;
    private final BlockingQueue<String> contentCache = new LinkedBlockingQueue<>(MAX_CONTENT_ITEMS);
    private static final String AI_QUEUE = "\"${content}\"" +
            "1.这是一条来自于剪切板复制的记录，请判断他是否具有记录为kv型笔记的价值\n" +
            "2.只挑选高价值信息，包含操作和配置，不要包含日志信息和不具有逻辑性的小片段代码\n" +
            "3.生成合适的json回答包含cover(bool),key(根据内容自动生成,中文优先) ,value(原始值),keys(提取供查询使用的关键字优先中文条目和必要的英文条目，通过空格隔开)\n" +
            "4.无价值请生成json回答包含，cover,keys\n" +
            "5.这是最近的十条剪切板记录(截取前部分)如有内容重复，请返回cover为false，无需生成keys,内容如下：${last}\n" +
            "6.只回答结果";
    // 快速hash判断
    private boolean quickCheckDuplicate(String content) {
        if (StringUtils.isEmpty(content)) {
            return false;
        }

        int hash = content.hashCode();
        if (hashCache.contains(hash)) {
            return true;
        }

        if (hashCache.remainingCapacity() == 0) {
            hashCache.poll();
        }
        hashCache.offer(hash);
        return false;
    }

    private String substringConcat() {
        StringBuilder sb = new StringBuilder();
        for (String content : contentCache) {
            String truncated = content.length() > CONTENT_PREVIEW_LENGTH
                ? content.substring(0, CONTENT_PREVIEW_LENGTH)
                : content;
            sb.append("[").append(truncated).append("]");
        }
        return sb.toString().trim();
    }
    
    @Autowired
    private BaseAiService baseAiService;

    @Autowired
    private KeysMapper keysMapper;

    @Resource
    private KeyTableService keyTableService;
    @RabbitListener(queues = "ai.queue")
    public void receiveMessage(String message) {
        if (!isInit){
            isInit = true;
            init();
        }
        KeysDTO bean = JSONUtil.toBean(message, KeysDTO.class);
        aiClipboardTask(bean);
    }

    private void init() {

        clipboardValuesMapper.selectList(new LambdaQueryWrapper<ClipboardValues>()
                .orderByDesc(ClipboardValues::getId)
                .last("limit 100")).forEach(clipboardValues -> {
                    hashCache.offer(clipboardValues.getContentMd5().hashCode());
                });
        List<Long> collect = keyTableMapper.selectList(new LambdaQueryWrapper<KeyTable>()
                        .orderByDesc(KeyTable::getId)
                        .last("limit 10")).stream().map(KeyTable::getId)
                .toList();
        Map<String, Integer> collect1 = new HashMap<>();
        valueTableMapper.selectList(new LambdaQueryWrapper<ValueTable>()
                .in(ValueTable::getKeyId, collect)
                .orderByDesc(ValueTable::getVersion)).forEach(valueTable -> {
                    String key =  valueTable.getId() +"￥￥"+ valueTable.getValueData();
                    if (collect1.containsKey(valueTable.getId()+valueTable.getValueData())){

                        Integer i = collect1.get(key);
                        if (i <  valueTable.getVersion()){
                            collect1.put(key,valueTable.getVersion());
                        }
                    }
                    collect1.put(key,valueTable.getVersion());
                });
        collect1.forEach((k,v)->{
            contentCache.offer(k.split("￥￥")[1]);
        });

    }

    private void aiClipboardTask(KeysDTO keysDTO) {
        if (quickCheckDuplicate(keysDTO.getValue())){
            return;
        }
        String s = baseAiService.simpleGenerateText(generateAsk(keysDTO.getValue()));
        String replace = s.replace("```json", "").replace("```", "");
        ResultDTO bean = JSONUtil.toBean(replace, ResultDTO.class);
        Keys keys = new Keys();
        if (bean.getCover()) {
            if (contentCache.remainingCapacity() == 0) {
                contentCache.poll(); // Remove the oldest entry if queue is full
            }
            contentCache.offer(keysDTO.getValue());
            UpdateKeyValueVO createClipboardVO = new UpdateKeyValueVO();
            ForKeyValue forKeyValue = new ForKeyValue();
            forKeyValue.setCreateBy("system");
            forKeyValue.setValues(new String[]{bean.getValue()});
            forKeyValue.setVersion(1);
            createClipboardVO.setValues(List.of(forKeyValue));
            createClipboardVO.setKey(bean.getKey());
            createClipboardVO.setPermission(1);
            createClipboardVO.setUserId(10000124L);
            keyTableService.createKeyTable(createClipboardVO);
        }
        keys.setKeys(bean.getKeys());
        if (keysDTO.getValueTableId()!=null){
            keys.setValueTableId(keysDTO.getValueTableId());
        }
        if (keysDTO.getClipboardValueId() != null) {
            keys.setClipboardValueId(keysDTO.getClipboardValueId());
        }
        if (!StringUtils.isEmpty(bean.getKeys())){
            keys.setKeys(bean.getKeys());
            keysMapper.insert(keys);
        }

    }

    private String generateAsk(String question) {
        return AI_QUEUE.replace("${content}",question).replace("${last}",substringConcat());
    }
}
