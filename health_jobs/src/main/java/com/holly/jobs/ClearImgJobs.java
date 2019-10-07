package com.holly.jobs;

import com.holly.constant.RedisConstant;
import com.holly.util.QiniuUtil;
import com.holly.util.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Set;

public class ClearImgJobs {

@Autowired
    private JedisPool jedisPool;

    /**
     * 删除多余图片
     */
    public void clearImg(){
    /*获取连接对象*/
    Jedis resource = jedisPool.getResource();
       /*两个set集合进行相减*/
        Set<String> flieName = resource.sdiff(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        if(flieName!=null&&flieName.size()>0){
            for (String prcName : flieName) {
                //删除在七牛云上的垃圾图片
                QiniuUtil.deleteFileFromQiniu(prcName);
                //删除在缓存中的垃圾图片
                resource.srem(RedisConstant.SETMEAL_PIC_RESOURCES,prcName);
                System.out.println("自定义任务,删除垃圾文件"+prcName);
            }
        }

    }

}
