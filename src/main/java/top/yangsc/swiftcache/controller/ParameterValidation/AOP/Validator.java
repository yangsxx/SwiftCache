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

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

// 传输层参数验证
public class Validator {

    static void doValidator(Class<?> clz,Object o){

        // 获取所有的成员属性
        Field[] declaredFields = clz.getDeclaredFields();

        // 遍历
        for (Field declaredField : declaredFields) {
            declaredField.setAccessible(true);
            //获取此字段对象
            Object o1 = null;
            try {
                o1 = declaredField.get(o);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }


            for (Annotation annotation : declaredField.getAnnotations()) {
                if (annotation!=null){
                    //如果属性标记为Entity将继续下一级校验
                    if (annotation instanceof Entity){
                        Entity entity = (Entity) annotation;
                        doValidator(entity.clz(),o1);
                    }
                    //处理非空校验
                    if (annotation instanceof NotNull){
                        boolean value = ((NotNull)annotation).value();
                        if (value){
                            if (o1 == null||  "".equals(o1) || "null".equals(o1)){
                                throw new ParameterValidationException(declaredField.getName() + "字段不可为空");
                            }
                        }
                    }

                    // 处理条件判断
                    if (annotation instanceof Condition){
                        String tableName="";
                        Condition c=(Condition) annotation;
                        Class<? extends Serializable> clz1 = c.clz();
                        if (c.fieldName().isBlank()){
                             tableName= TableUtil.toTab(declaredField.getName());
                        }else {
                            tableName=c.fieldName();
                        }


                        SimpleMapper mapper1 = SpringContextUtil.getBean(SimpleMapper.class);

                        if ((c.unique())|| c.absent()){
                            boolean exist = mapper1.isExist(c.table(), tableName, String.valueOf(o1));
                            if (c.unique()&&exist){
                                throw new ParameterValidationException(declaredField.getName() + "已存在相同的数据");
                            }
                            if (c.absent()&&!exist){
                                throw new ParameterValidationException(declaredField.getName() + "数据不存在");
                            }
                        }

                        if (!StringUtils.isEmpty(c.where())){
                            if (mapper1.isExistByWhere(TableUtil.toTab(tableName),c.where())){
                                throw new ParameterValidationException(declaredField.getName() + "不符合参数条件");
                            }
                        }

                    }
                    if (annotation instanceof RegexValidator){
                        RegexValidator regexValidator=(RegexValidator) annotation;
                        if (!StringUtils.isEmpty(regexValidator.regex())){
                            if (!o1.toString().matches(regexValidator.regex())){
                                throw new ParameterValidationException(declaredField.getName() + "参数格式不规则");
                            }
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


