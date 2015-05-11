package com.herongwang.p2p.service.member;

import java.util.List;

import com.herongwang.p2p.entity.users.UsersEntity;
import com.herongwang.p2p.model.users.UserModel;

public interface IMemberService
{
    /**
     * 查询会员信息李彪
     * @param member
     * @return
     */
    public List<UserModel> queryMemberInfo(UserModel member);
    
    /**
     * 更具帐号名称查询会员信息
     * @param account
     * @return
     */
    public UsersEntity getMmeberByAccount(String account);
    
    /**
     * 根据ID查询会员信息
     * @param id
     * @return
     */
    public UsersEntity getMmeberById(String id);
    
    /**
     * 新增会员
     * @param member
     */
    public UsersEntity addMember(UsersEntity member);
    
    /**
     * 根据会员ID查询会员详细信息
     */
    public UserModel getMmeberByMemberId(String id);
    
    /**
     * 查询会员数量
     */
    public int getMemberNum();
    
}
