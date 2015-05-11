/***********************************************************************
 * Module:  Fee.java
 * Author:  Administrator
 * Purpose: Defines the Class Fee
 ***********************************************************************/
package com.herongwang.p2p.entity.fee;

import java.util.Date;

import com.herongwang.p2p.dao.fee.IFeeDAO;
import com.sxj.mybatis.orm.annotations.Column;
import com.sxj.mybatis.orm.annotations.Entity;
import com.sxj.mybatis.orm.annotations.GeneratedValue;
import com.sxj.mybatis.orm.annotations.GenerationType;
import com.sxj.mybatis.orm.annotations.Id;
import com.sxj.mybatis.orm.annotations.Table;

/** 费率
 * 
 * @pdOid 143487cb-be68-4c13-bde7-77e5c09a8b01 */
@Entity(mapper = IFeeDAO.class)
@Table(name = "Fee")
public class FeeEntity
{
    /** 费率ID
     * 
     * @pdOid 30d7f7c6-e800-434a-9d28-f335ed8e0101 */
    @Id(column = "feeId")
    @GeneratedValue(strategy = GenerationType.UUID, length = 32)
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
    private int level;
    
    @Column(name = "name")
    private String name;
    
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
    
    public java.lang.String getFeeId()
    {
        return feeId;
    }
    
    public void setFeeId(java.lang.String feeId)
    {
        this.feeId = feeId;
    }
    
    public java.lang.Float getFee()
    {
        return fee;
    }
    
    public void setFee(java.lang.Float fee)
    {
        this.fee = fee;
    }
    
    public int getLevel()
    {
        return level;
    }
    
    public void setLevel(int level)
    {
        this.level = level;
    }
    
    public java.util.Date getCreateTime()
    {
        return createTime;
    }
    
    public void setCreateTime(java.util.Date createTime)
    {
        this.createTime = createTime;
    }
    
    public java.util.Date getUpdateTime()
    {
        return updateTime;
    }
    
    public void setUpdateTime(java.util.Date updateTime)
    {
        this.updateTime = updateTime;
    }
    
    public String getParentId()
    {
        return parentId;
    }
    
    public void setParentId(String parentId)
    {
        this.parentId = parentId;
    }
    
    public FeeStatus getStatus()
    {
        return status;
    }
    
    public void setStatus(FeeStatus status)
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