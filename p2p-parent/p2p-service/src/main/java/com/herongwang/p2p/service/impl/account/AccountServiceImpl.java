package com.herongwang.p2p.service.impl.account;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.herongwang.p2p.dao.account.IAccountDao;
import com.herongwang.p2p.dao.users.IUsersDao;
import com.herongwang.p2p.entity.account.AccountEntity;
import com.herongwang.p2p.service.account.IAccountService;
import com.sxj.util.exception.ServiceException;
import com.sxj.util.logger.SxjLogger;
import com.sxj.util.persistent.QueryCondition;

@Service
public class AccountServiceImpl implements IAccountService
{
    @Autowired
    private IAccountDao accountDao;
    
    @Autowired
    private IUsersDao userDao;
    
    @Override
    @Transactional
    public void addAccount(AccountEntity account) throws ServiceException
    {
        try
        {
            accountDao.addAccount(account);
        }
        catch (Exception e)
        {
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new ServiceException("增加账户信息错误", e);
        }
        
    }
    
    @Override
    public AccountEntity getAccountByCustomerId(String customerId)
            throws ServiceException
    {
        try
        {
            return accountDao.getAcoountByCustomerId(customerId);
        }
        catch (Exception e)
        {
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new ServiceException("查询账户信息错误", e);
        }
        
    }
    
    @Override
    @Transactional
    public void updateAccount(AccountEntity account)
    {
        accountDao.updateAccount(account);
        
    }
    
    @Override
    @Transactional
    public int updateAccountBalance(String customerId, BigDecimal balance)
    {
        QueryCondition<AccountEntity> condition = new QueryCondition<AccountEntity>();
        condition.addCondition("customerId", customerId);
        condition.addCondition("balance", balance);
        return accountDao.updateAccountBalance(condition);
        
    }
    
}
