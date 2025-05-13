package top.yangsc.swiftcache.base.emun;

import lombok.Data;

/**
 * 描述：top.yangsc.swiftcache.base.emun
 *
 * @author yang
 * @date 2025/5/13 16:54
 */

public enum HttpCode {
    SUCCESS(200, "成功"),
    UNAUTHORIZED(401, "未授权/未登录"),
    FORBIDDEN(403, "无权限访问"),
    METHOD_NOT_ALLOWED(405, "参数校验异常"),
    INTERNAL_SERVER_ERROR(500, "服务器内部错误");



    private final int code;
    private final String message;

    HttpCode(int i, String s) {
        this.code = i;
        this.message = s;
    }
}
