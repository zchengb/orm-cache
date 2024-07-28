package cn.zchengb.ormcache.infrastructure.mybatis.mapper;

import cn.zchengb.ormcache.model.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {

}
