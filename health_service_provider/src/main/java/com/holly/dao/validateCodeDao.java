package com.holly.dao;

import com.holly.pojo.OrderSetting;
import org.apache.ibatis.annotations.Select;

import java.util.Date;

public interface validateCodeDao {

   @Select("select * from t_ordersetting where orderDate=#{orderDate} ")
    OrderSetting findDate(Date orderDate);
}
