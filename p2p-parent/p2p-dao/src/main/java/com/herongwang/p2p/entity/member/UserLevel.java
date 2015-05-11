/***********************************************************************
 * Module:  UserLevel.java
 * Author:  Administrator
 * Purpose: Defines the Class UserLevel
 ***********************************************************************/
package com.herongwang.p2p.entity.member;

import java.util.Date;

import com.herongwang.p2p.dao.member.IUserLevelDAO;
import com.sxj.mybatis.orm.annotations.Column;
import com.sxj.mybatis.orm.annotations.Entity;
import com.sxj.mybatis.orm.annotations.GeneratedValue;
import com.sxj.mybatis.orm.annotations.GenerationType;
import com.sxj.mybatis.orm.annotations.Id;
import com.sxj.mybatis.orm.annotations.Table;

/** 用户级别
 * 
 * @pdOid 51ae8418-ec05-404f-b828-0e15a20904c9 */
@Entity(mapper = IUserLevelDAO.class)
@Table(name = "UserLevel")
public class UserLevel
{
    /** 级别ID
     * 
     * @pdOid 0bbcf285-e9e2-4c45-91fd-b3a27a5add2e */
    @Id(column = "levelId")
    @GeneratedValue(strategy = GenerationType.UUID, length = 32)
    private java.lang.String levelId;
    
    /** 级别名称
     * 
     * @pdOid 53711d67-fd77-47e8-a6ba-cef6f44f6419 */
    @Column(name = "name")
    private java.lang.String name;
    
    /** 级别状态
     * 
     * @pdOid 5a2d9230-ac21-4068-ab7c-8f0445b62d29 */
    @Column(name = "status")
    private int status;
    
    /** 创建时间
     * 
     * @pdOid 446b2d02-6554-4f85-9756-9f5d166d1977 */
    @Column(name = "createTime")
    private java.util.Date createTime = new Date();
    
    /** 更新时间
     * 
     * @pdOid 703f3b74-1d6e-4601-991c-c3ab17f84166 */
    @Column(name = "updateTime")
    private java.util.Date updateTime;
    
    public java.lang.String getLevelId()
    {
        return levelId;
    }
    
    public void setLevelId(java.lang.String levelId)
    {
        this.levelId = levelId;
    }
    
    public java.lang.String getName()
    {
        return name;
    }
    
    public void setName(java.lang.String name)
    {
        this.name = name;
    }
    
    public int getStatus()
    {
        return status;
    }
    
    public void setStatus(int status)
    {
        this.status = status;
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
    
}