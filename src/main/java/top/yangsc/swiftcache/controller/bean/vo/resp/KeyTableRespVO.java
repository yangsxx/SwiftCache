package top.yangsc.swiftcache.controller.bean.vo.resp;

import lombok.Data;

import java.util.List;

/**
 * 描述：top.yangsc.swiftcache.controller.bean.vo.resp
 *
 * @author yang
 * @date 2025/5/13 10:24
 */
@Data
public class KeyTableRespVO {
    private Long id;
    private String key;
    private List<ForKeyValue> values;
    private int permission;
    private String createTime;
    private String updateTime;
    private String createBy;
}
