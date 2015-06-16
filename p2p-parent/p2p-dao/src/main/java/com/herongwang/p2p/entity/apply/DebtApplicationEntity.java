package com.herongwang.p2p.entity.apply;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.herongwang.p2p.dao.apply.IDebtApplicationDao;
import com.sxj.mybatis.orm.annotations.Column;
import com.sxj.mybatis.orm.annotations.Entity;
import com.sxj.mybatis.orm.annotations.GeneratedValue;
import com.sxj.mybatis.orm.annotations.GenerationType;
import com.sxj.mybatis.orm.annotations.Id;
import com.sxj.mybatis.orm.annotations.Table;
import com.sxj.mybatis.pagination.Pagable;

/**
 * 融资申请
 * @author nishaotang
 *
 */
@Entity(mapper = IDebtApplicationDao.class)
@Table(name = "DebtApplication")
public class DebtApplicationEntity extends Pagable implements Serializable
{
    
    /**
     * 
     */
    private static final long serialVersionUID = -2444843981481631279L;
    
    /**
     * 主键1
     */
    @Id(column = "applicationId")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String applicationId;
    
    /**
    * 会员id
    */
    @Column(name = "customerId")
    private String customerId;
    
    /**
     * 金额
     */
    @Column(name = "amount")
    private BigDecimal amount;
    
    /**
     *联系电话
     */
    @Column(name = "tel")
    private String tel;
    
    /**
     * 申请时间
     */
    @Column(name = "applyTime")
    private Date applyTime;
    
    /**
     * 申请融资状态
     * 0：未处理；1：未通过；2：通过。
     */
    @Column(name = "status")
    private Integer status;
    
    /**
     * 说明
     */
    @Column(name = "remark")
    private String remark;
    
    /**
     * 会员姓名
     */
    @Column(name = "name")
    private String name;
    
    /**
     * 会员姓名
     */
    private String userName;
    
    public String getUserName()
    {
        return userName;
    }
    
    public void setUserName(String userName)
    {
        this.userName = userName;
    }
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public String getApplicationId()
    {
        return applicationId;
    }
    
    public void setApplicationId(String applicationId)
    {
        this.applicationId = applicationId;
    }
    
    public String getCustomerId()
    {
        return customerId;
    }
    
    public void setCustomerId(String customerId)
    {
        this.customerId = customerId;
    }
    
    public BigDecimal getAmount()
    {
        return amount;
    }
    
    public void setAmount(BigDecimal amount)
    {
        this.amount = amount;
    }
    
    public Date getApplyTime()
    {
        return applyTime;
    }
    
    public void setApplyTime(Date applyTime)
    {
        this.applyTime = applyTime;
    }
    
    public Integer getStatus()
    {
        return status;
    }
    
    public void setStatus(Integer status)
    {
        this.status = status;
    }
    
    public String getRemark()
    {
        return remark;
    }
    
    public void setRemark(String remark)
    {
        this.remark = remark;
    }
    
    public String getTel()
    {
        return tel;
    }
    
    public void setTel(String tel)
    {
        this.tel = tel;
    }
    
}
