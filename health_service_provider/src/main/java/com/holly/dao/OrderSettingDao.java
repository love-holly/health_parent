package com.holly.dao;



import com.holly.pojo.OrderSetting;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderSettingDao {

    @Select("select count(id) from t_ordersetting where orderDate=#{orderDate}")
    Long findBydate(Date orderDate);

    @Insert("insert into t_ordersetting(orderDate,number,reservations) values(#{orderDate},#{number},#{reservations})")
    void insertOrder(OrderSetting orderSetting);

   @Update("update t_ordersetting set number=#{number} where orderDate=#{orderDate}")
    void updateOrder(OrderSetting orderSetting);

     /*@Select("select * from t_ordersetting where orderDate between #{begin} and #{end}")
    List<OrderSetting> findAll(Map<String,String> map);*///2019-9

   @Select("select *from t_ordersetting where orderDate like concat(#{date},'%')")
    List<OrderSetting> findAll(String date);//2019-9

     @Select("select orderDate from t_ordersetting where orderDate between #{dateMin} and #{dateMax}")
    List<Date> findDate(Map<String,Date> map);


    @Insert({
            "<script>",
            "insert into t_ordersetting(orderDate,number) values ",
            "<foreach collection='orderSettingList' item='item' index='index' separator=','>",
            "(#{item.orderDate}, #{item.number})",
            "</foreach>",
            "</script>"
    })
    void insertList(@Param(value="orderSettingList")List<OrderSetting> orderSettingList);

    @Update("update t_ordersetting set reservations=#{reservations} where orderDate=#{orderDate}")
    public void editReservationsByOrderDate(OrderSetting orderSetting);
}
