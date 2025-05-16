package top.yangsc.swiftcache.controller.ParameterValidation.AOP;


import com.alibaba.druid.util.StringUtils;
import top.yangsc.swiftcache.base.Exception.ParameterValidationException;
import top.yangsc.swiftcache.base.mapper.SimpleMapper;
import top.yangsc.swiftcache.controller.ParameterValidation.BaseVO;
import top.yangsc.swiftcache.controller.annotation.Condition;
import top.yangsc.swiftcache.controller.annotation.Entity;
import top.yangsc.swiftcache.controller.annotation.NotNull;
import top.yangsc.swiftcache.controller.annotation.RegexValidator;
import top.yangsc.swiftcache.tools.SpringContextUtil;
import top.yangsc.swiftcache.tools.TableUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

// 传输层参数验证
public class Validator {
    // 缓存字段访问权限设置
    private static final ConcurrentHashMap<Field, Boolean> fieldAccessCache = new ConcurrentHashMap<>();
    // 预编译正则表达式
    private static final ConcurrentHashMap<String, Pattern> regexCache = new ConcurrentHashMap<>();
    // 复用SimpleMapper实例
    private static final SimpleMapper mapper = SpringContextUtil.getBean(SimpleMapper.class);

    static void doValidator(Class<?> clz, Object o) {
        Field[] declaredFields = clz.getDeclaredFields();

        for (Field declaredField : declaredFields) {
            // 使用缓存优化字段访问
            fieldAccessCache.computeIfAbsent(declaredField, f -> {
                f.setAccessible(true);
                return true;
            });

            Object o1 = null;
            try {
                o1 = declaredField.get(o);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            // 提前获取注解数组避免多次调用
            Annotation[] annotations = declaredField.getAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation instanceof Entity entity) {
                    doValidator(entity.clz(), o1);
                }
                else if (annotation instanceof NotNull notNull) {
                    boolean value = ((NotNull)annotation).value();
                    if (value){
                        if (o1 == null||  "".equals(o1) || "null".equals(o1)){
                            throw new ParameterValidationException(declaredField.getName() + "字段不可为空");
                        }
                    }
                }
                else if (annotation instanceof Condition c) {
                    String tableName = c.fieldName().isBlank() ?
                        TableUtil.toTab(declaredField.getName()) : c.fieldName();

                    // 使用预缓存的mapper实例
                    if ((c.unique()) || c.absent()) {
                        boolean exist = mapper.isExist(c.table(), tableName, String.valueOf(o1));
                        if (c.unique()&&exist){
                            throw new ParameterValidationException(declaredField.getName() + "已存在相同的数据");
                        }
                        if (c.absent()&&!exist){
                            throw new ParameterValidationException(declaredField.getName() + "数据不存在");
                        }
                    }
                    if (!StringUtils.isEmpty(c.where())){
                        if (mapper.isExistByWhere(TableUtil.toTab(tableName),c.where())){
                            throw new ParameterValidationException(declaredField.getName() + "不符合参数条件");
                        }
                    }
                }
                else if (annotation instanceof RegexValidator regexValidator) {
                    String regex = regexValidator.regex();
                    if (!StringUtils.isEmpty(regex)) {
                        // 使用预编译的正则表达式
                        Pattern pattern = regexCache.computeIfAbsent(regex, Pattern::compile);
                        if (!pattern.matcher(o1.toString()).matches()) {
                            throw new ParameterValidationException(declaredField.getName() + "参数格式不规则");
                        }
                    }
                }
            }
        }
        if(o instanceof BaseVO){
            BaseVO baseVO=(BaseVO)o;
            baseVO.validation();
        }

    }
}


