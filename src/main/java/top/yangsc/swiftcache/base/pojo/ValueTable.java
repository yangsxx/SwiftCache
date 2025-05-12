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
public class ValueTable implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID，自增长")
    @TableField(fill = FieldFill.INSERT)
    private Long id;

    @Schema(description = "外键，关联key_table.id")
    private Long keyId;

    @Schema(description = "存储的值数据，TEXT类型")
    private String valueData;

    @Schema(description = "版本号")
    private Integer version;

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
