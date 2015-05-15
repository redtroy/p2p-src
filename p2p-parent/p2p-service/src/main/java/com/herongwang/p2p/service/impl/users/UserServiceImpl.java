package com.herongwang.p2p.service.impl.users;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.herongwang.p2p.dao.users.IUsersDao;
import com.herongwang.p2p.entity.account.AccountEntity;
import com.herongwang.p2p.entity.users.UsersEntity;
import com.herongwang.p2p.service.account.IAccountService;
import com.herongwang.p2p.service.users.IUserService;
import com.sxj.util.common.EncryptUtil;
import com.sxj.util.exception.ServiceException;
import com.sxj.util.logger.SxjLogger;
import com.sxj.util.persistent.QueryCondition;

@Service
public class UserServiceImpl implements IUserService
{
    @Autowired
    private IUsersDao userDao;
    
    @Autowired
    private IAccountService accountService;
    
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
            member.setPassword(EncryptUtil.md5Hex(member.getPassword()));
            member.setRegisterTime(new Date());
            userDao.addUser(member);
            AccountEntity account = new AccountEntity();
            account.setCustomerId(member.getCustomerId());
            accountService.addAccount(account);
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
            Integer num = userDao.getUserNum();
            if (num != null)
            {
                return num;
            }
            return 0;
        }
        catch (Exception e)
        {
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new ServiceException("查询会员数量错误", e);
        }
    }
    
    @Override
    public UsersEntity updateUser(UsersEntity member)
    {
        try
        {
            userDao.updateUser(member);
            return userDao.getUserById(member.getCustomerId());
        }
        catch (Exception e)
        {
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new ServiceException("查询会员数量错误", e);
        }
    }
    
}
