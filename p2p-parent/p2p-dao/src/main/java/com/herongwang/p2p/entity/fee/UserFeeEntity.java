package com.herongwang.p2p.entity.fee;

import com.sxj.mybatis.orm.annotations.Column;
import com.sxj.mybatis.orm.annotations.Table;

@Table(name = "UserFee")
public class UserFeeEntity
{
    @Column(name = "customerId")
    private String customerId;
    
    @Column(name = "feeId")
    private String feeId;
    
    @Column(name = "discountId")
    private String discountId;
    
    public String getCustomerId()
    {
        return customerId;
    }
    
    public void setCustomerId(String customerId)
    {
        this.customerId = customerId;
    }
    
    public String getFeeId()
    {
        return feeId;
    }
    
    public void setFeeId(String feeId)
    {
        this.feeId = feeId;
    }
    
    public String getDiscountId()
    {
        return discountId;
    }
    
    public void setDiscountId(String discountId)
    {
        this.discountId = discountId;
    }
    
}
