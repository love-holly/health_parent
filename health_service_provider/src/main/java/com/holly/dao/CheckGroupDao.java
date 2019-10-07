package com.holly.dao;

import com.github.pagehelper.Page;
import com.holly.pojo.CheckGroup;
import com.holly.pojo.CheckItem;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;
import java.util.Map;

//@Mapper
public interface CheckGroupDao {

/*查询检查项*/
    @Select("select *from t_checkitem")
    List<CheckItem> findAll();


    //添加检查组
      @Insert("INSERT INTO t_checkgroup(code,name,helpCode,sex,remark,attention) " +
              " VALUES(#{checkGroup.code},#{checkGroup.name},#{checkGroup.helpCode}," +
              "#{checkGroup.sex},#{checkGroup.remark},#{checkGroup.attention})")
      @Options(useGeneratedKeys=true,keyProperty="checkGroup.id", keyColumn="id")
    Integer add(@Param("checkGroup") CheckGroup checkGroup);

      @Insert("Insert into t_checkgroup_checkitem values(#{checkgroup_id},#{checkitem_id})")
      void addById(Map map);


      @Select({"<script>",
              "SELECT * FROM t_checkgroup",
              "WHERE 1=1",
              "<when test='value !=null  and value.length >0  '>",
              "AND code like CONCAT('%',#{value},'%') or name like CONCAT('%',#{value},'%') or helpCode like CONCAT('%',#{value},'%')",
              "</when>",

              "</script>"})
    Page<CheckGroup> findPage(String queryString);



      @Select("select checkitem_id from t_checkgroup_checkitem where checkgroup_id=#{id}")
    List<Integer> findCheckItemIdsByCheckGroupId(Integer id);


      @Update("UPDATE t_checkgroup SET code=#{code},name=#{name},helpCode=#{helpCode},sex=#{sex},remark=#{remark},attention=#{attention} where id=#{id}")
    void edit(CheckGroup checkGroup);

    @Delete("delete from t_checkgroup_checkitem where checkgroup_id=#{id}")
    void deleteById(Integer id);

    @Delete("delete from t_checkgroup where id=#{id}")
    void deleteByCheckgroup(Integer id);


     @Results({
             @Result(id = true,column ="id",property = "id"),
             @Result(column ="code",property = "code"),
             @Result(column ="name",property = "name"),
             @Result(column ="helpCode",property = "helpCode"),
             @Result(column ="sex",property = "sex"),
             @Result(column ="remark",property = "remark"),
             @Result(column ="attention",property = "attention"),
             @Result(property ="checkItems",column = "id",javaType = List.class,
             many = @Many(select = "com.holly.dao.CheckItemDao.findByIds")
             ),
     })
     @Select("SELECT * FROM t_setmeal_checkgroup a,t_checkgroup b WHERE a.`checkgroup_id`=b.`id` AND setmeal_id=#{setmeal_id} ")
    List<CheckGroup> findById(Integer setmeal_id);
}
