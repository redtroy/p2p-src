package com.herongwang.p2p.service.impl.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.herongwang.p2p.dao.account.IAccountDao;
import com.herongwang.p2p.dao.member.IMemberDao;
import com.herongwang.p2p.entity.account.AccountEntity;
import com.herongwang.p2p.model.member.MemberModel;
import com.herongwang.p2p.service.account.IAccountService;
import com.sxj.util.exception.ServiceException;
import com.sxj.util.logger.SxjLogger;

@Service
public class AccountServiceImpl implements IAccountService
{
    @Autowired
    private IAccountDao accountDao;
    
    @Autowired
    private IMemberDao memberDao;
    
    @Override
    public MemberModel addAccount(AccountEntity account)
            throws ServiceException
    {
        try
        {
            accountDao.addAccount(account);
            return memberDao.getMemberModelByMemberId(account.getMemberId());
        }
        catch (Exception e)
        {
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new ServiceException("增加账户信息错误", e);
        }
        
    }
    
}
