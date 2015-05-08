package com.herongwang.p2p.model.apply;

import java.io.Serializable;
import java.util.Date;

import com.herongwang.p2p.entity.apply.ApplyForEntity;
import com.sxj.mybatis.pagination.Pagable;

/**
 * 融资申请模型1
 * @author nishaotang
 *
 */
public class ApplyForModel extends Pagable implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8936235244306343306L;

	/**
	 * 主键
	 */
    private String applyId;
	
	 /**
     * 会员id
     */
    private String memberId;
    /**
     * 申请金额
     */
    private Double MONEY;
    /**
     * 申请时间
     */
    private Date forTime;
    /**
     * 处理结果
     * 0：未处理；1：未通过；2：通过。
     */
    private Integer status;
    /**
     * 说明
     */
    private String remark;
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
	public Double getMONEY() {
		return MONEY;
	}
	public void setMONEY(Double mONEY) {
		MONEY = mONEY;
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
}
