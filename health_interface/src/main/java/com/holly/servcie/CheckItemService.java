package com.holly.servcie;

import com.holly.entity.PageResult;
import com.holly.entity.QueryPageBean;
import com.holly.pojo.CheckItem;

public interface CheckItemService {

    public void add(CheckItem checkItem);


    public PageResult findPage(QueryPageBean queryPageBean);



    public  void deleteById(Integer id);

   public CheckItem findById(Integer id);

   public void edit(CheckItem checkItem);
}
