package top.yangsc.swiftcache.controller.bean.vo;

import lombok.Data;

/**
 * 描述：top.yangsc.swiftcache.controller.bean.vo
 *
 * @author yang
 * @date 2025/5/13 17:25
 */
@Data
public class KeyTablePageVO extends PageBaseVO{
    String key;
    Integer permission;
    Long userId;
}
