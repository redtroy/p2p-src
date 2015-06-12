package com.herongwang.p2p.entity.profitlist;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.herongwang.p2p.dao.profitlist.IProfitListDao;
import com.sxj.mybatis.orm.annotations.Column;
import com.sxj.mybatis.orm.annotations.Entity;
import com.sxj.mybatis.orm.annotations.GeneratedValue;
import com.sxj.mybatis.orm.annotations.GenerationType;
import com.sxj.mybatis.orm.annotations.Id;
import com.sxj.mybatis.orm.annotations.Table;
import com.sxj.mybatis.pagination.Pagable;

/**
 * 收益明细
 * @author anshaoshuai
 *
 */
@Entity(mapper = IProfitListDao.class)
@Table(name = "ProfitList")
public class ProfitListEntity extends Pagable implements Serializable
{
    
    /**
     * 
     */
    private static final long serialVersionUID = 2063433286701017069L;
    
    /**
     * 明细ID
     */
    @Id(column = "profitId")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String profitId;
    
    /**
     * 订单ID
     */
    @Column(name = "orderId")
    private String orderId;
    
    /**
     * 序号
     */
    @Column(name = "sequence")
    private Integer sequence;
    
    /**
     * 月本金
     */
    @Column(name = "monthCapital")
    private BigDecimal monthCapital;
    
    /**
     * 月收益
     */
    @Column(name = "monthProfit")
    private BigDecimal monthProfit;
    
    /**
     * 月总额
     */
    @Column(name = "monthAmount")
    private BigDecimal monthAmount;
    
    /**
     * 剩余本息总额
     */
    @Column(name = "leftAmount")
    private BigDecimal leftAmount;
    
    /**
     * 状态 
     * 0 未结算（未还），1 已结算（已还）
     */
    @Column(name = "status")
    private Integer status;
    
    /**
     * 创建时间
     */
    @Column(name = "createTime")
    private Date createTime;
    
    /**
     *更新时间
     */
    @Column(name = "updateTime")
    private Date updateTime;
    
    /**
     * 手续费
     */
    @Column(name = "fee")
    private BigDecimal fee;
    
    /**
     * 订单ID
     */
    @Column(name = "loanNo")
    private String loanNo;
    
    public String getLoanNo()
    {
        return loanNo;
    }
    
    public void setLoanNo(String loanNo)
    {
        this.loanNo = loanNo;
    }
    
    public BigDecimal getFee()
    {
        return fee;
    }
    
    public void setFee(BigDecimal fee)
    {
        this.fee = fee;
    }
    
    public String getProfitId()
    {
        return profitId;
    }
    
    public void setProfitId(String profitId)
    {
        this.profitId = profitId;
    }
    
    public String getOrderId()
    {
        return orderId;
    }
    
    public void setOrderId(String orderId)
    {
        this.orderId = orderId;
    }
    
    public Integer getSequence()
    {
        return sequence;
    }
    
    public void setSequence(Integer sequence)
    {
        this.sequence = sequence;
    }
    
    public BigDecimal getMonthCapital()
    {
        return monthCapital;
    }
    
    public void setMonthCapital(BigDecimal monthCapital)
    {
        this.monthCapital = monthCapital;
    }
    
    public BigDecimal getMonthProfit()
    {
        return monthProfit;
    }
    
    public void setMonthProfit(BigDecimal monthProfit)
    {
        this.monthProfit = monthProfit;
    }
    
    public BigDecimal getMonthAmount()
    {
        return monthAmount;
    }
    
    public void setMonthAmount(BigDecimal monthAmount)
    {
        this.monthAmount = monthAmount;
    }
    
    public BigDecimal getLeftAmount()
    {
        return leftAmount;
    }
    
    public void setLeftAmount(BigDecimal leftAmount)
    {
        this.leftAmount = leftAmount;
    }
    
    public Integer getStatus()
    {
        return status;
    }
    
    public void setStatus(Integer status)
    {
        this.status = status;
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
    
}
