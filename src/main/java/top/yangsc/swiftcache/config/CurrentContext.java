package top.yangsc.swiftcache.config;

import top.yangsc.swiftcache.base.pojo.Users;

import java.util.Map;

public class CurrentContext {

    private static final ThreadLocal<Map<String, Object>> currentUser = new ThreadLocal<>();


    public static void setCurrentUser(Users user) {
        if (null == currentUser.get()) {
             currentUser.set(Map.of("user", user));
        }
        else {
            currentUser.get().put("user", user);
        }
    }
    public static Users getCurrentUser() {
        return (Users) currentUser.get().get("user");
    }
}


