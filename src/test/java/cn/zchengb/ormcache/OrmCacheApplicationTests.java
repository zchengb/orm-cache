package cn.zchengb.ormcache;

import cn.zchengb.ormcache.infrastructure.UserRepository;
import cn.zchengb.ormcache.infrastructure.mybatis.mapper.UserCacheMapper;
import cn.zchengb.ormcache.infrastructure.mybatis.mapper.UserMapper;
import cn.zchengb.ormcache.model.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
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

    @Autowired
    private SqlSessionFactory mybatisSqlSessionFactory;

    @Autowired
    private UserCacheMapper userCacheMapper;

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
    void mybatis_query_cache_will_be_clear_when_exec_update_in_same_sql_session() {
        userMyBatisRepository.saveUser(new User("user1"));
        var user = userMyBatisRepository.findById(1L).orElseThrow();
        user.setName("user2");
        userMyBatisRepository.saveUser(user);
        userMyBatisRepository.findById(1L).orElseThrow();
    }

    @Test
    void mybatis_query_with_multiple_sql_session() {
        var sqlSession1 = mybatisSqlSessionFactory.openSession(true);
        var mapper1 = sqlSession1.getMapper(UserMapper.class);
        var sqlSession2 = mybatisSqlSessionFactory.openSession(true);
        var mapper2 = sqlSession2.getMapper(UserMapper.class);

        var user = new User("old user name");
        mapper1.insert(user);
        var userId = user.getId();

        var user1 = mapper1.selectById(userId);
        log.info("before update user1 name: {}", user1.getName());

        var user2 = mapper2.selectById(userId);
        log.info("before update user2 name: {}", user2.getName());

        user1.setName("new name");
        mapper1.updateById(user1);
        log.info("after update user1 name: {}", user1.getName());

        user2 = mapper2.selectById(userId);
        log.info("after update user2 name: {}", user2.getName());
    }

    @Test
    void mybatis_l2_cache() {
        var user = new User("old user name");
        userCacheMapper.insert(user);
        userCacheMapper.selectById(user.getId());
        userCacheMapper.selectById(user.getId());
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
