package com.herongwang.p2p.entity.account;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.herongwang.p2p.dao.account.IAccountDao;
import com.sxj.mybatis.orm.annotations.Column;
import com.sxj.mybatis.orm.annotations.Entity;
import com.sxj.mybatis.orm.annotations.GeneratedValue;
import com.sxj.mybatis.orm.annotations.GenerationType;
import com.sxj.mybatis.orm.annotations.Id;
import com.sxj.mybatis.orm.annotations.Table;

@Entity(mapper = IAccountDao.class)
@Table(name = "Accounts")
public class AccountEntity implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = -3102751924185282943L;
    
    /**
     * 主键ID
     */
    @Id(column = "accountId")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String accountId;
    
    /**
     * 会员ID
     */
    @Column(name = "customerId")
    private String customerId;
    
    /**
     * 可用金额
     */
    @Column(name = "balance")
    private BigDecimal balance;
    
    /**
     * 创建时间
     */
    @Column(name = "createTime")
    private Date createTime;
    
    /**
     * 更新时间
     */
    @Column(name = "updateTime")
    private Date updateTime;
    
    /**
     * 状态
     */
    @Column(name = "ststus")
    private Integer ststus;
    
    public String getAccountId()
    {
        return accountId;
    }
    
    public void setAccountId(String accountId)
    {
        this.accountId = accountId;
    }
    
    public String getCustomerId()
    {
        return customerId;
    }
    
    public void setCustomerId(String customerId)
    {
        this.customerId = customerId;
    }
    
    public BigDecimal getBalance()
    {
        return balance;
    }
    
    public void setBalance(BigDecimal balance)
    {
        this.balance = balance;
    }
    
    public Date getCreateTime()
    {
        return createTime;
    }
    
    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }
    
    public Date getUpdateTime()
    {
        return updateTime;
    }
    
    public void setUpdateTime(Date updateTime)
    {
        this.updateTime = updateTime;
    }
    
    public Integer getStstus()
    {
        return ststus;
    }
    
    public void setStstus(Integer ststus)
    {
        this.ststus = ststus;
    }
    
}
