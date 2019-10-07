package com.holly.dao;

import com.holly.pojo.Permission;
import org.apache.ibatis.annotations.Select;

import java.util.Set;

public interface PermissionDao {

    @Select("SELECT *FROM t_role_permission r,t_permission p WHERE r.`permission_id`=p.`id` AND r.`role_id`=#{role_id}")
    Set<Permission> findByPermission(Integer role1_id);
}
