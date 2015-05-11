package com.herongwang.p2p.service.users;

import java.util.List;

import com.herongwang.p2p.entity.users.UsersEntity;
import com.herongwang.p2p.model.users.UserModel;

public interface IUserService
{
    /**
     * 查询会员信息列表
     * @param member
     * @return
     */
    public List<UserModel> queryUsers(UserModel member);
    
    /**
     * 更具帐号名称查询会员信息
     * @param account
     * @return
     */
    public UsersEntity getUserByAccount(String account);
    
    /**
     * 根据ID查询会员信息
     * @param id
     * @return
     */
    public UsersEntity getUserById(String id);
    
    /**
     * 新增会员
     * @param member
     */
    public UsersEntity addUser(UsersEntity member);
    
    /**
     * 根据会员ID查询会员详细信息
     */
    public UserModel getUserByUserId(String id);
    
    /**
     * 查询会员数量
     */
    public int getUserNum();
    
}
