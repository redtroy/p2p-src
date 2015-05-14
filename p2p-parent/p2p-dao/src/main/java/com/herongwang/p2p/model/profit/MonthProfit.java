package com.herongwang.p2p.model.profit;

import java.math.BigDecimal;

public class MonthProfit
{
    /**
     * 月总额
     */
    private BigDecimal total;
    
    /**
     * 月利息
     */
    private BigDecimal profit;
    
    /**
     * 本金
     */
    private BigDecimal capital;
    
    /**
     * 手续费
     */
    private BigDecimal fee;
    
    /**
     * 月份
     */
    private Integer month;
    
    public BigDecimal getFee()
    {
        return fee;
    }
    
    public void setFee(BigDecimal fee)
    {
        this.fee = fee;
    }
    
    public Integer getMonth()
    {
        return month;
    }
    
    public void setMonth(Integer month)
    {
        this.month = month;
    }
    
    public BigDecimal getTotal()
    {
        return total;
    }
    
    public void setTotal(BigDecimal total)
    {
        this.total = total;
    }
    
    public BigDecimal getProfit()
    {
        return profit;
    }
    
    public void setProfit(BigDecimal profit)
    {
        this.profit = profit;
    }
    
    public BigDecimal getCapital()
    {
        return capital;
    }
    
    public void setCapital(BigDecimal capital)
    {
        this.capital = capital;
    }
}
