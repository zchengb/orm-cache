package cn.zchengb.ormcache;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("cn.zchengb.ormcache.infrastructure.mybatis")
public class OrmCacheApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrmCacheApplication.class, args);
    }

}
