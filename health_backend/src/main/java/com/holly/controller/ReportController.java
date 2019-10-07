package com.holly.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.holly.constant.MessageConstant;
import com.holly.entity.Result;
import com.holly.servcie.MemberService;
import com.holly.servcie.ReportService;
import com.holly.servcie.SetmealService;
import com.holly.util.DateUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.*;

@RequestMapping("/report")
@RestController
public class ReportController {
    @Reference
    private MemberService memberService;

    @Reference
    private SetmealService setmealService;

    @Reference
    private ReportService reportService;
    /**
     * 每月会员数据统计
     * @return
     */
    @RequestMapping("/getMemberReport")
    public Result getMemberReport(){
        //因为前端需要返回一个months和memberCount数据
        //故而采用Map集合返回
        Calendar calendar=Calendar.getInstance();//日历类
        calendar.add(Calendar.MONTH,-12);//往后推迟一年
        List<String> list=new ArrayList<>();
        for (int i = 0; i <12 ; i++) {
            calendar.add(Calendar.MONTH,1);
            Date time = calendar.getTime();
            list.add(new SimpleDateFormat("yyyy.MM").format(time));//获取过去一年每一个月
        }

          List<Integer> countList=memberService.getMemberReport(list);

        Map<String,Object> map=new HashMap<>();
        map.put("months",list);
        map.put("memberCount",countList);

        return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,map);

    }

    /**
     * 套餐数据统计
     */
    @RequestMapping("/getSetmealReport")
    public Result getSetmealReport(){
        try {
            //设计返回数据类型
            Map<String,Object> listMap=new HashMap<>();
            List<Map<String,Object>> setmealCount=setmealService.findSetmealCount();
            List<String> setmealNames=new ArrayList<>();
            for (Map<String, Object> objectMap : setmealCount) {
                String name = (String) objectMap.get("name");
                setmealNames.add(name);
            }

            listMap.put("setmealCount",setmealCount);
            listMap.put("setmealNames",setmealNames);
            return new Result(true,MessageConstant.GET_SETMEAL_LIST_SUCCESS,listMap);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_SETMEAL_LIST_SUCCESS);
        }

    }

    /**
     * 获取营运数据
     * @return
     */
    @RequestMapping("/getBusinessReportData")
    public Result getBusinessReportData(){

        try {
            Map<String,Object> map =reportService.getBusinessReportData();
            return new Result(true,MessageConstant.GET_BUSINESS_REPORT_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_BUSINESS_REPORT_SUCCESS);
        }


    }

    @RequestMapping("/exportBusinessReport")
    public Result exportBusinessReport(HttpServletRequest request, HttpServletResponse response){
        try {
            Map<String,Object> map =reportService.getBusinessReportData();
            String reportDate = (String) map.get("reportDate");
            Integer todayNewMember = (Integer) map.get("todayNewMember");

            Integer totalMember = (Integer) map.get("totalMember");
            Integer thisWeekNewMember = (Integer) map.get("thisWeekNewMember");
            Integer thisMonthNewMember = (Integer) map.get("thisMonthNewMember");
            Integer todayOrderNumber = (Integer) map.get("todayOrderNumber");
            Integer todayVisitsNumber = (Integer) map.get("todayVisitsNumber");
            Integer thisWeekOrderNumber = (Integer) map.get("thisWeekOrderNumber");
            Integer thisWeekVisitsNumber = (Integer) map.get("thisWeekVisitsNumber");
            Integer thisMonthOrderNumber = (Integer) map.get("thisMonthOrderNumber");
            Integer thisMonthVisitsNumber = (Integer) map.get("thisMonthVisitsNumber");
            //获取内存文件模板相对路径
            String path=request.getSession().getServletContext().getRealPath("template")+ File.separator+ "report_template.xlsx";

            //创建内存excel文档
            XSSFWorkbook xssfWorkbook=new XSSFWorkbook(new FileInputStream(new File(path)));
            XSSFSheet sheet = xssfWorkbook.getSheetAt(0);//获取第一个文件簿
            XSSFRow row2 = sheet.getRow(2);
            XSSFCell row2Cell = row2.getCell(5);
            row2Cell.setCellValue(reportDate);//设置日期

            XSSFRow row5=sheet.getRow(4);
            XSSFCell row5Cell = row5.getCell(5);
            row5Cell.setCellValue(todayNewMember);/*新增会员数*/
              row5.getCell(7).setCellValue(totalMember);//总会员数

            XSSFRow row6 = sheet.getRow(5);
            //本周新增会员数
            row6.getCell(5).setCellValue(thisWeekNewMember);
            //本月新增会员数
            row6.getCell(7).setCellValue(thisMonthNewMember);

            //
            XSSFRow row8 = sheet.getRow(7);
            //今日预约数
            row8.getCell(5).setCellValue(todayOrderNumber);
            //今日到诊数
            row8.getCell(7).setCellValue(todayVisitsNumber);

            XSSFRow row9 = sheet.getRow(8);
            row9.getCell(5).setCellValue(thisWeekOrderNumber);
            row9.getCell(7).setCellValue(thisWeekVisitsNumber);

            XSSFRow row10 = sheet.getRow(9);
            row10.getCell(5).setCellValue(thisMonthOrderNumber);
            row10.getCell(7).setCellValue(thisMonthVisitsNumber);

            int rowNum=12;
            List<Map> hotSetmeal = (List<Map>) map.get("hotSetmeal");
            for (Map<String,Object> maps : hotSetmeal) {
                //热门套餐名字
                String name = (String) maps.get("name");
                //热门套餐数量
                Long setmeal_count = (Long) maps.get("setmeal_count");
                BigDecimal proportion = (BigDecimal) maps.get("proportion");
                XSSFRow row13 = sheet.getRow(rowNum++);
                row13.getCell(4).setCellValue(name);
                row13.getCell(5).setCellValue(setmeal_count);
                row13.getCell(6).setCellValue(proportion.doubleValue());

            }

            //以响应的方式下载至制定的地方
            ServletOutputStream outputStream = response.getOutputStream();
            //excel下载的方式
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("content-Disposition","attachment;filename=report.xlsx");

            xssfWorkbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
            xssfWorkbook.close();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_BUSINESS_REPORT_SUCCESS);
        }



    }

}
