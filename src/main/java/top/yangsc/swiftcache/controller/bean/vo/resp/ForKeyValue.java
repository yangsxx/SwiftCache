package top.yangsc.swiftcache.controller.bean.vo.resp;

import lombok.Data;

/**
 * 描述：top.yangsc.swiftcache.controller.bean.vo.resp
 *
 * @author yang
 * @date 2025/5/13 10:47
 */
@Data
public class ForKeyValue {
    private Long id;
    private String[] values;
    private String createTime;
    private String createBy;
    private Integer version;
}
