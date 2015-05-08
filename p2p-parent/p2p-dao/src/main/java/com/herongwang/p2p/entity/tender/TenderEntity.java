package com.herongwang.p2p.entity.tender;

import java.io.Serializable;
import java.util.Date;

import com.herongwang.p2p.dao.tender.ITenderDao;
import com.sxj.mybatis.orm.annotations.Column;
import com.sxj.mybatis.orm.annotations.Entity;
import com.sxj.mybatis.orm.annotations.GeneratedValue;
import com.sxj.mybatis.orm.annotations.GenerationType;
import com.sxj.mybatis.orm.annotations.Id;
import com.sxj.mybatis.orm.annotations.Table;
import com.sxj.mybatis.pagination.Pagable;

/**
 * 借款标
 * @author nishaotang
 *
 */
@Entity(mapper = ITenderDao.class)
@Table(name = "MARK_INFO")
public class TenderEntity extends Pagable implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5284030789397025667L;

	/**
	 * 主键1
	 */
	@Id(column = "ID")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
	
	 /**
     * 编号
     */
    @Column(name = "MARK_ID")
    private String markId;
    /**
     * 标题
     */
    @Column(name = "TITLE")
    private String title;
    /**
     * 借款人
     */
    @Column(name = "BORROWER")
    private String borrower;
    /**
     * 还款类型
     * 1：按月分期还款；2：每月还息到期还本；3：一次性还款。
     */
    @Column(name = "REPAYMENT_TYPE")
    private Integer repaymentType;
    /**
     *借款期限
     */
    @Column(name = "BORRPW_TIME")
    private Integer borrpwTime;
    /**
     *年化利率
     */
    @Column(name = "INTEREST")
    private Double interest;
    /**
     *最小投资金额
     */
    @Column(name = "MIN_AMOUNT")
    private Double minAmount;
    /**
     *最大投资金额
     */
    @Column(name = "MAX_AMOUNT")
    private Double maxAmount;
    /**
     *管理费
     */
    @Column(name = "MANAGE_FEE")
    private Double manageFee;
    /**
     *借款金额
     */
    @Column(name = "BORROWING_AMOUNT")
    private Double borrowingAmount;
    /**
     *创建时间
     */
    @Column(name = "CREATE_TIME")
    private Date createTime;
    /**
     *满标时间
     */
    @Column(name = "FULL_TIME")
    private Date fullTime;
    /**
     *状态
     *0：未审核；1：投资中；2：流标；3：满标；4：还款中；5：完成。
     */
    @Column(name = "STATUS")
    private Integer status;
    
    /**
     * 会员姓名
     */
    private String name;
    /**
     * 还款类型
     */
    private String repaymentText;
    /**
     * 状态名
     */
    private String statusText;
    
    
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMarkId() {
		return markId;
	}
	public void setMarkId(String markId) {
		this.markId = markId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getBorrower() {
		return borrower;
	}
	public void setBorrower(String borrower) {
		this.borrower = borrower;
	}
	public Integer getRepaymentType() {
		return repaymentType;
	}
	public void setRepaymentType(Integer repaymentType) {
		this.repaymentType = repaymentType;
	}
	public Integer getBorrpwTime() {
		return borrpwTime;
	}
	public void setBorrpwTime(Integer borrpwTime) {
		this.borrpwTime = borrpwTime;
	}
	public Double getInterest() {
		return interest;
	}
	public void setInterest(Double interest) {
		this.interest = interest;
	}
	public Double getMinAmount() {
		return minAmount;
	}
	public void setMinAmount(Double minAmount) {
		this.minAmount = minAmount;
	}
	public Double getMaxAmount() {
		return maxAmount;
	}
	public void setMaxAmount(Double maxAmount) {
		this.maxAmount = maxAmount;
	}
	public Double getManageFee() {
		return manageFee;
	}
	public void setManageFee(Double manageFee) {
		this.manageFee = manageFee;
	}
	public Double getBorrowingAmount() {
		return borrowingAmount;
	}
	public void setBorrowingAmount(Double borrowingAmount) {
		this.borrowingAmount = borrowingAmount;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getFullTime() {
		return fullTime;
	}
	public void setFullTime(Date fullTime) {
		this.fullTime = fullTime;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getStatusText() {
		return statusText;
	}
	public void setStatusText(String statusText) {
		this.statusText = statusText;
	}
	public String getRepaymentText() {
		return repaymentText;
	}
	public void setRepaymentText(String repaymentText) {
		this.repaymentText = repaymentText;
	}
    
}
