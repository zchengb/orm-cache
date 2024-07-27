package cn.zchengb.ormcache;

import cn.zchengb.ormcache.infrastructure.UserRepository;
import cn.zchengb.ormcache.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OrmCacheApplicationTests {
    @Autowired
    private UserRepository userRepository;

    @Test
    void mybatis_l1_cache() {
        var user = new User("user1");
        userRepository.save(user);
        user = userRepository.findById(user.getId()).orElseThrow();
        System.out.println(user);
    }
}
