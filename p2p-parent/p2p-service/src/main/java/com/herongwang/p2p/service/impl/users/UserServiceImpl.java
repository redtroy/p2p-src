package com.herongwang.p2p.service.impl.users;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.herongwang.p2p.dao.account.IAccountDao;
import com.herongwang.p2p.dao.users.IUsersDao;
import com.herongwang.p2p.entity.account.AccountEntity;
import com.herongwang.p2p.entity.users.UsersEntity;
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
    private IAccountDao accountDao;
    
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
            member.setStatus(0);
            userDao.addUser(member);
            AccountEntity account = new AccountEntity();
            account.setCustomerId(member.getCustomerId());
            accountDao.addAccount(account);
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
    
    @Override
    public int checkStatus(String custId) throws ServiceException
    {
        try
        {
            int status;
            UsersEntity user = userDao.getUserById(custId);
            if (user.getStatus() == 0)
            {
                user.setStatus(1);
                status = 1;
            }
            else
            {
                user.setStatus(0);
                status = 0;
            }
            userDao.updateUser(user);
            return status;
        }
        catch (Exception e)
        {
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new ServiceException("改变会员状态错误", e);
        }
    }
    
}
