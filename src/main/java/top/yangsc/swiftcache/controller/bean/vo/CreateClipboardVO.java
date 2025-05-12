package top.yangsc.swiftcache.controller.bean.vo;


import lombok.Data;
import top.yangsc.swiftcache.controller.annotation.NotNull;

@Data
public class CreateClipboardVO {
    @NotNull
    private String content;

    @NotNull
    private String systemTag;

    @NotNull
    private String tagName;


}
