package com.holly.servcie;

import com.holly.pojo.OrderSetting;

import java.util.List;
import java.util.Map;

public interface OrderSettingService {
    void upload(List<OrderSetting> settingList);

    List<Map> findAll(String date);

    void handle(OrderSetting orderSetting);
}
