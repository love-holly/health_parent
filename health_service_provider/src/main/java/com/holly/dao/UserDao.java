package com.holly.dao;

import com.holly.pojo.User;
import org.apache.ibatis.annotations.Select;

public interface UserDao {
    @Select("select * from t_user where username=#{username}")
    User findByUser(String username);
}
