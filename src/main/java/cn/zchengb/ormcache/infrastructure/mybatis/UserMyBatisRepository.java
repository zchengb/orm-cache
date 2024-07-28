package cn.zchengb.ormcache.infrastructure.mybatis;

import cn.zchengb.ormcache.infrastructure.UserRepository;
import cn.zchengb.ormcache.infrastructure.mybatis.mapper.UserMapper;
import cn.zchengb.ormcache.model.User;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserMyBatisRepository extends ServiceImpl<UserMapper, User> implements UserRepository {
    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(getBaseMapper().selectById(id));
    }

    @Override
    public void saveUser(User user) {
        saveOrUpdate(user);
    }
}
