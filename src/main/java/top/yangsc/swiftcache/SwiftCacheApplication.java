package top.yangsc.swiftcache;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("top.yangsc.swiftcache.base.mapper")
public class SwiftCacheApplication {

	public static void main(String[] args) {
		SpringApplication.run(SwiftCacheApplication.class, args);
	}

}
