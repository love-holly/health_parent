package com.holly.util;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * 七牛云工具类
 */



public class QiniuUtilsss {
    //应当将此静态变量使用xml配置文件

    @Value("${qiniu.accessKey}")
    public   String _accessKey;

    public  static String accessKey;

    @Value("${qiniu.secretKey}")
    public  String _secretKey;
    public  static String secretKey;
    @Value("${qiniu.bucket}")
    public  String _bucket ;
    public  static String bucket ;

    @PostConstruct
    public void init(){
        QiniuUtilsss.accessKey=_accessKey;
        QiniuUtilsss.secretKey=_secretKey;
        QiniuUtilsss.bucket=_bucket;
    }

@Test
    public void show(){
        System.out.println(accessKey);
        System.out.println(secretKey);
        System.out.println(bucket);

    }

}
