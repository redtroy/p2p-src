package com.herongwang.p2p.model.invest;

import com.herongwang.p2p.entity.debt.DebtEntity;

public class InvestModel extends DebtEntity
{
    
    /**
     * 
     */
    private static final long serialVersionUID = 3894299311199968967L;
    
    private String orderId;
    
    public String getOrderId()
    {
        return orderId;
    }
    
    public void setOrderId(String orderId)
    {
        this.orderId = orderId;
    }
    
}
