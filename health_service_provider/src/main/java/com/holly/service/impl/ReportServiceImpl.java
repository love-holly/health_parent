package com.holly.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.holly.dao.MemberDao;
import com.holly.dao.OrderDao;
import com.holly.servcie.ReportService;
import com.holly.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service(interfaceClass = ReportService.class)
@Transactional
public class ReportServiceImpl implements ReportService {

    @Autowired
    private MemberDao memberDao;
    
    @Autowired
    private OrderDao orderDao;

    /**
     * 获取运营数据
     * @return
     */
    @Override
    public Map<String, Object> getBusinessReportData() throws Exception {

        Map<String, Object> map=new HashMap<>();

        //获取当日时间
        String today = DateUtils.parseDate2String(new Date());
        //获取当前本周一
        String monday = DateUtils.parseDate2String(DateUtils.getThisWeekMonday());
        //获取当前月
        String months = DateUtils.parseDate2String(DateUtils.getFirstDay4ThisMonth());
        //获取当日会员数
        Integer todayNewMember = memberDao.findMemberCountByDate(today);
        //获取总会员 数
        Integer totalMember = memberDao.findMemberTotalCount();
        //获取本周新增会员数
        Integer thisWeekNewMember = memberDao.findMemberCountAfterDate(monday);
        //获取本月会员数
        Integer thisMonthNewMember = memberDao.findMemberCountAfterDate(months);
         //查询当日已到诊人数
        Integer todayVisitsNumber = orderDao.findVisitsCountByDate(today);
        //今日预约人数
        Integer todayOrderNumber = orderDao.findOrderCountByDate(today);
        //本周预约人数
        Integer thisWeekOrderNumber = orderDao.findOrderCountAfterDate(monday);
        //本周到诊人数
        Integer thisWeekVisitsNumber = orderDao.findVisitsCountAfterDate(monday);
        //本月预约数
        Integer thisMonthOrderNumber = orderDao.findOrderCountAfterDate(months);
        //本月到诊人数
        Integer thisMonthVisitsNumber = orderDao.findVisitsCountAfterDate(months);
       //查询热门套餐
        List<Map> hotSetmeal = orderDao.findHotSetmeal();

        map.put("hotSetmeal",hotSetmeal);
        map.put("thisMonthVisitsNumber",thisMonthVisitsNumber);
        map.put("thisMonthOrderNumber",thisMonthOrderNumber);
        map.put("thisWeekVisitsNumber",thisWeekVisitsNumber);
        map.put("thisWeekOrderNumber",thisWeekOrderNumber);
        map.put("todayOrderNumber",todayOrderNumber);
        map.put("todayVisitsNumber",todayVisitsNumber);
        map.put("thisMonthNewMember",thisMonthNewMember);
        map.put("thisWeekNewMember",thisWeekNewMember);
        map.put("totalMember",totalMember);
        map.put("todayNewMember",todayNewMember);
        map.put("reportDate",today);

        return map;
    }
}
