package top.yangsc.swiftcache.base.pojo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.io.Serial;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ClipboardValues implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "实际的剪切板内容")
    private String content;

    @Schema(description = "16字节固定长度的MD5哈希值(二进制格式)，用于快速比对")
    private byte[] contentMd5;
}