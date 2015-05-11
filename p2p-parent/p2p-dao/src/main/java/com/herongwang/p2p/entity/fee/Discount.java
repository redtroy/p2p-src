/***********************************************************************
 * Module:  Discount.java
 * Author:  Administrator
 * Purpose: Defines the Class Discount
 ***********************************************************************/
package com.herongwang.p2p.entity.fee;

import com.sxj.mybatis.orm.annotations.Column;
import com.sxj.mybatis.orm.annotations.Entity;
import com.sxj.mybatis.orm.annotations.GeneratedValue;
import com.sxj.mybatis.orm.annotations.GenerationType;
import com.sxj.mybatis.orm.annotations.Id;
import com.sxj.mybatis.orm.annotations.Table;

@Entity
@Table(name = "Discount")
/** 折扣
 * 
 * @pdOid d7b3a4ed-17cd-4c70-8013-952ce63600e2 */
public class Discount extends Fee
{
    
    /** 折扣ID
     * 
     * @pdOid 77c995d0-dd11-4a45-8ca9-a71cefc7dc31 */
    @Id(column = "discountId")
    @GeneratedValue(strategy = GenerationType.UUID, length = 32)
    private java.lang.String discountId;
    
    /** 折扣
     * 
     * @pdOid 0e156e4e-10f3-4300-940b-64a101e4f61e */
    @Column(name = "discount")
    private Float discount;
    
    /** @pdOid 5fd93f16-a877-48b0-a480-6b6c8e0af426 */
    @Column(name = "discountFee")
    private java.lang.Float discountFee;
    
    public java.lang.String getDiscountId()
    {
        return discountId;
    }
    
    public void setDiscountId(java.lang.String discountId)
    {
        this.discountId = discountId;
    }
    
    public Float getDiscount()
    {
        return discount;
    }
    
    public void setDiscount(Float discount)
    {
        this.discount = discount;
    }
    
    public java.lang.Float getDiscountFee()
    {
        return discountFee;
    }
    
    public void setDiscountFee(java.lang.Float discountFee)
    {
        this.discountFee = discountFee;
    }
    
}