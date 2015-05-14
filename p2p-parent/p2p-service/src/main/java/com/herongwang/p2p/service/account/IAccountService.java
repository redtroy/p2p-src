package com.herongwang.p2p.service.account;

import java.math.BigDecimal;

import com.herongwang.p2p.entity.account.AccountEntity;
import com.herongwang.p2p.model.users.UserModel;

public interface IAccountService
{
    /**
     * 增加用户信息
     */
    public UserModel addAccount(AccountEntity account);
    
    /**
     * 根据用户ID 查询用户账户
     */
    public AccountEntity getAccountByCustomerId(String customerId);
    
    /**
     * 更新账户信息
     * @param account
     */
    public void updateAccount(AccountEntity account);
    
    /**
     * 投标更新账户余额
     * @param account
     */
    public int updateAccountBalance(String customerId, BigDecimal balance);
    
}
