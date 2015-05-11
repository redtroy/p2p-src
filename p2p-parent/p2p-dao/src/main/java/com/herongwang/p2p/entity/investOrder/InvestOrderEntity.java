package com.herongwang.p2p.entity.investOrder;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.herongwang.p2p.dao.investOrder.IInvestOrderDao;
import com.sxj.mybatis.orm.annotations.Column;
import com.sxj.mybatis.orm.annotations.Entity;
import com.sxj.mybatis.orm.annotations.GeneratedValue;
import com.sxj.mybatis.orm.annotations.GenerationType;
import com.sxj.mybatis.orm.annotations.Id;
import com.sxj.mybatis.orm.annotations.Table;
import com.sxj.mybatis.pagination.Pagable;

/**
 * 融资申请
 * @author nishaotang
 *
 */
@Entity(mapper = IInvestOrderDao.class)
@Table(name = "InvestOrder")
public class InvestOrderEntity extends Pagable implements Serializable
{
    
    /**
     * 
     */
    private static final long serialVersionUID = -2444843981481631279L;
    
    /**
     * 订单ID
     */
    @Id(column = "orderId")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String orderId;
    
    /**
    * 客户ID
    */
    @Column(name = "customerId")
    private String customerId;
    
    /**
     * 标的ID
     */
    @Column(name = "debtId")
    private String debtId;
    
    /**
     * 订单金额
     */
    @Column(name = "amount")
    private BigDecimal amount;
    
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
     * 到帐时间
     */
    @Column(name = "arriveTime")
    private Date arriveTime;
    
    /**
     * specifAttr
     */
    @Column(name = "specifAttr")
    private Integer specifAttr;
    
    /**
     * 应收本息总额
     */
    @Column(name = "dueTotalAmount")
    private BigDecimal dueTotalAmount;
    
    /**
     * 应收收益总额
     */
    @Column(name = "dueProfitAmount")
    private BigDecimal dueProfitAmount;
    
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
     *总费用
     */
    @Column(name = "totalFee")
    private BigDecimal totalFee;
    
    public String getOrderId()
    {
        return orderId;
    }
    
    public void setOrderId(String orderId)
    {
        this.orderId = orderId;
    }
    
    public String getCustomerId()
    {
        return customerId;
    }
    
    public void setCustomerId(String customerId)
    {
        this.customerId = customerId;
    }
    
    public String getDebtId()
    {
        return debtId;
    }
    
    public void setDebtId(String debtId)
    {
        this.debtId = debtId;
    }
    
    public BigDecimal getAmount()
    {
        return amount;
    }
    
    public void setAmount(BigDecimal amount)
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
    
    public BigDecimal getDueTotalAmount()
    {
        return dueTotalAmount;
    }
    
    public void setDueTotalAmount(BigDecimal dueTotalAmount)
    {
        this.dueTotalAmount = dueTotalAmount;
    }
    
    public BigDecimal getDueProfitAmount()
    {
        return dueProfitAmount;
    }
    
    public void setDueProfitAmount(BigDecimal dueProfitAmount)
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
    
    public BigDecimal getTotalFee()
    {
        return totalFee;
    }
    
    public void setTotalFee(BigDecimal totalFee)
    {
        this.totalFee = totalFee;
    }
    
}
