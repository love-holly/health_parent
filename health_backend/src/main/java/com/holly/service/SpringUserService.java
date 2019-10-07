package com.holly.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.holly.pojo.Permission;
import com.holly.pojo.Role;
import com.holly.pojo.User;
import com.holly.servcie.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
@Component
public class SpringUserService implements UserDetailsService {
    @Reference
    private UserService service;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //从客户段获取用户名,将获取到的用户名放入数据库查找
        User user=service.findByUser(username);
           if(user==null){//如果数据库中查询不到此人,那么就直接返回null
               return null;
           }
        List<GrantedAuthority> list=new ArrayList<>();
           //如果有此用户,则将其对应的角色和权限加入到管理中
        Set<Role> roles = user.getRoles();//获取权限集合
        if (roles!=null&&roles.size()>0){
        for (Role role : roles) {
            list.add(new SimpleGrantedAuthority(role.getKeyword()));//加入角色
            Set<Permission> permissions = role.getPermissions();
            if (permissions != null && permissions.size() > 0) {
                for (Permission permission : permissions) {
                    //加入权限
                    list.add(new SimpleGrantedAuthority(permission.getKeyword()));
                }
            }
        }
        }
        UserDetails userDetails=new org.springframework.security.core.userdetails.User(username,user.getPassword(),list);

        return userDetails;
    }
}
