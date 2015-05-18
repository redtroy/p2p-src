package com.herongwang.p2p.model.profit;

import java.math.BigDecimal;
import java.util.List;

import com.herongwang.p2p.entity.profitlist.ProfitListEntity;

public class ProfitModel
{
    private List<ProfitListEntity> monthProfit;
    
    /**
     * 总利息
     */
    private BigDecimal totalInterest;
    
    /**
     * 总额
     */
    private BigDecimal amount;
    
    /**
     * 投资金额
     */
    private BigDecimal investment;
    
    /**
     * 管理费
     */
    private BigDecimal totalFee;
    
    private Integer Type;
    
    public Integer getType()
    {
        return Type;
    }
    
    public void setType(Integer type)
    {
        Type = type;
    }
    
    public BigDecimal getTotalFee()
    {
        return totalFee;
    }
    
    public void setTotalFee(BigDecimal totalFee)
    {
        this.totalFee = totalFee;
    }
    
    public List<ProfitListEntity> getMonthProfit()
    {
        return monthProfit;
    }
    
    public void setMonthProfit(List<ProfitListEntity> monthProfit)
    {
        this.monthProfit = monthProfit;
    }
    
    public BigDecimal getTotalInterest()
    {
        return totalInterest;
    }
    
    public void setTotalInterest(BigDecimal totalInterest)
    {
        this.totalInterest = totalInterest;
    }
    
    public BigDecimal getAmount()
    {
        return amount;
    }
    
    public void setAmount(BigDecimal amount)
    {
        this.amount = amount;
    }
    
    public BigDecimal getInvestment()
    {
        return investment;
    }
    
    public void setInvestment(BigDecimal investment)
    {
        this.investment = investment;
    }
    
}
