package com.holly.test;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.*;

public class PoiTest {


    /**
     * 获取每行数据方法一
     * @throws IOException
   /*  *//*
    @Test
    public void show() throws IOException {
        //创建一个导入excel表对象
        XSSFWorkbook excel=new XSSFWorkbook(new FileInputStream(new File("E:\\ceshi\\测试.xlsx")));
      //获取其对应的页
        XSSFSheet sheetAt = excel.getSheetAt(0);
        //获取对应的行
        for (Row cells : sheetAt) {
            //获取所在行的对应的单元格
            for (Cell cell : cells) {
                System.out.println(cell);
            }
        }
           excel.close();
    }

    @Test
    public void show1()throws IOException{
        XSSFWorkbook excel=new XSSFWorkbook(new FileInputStream(new File("E:\\ceshi\\测试.xlsx")));
        //获取对应的工作簿
        XSSFSheet sheetAt = excel.getSheetAt(0);
        //获取最后一行
        int lastRowNum = sheetAt.getLastRowNum();
        //根据最后一行索引位置进行遍历所有行
        for (int i = 0; i <=lastRowNum ; i++) {

            XSSFRow row = sheetAt.getRow(i);
            //获取每一行最后一个单元格
            short lastCellNum = row.getLastCellNum();
            for (int j = 0; j <lastCellNum ; j++) {
                System.out.println(row.getCell(j));
            }
        }
        excel.close();
    }

   @Test
    public void show3()throws IOException{
        //在内存中创建一个类
        XSSFWorkbook excel=new XSSFWorkbook();
        //创建一个工作簿
        XSSFSheet heima = excel.createSheet("heima");
        //创建第一行
        XSSFRow TitleRow = heima.createRow(0);
         //创建单元格并进行赋值操作
        TitleRow.createCell(0).setCellValue("序号");
        TitleRow.createCell(1).setCellValue("姓名");
        TitleRow.createCell(2).setCellValue("年龄");
      //创建第二个行
        XSSFRow BodyRow = heima.createRow(1);
        BodyRow.createCell(0).setCellValue(1);
        BodyRow.createCell(1).setCellValue("王军");
        BodyRow.createCell(2).setCellValue(28);

        //创建一个输出流
        FileOutputStream fos=new FileOutputStream(new File("E:\\ceshi\\测试2.xlsx"));
        //将其内存excel文档写出
        excel.write(fos);
        fos.close();
    }*/
}
