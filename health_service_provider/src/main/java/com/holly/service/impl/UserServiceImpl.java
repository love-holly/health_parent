package com.holly.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.holly.dao.PermissionDao;
import com.holly.dao.RoleDao;
import com.holly.dao.UserDao;
import com.holly.pojo.Permission;
import com.holly.pojo.Role;
import com.holly.pojo.User;
import com.holly.servcie.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service(interfaceClass = UserService.class)
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private PermissionDao permissionDao;
    @Override
    public User findByUser(String username) {
        //通过用户名查找是否有此用户
        User user=userDao.findByUser(username);
        if (user==null){
            return null;
        }
        //通过userID和中间表去查询角色
        Integer userId = user.getId();
        Set<Role> role=roleDao.findByRole(userId);
        if(role!=null&&role.size()>0){

            for (Role role1 : role) {
                Integer role1Id = role1.getId();
                //通过角色id以及中间表去查询权限
                Set<Permission> permission=permissionDao.findByPermission(role1Id);
                if(permission!=null&&permission.size()>0){
                    role1.setPermissions(permission);
                }
            }
            user.setRoles(role);

        }


        return user;
    }
}
