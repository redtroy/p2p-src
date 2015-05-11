package com.herongwang.p2p.entity.financing;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.herongwang.p2p.dao.financing.IFinancingOrdersDao;
import com.sxj.mybatis.orm.annotations.Column;
import com.sxj.mybatis.orm.annotations.Entity;
import com.sxj.mybatis.orm.annotations.GeneratedValue;
import com.sxj.mybatis.orm.annotations.GenerationType;
import com.sxj.mybatis.orm.annotations.Id;
import com.sxj.mybatis.orm.annotations.Table;
import com.sxj.mybatis.pagination.Pagable;

/**
 * 借款标
 * @author nishaotang
 *
 */
@Entity(mapper = IFinancingOrdersDao.class)
@Table(name = "FinancingOrders")
public class FinancingOrdersEntity extends Pagable implements Serializable
{
    
    /**
     * 
     */
    private static final long serialVersionUID = -5284030789397025667L;
    /** 
    * 订单ID
    */
   @Column(name = "orderId")
   @GeneratedValue(strategy = GenerationType.UUID)
   private String orderId;
    /**
     * 标的ID
     */
    @Id(column = "debtId")
    private String debtId;
    
    /**
    * 客户ID
    */
    @Column(name = "customerId")
    private String customerId;
    
    
    /**
     *借款金额
     */
    @Column(name = "amount")
    private BigDecimal amount;
    
    /**
     *创建时间
     */
    @Column(name = "createTime")
    private Date createTime;
    /**
     *借款总额
     */
    @Column(name = "loanAmount")
    private BigDecimal loanAmount;
    /**
     *实收总额
     */
    @Column(name = "actualAmount")
    private BigDecimal actualAmount;
    /**
     *总费用
     */
    @Column(name = "totalFee")
    private BigDecimal totalFee;
    /**
     *本息总额
     */
    @Column(name = "totalAmount")
    private BigDecimal totalAmount;
    /**
     *利息总额
     */
    @Column(name = "profitAmount")
    private BigDecimal profitAmount;
    
    /**
     *状态
     *0：未审核；1：投资中；2：流标；3：满标；4：还款中；5：完成。
     */
    @Column(name = "status")
    private Integer status;
    
    /**
     * 会员姓名
     */
    private String name;

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

    public BigDecimal getLoanAmount()
    {
        return loanAmount;
    }

    public void setLoanAmount(BigDecimal loanAmount)
    {
        this.loanAmount = loanAmount;
    }

    public BigDecimal getActualAmount()
    {
        return actualAmount;
    }

    public void setActualAmount(BigDecimal actualAmount)
    {
        this.actualAmount = actualAmount;
    }

    public BigDecimal getTotalFee()
    {
        return totalFee;
    }

    public void setTotalFee(BigDecimal totalFee)
    {
        this.totalFee = totalFee;
    }

    public BigDecimal getTotalAmount()
    {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount)
    {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getProfitAmount()
    {
        return profitAmount;
    }

    public void setProfitAmount(BigDecimal profitAmount)
    {
        this.profitAmount = profitAmount;
    }

    public Integer getStatus()
    {
        return status;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
    
}
