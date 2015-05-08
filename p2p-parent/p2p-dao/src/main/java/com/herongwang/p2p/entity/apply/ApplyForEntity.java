package com.herongwang.p2p.entity.apply;

import java.io.Serializable;
import java.util.Date;

import com.herongwang.p2p.dao.apply.IApplyForDao;
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
@Entity(mapper = IApplyForDao.class)
@Table(name = "APPLY_FOR")
public class ApplyForEntity extends Pagable implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2444843981481631279L;
	/**
	 * 主键1
	 */
	@Id(column = "APPLY_ID")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String applyId;
	
	 /**
     * 会员id
     */
    @Column(name = "MEMBER_ID")
    private String memberId;
    /**
     * 金额
     */
    @Column(name = "MONEY")
    private Double money;
    /**
     * 申请时间
     */
    @Column(name = "FOR_TIME")
    private Date forTime;
    /**
     * 申请融资状态
     * 0：未处理；1：未通过；2：通过。
     */
    @Column(name = "STATUS")
    private Integer status;
    /**
     * 说明
     */
    @Column(name = "REMARK")
    private String remark;
    
    /**
     * 会员姓名
     */
    private String name;
    
    
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
    
    public String getApplyId() {
		return applyId;
	}
	public void setApplyId(String applyId) {
		this.applyId = applyId;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public Double getMoney() {
		return money;
	}
	public void setMoney(Double money) {
		this.money = money;
	}
	public Date getForTime() {
		return forTime;
	}
	public void setForTime(Date forTime) {
		this.forTime = forTime;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
    
}
