package com.herongwang.p2p.service.member;

import java.util.List;

import com.herongwang.p2p.entity.member.MemberEntity;
import com.herongwang.p2p.model.member.MemberModel;

public interface IMemberService
{
    public List<MemberModel> queryMemberInfo(MemberModel member);
    
    public MemberEntity getMmeberByAccount(String account);
}
