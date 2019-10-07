package com.holly.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.holly.constant.MessageConstant;
import com.holly.constant.RedisMessageConstant;
import com.holly.entity.Result;
import com.holly.pojo.Setmeal;
import com.holly.servcie.SetmealService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/setmeal")
@RestController
public class SetmealController {

    @Reference
    private SetmealService service;

    @RequestMapping("/getSetmeal")
    public Result getSetmeal(){
        try {
            List<Setmeal> setmeal=service.findgetSetmeal();
            return new Result(true,MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS,setmeal);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_SETMEAL_COUNT_REPORT_FAIL);
        }

    }

    @RequestMapping("/findById")
    public Result findById(Integer id){


        try {
            Setmeal setmeal=service.findById(id);
            System.out.println(setmeal);
            return new Result(true,MessageConstant.GET_SETMEAL_LIST_SUCCESS,setmeal);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true,MessageConstant.GET_SETMEAL_LIST_FAIL);
        }

    }

}
