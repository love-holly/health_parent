package com.holly.dao;

import com.github.pagehelper.Page;
import com.holly.entity.PageResult;
import com.holly.entity.QueryPageBean;
import com.holly.pojo.CheckItem;
import org.apache.ibatis.annotations.*;

import java.util.List;


public interface CheckItemDao {

   /*添加检查组*/
    @Insert("INSERT INTO t_checkitem VALUES(#{id},#{code},#{name},#{sex},#{age},#{price},#{type},#{attention},#{remark}) ")
    public void add(CheckItem checkItem);


    /*查询*/
  /*  @Select("select *from t_checkitem where code like #{code}")*/

    @Select({"<script>",
            "SELECT * FROM t_checkitem",
            "WHERE 1=1",
            "<when test='value !=null and value.length >0 '>",
            "AND code like CONCAT('%',#{value},'%') or name like CONCAT('%',#{value},'%')",
            "</when>",

            "</script>"})
    public Page<CheckItem> selectByCondition( String queryString);



    /*查询此项是否在项目组中*/
   @Select("select count(*) from t_checkgroup_checkitem where checkitem_id=#{id}")
    public Long findCountByCheckItem(Integer id);

    /*删除检查项*/
    @Delete("delete from t_checkitem where id =#{id}")
   public void deleteById(Integer id);

   @Select("select *from t_checkitem where id=#{id}")
   public CheckItem findById(Integer id);


   @Update("UPDATE t_checkitem SET CODE=#{code},name=#{name},sex=#{sex},age=#{age},price=#{price},type=#{type},attention=#{attention},remark=#{remark} where id=#{id} ")
   public void edit(CheckItem checkItem);



  /* @Results({
           @Result(column = "id",property = "id"),
           @Result(column = "code",property = "code"),
           @Result(column = "name",property = "name"),
           @Result(column = "sex",property = "sex"),
           @Result(column = "age",property = "age"),
           @Result(column = "price",property = "price"),
           @Result(column = "type",property = "type"),
           @Result(column = "attention",property = "attention"),
           @Result(column = "remark",property = "remark"),
   })*/
   /*@Select("select * from t_checkitem where id in (select checkitem_id from t_checkgroup_checkitem where checkgroup_id=#{checkgroup_id})")*/
   @Select("SELECT *FROM t_checkgroup_checkitem a,t_checkitem b WHERE a.`checkitem_id`=b.`id` AND a.`checkgroup_id`=#{checkgroup_id}")
   List<CheckItem> findByIds(Integer checkgroup_id);

}
