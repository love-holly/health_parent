package com.holly.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.aliyuncs.exceptions.ClientException;
import com.holly.constant.MessageConstant;
import com.holly.constant.RedisMessageConstant;
import com.holly.entity.Result;
import com.holly.pojo.Order;
import com.holly.servcie.ValidateCodeService;
import com.holly.util.SMSUtils;
import com.holly.util.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {
    @Autowired
    private JedisPool jedisPool;

    @Reference
    private ValidateCodeService codeService;

    /**
     * 将验证码存入到redis中
     * @param telephone
     * @return
     */
    @RequestMapping("/send4Order")
    public Result send4Order(String telephone){
        //首先生成验证码
        Integer validateCode = ValidateCodeUtils.generateValidateCode(4);
            //发送验证码信息
        try {
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,validateCode.toString());

        } catch (ClientException e) {
            e.printStackTrace();
            //如果发送失败
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        //将验证码存入到redis中

        jedisPool.getResource().setex(telephone+ RedisMessageConstant.SENDTYPE_ORDER,300,validateCode.toString());
        return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }

    @RequestMapping("/submit")
    public Result submit(@RequestBody Map map){
        //首先进行验证码校验
        String telephone = (String) map.get("telephone");
        //从参数中获取验证码
        String validateCode = (String) map.get("validateCode");
        //从redis缓存中获取数据
        String telephones = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_ORDER);
        if(telephones!=null&&validateCode!=null&&validateCode.equals(telephones)){
            //当以上判断正确时,再进行预约的下一步
            map.put("orderType", Order.ORDERTYPE_WEIXIN);
            Result result=null;
            try {
               result=codeService.submit(map);
                System.out.println("返回类型"+result);
            } catch (Exception e) {
                e.printStackTrace();
                return result;
            }
            if(result.isFlag()){
                //当预约成功的时候.像用户发送成功短信
                System.out.println("预约成功");
                try {
                    SMSUtils.sendShortMessage(SMSUtils.ORDER_NOTICE,telephone, (String) map.get("orderDate"));
                } catch (ClientException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("返回结果"+result);
                 return result;
        }else {
            return new Result(false,MessageConstant.VALIDATECODE_ERROR);
        }

    }
    @RequestMapping("/send4Login")
    public Result send4Login(String telephone){
        //随机获取验证码
        Integer integer = ValidateCodeUtils.generateValidateCode(6);

        //发送验证码
        try {
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,integer.toString());
        } catch (ClientException e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        jedisPool.getResource().setex(telephone+RedisMessageConstant.SENDTYPE_LOGIN,240,integer.toString());

        return new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }

}
