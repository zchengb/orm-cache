package cn.zchengb.ormcache.infrastructure;


import cn.zchengb.ormcache.model.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findById(Long id);

    void saveUser(User user);
}
