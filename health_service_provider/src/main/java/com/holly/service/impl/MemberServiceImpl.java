package com.holly.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.holly.dao.MemberDao;
import com.holly.pojo.Member;
import com.holly.servcie.MemberService;
import com.holly.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service(interfaceClass = MemberService.class)
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberDao memberDao;

    /**
     * 查询是否有该手机号的会员
     * @param telephone
     * @return
     */
    @Override
    public Member findBytelephone(String telephone) {

        Member member = memberDao.findByTelephone(telephone);
        return member;
    }

    /**
     * 如果没有注册成会员,现在注册
     * @param member
     */
    @Override
    public void add(Member member) {
        //首先判断是否有密码
        String password = member.getPassword();
        if(password!=null){
            //若有密码,则对密码进行加密
            password = MD5Utils.md5(password);
            member.setPassword(password);
        }
        //保存会员
        memberDao.add(member);

    }

    /**
     * 获取每个月用户数据
     * @param list
     * @return
     */
    @Override
    public List<Integer> getMemberReport(List<String> list) {
        List<Integer> memberCount =new ArrayList<>();

        for (String monthLists : list) {
            String monthList=monthLists+".31";//转换成2019.09.19
            Integer memberCountDate = memberDao.findMemberCountBeforeDate(monthList);
            memberCount.add(memberCountDate);
        }


        return memberCount;
    }
}
