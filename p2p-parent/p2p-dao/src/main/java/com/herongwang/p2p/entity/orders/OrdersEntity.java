package com.herongwang.p2p.entity.orders;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.herongwang.p2p.dao.orders.IOrdersDao;
import com.sxj.mybatis.orm.annotations.Column;
import com.sxj.mybatis.orm.annotations.Entity;
import com.sxj.mybatis.orm.annotations.GeneratedValue;
import com.sxj.mybatis.orm.annotations.GenerationType;
import com.sxj.mybatis.orm.annotations.Id;
import com.sxj.mybatis.orm.annotations.Table;

@Entity(mapper = IOrdersDao.class)
@Table(name = "Orders")
public class OrdersEntity implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 2828188766764116079L;
    
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
     * 订单类型
     */
    @Column(name = "orderType")
    private Integer orderType;
    /**
     * 银行卡号
     */
    @Column(name = "bankNo")
    private String bankNo;
    /**
     * 提现时间
     */
    @Column(name = "withdrawTime")
    private Date withdrawTime;
    /**
     * 到帐时间
     */
    @Column(name = "arriveTime")
    private Date arriveTime;
    /**
     * 提现渠道
     */
    @Column(name = "channel")
    private String channel;
    /**
     * 会员名称
     */
    private String name;
    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }
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
    public Integer getOrderType()
    {
        return orderType;
    }
    public void setOrderType(Integer orderType)
    {
        this.orderType = orderType;
    }
    public String getBankNo()
    {
        return bankNo;
    }
    public void setBankNo(String bankNo)
    {
        this.bankNo = bankNo;
    }
    public Date getWithdrawTime()
    {
        return withdrawTime;
    }
    public void setWithdrawTime(Date withdrawTime)
    {
        this.withdrawTime = withdrawTime;
    }
    public Date getArriveTime()
    {
        return arriveTime;
    }
    public void setArriveTime(Date arriveTime)
    {
        this.arriveTime = arriveTime;
    }
    public String getChannel()
    {
        return channel;
    }
    public void setChannel(String channel)
    {
        this.channel = channel;
    }
    
    
}
