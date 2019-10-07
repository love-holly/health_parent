package com.holly.controller;


import com.holly.constant.MessageConstant;
import com.holly.entity.Result;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/user")
@RestController
public class UserController {


      @RequestMapping("/getUsername")
    public Result getUsername(){
    //security在登录认证时,会将用户名存入session中
    User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if (user!=null){
        String username = user.getUsername();
        return new Result(true, MessageConstant.GET_USERNAME_SUCCESS,username);
    }else {
        return new Result(false,MessageConstant.GET_USERNAME_FAIL);
    }


}
}
