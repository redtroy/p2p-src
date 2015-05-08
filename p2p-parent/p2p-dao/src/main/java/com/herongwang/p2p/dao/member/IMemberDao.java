package com.herongwang.p2p.dao.member;

import java.util.List;

import com.herongwang.p2p.entity.member.MemberEntity;
import com.herongwang.p2p.model.member.MemberModel;
import com.sxj.mybatis.orm.annotations.Get;
import com.sxj.mybatis.orm.annotations.Insert;
import com.sxj.util.persistent.QueryCondition;

public interface IMemberDao
{
    /**
     * 查询会员列表
     */
    public List<MemberModel> queryMembers(QueryCondition<MemberModel> query);
    
    /**
     * 根据会员帐号查询会员
     * @param account
     * @return
     */
    public MemberEntity getMmeberByAccount(String account);
    
    /**
     * 新增会员
     */
    @Insert
    public void addMember(MemberEntity member);
    
    /**
     * 根据ID查询用户信息 
     */
    @Get
    public MemberEntity getMemberById(String id);
}
