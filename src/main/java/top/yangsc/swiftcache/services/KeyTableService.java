package top.yangsc.swiftcache.services;

import com.baomidou.mybatisplus.extension.service.IService;
import top.yangsc.swiftcache.base.pojo.KeyTable;
import top.yangsc.swiftcache.controller.bean.vo.CreateKeyTableVO;
import top.yangsc.swiftcache.controller.bean.vo.PageBaseVO;
import top.yangsc.swiftcache.controller.bean.vo.resp.KeyTableRespVO;

public interface KeyTableService extends IService<KeyTable> {
    boolean createKeyTable(CreateKeyTableVO createKeyTableVO);

    boolean updateKeyTable(CreateKeyTableVO createKeyTableVO);

    KeyTableRespVO getValue(String id);

    boolean delete(String id);

    boolean updatePermission(String id, int permission);

    KeyTableRespVO getKeyTableByPage(PageBaseVO pageBaseVO);
}
