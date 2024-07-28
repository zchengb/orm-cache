package cn.zchengb.ormcache;

import cn.zchengb.ormcache.infrastructure.UserRepository;
import cn.zchengb.ormcache.model.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.IntStream;

@Slf4j
@SpringBootTest
class OrmCacheApplicationTests {
    @Autowired
    @Qualifier("userMyBatisRepository")
    private UserRepository userMyBatisRepository;

    @Autowired
    @Qualifier("userJpaRepository")
    private UserRepository userJpaRepository;

    @Test
    @Transactional
    void mybatis_query_within_same_session() {
        var start = System.currentTimeMillis();
        userMyBatisRepository.saveUser(new User("user1"));
        IntStream.range(0, 1_000).forEach(i -> userMyBatisRepository.findById(1L).orElseThrow());
        var end = System.currentTimeMillis();
        log.info("mybatis_query_within_same_session: {} ms", end - start);
    }

    @Test
    void mybatis_query_without_same_session() {
        var start = System.currentTimeMillis();
        userMyBatisRepository.saveUser(new User("user1"));
        IntStream.range(0, 1_000).forEach(i -> userMyBatisRepository.findById(1L).orElseThrow());
        var end = System.currentTimeMillis();
        log.info("mybatis_query_without_same_session: {} ms", end - start);
    }

    @Test
    @Transactional
    void jpa_query_within_same_session() {
        var start = System.currentTimeMillis();
        userJpaRepository.saveUser(new User("user1"));
        IntStream.range(0, 1_000).forEach(i -> userJpaRepository.findById(1L).orElseThrow());
        var end = System.currentTimeMillis();
        log.info("jpa_query_within_same_session: {} ms", end - start);
    }

    @Test
    void jpa_query_without_same_session() {
        var start = System.currentTimeMillis();
        userJpaRepository.saveUser(new User("user1"));
        IntStream.range(0, 1_000).forEach(i -> userJpaRepository.findById(1L).orElseThrow());
        var end = System.currentTimeMillis();
        log.info("jpa_query_without_same_session: {} ms", end - start);
    }
}
