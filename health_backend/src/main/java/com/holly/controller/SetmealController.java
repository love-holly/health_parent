package com.holly.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.holly.constant.MessageConstant;
import com.holly.constant.RedisConstant;
import com.holly.entity.PageResult;
import com.holly.entity.QueryPageBean;
import com.holly.entity.Result;
import com.holly.pojo.CheckGroup;
import com.holly.pojo.Setmeal;
import com.holly.servcie.SetmealService;
import com.holly.util.QiniuUtil;
import com.holly.util.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/setmeal")
public class SetmealController {

private String imgFileS=null;
private String ImgFiless=null;

    @Reference
    private SetmealService setmealService;

    @Autowired
    private JedisPool jedisPool;

  /*  @Autowired
    private QiniuUtils qiniuUtils;*/

    /**
     * 获取检查组
     * @return
     */
    @RequestMapping("/findAll")
    public Result findAll(){
        List<CheckGroup> groupList =setmealService.findAll();
        if (groupList!=null&&groupList.size()>0){
            return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS,groupList);
        }else {
            return new Result(false,MessageConstant.QUERY_CHECKITEM_FAIL);
        }
    }

    /**
     * 上传图片
     * @param imgFile
     * @return
     */
    @RequestMapping("/upload")
    public Result upload(@RequestParam("imgFile") MultipartFile imgFile){
        System.out.println("文件名"+imgFile);
        try {
            //获取原始文件名
            String originalFilename = imgFile.getOriginalFilename();
            System.out.println("真实文件名"+originalFilename);
            int i = originalFilename.lastIndexOf(".");
            //获取文件名后缀
            String substring = originalFilename.substring(i - 1);
            //随机产生一个36位的字符串
            String string = UUID.randomUUID().toString()+substring;
             //获取其文件名
                imgFileS=string;
            ImgFiless=string;
            //将图片名保存在redis中
            Jedis resource = jedisPool.getResource();
            resource.sadd(RedisConstant.SETMEAL_PIC_RESOURCES,string);

            QiniuUtil.upload2Qiniu(imgFile.getBytes(),string);
            return new Result(true,MessageConstant.PIC_UPLOAD_SUCCESS,string);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.PIC_UPLOAD_FAIL);
        }

    }

    /**
     * 删除垃圾图片
     * @return
     */
    @RequestMapping("/handleDelete")
    public Result handleDelete(){
        if(imgFileS!=null&&imgFileS.length()>0){
            QiniuUtils.deleteFileFromQiniu(imgFileS);
                   imgFileS=null;
            return new Result(true,"垃圾图片已删除");
        }else {
            return new Result(false,"无图片删除");
        }
    }


    /**
     * 连接上传的
     * @return
     */
    @RequestMapping("/handleDeletes")
    public Result handleDeletes(){
        if(ImgFiless!=null&&ImgFiless.length()>0){
            QiniuUtils.deleteFileFromQiniu(ImgFiless);
           /* ImgFiless=null;*/
            return new Result(true,"垃圾图片已删除");
        }else {
            return new Result(false,"无图片删除");
        }
    }
    /**
     * 添加套餐
     * @param setmeal
     * @param checkgroupIds
     * @return
     */
    @RequestMapping("/add")
    public Result add(@RequestBody Setmeal setmeal,Integer[] checkgroupIds){

        for (Integer checkgroupId : checkgroupIds) {
            System.out.println(checkgroupId);
        }
        try {
            setmealService.add(setmeal,checkgroupIds);
            return new Result(true,MessageConstant.ADD_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ADD_SETMEAL_FAIL);
        }


    }

    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {

        PageResult pageResult=setmealService.findPage(queryPageBean);

        return pageResult;

    }
}
