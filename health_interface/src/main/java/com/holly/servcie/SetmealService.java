package com.holly.servcie;

import com.holly.entity.PageResult;
import com.holly.entity.QueryPageBean;
import com.holly.pojo.CheckGroup;
import com.holly.pojo.Setmeal;

import java.util.List;
import java.util.Map;

public interface SetmealService {

    List<CheckGroup> findAll();

    void add(Setmeal setmeal, Integer[] checkgroupIds);

    PageResult findPage(QueryPageBean queryPageBean);

    List<Setmeal> findgetSetmeal();

    Setmeal findById(Integer id);

    List<Map<String,Object>> findSetmealCount();

}
