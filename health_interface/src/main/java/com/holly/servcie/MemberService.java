package com.holly.servcie;

import com.holly.pojo.Member;

import java.util.List;

public interface MemberService {
    Member findBytelephone(String telephone);

    void add(Member member);

    List<Integer> getMemberReport(List<String> list);
}
