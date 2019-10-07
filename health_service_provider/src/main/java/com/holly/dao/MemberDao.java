package com.holly.dao;

import com.github.pagehelper.Page;
import com.holly.pojo.Member;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface MemberDao {


    @Select("select * from t_member")
    public List<Member> findAll();

    @Select({"<script>",
            "SELECT * FROM t_member",
            "WHERE 1=1",
            "<when test='value !=null  and value.length >0  '>",
            "AND fileNumber like CONCAT('%',#{value},'%') or phoneNumber like CONCAT('%',#{value},'%') or name like CONCAT('%',#{value},'%')",
            "</when>",
            "</script>"})
    public Page<Member> selectByCondition(String queryString);

    @Insert("insert into t_member values(#{id},#{fileNumber},#{name},#{sex},#{idCard},#{phoneNumber},#{regTime},#{password},#{email},#{birthday},#{remark})")
    @Options(useGeneratedKeys = true,keyColumn = "id")
    public void add(Member member);

    @Delete("delete from t_member where id = #{id}")
    public void deleteById(Integer id);

    @Select("select * from t_member where id = #{id}")
    public Member findById(Integer id);

    @Select("select * from t_member where phoneNumber = #{phoneNumber}")
    public Member findByTelephone(String telephone);

    @Update("update t_member set fileNumber = #{fileNumber},name = #{name},sex = #{sex}, idCard = #{idCard},phoneNumber = #{phoneNumber},regTime = #{regTime},password = #{password},email = #{email},birthday = #{birthday}, remark = #{remark} where id=#{id} ")
    public void edit(Member member);

    @Select("select count(id) from t_member where regTime <= #{value}")
    public Integer findMemberCountBeforeDate(String date);

    @Select("select count(id) from t_member where regTime = #{value}")
    public Integer findMemberCountByDate(String date);

    @Select("select count(id) from t_member where regTime >= #{value}")
    public Integer findMemberCountAfterDate(String date);

    @Select("select count(id) from t_member")
    public Integer findMemberTotalCount();
}
