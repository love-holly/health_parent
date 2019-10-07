package com.holly.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class QiniuUtil {

    //上传图片
    public static void upload2Qiniu(byte[] bytes, String fileName){

        ApplicationContext app=new ClassPathXmlApplicationContext("applicationContext.xml");
        QiniuUtils.upload2Qiniu(bytes,fileName);

    }


    //删除文件
    public static void deleteFileFromQiniu(String fileName){
        ApplicationContext app=new ClassPathXmlApplicationContext("applicationContext.xml");
        QiniuUtils.deleteFileFromQiniu(fileName);

    }
}
