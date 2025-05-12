package top.yangsc.swiftcache.tools;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;

import java.io.IOException;

public class ObjectUtil {

    //通过对象和类路径扫描，获取class对象
    public static Class getClassByObject(Object o ,String classPath){
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = new Resource[0];
        try {
            resources = resolver.getResources("classpath*:top/yangsc/notesTool"+classPath);
            for(Resource res :resources) {

                // 先获取resource的元信息，然后获取class元信息，最后得到 class 全路径
                String clsName = new SimpleMetadataReaderFactory().getMetadataReader(res).getClassMetadata().getClassName();
                // 通过名称加载
                Class<?> aClass = Class.forName(clsName);
                if (aClass.isInstance(o)){
                    return aClass;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static <T> T getObjectByClass(Object o,Class<T> clz){
        if (clz.isInstance(o)){
            return (T)o;
        }
        return null;
    }

}

