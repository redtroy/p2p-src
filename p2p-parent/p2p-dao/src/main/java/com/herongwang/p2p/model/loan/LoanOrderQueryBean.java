package com.herongwang.p2p.model.loan;

/**
 * 网贷平台 对账接口 返回
 * 
 * @author
 * 
 */
public class LoanOrderQueryBean
{
	/*
	 * 付款人标识
	 */
	private String LoanOutMoneymoremore = "";
	
	/*
	 * 收款人标识
	 */
	private String LoanInMoneymoremore = "";
	
	/*
	 * 乾多多流水号
	 */
	private String LoanNo = "";
	
	/*
	 * 平台订单号
	 */
	private String OrderNo = "";
	
	/*
	 * 平台标号
	 */
	private String BatchNo = "";
	
	/*
	 * 金额
	 */
	private String Amount = "";
	
	/*
	 * 平台乾多多标识
	 */
	private String PlatformMoneymoremore = "";
	
	/*
	 * 转账类型 1.投标 2.还款
	 */
	private String TransferAction = "";
	
	/*
	 * 操作类型 1.手动 2.自动
	 */
	private String Action = "";
	
	/*
	 * 转账方式 1.桥连 2.直连
	 */
	private String TransferType = "";
	
	/*
	 * 转账状态 0.未转账 1.已转账
	 */
	private String TransferState = "";
	
	/*
	 * 转账时间
	 */
	private String TransferTime = "";
	
	/*
	 * 操作状态 0.未操作 1.已通过 2.已退回 3.自动通过
	 */
	private String ActState = "";
	
	/*
	 * 通过/退回时间
	 */
	private String ActTime = "";
	
	/*
	 * 通过/退回流水号
	 */
	private String ActNo = "";
	
	/*
	 * 二次分配状态 null.无二次分配 0.未审核 1.同意 2.不同意 3.系统自动同意
	 */
	private String SecondaryState = "";
	
	/*
	 * 二次分配时间
	 */
	private String SecondaryTime = "";
	
	/*
	 * 二次分配列表
	 */
	private String SecondaryJsonList = "";
	
	/*
	 * 用途
	 */
	private String TransferName = "";
	
	/*
	 * 备注
	 */
	private String Remark = "";
	
	public String getLoanOutMoneymoremore()
	{
		return LoanOutMoneymoremore;
	}
	
	public void setLoanOutMoneymoremore(String loanOutMoneymoremore)
	{
		LoanOutMoneymoremore = loanOutMoneymoremore;
	}
	
	public String getLoanInMoneymoremore()
	{
		return LoanInMoneymoremore;
	}
	
	public void setLoanInMoneymoremore(String loanInMoneymoremore)
	{
		LoanInMoneymoremore = loanInMoneymoremore;
	}
	
	public String getOrderNo()
	{
		return OrderNo;
	}
	
	public void setOrderNo(String orderNo)
	{
		OrderNo = orderNo;
	}
	
	public String getAmount()
	{
		return Amount;
	}
	
	public void setAmount(String amount)
	{
		Amount = amount;
	}
	
	public String getRemark()
	{
		return Remark;
	}
	
	public void setRemark(String remark)
	{
		Remark = remark;
	}
	
	public String getBatchNo()
	{
		return BatchNo;
	}
	
	public void setBatchNo(String batchNo)
	{
		BatchNo = batchNo;
	}
	
	public String getTransferName()
	{
		return TransferName;
	}
	
	public void setTransferName(String transferName)
	{
		TransferName = transferName;
	}
	
	public String getLoanNo()
	{
		return LoanNo;
	}
	
	public void setLoanNo(String loanNo)
	{
		LoanNo = loanNo;
	}
	
	public String getPlatformMoneymoremore()
	{
		return PlatformMoneymoremore;
	}
	
	public void setPlatformMoneymoremore(String platformMoneymoremore)
	{
		PlatformMoneymoremore = platformMoneymoremore;
	}
	
	public String getTransferAction()
	{
		return TransferAction;
	}
	
	public void setTransferAction(String transferAction)
	{
		TransferAction = transferAction;
	}
	
	public String getAction()
	{
		return Action;
	}
	
	public void setAction(String action)
	{
		Action = action;
	}
	
	public String getTransferType()
	{
		return TransferType;
	}
	
	public void setTransferType(String transferType)
	{
		TransferType = transferType;
	}
	
	public String getTransferState()
	{
		return TransferState;
	}
	
	public void setTransferState(String transferState)
	{
		TransferState = transferState;
	}
	
	public String getTransferTime()
	{
		return TransferTime;
	}
	
	public void setTransferTime(String transferTime)
	{
		TransferTime = transferTime;
	}
	
	public String getActState()
	{
		return ActState;
	}
	
	public void setActState(String actState)
	{
		ActState = actState;
	}
	
	public String getActTime()
	{
		return ActTime;
	}
	
	public void setActTime(String actTime)
	{
		ActTime = actTime;
	}
	
	public String getActNo()
	{
		return ActNo;
	}
	
	public void setActNo(String actNo)
	{
		ActNo = actNo;
	}
	
	public String getSecondaryState()
	{
		return SecondaryState;
	}
	
	public void setSecondaryState(String secondaryState)
	{
		SecondaryState = secondaryState;
	}
	
	public String getSecondaryTime()
	{
		return SecondaryTime;
	}
	
	public void setSecondaryTime(String secondaryTime)
	{
		SecondaryTime = secondaryTime;
	}
	
	public String getSecondaryJsonList()
	{
		return SecondaryJsonList;
	}
	
	public void setSecondaryJsonList(String secondaryJsonList)
	{
		SecondaryJsonList = secondaryJsonList;
	}
	
}
