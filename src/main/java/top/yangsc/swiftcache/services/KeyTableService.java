package top.yangsc.swiftcache.services;

import com.baomidou.mybatisplus.extension.service.IService;
import top.yangsc.swiftcache.base.pojo.KeyTable;
import top.yangsc.swiftcache.controller.bean.vo.KeyTablePageVO;
import top.yangsc.swiftcache.controller.bean.vo.UpdateKeyValueVO;
import top.yangsc.swiftcache.controller.bean.vo.resp.KeyTableRespVO;

import java.util.List;

public interface KeyTableService extends IService<KeyTable> {
    boolean createKeyTable(UpdateKeyValueVO createKeyTableVO);

    boolean updateKeyTable(UpdateKeyValueVO createKeyTableVO);

    KeyTableRespVO getValue(Long id);

    boolean delete(Long id);

    boolean updatePermission(Long id, int permission);

    List<KeyTableRespVO> getKeyTableByPage(KeyTablePageVO pageBaseVO);
}
