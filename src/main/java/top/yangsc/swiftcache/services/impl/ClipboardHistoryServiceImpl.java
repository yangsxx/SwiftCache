package top.yangsc.swiftcache.services.impl;

import cn.hutool.crypto.digest.MD5;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.yangsc.swiftcache.base.ResultData;
import top.yangsc.swiftcache.base.mapper.ClipboardHistoryMapper;
import top.yangsc.swiftcache.base.mapper.ClipboardValuesMapper;
import top.yangsc.swiftcache.base.pojo.ClipboardHistory;
import top.yangsc.swiftcache.base.pojo.ClipboardValues;
import top.yangsc.swiftcache.config.CurrentContext;
import top.yangsc.swiftcache.controller.bean.vo.CreateClipboardVO;
import top.yangsc.swiftcache.controller.bean.vo.resp.ClipboardRespVO;
import top.yangsc.swiftcache.services.ClipboardHistoryService;

import javax.annotation.Resource;

@Service
public class ClipboardHistoryServiceImpl extends ServiceImpl<ClipboardHistoryMapper, ClipboardHistory> implements ClipboardHistoryService {

    @Resource
    private ClipboardHistoryMapper clipboardHistoryMapper;

    @Resource
    private ClipboardValuesMapper clipboardValuesMapper;

    MD5 md5 = MD5.create();
    @Override
    public ResultData<String> createClipboard(CreateClipboardVO createClipboardVO) {
//      查重插入，并获取id
        Long l = clipboardValuesMapper.insertOne(createClipboardVO.getContent());

        if (l == null){
            throw  new RuntimeException("数据库操作失败，请联系管理员");
        }
        ClipboardHistory clipboardHistory = new ClipboardHistory();
        clipboardHistory.setSystem(createClipboardVO.getSystemTag());
        clipboardHistory.setTagName(createClipboardVO.getTagName());
        clipboardHistory.setValueId(l);


        int insert = clipboardHistoryMapper.insert(clipboardHistory);
        if (insert != 1){
            throw  new RuntimeException("数据库操作失败，请联系管理员");
        }


        return ResultData.ok("记录完成");
    }

    @Override
    public ResultData<ClipboardRespVO> getClipboard() {
        return null;
    }
}
