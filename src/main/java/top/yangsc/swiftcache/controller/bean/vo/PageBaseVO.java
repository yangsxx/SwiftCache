package top.yangsc.swiftcache.controller.bean.vo;

import lombok.Data;
import top.yangsc.swiftcache.base.Exception.ParameterValidationException;
import top.yangsc.swiftcache.controller.ParameterValidation.BaseVO;
import top.yangsc.swiftcache.controller.annotation.Condition;

@Data
public class PageBaseVO implements BaseVO {
    @Condition(clz = Integer.class)
    public int pageNum;
    @Condition(clz = Integer.class)
    public int pageSize;
    private int offset;

    @Override
    public void validation() {
        if (pageNum <= 0) {
            throw new ParameterValidationException("pageNum必须大于0");
        }
        if (pageSize <= 0) {
            throw new ParameterValidationException("pageSize必须大于0");
        }
    }
}
