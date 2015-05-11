package com.herongwang.p2p.service.impl.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.herongwang.p2p.dao.account.IAccountDao;
import com.herongwang.p2p.dao.users.IUsersDao;
import com.herongwang.p2p.entity.account.AccountEntity;
import com.herongwang.p2p.model.users.UserModel;
import com.herongwang.p2p.service.account.IAccountService;
import com.sxj.util.exception.ServiceException;
import com.sxj.util.logger.SxjLogger;

@Service
public class AccountServiceImpl implements IAccountService
{
    @Autowired
    private IAccountDao accountDao;
    
    @Autowired
    private IUsersDao memberDao;
    
    @Override
    public UserModel addAccount(AccountEntity account)
            throws ServiceException
    {
        try
        {
            accountDao.addAccount(account);
            return memberDao.getUserModelByUserId(account.getMemberId());
        }
        catch (Exception e)
        {
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new ServiceException("增加账户信息错误", e);
        }
        
    }
    
}
