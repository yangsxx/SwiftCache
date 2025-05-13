package top.yangsc.swiftcache.controller.bean.vo;

import com.alibaba.druid.util.StringUtils;
import lombok.Data;
import top.yangsc.swiftcache.base.Exception.ParameterValidationException;
import top.yangsc.swiftcache.controller.ParameterValidation.BaseVO;
import top.yangsc.swiftcache.controller.annotation.NotNull;
import top.yangsc.swiftcache.controller.bean.vo.resp.ForKeyValue;

import java.util.List;

/**
 * 描述：top.yangsc.swiftcache.controller.bean.vo
 *
 * @author yang
 * @date 2025/5/13 15:50
 */
@Data
public class UpdateKeyValueVO implements BaseVO {
    private Long id;
    @NotNull
    private String key;
    private List<ForKeyValue> values;
    private int permission = 0;


    @Override
    public void validation() {
        if (values == null || values.isEmpty()) {
            throw new ParameterValidationException("值不可为空！");
        }
    }
}
