package com.herongwang.p2p.service.impl.users;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.herongwang.p2p.dao.users.IUsersDao;
import com.herongwang.p2p.entity.users.UsersEntity;
import com.herongwang.p2p.service.users.IUserService;
import com.sxj.util.exception.ServiceException;
import com.sxj.util.logger.SxjLogger;
import com.sxj.util.persistent.QueryCondition;

@Service
public class UserServiceImpl implements IUserService
{
    @Autowired
    private IUsersDao userDao;
    
    @Override
    public List<UsersEntity> queryUsers(UsersEntity user)
            throws ServiceException
    {
        try
        {
            QueryCondition<UsersEntity> condition = new QueryCondition<UsersEntity>();
            List<UsersEntity> userList = new ArrayList<UsersEntity>();
            if (user == null)
            {
                return userList;
            }
            /* 
             *  
            condition.setPage(query);*/
            userList = userDao.queryUserList(condition);
            // query.setPage(condition);
            return userList;
        }
        catch (Exception e)
        {
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new ServiceException("查询会员列表信息错误", e);
        }
    }
    
    @Override
    public UsersEntity getUserByAccount(String account) throws ServiceException
    {
        try
        {
            return userDao.getUserByAccount(account);
        }
        catch (Exception e)
        {
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new ServiceException("查询会员列表信息错误", e);
        }
    }
    
    @Override
    public UsersEntity getUserById(String id) throws ServiceException
    {
        try
        {
            return userDao.getUserById(id);
        }
        catch (Exception e)
        {
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new ServiceException("查询会员信息错误", e);
        }
        
    }
    
    @Override
    public UsersEntity addUser(UsersEntity member) throws ServiceException
    {
        try
        {
            userDao.addUser(member);
            return member;
        }
        catch (Exception e)
        {
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new ServiceException("新增会员信息错误", e);
        }
        
    }
    
    @Override
    public UsersEntity getUserByUserId(String id) throws ServiceException
    {
        try
        {
            return userDao.getUserById(id);
        }
        catch (Exception e)
        {
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new ServiceException("查询会员详细信息错误", e);
        }
    }
    
    @Override
    public int getUserNum()
    {
        try
        {
            return userDao.getUserNum();
        }
        catch (Exception e)
        {
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new ServiceException("查询会员数量错误", e);
        }
    }
    
}
