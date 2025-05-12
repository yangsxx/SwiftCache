package top.yangsc.swiftcache.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import top.yangsc.swiftcache.base.field.RedisPreFix;
import top.yangsc.swiftcache.tools.RedisUtil;


public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //String token = request.getHeader("token");
        //Object value = RedisUtil.getValue(RedisPreFix.USERLONGIN + token);
//        if (value != null){
//            return true;
//        }
        System.out.println("已拦截请求："+ request.getRequestURI());
        return false;
    }



}
