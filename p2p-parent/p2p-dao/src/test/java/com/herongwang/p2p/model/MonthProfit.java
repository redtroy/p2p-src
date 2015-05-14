package com.herongwang.p2p.model;

import java.math.BigDecimal;

public class MonthProfit
{
    private BigDecimal total;
    
    private BigDecimal profit;
    
    private BigDecimal capital;
    
    public MonthProfit(BigDecimal total, BigDecimal profit, BigDecimal capital)
    {
        super();
        this.total = total;
        this.profit = profit;
        this.capital = capital;
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
