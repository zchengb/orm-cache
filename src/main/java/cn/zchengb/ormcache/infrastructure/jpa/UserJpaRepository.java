package cn.zchengb.ormcache.infrastructure.jpa;

import cn.zchengb.ormcache.infrastructure.UserRepository;
import cn.zchengb.ormcache.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserJpaRepository extends JpaRepository<User, Long>, UserRepository {
    @Override
    default void saveUser(User user) {
        this.save(user);
    }
}
