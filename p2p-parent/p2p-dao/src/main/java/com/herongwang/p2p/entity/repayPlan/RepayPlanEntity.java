package com.herongwang.p2p.entity.repayPlan;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.herongwang.p2p.dao.repayPlan.IRepayPlanDao;
import com.sxj.mybatis.orm.annotations.Column;
import com.sxj.mybatis.orm.annotations.Entity;
import com.sxj.mybatis.orm.annotations.GeneratedValue;
import com.sxj.mybatis.orm.annotations.GenerationType;
import com.sxj.mybatis.orm.annotations.Id;
import com.sxj.mybatis.orm.annotations.Table;
import com.sxj.mybatis.pagination.Pagable;

/**
 * 还款计划
 * @author anshaoshuai
 *
 */
@Entity(mapper = IRepayPlanDao.class)
@Table(name = "RepayPlan")
public class RepayPlanEntity extends Pagable implements Serializable
{
    
    /**
     * 
     */
    private static final long serialVersionUID = -4816462783472339716L;
    
    /**
     * 订单号
     */
    @Column(name = "orderId")
    private String orderId;
    
    /**
     * 还款ID
     */
    @Id(column = "planId")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String planId;
    
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
     * 月利息
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
     * 0代表未完成，1代表完成
     */
    @Column(name = "status")
    private Integer status;
    
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
     * 垫付状态
     * 0代表未垫付，1代表垫付
     */
    @Column(name = "prepaidStatus")
    private Integer prepaidStatus;
    
    public String getOrderId()
    {
        return orderId;
    }
    
    public void setOrderId(String orderId)
    {
        this.orderId = orderId;
    }
    
    public String getPlanId()
    {
        return planId;
    }
    
    public void setPlanId(String planId)
    {
        this.planId = planId;
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
    
    public Integer getPrepaidStatus()
    {
        return prepaidStatus;
    }
    
    public void setPrepaidStatus(Integer prepaidStatus)
    {
        this.prepaidStatus = prepaidStatus;
    }
    
}
