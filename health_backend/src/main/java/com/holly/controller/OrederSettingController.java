package com.holly.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.holly.constant.MessageConstant;
import com.holly.entity.Result;
import com.holly.pojo.OrderSetting;
import com.holly.servcie.OrderSettingService;
import com.holly.util.POIUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ordersetting")
public class OrederSettingController {
    @Reference
      private OrderSettingService orderSettingService;


    /**
     * 上传文件
     * @param excelFile
     * @return
     */
    @RequestMapping("/upload")
    public Result upload(@RequestParam("excelFile") MultipartFile excelFile){
        //将文件放入工具类中进行解析
        try {
            List<String[]> Lists = POIUtils.readExcel(excelFile);
            //将其转换成下面格式
            List<OrderSetting> settingList=new ArrayList<>();
            for (String[] list : Lists) {
                //根据行获取单元格数据
                if(list.length!=2){
                    continue;
                }

                String OrderDate=list[0];

                String OrderNum=list[1];
                OrderSetting orderSetting=new OrderSetting(new Date(OrderDate),Integer.parseInt(OrderNum));
                settingList.add(orderSetting);
            }
            if(settingList==null&&settingList.size()<=0){
                return new Result(false,"数据为空");
            }
            orderSettingService.upload(settingList);
            return new Result(true,MessageConstant.UPLOAD_SUCCESS);
        } catch (IOException e) {
            e.printStackTrace();
            //当文件上传失败
            return new Result(false, "文件上传失败");
        }
    }


    @RequestMapping("/findAll")
    public Result findAll(String date){


        try {
            //因为前端页面以键值对形式展示,故而返回格式以list包装Map集合
            List<Map> maps=orderSettingService.findAll(date);
            return new Result(true,MessageConstant.GET_ORDERSETTING_SUCCESS,maps);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_ORDERSETTING_FAIL);
        }
    }

    @RequestMapping("/handle")
    public Result handle(@RequestBody OrderSetting orderSetting){
        System.out.println("修改预约数:"+orderSetting);
        try {
            orderSettingService.handle(orderSetting);
            return new Result(true,"已修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"修改预约人数失败,请刷新当前页面");
        }


    }
}
