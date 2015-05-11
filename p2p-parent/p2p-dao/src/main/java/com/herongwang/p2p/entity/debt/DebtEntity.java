package com.herongwang.p2p.entity.debt;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.herongwang.p2p.dao.debt.IDebtDao;
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
@Entity(mapper = IDebtDao.class)
@Table(name = "Debt")
public class DebtEntity extends Pagable implements Serializable
{
    
    /**
     * 
     */
    private static final long serialVersionUID = -5284030789397025667L;
    
    /**
     * 标的ID
     */
    @Id(column = "debtId")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String debtId;
    
    /**
    * 客户ID
    */
    @Column(name = "customerId")
    private String customerId;
    
    /**
     * 标题
     */
    @Column(name = "title")
    private String title;
    
    /**
     * 还款类型
     * 1：按月分期还款；2：每月还息到期还本；3：一次性还款。
     */
    @Column(name = "repayType")
    private Integer repayType;
    
    /**
     *借款期限
     */
    @Column(name = "deadLine")
    private Date deadLine;
    
    /**
     *借款期限月
     */
    @Column(name = "months")
    private Integer months;
    
    /**
     *年化利率
     */
    @Column(name = "annualizedRate")
    private Double annualizedRate;
    
    /**
     *最小投资金额
     */
    @Column(name = "minInvest")
    private BigDecimal minInvest;
    
    /**
     *最大投资金额
     */
    @Column(name = "maxInvest")
    private BigDecimal maxInvest;
    
    /**
     *管理费
     */
    @Column(name = "managementFee")
    private BigDecimal managementFee;
    
    /**
     *借款金额
     */
    @Column(name = "amount")
    private BigDecimal amount;
    
    /**
     *已融资金额
     */
    @Column(name = "finance")
    private BigDecimal finance;
    
    /**
     *创建时间
     */
    @Column(name = "createTime")
    private Date createTime;
    
    /**
     *满标时间
     */
    @Column(name = "finishTime")
    private Date finishTime;
    
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
    
    /**
     * 还款类型
     */
    private String repaymentText;
    
    /**
     * 状态名
     */
    private String statusText;
    
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
    
    public String getTitle()
    {
        return title;
    }
    
    public void setTitle(String title)
    {
        this.title = title;
    }
    
    public Integer getRepayType()
    {
        return repayType;
    }
    
    public void setRepayType(Integer repayType)
    {
        this.repayType = repayType;
    }
    
    public Date getDeadLine()
    {
        return deadLine;
    }
    
    public BigDecimal getFinance()
    {
        return finance;
    }
    
    public void setFinance(BigDecimal finance)
    {
        this.finance = finance;
    }
    
    public void setDeadLine(Date deadLine)
    {
        this.deadLine = deadLine;
    }
    
    public Integer getMonths()
    {
        return months;
    }
    
    public void setMonths(Integer months)
    {
        this.months = months;
    }
    
    public Double getAnnualizedRate()
    {
        return annualizedRate;
    }
    
    public void setAnnualizedRate(Double annualizedRate)
    {
        this.annualizedRate = annualizedRate;
    }
    
    public BigDecimal getMinInvest()
    {
        return minInvest;
    }
    
    public void setMinInvest(BigDecimal minInvest)
    {
        this.minInvest = minInvest;
    }
    
    public BigDecimal getMaxInvest()
    {
        return maxInvest;
    }
    
    public void setMaxInvest(BigDecimal maxInvest)
    {
        this.maxInvest = maxInvest;
    }
    
    public BigDecimal getManagementFee()
    {
        return managementFee;
    }
    
    public void setManagementFee(BigDecimal managementFee)
    {
        this.managementFee = managementFee;
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
    
    public Date getFinishTime()
    {
        return finishTime;
    }
    
    public void setFinishTime(Date finishTime)
    {
        this.finishTime = finishTime;
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
    
    public String getRepaymentText()
    {
        return repaymentText;
    }
    
    public void setRepaymentText(String repaymentText)
    {
        this.repaymentText = repaymentText;
    }
    
    public String getStatusText()
    {
        return statusText;
    }
    
    public void setStatusText(String statusText)
    {
        this.statusText = statusText;
    }
    
}
