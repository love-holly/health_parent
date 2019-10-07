package com.holly.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.holly.dao.OrderDao;
import com.holly.servcie.Orderservice;
import com.holly.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;

@Service(interfaceClass = Orderservice.class)
@Transactional
public class OrderServiceImpl implements Orderservice {


       @Autowired
       private OrderDao orderDao;

    /**
     * 查询个人预定信息
     * @param id
     * @return
     */
    @Override
    public Map findById(Integer id) throws Exception {

        Map detail = orderDao.findById4Detail(id);

        if(detail!=null){//对时间进行格式转换
            Date orderDate = (Date) detail.get("orderDate");
            detail.put("orderDate", DateUtils.parseDate2String(orderDate));
        }

        return detail;
    }
}
