package top.yangsc.swiftcache.controller.bean.vo.resp;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class ClipboardRespVO {
    private Long id;
    private Long userId;
    private String system;
    private String tagName;
    private Long valueId;
    private String content;
    private String userName;
    private String createTime;
    private Timestamp createdAt;
}
