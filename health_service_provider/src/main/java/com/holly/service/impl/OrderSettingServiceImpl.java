package com.holly.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.holly.dao.OrderSettingDao;
import com.holly.pojo.OrderSetting;
import com.holly.servcie.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {

    @Autowired
    private OrderSettingDao orderSettingDao;

    /**
     * 上传文件
     * @param settingList
     */
    @Override
    public void upload(List<OrderSetting> settingList) {
        //需要判断文件是否被多次上传

        /*if (settingList != null && settingList.size() > 0) {
            for (OrderSetting orderSetting : settingList) {

                //根据日期判断文件是否被多次上传
                Long bydate = orderSettingDao.findBydate(orderSetting.getOrderDate());
                //如果之前已上传此日期的数据,那么执行修改语句
                if (bydate > 0){
                    orderSettingDao.updateOrder(orderSetting);
                }else {
                    //如果之前未上传此日期的数据,那么执行插入语句
                  orderSettingDao.insertOrder(orderSetting);
                }
            }
        }*/
       //对集合进行出重,并封装
      List<OrderSetting> list=new ArrayList<>();
          HashSet<OrderSetting> hashSet=new HashSet<>(settingList);
          list.addAll(hashSet);
        System.out.println("去重之后的"+list);

           //创建一个时间集合,将时间封装
           List<Date> dateList=new ArrayList<>();
           List<Date> dateses=new ArrayList<>();
           //对去重后的集合进行遍历
        for (OrderSetting orderSetting : list) {
            dateList.add(orderSetting.getOrderDate());
            dateses.add(orderSetting.getOrderDate());
        }
        Date dateMin=Collections.min(dateList);
        Date dateMax=Collections.max(dateList);
        System.out.println("最大时间是"+dateMax+"最小时间是"+dateMin);

        Map<String,Date> map=new HashMap<>();
        map.put("dateMin",dateMin);
        map.put("dateMax",dateMax);

        //通过最大时间和最小时间获取数据库中的集合
       /* List<Date> dateList1=orderSettingDao.findDate(dateMin,dateMax);*/
        List<Date> dateList1=orderSettingDao.findDate(map);
        System.out.println("非重复集合获取到数据库中的数据"+dateList1);
        List<OrderSetting> orderSettingList=new ArrayList<>();
        dateList.retainAll(dateList1);//获取两个集合的交集部分
        dateses.removeAll(dateList1);//获取两个集合非交集部分
        //交集部分是
        System.out.println("交集部分"+dateList);
        System.out.println("非交集部分"+dateses);
         if(dateList!=null&&dateList.size()>0){
             //当非空时说明有交集部分
             for (Date date : dateList) {
                 for (OrderSetting orderSetting : list) {
                     if(date.equals(orderSetting.getOrderDate())){
                         //找出相同元素的集合
                         orderSettingList.add(orderSetting);
                     }
                 }
             }
             for (OrderSetting orderSetting : orderSettingList) {
                 orderSettingDao.updateOrder(orderSetting);
             }
         }


        List<OrderSetting> orderSettingList2=new ArrayList<>();//创建一个非交集集合
        if(dateses!=null&&dateses.size()>0){
            //当非空时说明有交集部分
            for (Date date : dateses) {
                for (OrderSetting orderSetting : list) {
                    if(date.equals(orderSetting.getOrderDate())){
                        //找出相同元素的集合
                        orderSettingList2.add(orderSetting);
                    }
                }
            }
            /*for (OrderSetting orderSetting : orderSettingList2) {
                orderSettingDao.insertOrder(orderSetting);
            }*/
            orderSettingDao.insertList(orderSettingList2);
        }


    }



    @Override
    public List<Map> findAll(String date) {
        String begin=date+"-1";
        String end=date+"-31";
        Map<String,String> m=new HashMap<>();
        m.put("begin",begin);
        m.put("end",end);

       /* List<OrderSetting> settingList=orderSettingDao.findAll(m);*/
        List<OrderSetting> settingList=orderSettingDao.findAll(date);
        System.out.println("settingList"+settingList);
        List<Map> mapList=new ArrayList<>();
        if(settingList!=null&&settingList.size()>0){
            for (OrderSetting orderSetting : settingList) {
                //将获取的数据封装进map集合中
                Map<String,Object> maps=new HashMap<>();
                maps.put("date",orderSetting.getOrderDate().getDate());
                maps.put("number",orderSetting.getNumber());
                maps.put("reservations",orderSetting.getReservations());
                mapList.add(maps);
            }

        }

        return mapList;
    }

    /**
     * 修改可预约人数
     * @param orderSetting
     */
    @Override
    public void handle(OrderSetting orderSetting) {
        //需判断当前日期是否之前有设置
        Long bydate = orderSettingDao.findBydate(orderSetting.getOrderDate());
        //如果之前有设置那就执行修改任务
        if (bydate>0){
            orderSettingDao.updateOrder(orderSetting);
        }else {
            //否则执行添加任务
            orderSettingDao.insertOrder(orderSetting);
        }

    }
}
