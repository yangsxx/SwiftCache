package top.yangsc.swiftcache.base.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
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
public class KeyTable implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID，自增长")
    @TableField(fill = FieldFill.INSERT)
    private Long id;

    @Schema(description = "键名称，不可为空")
    private String keyName;

    @Schema(description = "标识是否支持一对多关系")
    private Boolean isMultiple;

    @Schema(description = "所有者用户ID")
    @TableField(fill = FieldFill.INSERT)
    private Long ownerId;

    @Schema(description = "当前值,对于multiple可为空")
    private String currentValue;

    @Schema(description = "权限级别：1(完全公开)/0(公开)/-1(私有)")
    private Short permissionLevel;

    @Schema(description = "软删除标记，TRUE表示已删除")
    private Boolean isDeleted;

    @Schema(description = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Timestamp createdAt;

    @Schema(description = "更新时间")
    @TableField(fill = FieldFill.UPDATE)
    private Timestamp updatedAt;

    @Schema(description = "创建人ID")
    @TableField(fill = FieldFill.INSERT)
    private Long createdBy;

    @Schema(description = "更新人ID")
    @TableField(fill = FieldFill.UPDATE)
    private Long updatedBy;
}