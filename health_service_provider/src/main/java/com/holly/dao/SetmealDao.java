package com.holly.dao;

import com.github.pagehelper.Page;
import com.holly.pojo.CheckGroup;
import com.holly.pojo.Setmeal;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;
import java.util.Map;

public interface SetmealDao {

    @Select("select *from t_checkgroup")
    List<CheckGroup> findAll();

    @Insert("INSERT INTO t_setmeal VALUES(#{id},#{name},#{code},#{helpCode},#{sex},#{age},#{price},#{remark},#{attention},#{img})")
    @Options(useGeneratedKeys=true, keyColumn="id")
    void add(Setmeal setmeal);

   @Insert("insert into t_setmeal_checkgroup values(#{setmealId},#{checkgroupId})")
    void addById(Map<String, Integer> map);

   @Select({"<script>",
           "SELECT * FROM t_setmeal",
           "WHERE 1=1",
           "<when test='value !=null  and value.length >0  '>",
           "AND code like CONCAT('%',#{value},'%') or name like CONCAT('%',#{value},'%') or helpCode like CONCAT('%',#{value},'%')",
           "</when>",
           "</script>"})
    Page<Setmeal> findPage(String queryString);


   @Select("select *from t_setmeal")
    List<Setmeal> findgetSetmeal();


   @Select("select * from t_setmeal where id=#{id}")
   @Results({
           @Result(id = true,column ="id",property = "id"),
           @Result(column ="name",property = "name"),
           @Result(column ="code",property = "code"),
           @Result(column ="helpCode",property = "helpCode"),
           @Result(column ="sex",property = "sex"),
           @Result(column ="age",property = "age"),
           @Result(column ="price",property = "price"),
           @Result(column ="remark",property = "remark"),
           @Result(column ="attention",property = "attention"),
           @Result(column ="img",property = "img"),
           @Result(property = "checkGroups",column = "id",
                   javaType = List.class,
           many = @Many(select = "com.holly.dao.CheckGroupDao.findById")
           )

   })
    Setmeal findById( Integer id);


   @Select("SELECT s.`name`,COUNT(o.`id` ) as value FROM t_setmeal s,t_order o WHERE s.`id`=o.`setmeal_id` GROUP BY s.`name`")
    List<Map<String,Object>> findSetmealCount();
}
