package com.herongwang.p2p.entity.investorder;

import java.io.Serializable;
import java.util.Date;

import com.herongwang.p2p.dao.investorder.IInvestorderDao;
import com.sxj.mybatis.orm.annotations.Column;
import com.sxj.mybatis.orm.annotations.Entity;
import com.sxj.mybatis.orm.annotations.GeneratedValue;
import com.sxj.mybatis.orm.annotations.GenerationType;
import com.sxj.mybatis.orm.annotations.Id;
import com.sxj.mybatis.orm.annotations.Table;

@Entity(mapper = IInvestorderDao.class)
@Table(name = "InvestOrder")
public class InvestorderEntity implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 8198952267890045283L;
    
    /**
     * 订单ID
     */
    @Id(column = "orderId")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String orderId;
    
    /**
     * 标的ID
     */
    @Column(name = "debtId")
    private String debtId;
    
    /**
     * 客户ID
     */
    @Column(name = "customerId")
    private String customerId;
    
    /**
     * 订单金额
     */
    @Column(name = "amount")
    private Long amount;
    
    /**
     * 创建时间
     */
    @Column(name = "createTime")
    private Date createTime;
    
    /**
     * 状态
     */
    @Column(name = "status")
    private Integer status;
    
    /**
     * 支付渠道
     */
    @Column(name = "channel")
    private Integer channel;
    
    /**
     * 支付时间
     */
    @Column(name = "payTime")
    private Date payTime;
    
    /**
     * 到账时间
     */
    @Column(name = "arriveTime")
    private Date arriveTime;
    
    /**
     *
     */
    @Column(name = "specifAttr")
    private Integer specifAttr;
    
    /**
     * 应收本息总额
     */
    @Column(name = "dueTotalAmount")
    private Long dueTotalAmount;
    
    /**
     * 应收收益总额
     */
    @Column(name = "dueProfitAmount")
    private Long dueProfitAmount;
    
    /**
     * 综合年利率
     */
    @Column(name = "yearRatio")
    private Float yearRatio;
    
    /**
     * 综合月利率
     */
    @Column(name = "monthRatio")
    private Float monthRatio;
    
    /**
     * 综合天利率
     */
    @Column(name = "dayRatio")
    private Float dayRatio;
    
    /**
     * 总费用
     */
    @Column(name = "totalFee")
    private Long totalFee;
    
    public String getOrderId()
    {
        return orderId;
    }
    
    public void setOrderId(String orderId)
    {
        this.orderId = orderId;
    }
    
    public String getDebtId()
    {
        return debtId;
    }
    
    public void setDebtId(String debtId)
    {
        this.debtId = debtId;
    }
    
    public String getCustomerId()
    {
        return customerId;
    }
    
    public void setCustomerId(String customerId)
    {
        this.customerId = customerId;
    }
    
    public Long getAmount()
    {
        return amount;
    }
    
    public void setAmount(Long amount)
    {
        this.amount = amount;
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
    
    public Integer getChannel()
    {
        return channel;
    }
    
    public void setChannel(Integer channel)
    {
        this.channel = channel;
    }
    
    public Date getPayTime()
    {
        return payTime;
    }
    
    public void setPayTime(Date payTime)
    {
        this.payTime = payTime;
    }
    
    public Date getArriveTime()
    {
        return arriveTime;
    }
    
    public void setArriveTime(Date arriveTime)
    {
        this.arriveTime = arriveTime;
    }
    
    public Integer getSpecifAttr()
    {
        return specifAttr;
    }
    
    public void setSpecifAttr(Integer specifAttr)
    {
        this.specifAttr = specifAttr;
    }
    
    public Long getDueTotalAmount()
    {
        return dueTotalAmount;
    }
    
    public void setDueTotalAmount(Long dueTotalAmount)
    {
        this.dueTotalAmount = dueTotalAmount;
    }
    
    public Long getDueProfitAmount()
    {
        return dueProfitAmount;
    }
    
    public void setDueProfitAmount(Long dueProfitAmount)
    {
        this.dueProfitAmount = dueProfitAmount;
    }
    
    public Float getYearRatio()
    {
        return yearRatio;
    }
    
    public void setYearRatio(Float yearRatio)
    {
        this.yearRatio = yearRatio;
    }
    
    public Float getMonthRatio()
    {
        return monthRatio;
    }
    
    public void setMonthRatio(Float monthRatio)
    {
        this.monthRatio = monthRatio;
    }
    
    public Float getDayRatio()
    {
        return dayRatio;
    }
    
    public void setDayRatio(Float dayRatio)
    {
        this.dayRatio = dayRatio;
    }
    
    public Long getTotalFee()
    {
        return totalFee;
    }
    
    public void setTotalFee(Long totalFee)
    {
        this.totalFee = totalFee;
    }
    
}
