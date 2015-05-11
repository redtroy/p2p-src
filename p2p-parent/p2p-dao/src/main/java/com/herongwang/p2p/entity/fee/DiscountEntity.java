/***********************************************************************
 * Module:  Discount.java
 * Author:  Administrator
 * Purpose: Defines the Class Discount
 ***********************************************************************/
package com.herongwang.p2p.entity.fee;

import java.util.Date;

import com.herongwang.p2p.dao.fee.IDiscountDAO;
import com.sxj.mybatis.orm.annotations.Column;
import com.sxj.mybatis.orm.annotations.Entity;
import com.sxj.mybatis.orm.annotations.GeneratedValue;
import com.sxj.mybatis.orm.annotations.GenerationType;
import com.sxj.mybatis.orm.annotations.Id;
import com.sxj.mybatis.orm.annotations.Table;

@Entity(mapper = IDiscountDAO.class)
@Table(name = "Discount")
/** 折扣
 * 
 * @pdOid d7b3a4ed-17cd-4c70-8013-952ce63600e2 */
public class DiscountEntity
{
    
    private FeeEntity feeEntity;
    
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
    
    @Column(name = "feeId")
    private java.lang.String feeId;
    
    /** 费率
     * 
     * @pdOid 7fdde590-efd1-4248-b49c-d594a16471f4 */
    @Column(name = "fee")
    private java.lang.Float fee;
    
    /** 级别
     * 
     * @pdOid c276c304-6020-40ae-887a-0705b27a736f */
    @Column(name = "level")
    private Integer level;
    
    /** 状态
     * 
     * @pdOid 36eaa55f-354d-4dbe-9404-b4676f856dc8 */
    @Column(name = "status")
    private FeeStatus status = FeeStatus.AVAILABLE;
    
    /** 创建时间
     * 
     * @pdOid 635da763-14c6-4d7a-aaad-df4e679b3a45 */
    @Column(name = "createTime")
    private java.util.Date createTime = new Date();
    
    /** 更新时间
     * 
     * @pdOid c4ded15d-98d1-47c4-ae9e-b20a4baac5df */
    @Column(name = "updateTime")
    private java.util.Date updateTime;
    
    @Column(name = "parentId")
    private String parentId;
    
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
        return (getDiscount() == null ? 1.0f : getDiscount())
                * (getFee() == null ? 0 : getFee());
    }
    
    public void setDiscountFee(java.lang.Float discountFee)
    {
        this.discountFee = discountFee;
    }
    
    public FeeEntity getFeeEntity()
    {
        return feeEntity;
    }
    
    public void setFeeEntity(FeeEntity feeEntity)
    {
        this.feeEntity = feeEntity;
    }
    
    public void setFeeId(String feeId)
    {
        throw new UnsupportedOperationException();
    }
    
    public void setFee(Float fee)
    {
        throw new UnsupportedOperationException();
    }
    
    public void setLevel(int level)
    {
        throw new UnsupportedOperationException();
    }
    
    public void setParentId(String parentId)
    {
        throw new UnsupportedOperationException();
    }
    
    public String getFeeId()
    {
        if (feeEntity == null)
            return null;
        return feeEntity.getFeeId();
    }
    
    public Float getFee()
    {
        if (feeEntity == null)
            return null;
        return feeEntity.getFee();
    }
    
    public Integer getLevel()
    {
        if (feeEntity == null)
            return null;
        return feeEntity.getLevel();
    }
    
    public String getParentId()
    {
        if (feeEntity == null)
            return null;
        return feeEntity.getParentId();
    }
    
}