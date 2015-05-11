package com.herongwang.p2p.entity.fee;

import com.sxj.mybatis.orm.annotations.Column;
import com.sxj.mybatis.orm.annotations.Table;

@Table(name = "UserLevelFee")
public class UserLevelFee
{
    @Column(name = "levelId")
    private String userLevelId;
    
    @Column(name = "discountId")
    private String discountId;
    
    public String getUserLevelId()
    {
        return userLevelId;
    }
    
    public void setUserLevelId(String userLevelId)
    {
        this.userLevelId = userLevelId;
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
