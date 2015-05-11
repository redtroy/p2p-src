package com.herongwang.p2p.entity.fundDetail;

import java.io.Serializable;
import java.util.Date;

import com.herongwang.p2p.dao.fundDetail.IFundDetailDao;
import com.sxj.mybatis.orm.annotations.Column;
import com.sxj.mybatis.orm.annotations.Entity;
import com.sxj.mybatis.orm.annotations.GeneratedValue;
import com.sxj.mybatis.orm.annotations.GenerationType;
import com.sxj.mybatis.orm.annotations.Id;
import com.sxj.mybatis.orm.annotations.Table;
import com.sxj.mybatis.pagination.Pagable;

/**
 * 资金明细
 * @author Administrator
 *
 */
@Entity(mapper = IFundDetailDao.class)
@Table(name = "FUND_DETAIL")
public class FundDetailEntity extends Pagable implements Serializable
{
    
    /**
     * 
     */
    private static final long serialVersionUID = 8625878562295372104L;
    
    /**
     * 明细id
     */
    @Id(column = "DETAIL_ID")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String detailId;
    
    /**
     * 会员ID
     */
    @Column(name = "MEMBER_ID")
    private String memberId;
    
    /**
     * 类型
     */
    @Column(name = "TYPE")
    private Integer type;
    
    /**
     * 金额
     */
    @Column(name = "AMOUNT")
    private Double amount;
    
    /**
     * 账户余额
     */
    @Column(name = "BALANCE")
    private Double balance;
    
    /**
     * 冻结金额
     */
    @Column(name = "FROZEN_AMOUNT")
    private Double frozenAmount;
    
    /**
     * 待收金额
     */
    @Column(name = "DUE_AMOUNT")
    private Double dueAmount;
    
    /**
     * 交易时间
     */
    @Column(name = "CREATE_TIME")
    private Date createTime;
    
    /**
     * 状态
     */
    @Column(name = "STATUS")
    private Integer status;
    
    /**
     * 出入状态
     * 0 : 支出  1: 收入
     */
    @Column(name = "INCOME_STATUS")
    private Integer incomeStatus;
    
    /**
     * 说明
     */
    @Column(name = "REMARK")
    private Integer remark;
    
    public String getDetailId()
    {
        return detailId;
    }
    
    public void setDetailId(String detailId)
    {
        this.detailId = detailId;
    }
    
    public String getMemberId()
    {
        return memberId;
    }
    
    public void setMemberId(String memberId)
    {
        this.memberId = memberId;
    }
    
    public Integer getType()
    {
        return type;
    }
    
    public void setType(Integer type)
    {
        this.type = type;
    }
    
    public Double getAmount()
    {
        return amount;
    }
    
    public void setAmount(Double amount)
    {
        this.amount = amount;
    }
    
    public Double getBalance()
    {
        return balance;
    }
    
    public void setBalance(Double balance)
    {
        this.balance = balance;
    }
    
    public Double getFrozenAmount()
    {
        return frozenAmount;
    }
    
    public void setFrozenAmount(Double frozenAmount)
    {
        this.frozenAmount = frozenAmount;
    }
    
    public Double getDueAmount()
    {
        return dueAmount;
    }
    
    public void setDueAmount(Double dueAmount)
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
