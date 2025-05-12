package top.yangsc.swiftcache.base.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SimpleMapper {

    boolean isExist(@Param("TabName") String TabName, @Param("CName") String CName,@Param("data")String data);

    boolean isExistByWhere(@Param("TabName") String TabName,@Param("condition")String condition);
}
