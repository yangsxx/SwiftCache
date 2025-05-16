package top.yangsc.swiftcache.base.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.sql.Timestamp;

/**
 * 描述：top.yangsc.swiftcache.base.pojo
 *
 * @author yang
 * @date 2025/5/16 22:22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Keys {
    @Schema(description = "主键ID，自增长")
    @TableField(fill = FieldFill.INSERT)
    private Long id;

    private Long clipboardValueId;

    private Long valueTableId;

    @Schema(description = "键名称")
    private String keys;


    @Schema(description = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Timestamp createdAt;



}
