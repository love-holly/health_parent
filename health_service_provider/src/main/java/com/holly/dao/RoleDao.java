package com.holly.dao;

import com.holly.pojo.Role;
import org.apache.ibatis.annotations.Select;

import java.util.Set;

public interface RoleDao {

    @Select("SELECT *FROM t_user_role u,t_role r WHERE u.`role_id`=r.`id` AND u.`user_id`=#{user_id}")
    Set<Role> findByRole(Integer userId);
}
