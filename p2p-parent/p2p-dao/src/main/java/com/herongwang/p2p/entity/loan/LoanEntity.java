package com.herongwang.p2p.entity.loan;

import java.io.Serializable;
import java.util.Date;

import com.herongwang.p2p.dao.loan.ILoanDao;
import com.sxj.mybatis.orm.annotations.Column;
import com.sxj.mybatis.orm.annotations.Entity;
import com.sxj.mybatis.orm.annotations.GeneratedValue;
import com.sxj.mybatis.orm.annotations.GenerationType;
import com.sxj.mybatis.orm.annotations.Id;
import com.sxj.mybatis.orm.annotations.Table;
import com.sxj.mybatis.pagination.Pagable;

/**
 * 双乾报文表
 * @author nishaotang
 *
 */
@Entity(mapper = ILoanDao.class)
@Table(name = "LoanInfo")
public class LoanEntity extends Pagable implements Serializable
{
    
    /**
     * 
     */
    private static final long serialVersionUID = 642067902339238693L;
    
    /**
     * ID
     */
    @Id(column = "loanId")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String loanId;
    
    /**
     * 报文
     */
    @Column(name = "message")
    private String message;
    
    /**
    * 报文类型
    */
    @Column(name = "className")
    private String className;
    
    /**
     * 创建时间
     */
    @Column(name = "createTime")
    private Date createTime;
    
    /**
     * 说明
     */
    @Column(name = "remark")
    private String remark;
    
    public String getLoanId()
    {
        return loanId;
    }
    
    public void setLoanId(String loanId)
    {
        this.loanId = loanId;
    }
    
    public String getMessage()
    {
        return message;
    }
    
    public void setMessage(String message)
    {
        this.message = message;
    }
    
    public String getClassName()
    {
        return className;
    }
    
    public void setClassName(String className)
    {
        this.className = className;
    }
    
    public Date getCreateTime()
    {
        return createTime;
    }
    
    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }
    
    public String getRemark()
    {
        return remark;
    }
    
    public void setRemark(String remark)
    {
        this.remark = remark;
    }
    
}
