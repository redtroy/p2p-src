package com.herongwang.p2p.dao.users;

import java.util.List;

import com.herongwang.p2p.entity.users.UsersEntity;
import com.sxj.mybatis.orm.annotations.Get;
import com.sxj.mybatis.orm.annotations.Insert;
import com.sxj.mybatis.orm.annotations.Update;
import com.sxj.util.persistent.QueryCondition;

public interface IUsersDao
{
    /**
     * 查询会员列表
     */
    public List<UsersEntity> queryUserList(QueryCondition<UsersEntity> query);
    
    /**
     * 根据会员帐号查询会员(邮箱帐号)
     * @param account
     * @return
     */
    public UsersEntity getUserByAccount(String email);
    
    /**
     * 新增用户
     */
    @Insert
    public void addUser(UsersEntity member);
    
    /**
     * 根据ID查询用户信息 
     */
    @Get
    public UsersEntity getUserById(String id);
    
    /**
     * 查询会员数量
     */
    public int getUserNum();
    
    /**
     * 更新会员
     */
    @Update
    public void updateUser(UsersEntity user);
}
