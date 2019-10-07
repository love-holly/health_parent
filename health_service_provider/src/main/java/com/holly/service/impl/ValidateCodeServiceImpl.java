package com.holly.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.holly.constant.MessageConstant;
import com.holly.dao.MemberDao;
import com.holly.dao.OrderDao;
import com.holly.dao.OrderSettingDao;
import com.holly.dao.validateCodeDao;
import com.holly.entity.Result;
import com.holly.pojo.Member;
import com.holly.pojo.Order;
import com.holly.pojo.OrderSetting;
import com.holly.servcie.ValidateCodeService;
import com.holly.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = ValidateCodeService.class)
@Transactional
public class ValidateCodeServiceImpl implements ValidateCodeService {
    @Autowired
    private validateCodeDao validateCodeDao;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderSettingDao orderSettingDao;

    /**
     * 体检预约
     * @param map
     * @return
     */
    @Override
    public Result submit(Map map) throws Exception {
        //1、检查用户所选择的预约日期是否已经提前进行了预约设置，如果没有设置则无法进行 预约
        String orderDate = (String) map.get("orderDate");
        OrderSetting orderSetting = validateCodeDao.findDate(DateUtils.parseString2Date(orderDate));

        if (orderSetting==null){
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }


        // 2、检查用户所选择的预约日期是否已经约满，如果已经约满则无法预约
        int number = orderSetting.getNumber();
        int reservations = orderSetting.getReservations();
        if(number==reservations){
            return new Result(false,MessageConstant.ORDER_FULL);
        }

        //3、检查用户是否重复预约（同一个用户在同一天预约了同一个套餐），如果是重复预约 则无法完成再次预约
        String telephone = (String) map.get("telephone");
        Member byTelephone = memberDao.findByTelephone(telephone);//获取该手机号的信息
        if(byTelephone!=null){
            Integer membei_id = byTelephone.getId();
            Date date = DateUtils.parseString2Date(orderDate);
            String setmealId = (String) map.get("setmealId");//获取套餐id
            Order order = new Order(membei_id, date, Integer.parseInt(setmealId));
            List<Order> orderList = orderDao.findByCondition(order);
            if(orderList!=null&&orderList.size()>0){
                //当以上不为空时,说明用户进行了重复预约
                return new Result(false,MessageConstant.HAS_ORDERED);
            }
        }else {
            //4、检查当前用户是否为会员，如果是会员则直接完成预约，如果不是会员则自动完成注 册并进行预约
            byTelephone=new Member();
            byTelephone.setName((String) map.get("name"));
            byTelephone.setSex((String) map.get("sex"));
            byTelephone.setIdCard((String) map.get("idCard"));
            byTelephone.setPhoneNumber(telephone);
            byTelephone.setRegTime(new Date());
            memberDao.add(byTelephone);//自动注册会员

        }

       // 5、预约成功，更新当日的已预约人数
       Order order=new Order();
        order.setMemberId(byTelephone.getId());
        order.setOrderDate(DateUtils.parseString2Date(orderDate));
        order.setOrderType((String) map.get("orderType"));
        order.setSetmealId(Integer.parseInt((String) map.get("setmealId")));
       order.setOrderStatus(Order.ORDERSTATUS_NO);
       orderDao.add(order);
        Integer orderId = order.getId();
        orderSetting.setReservations(orderSetting.getReservations()+1);
       orderSettingDao.editReservationsByOrderDate(orderSetting);
        return new Result(true,MessageConstant.ORDER_SUCCESS,orderId);
    }
}
