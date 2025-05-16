package top.yangsc.swiftcache.controller.bean.vo;

import lombok.Data;
import top.yangsc.swiftcache.base.Exception.ParameterValidationException;
import top.yangsc.swiftcache.controller.ParameterValidation.BaseVO;
import top.yangsc.swiftcache.controller.annotation.Condition;

@Data
public class PageBaseVO implements BaseVO {
    public long pageNum;
    public long pageSize;
    private long offset;

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
