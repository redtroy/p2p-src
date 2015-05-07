package com.herongwang.p2p.entity.deal;

import java.io.Serializable;
import java.util.Date;

import com.herongwang.p2p.dao.deal.IDealDetail;
import com.sxj.mybatis.orm.annotations.Column;
import com.sxj.mybatis.orm.annotations.Entity;
import com.sxj.mybatis.orm.annotations.GeneratedValue;
import com.sxj.mybatis.orm.annotations.GenerationType;
import com.sxj.mybatis.orm.annotations.Id;
import com.sxj.mybatis.orm.annotations.Table;
import com.sxj.mybatis.pagination.Pagable;

/**
 * 交易明细
 * @author nishaotang
 *
 */
@Entity(mapper = IDealDetail.class)
@Table(name = "DEAL_DETAIL")
public class DealDetailEntity  extends Pagable implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = -2444843981481631279L;
	/**
	 * 主键
	 */
	@Id(column = "DEAL_ID")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String dealId;
	
	 /**
     * 会员id
     */
    @Column(name = "MEMBER_ID")
    private String memberId;
    /**
     * 交易类型
     */
    @Column(name = "DEAL_TYPE")
    private Integer dealType;
    /**
     * 交易时间
     */
    @Column(name = "DEAL_TIME")
    private Date dealTime;
    /**
     * 交易金额
     */
    @Column(name = "DEAL_FEE")
    private Double dealFee;
    /**
     * 账户余额
     */
    @Column(name = "BALANCE")
    private Double balance;
    /**
     * 说明
     */
    @Column(name = "REMARK")
    private String remark;
    /**
     * 招标号
     */
    @Column(name = "MARK_ID")
    private String markId;
    /**
     * 投资号
     */
    @Column(name = "INV_ID")
    private String invId;
    /**
     *还款号
     */
    @Column(name = "PLAN_ID")
    private String planId;
	public String getDealId() {
		return dealId;
	}
	public void setDealId(String dealId) {
		this.dealId = dealId;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public Integer getDealType() {
		return dealType;
	}
	public void setDealType(Integer dealType) {
		this.dealType = dealType;
	}
	public Date getDealTime() {
		return dealTime;
	}
	public void setDealTime(Date dealTime) {
		this.dealTime = dealTime;
	}
	public Double getDealFee() {
		return dealFee;
	}
	public void setDealFee(Double dealFee) {
		this.dealFee = dealFee;
	}
	public Double getBalance() {
		return balance;
	}
	public void setBalance(Double balance) {
		this.balance = balance;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getMarkId() {
		return markId;
	}
	public void setMarkId(String markId) {
		this.markId = markId;
	}
	public String getInvId() {
		return invId;
	}
	public void setInvId(String invId) {
		this.invId = invId;
	}
	public String getPlanId() {
		return planId;
	}
	public void setPlanId(String planId) {
		this.planId = planId;
	}
    
}
