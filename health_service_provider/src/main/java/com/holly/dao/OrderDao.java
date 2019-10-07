package com.holly.dao;

import com.holly.pojo.Order;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

public interface OrderDao {

    @Insert("insert into t_order values (#{id}, #{memberId},#{orderDate},#{orderType},#{orderStatus},#{setmealId})")
    @Options(useGeneratedKeys = true,keyColumn = "id")
    public void add(Order order);


    @Select({"<script>",
            "SELECT * FROM t_order",
            "WHERE 1=1",
            "<when test='id !=null '>",
            "AND and id = #{id}",
            "</when>",
            "<when test='memberId !=null '>",
            "and member_id = #{memberId}",
            "</when>",
            "<when test='orderDate !=null '>",
            "and orderDate = #{orderDate}",
            "</when>",
            "<when test='orderType !=null '>",
            "and orderType = #{orderType}",
            "</when>",
            "<when test='orderStatus !=null '>",
            " and orderStatus = #{orderStatus}",
            "</when>",
            "<when test='setmealId !=null '>",
            " and setmeal_id = #{setmealId}",
            "</when>",
            "</script>"})
    @Results({
            @Result(id = true,column ="id",property = "id"),
            @Result(column ="member_id",property = "memberId"),
            @Result(column ="orderDate",property = "orderDate"),
            @Result(column ="orderType",property = "orderType"),
            @Result(column ="orderStatus",property = "orderStatus"),
            @Result(column ="setmeal_id",property = "setmealId"),

    })
    public List<Order> findByCondition(Order order);

    @Select(" select m.name member ,s.name setmeal,o.orderDate orderDate,o.orderType orderType from t_order o, t_member m, t_setmeal s where o.member_id=m.id and o.setmeal_id=s.id and o.id=#{id}")
    public Map findById4Detail(Integer id) throws Exception;

    @Select("select count(id) from t_order where orderDate = #{date}")
    public Integer findOrderCountByDate(String date);
    @Select("select count(id) from t_order where orderDate >= #{date}")
    public Integer findOrderCountAfterDate(String date);

    @Select("select count(id) from t_order where orderDate = #{date} and orderStatus = '已到诊'")
    public Integer findVisitsCountByDate(String date);
    @Select("select count(id) from t_order where orderDate >= #{date} and orderStatus = '已到诊'")
    public Integer findVisitsCountAfterDate(String date);
    @Select("select s.name, count(o.id) setmeal_count ,count(o.id)/(select count(id) from t_order) proportion from t_order o inner join t_setmeal s on s.id = o.setmeal_id group by o.setmeal_id order by setmeal_count desc limit 0,2")
    public List<Map> findHotSetmeal();
}
