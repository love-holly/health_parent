package com.holly.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.holly.constant.RedisMessageConstant;
import com.holly.entity.Result;
import com.holly.pojo.Member;
import com.holly.servcie.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

@RequestMapping("/login")
@RestController
public class LoginController {

    @Reference
    private MemberService memberService;

    @Autowired
    private JedisPool jedisPool;

    public LoginController() {
    }

    @RequestMapping("/check")
    public Result check(HttpServletResponse response,@RequestBody Map map){
        System.out.println(map);
        //获取前端给出的验证码和手机号进行判断
        String validateCode = (String) map.get("validateCode");
        String telephone = (String) map.get("telephone");
        String s = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_LOGIN);

        if(validateCode!=null&&s!=null&&validateCode.equals(s)){
            //当验证通过后,我们需要判断是否为会员
               Member member=memberService.findBytelephone(telephone);
               if(member==null){
                   //当不是会员的时候,需要自动为其注册会员
                   member.setRegTime(new Date());//设置当前时间为注册时间
                   member.setPhoneNumber(telephone);
                   memberService.add(member);//进行会员注册

               }
               //如果已经是会员,将手机号信息保存在cookie中,当下次客户访问时,可以利用其手机号进行操作
            Cookie cookie=new Cookie("login_member_telephone",telephone);
               cookie.setPath("/");//设置cookie的携带路径,及所有访问路径都携带
            cookie.setMaxAge(60*60*24*30);//设置保质期为一个月
            //将cookie保存在响应中
            response.addCookie(cookie);
            //将用户数据已json的格式保存在redis中
            String json_member = JSON.toJSON(member).toString();
            jedisPool.getResource().setex(telephone,60*30,json_member);


            return new Result(true, "验证成功");
        }else {
            return new Result(false,"验证码错误");
        }

    }

}
