package com.holly.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.holly.dao.CheckItemDao;
import com.holly.entity.PageResult;
import com.holly.entity.QueryPageBean;
import com.holly.pojo.CheckItem;
import com.holly.servcie.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(interfaceClass = CheckItemService.class)
@Transactional
public class CheckItemServiceImp implements CheckItemService {

    @Autowired
    private CheckItemDao checkItemDao;

    /*添加检查项*/
    @Override
    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
    }

    /*查询项目*/
    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();

      /*  if (queryString!=null&&queryString.length()>0){
            PageHelper.startPage(1,pageSize);
        }else {*/
            PageHelper.startPage(currentPage,pageSize);


        Page<CheckItem> checkItems = checkItemDao.selectByCondition(queryString);


        return new PageResult(checkItems.getTotal(),checkItems.getResult());
    }



    /*查询表是否被关联*/


    @Override
    public void deleteById(Integer id) {

        long i=checkItemDao.findCountByCheckItem(id);
        if (i>0){
            //如果大于0,说明有关联
            new RuntimeException();
        }
            checkItemDao.deleteById(id);
        }


        /*查询回显信息*/
    @Override
    public CheckItem findById(Integer id) {
        CheckItem checkItem=checkItemDao.findById(id);

        return checkItem;
    }

    @Override
    public void edit(CheckItem checkItem) {
       checkItemDao.edit(checkItem);
    }

}



