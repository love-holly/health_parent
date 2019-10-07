package com.holly.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.holly.constant.RedisConstant;
import com.holly.dao.SetmealDao;
import com.holly.entity.PageResult;
import com.holly.entity.QueryPageBean;
import com.holly.pojo.CheckGroup;
import com.holly.pojo.Setmeal;
import com.holly.servcie.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {

    @Autowired
     private SetmealDao setmealDao;

    /*开启注解we*/
    @Autowired
    private JedisPool jedisPool;

    /**
     * 添加项获取检查组
     * @return
     */
    @Override
    public List<CheckGroup> findAll() {

          List<CheckGroup> groupList=setmealDao.findAll();
        return groupList;
    }

    /**
     * 添加数据
     * @param setmeal
     * @param checkgroupIds
     */
    @Override
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {

     //   System.out.println("数组的长度是"+checkgroupIds.length);
        for (Integer integer : checkgroupIds) {
            System.out.println("选中的号码"+integer);
        }

        setmealDao.add(setmeal);
        Integer setmealId = setmeal.getId();
        System.out.println("id="+setmealId);
      this.addById(setmealId,checkgroupIds);
        //将图片名保存在redis中
        Jedis resource = jedisPool.getResource();
        resource.sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,setmeal.getImg());
    }

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        String queryString = queryPageBean.getQueryString();
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();

        PageHelper.startPage(currentPage,pageSize);
        Page<Setmeal> setmealPage=setmealDao.findPage(queryString);

        return new PageResult(setmealPage.getTotal(),setmealPage.getResult());
    }


    /**
     * 获取套餐数据
     * @return
     */
    @Override
    public List<Setmeal> findgetSetmeal() {
        List<Setmeal> list  =setmealDao.findgetSetmeal();
        return list;
    }

    @Override
    public Setmeal findById(Integer id) {
        Setmeal setmeal=setmealDao.findById(id);
        return setmeal;
    }


    /**
     * 获取会员套餐数据
     * @return
     */
    @Override
    public List<Map<String, Object>> findSetmealCount() {

        List<Map<String, Object>> mapList=setmealDao.findSetmealCount();

        return mapList;
    }


    public void addById(Integer setmealId,Integer[] checkgroupIds){

        if(checkgroupIds!=null&&checkgroupIds.length>0){
            for (Integer checkgroupId : checkgroupIds) {
                Map<String,Integer> map=new HashMap<>();
                map.put("setmealId",setmealId);
                map.put("checkgroupId",checkgroupId);
                setmealDao.addById(map);
            }
        }


    }
}
