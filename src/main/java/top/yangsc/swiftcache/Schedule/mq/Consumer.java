package top.yangsc.swiftcache.Schedule.mq;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.yangsc.swiftcache.ai.BaseAiService;
import top.yangsc.swiftcache.ai.HuoShanAiService;
import top.yangsc.swiftcache.ai.ResultDTO;
import top.yangsc.swiftcache.base.mapper.KeysMapper;
import top.yangsc.swiftcache.base.pojo.Keys;
import top.yangsc.swiftcache.controller.bean.vo.CreateClipboardVO;
import top.yangsc.swiftcache.controller.bean.vo.UpdateKeyValueVO;
import top.yangsc.swiftcache.controller.bean.vo.resp.ForKeyValue;
import top.yangsc.swiftcache.services.KeyTableService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Component
public class Consumer {

    private static String AI_QUEUE = "\"${}\"" +
            "这是一条来自于剪切板复制的记录，请较为严格的判断他是否具有记录为kv型笔记的价值(尽量只挑选高价值信息，如url连接，非常用复杂的命令等)，注意不要包含日志信息，小片段代码(不具有逻辑性的)。" +
            "如有请生成合适的json回答包含，" +
            "cover(bool)  ,key(根据内容自动生成,中文优先) ,value(原始值),keys(提取供查询使用的关键字，" +
            "优先中文条目和必要的英文条目，通过空格隔开)。如无请生成json回答包含，" +
            "cover,keys。只回答结果";

    @Autowired
    private BaseAiService baseAiService;

    @Autowired
    private KeysMapper keysMapper;

    @Resource
    private KeyTableService keyTableService;
    @RabbitListener(queues = "aiClipboard.queue")
    public void receiveMessage(String message) {
        KeysDTO bean = JSONUtil.toBean(message, KeysDTO.class);
        aiClipboardTask(bean);
    }

    private void aiClipboardTask(KeysDTO keysDTO) {
        String s = baseAiService.simpleGenerateText(generateAsk(keysDTO.getValue()));
        String replace = s.replace("```json", "").replace("```", "");
        ResultDTO bean = JSONUtil.toBean(replace, ResultDTO.class);
        Keys keys = new Keys();
        if (bean.getCover()) {
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
            keys.setClipboardValueId(keysDTO.getId());
        }
        keys.setKeys(bean.getKeys());
        keysMapper.insert(keys);
    }

    private String generateAsk(String question) {
        return AI_QUEUE.replace("${}",question);
    }
}
