package top.yangsc.swiftcache.controller.bean.vo;

import com.alibaba.druid.util.StringUtils;
import lombok.Data;
import top.yangsc.swiftcache.base.Exception.ParameterValidationException;
import top.yangsc.swiftcache.controller.ParameterValidation.BaseVO;
import top.yangsc.swiftcache.controller.annotation.NotNull;

/**
 * 描述：top.yangsc.swiftcache.controller.bean.vo
 *
 * @author yang
 * @date 2025/5/13 10:11
 */
@Data
public class CreateKeyTableVO implements BaseVO {
    private Long id;
    @NotNull
    private String key;
    private String value;
    private String[] values;
    private int permission = 0;


    @Override
    public void validation() {
        if (id!=null && StringUtils.isEmpty(key) && ((values == null)|| values.length == 0)) {
            throw new ParameterValidationException("值不可为空！");
        }
    }
}
