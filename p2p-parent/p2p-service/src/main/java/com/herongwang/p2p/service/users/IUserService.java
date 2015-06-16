package com.herongwang.p2p.service.users;

import java.util.List;

import com.herongwang.p2p.entity.users.UsersEntity;

public interface IUserService
{
    /**
     * 查询会员信息列表
     * @param member
     * @return
     */
    public List<UsersEntity> queryUsers(UsersEntity user);
    
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
     * 更新会员
     */
    public UsersEntity updateUser(UsersEntity member);
    
    /**
     * 根据会员ID查询会员详细信息
     */
    public UsersEntity getUserByUserId(String id);
    
    /**
     * 查询会员数量
     */
    public int getUserNum();
    
    /**
     * 改变会员状态
     */
    public int checkStatus(String custId);
    
    /**
     * 会员短信验证码
     */
    public String sendMs(String phone);
    
    /**
     * 检查号码是否被注册
     */
    public Boolean checkPhone(String phone);
    
    /**
     * 检查邮箱是否被注册
     */
    public Boolean checkEmail(String email);
    
}
