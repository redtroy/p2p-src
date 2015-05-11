package com.herongwang.p2p.entity.funddetail;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.herongwang.p2p.dao.funddetail.IFundDetailDao;
import com.sxj.mybatis.orm.annotations.Column;
import com.sxj.mybatis.orm.annotations.Entity;
import com.sxj.mybatis.orm.annotations.GeneratedValue;
import com.sxj.mybatis.orm.annotations.GenerationType;
import com.sxj.mybatis.orm.annotations.Id;
import com.sxj.mybatis.orm.annotations.Table;
import com.sxj.mybatis.pagination.Pagable;

/**
 * 资金明细
 * @author anshaoshuai
 *
 */
@Entity(mapper = IFundDetailDao.class)
@Table(name = "FundDetail")
public class FundDetailEntity extends Pagable implements Serializable
{
    
    /**
     * 
     */
    private static final long serialVersionUID = 8625878562295372104L;
    
    /**
     * 明细id
     */
    @Id(column = "detailId")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String detailId;
    
    /**
     * 会员ID
     */
    @Column(name = "customerId")
    private String customerId;
    
    /**
     * 账户ID
     */
    @Column(name = "accountId")
    private String accountId;
    
    /**
     * 订单ID
     */
    @Column(name = "orderId")
    private String orderId;
    
    /**
     * 类型
     */
    @Column(name = "type")
    private Integer type;
    
    /**
     * 金额
     */
    @Column(name = "amount")
    private BigDecimal amount;
    
    /**
     * 账户余额
     */
    @Column(name = "balance")
    private BigDecimal balance;
    
    /**
     * 冻结金额
     */
    @Column(name = "frozenAmount")
    private BigDecimal frozenAmount;
    
    /**
     * 待收金额
     */
    @Column(name = "dueAmount")
    private BigDecimal dueAmount;
    
    /**
     * 交易时间
     */
    @Column(name = "createTime")
    private Date createTime;
    
    /**
     * 状态
     */
    @Column(name = "status")
    private Integer status;
    
    /**
     * 出入状态
     * 0 : 支出  1: 收入
     */
    @Column(name = "incomeStatus")
    private Integer incomeStatus;
    
    /**
     * 说明
     */
    @Column(name = "remark")
    private Integer remark;
    
    public String getDetailId()
    {
        return detailId;
    }
    
    public void setDetailId(String detailId)
    {
        this.detailId = detailId;
    }
    
    public String getCustomerId()
    {
        return customerId;
    }
    
    public void setCustomerId(String customerId)
    {
        this.customerId = customerId;
    }
    
    public String getAccountId()
    {
        return accountId;
    }
    
    public void setAccountId(String accountId)
    {
        this.accountId = accountId;
    }
    
    public String getOrderId()
    {
        return orderId;
    }
    
    public void setOrderId(String orderId)
    {
        this.orderId = orderId;
    }
    
    public Integer getType()
    {
        return type;
    }
    
    public void setType(Integer type)
    {
        this.type = type;
    }
    
    public BigDecimal getAmount()
    {
        return amount;
    }
    
    public void setAmount(BigDecimal amount)
    {
        this.amount = amount;
    }
    
    public BigDecimal getBalance()
    {
        return balance;
    }
    
    public void setBalance(BigDecimal balance)
    {
        this.balance = balance;
    }
    
    public BigDecimal getFrozenAmount()
    {
        return frozenAmount;
    }
    
    public void setFrozenAmount(BigDecimal frozenAmount)
    {
        this.frozenAmount = frozenAmount;
    }
    
    public BigDecimal getDueAmount()
    {
        return dueAmount;
    }
    
    public void setDueAmount(BigDecimal dueAmount)
    {
        this.dueAmount = dueAmount;
    }
    
    public Date getCreateTime()
    {
        return createTime;
    }
    
    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }
    
    public Integer getStatus()
    {
        return status;
    }
    
    public void setStatus(Integer status)
    {
        this.status = status;
    }
    
    public Integer getIncomeStatus()
    {
        return incomeStatus;
    }
    
    public void setIncomeStatus(Integer incomeStatus)
    {
        this.incomeStatus = incomeStatus;
    }
    
    public Integer getRemark()
    {
        return remark;
    }
    
    public void setRemark(Integer remark)
    {
        this.remark = remark;
    }
    
}
