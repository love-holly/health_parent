package com.holly.servcie;

import com.holly.entity.PageResult;
import com.holly.entity.QueryPageBean;
import com.holly.pojo.CheckGroup;
import com.holly.pojo.CheckItem;

import java.util.List;

public interface CheckGroupService {


    List<CheckItem> findAll();

    void add(CheckGroup checkGroup, Integer[] checkitemIds);

    PageResult findPage(QueryPageBean queryPageBean);

    List<Integer> findCheckItemIdsByCheckGroupId(Integer id);

    void edit(CheckGroup checkGroup, Integer[] checkitemIds);

    void deleteyId(Integer id);
}
