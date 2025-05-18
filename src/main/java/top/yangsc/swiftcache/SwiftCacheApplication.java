package top.yangsc.swiftcache;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("top.yangsc.swiftcache.base.mapper")
@EnableScheduling
public class SwiftCacheApplication {


	public static void main(String[] args) {
		SpringApplication.run(SwiftCacheApplication.class, args);
	}

}
