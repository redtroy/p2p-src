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
import com.sxj.mybatis.orm.annotations.Sn;
import com.sxj.mybatis.orm.annotations.Table;
import com.sxj.mybatis.pagination.Pagable;

@Entity(mapper = IOrdersDao.class)
@Table(name = "Orders")
public class OrdersEntity extends Pagable implements Serializable
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
     * 订单号
     */
    @Column(name = "ordersNo")
    @Sn(pattern = "000000", step = 1, table = "T_SN", stub = "F_SN_NAME", sn = "F_SN_NUMBER", stubValue = "B")
    private String ordersNo;
    
    public String getOrdersNo()
    {
        return ordersNo;
    }
    
    public void setOrdersNo(String ordersNo)
    {
        this.ordersNo = ordersNo;
    }
    
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
     * 乾多多流水号
     */
    @Column(name = "loanNo")
    private String loanNo;
    
    /**
     * 订单签名
     */
    @Column(name = "strSignMsg")
    private String strSignMsg;
    
    /**
     * 会员名称
     */
    private String name;
    
    private String statusText;//状态名称
    
    private String typeText;//类型名称
    
    private String tlNo;//通联订单号
    
    private String manageFee;//手续费
    
    private String actuaFee;//手续费
    
    public String getActuaFee()
    {
        return actuaFee;
    }
    
    public void setActuaFee(String actuaFee)
    {
        this.actuaFee = actuaFee;
    }
    
    public String getManageFee()
    {
        return manageFee;
    }
    
    public void setManageFee(String manageFee)
    {
        this.manageFee = manageFee;
    }
    
    public String getStatusText()
    {
        return statusText;
    }
    
    public void setStatusText(String statusText)
    {
        this.statusText = statusText;
    }
    
    public String getTypeText()
    {
        return typeText;
    }
    
    public void setTypeText(String typeText)
    {
        this.typeText = typeText;
    }
    
    public String getTlNo()
    {
        return tlNo;
    }
    
    public void setTlNo(String tlNo)
    {
        this.tlNo = tlNo;
    }
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public String getLoanNo()
    {
        return loanNo;
    }
    
    public void setLoanNo(String loanNo)
    {
        this.loanNo = loanNo;
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
    
    public String getStrSignMsg()
    {
        return strSignMsg;
    }
    
    public void setStrSignMsg(String strSignMsg)
    {
        this.strSignMsg = strSignMsg;
    }
    
}
