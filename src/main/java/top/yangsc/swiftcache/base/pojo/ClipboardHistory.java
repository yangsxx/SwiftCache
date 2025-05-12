package top.yangsc.swiftcache.base.pojo;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("clipboard_history")
public class ClipboardHistory implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    @TableField(fill = FieldFill.INSERT)
    @TableId(type = IdType.NONE)
    private Long id;

    @Schema(description = "关联的用户ID")
    @TableField(fill = FieldFill.INSERT)
    private Long userId;

    @Schema(description = "记录创建时间，自动填充")
    @TableField(fill = FieldFill.INSERT)
    private Timestamp createdAt;

    @Schema(description = "来源操作系统，如Windows/MacOS/Linux")
    private String system;

    @Schema(description = "终端设备名称或标识")
    private String tagName;

    @Schema(description = "关联clipboard_values表的外键")
    private Long valueId;
}