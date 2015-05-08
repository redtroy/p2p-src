package com.herongwang.p2p.service.member;

import java.util.List;

import com.herongwang.p2p.entity.member.MemberEntity;
import com.herongwang.p2p.model.member.MemberModel;

public interface IMemberService
{
    /**
     * 查询会员信息李彪
     * @param member
     * @return
     */
    public List<MemberModel> queryMemberInfo(MemberModel member);
    
    /**
     * 更具帐号名称查询会员信息
     * @param account
     * @return
     */
    public MemberEntity getMmeberByAccount(String account);
    
    /**
     * 根据ID查询会员信息
     * @param id
     * @return
     */
    public MemberEntity getMmeberById(String id);
    
    /**
     * 新增会员
     * @param member
     */
    public MemberEntity addMember(MemberEntity member);
    
    /**
     * 根据会员ID查询会员详细信息
     */
    public MemberModel getMmeberByMemberId(String id);
    
}
