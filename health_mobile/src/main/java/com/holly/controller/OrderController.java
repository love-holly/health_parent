package com.holly.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.holly.constant.MessageConstant;
import com.holly.entity.Result;
import com.holly.servcie.Orderservice;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequestMapping("/order")
@RestController
public class OrderController {

    @Reference
    private Orderservice orderService;

    @RequestMapping("/findById")
    public Result findById(Integer id){

        try {
            Map map=orderService.findById(id);
            //当查询成功时
            return new Result(true, MessageConstant.QUERY_ORDER_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_ORDER_FAIL);
        }

    }

}
