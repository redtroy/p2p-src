package com.herongwang.p2p.dao.member;

import com.herongwang.p2p.entity.member.UserLevel;
import com.sxj.mybatis.orm.annotations.Delete;
import com.sxj.mybatis.orm.annotations.Get;
import com.sxj.mybatis.orm.annotations.Insert;
import com.sxj.mybatis.orm.annotations.Update;

public interface IUserLevelDAO
{
    @Insert
    public void createUserLevel(UserLevel userLevel);
    
    @Delete
    public int deleteUserLevelById(String levelId);
    
    @Update
    public int updateUserLevel(UserLevel newUserLevel);
    
    @Get
    public UserLevel getUserLevelById(String levelId);
}
