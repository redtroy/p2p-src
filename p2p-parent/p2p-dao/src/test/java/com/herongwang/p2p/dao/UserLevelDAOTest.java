package com.herongwang.p2p.dao;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.herongwang.p2p.dao.member.IUserLevelDAO;
import com.herongwang.p2p.entity.users.UserLevel;

public class UserLevelDAOTest extends TestBase
{
    @Autowired
    IUserLevelDAO userLevelDAO;
    
    @Test
    public void test()
    {
        
        UserLevel level = new UserLevel();
        level.setName("demo测试2");
        userLevelDAO.createUserLevel(level);
        UserLevel userLevelById = userLevelDAO.getUserLevelById(level.getLevelId());
        Assert.assertEquals(level.getLevelId(), userLevelById.getLevelId());
    }
    
}
