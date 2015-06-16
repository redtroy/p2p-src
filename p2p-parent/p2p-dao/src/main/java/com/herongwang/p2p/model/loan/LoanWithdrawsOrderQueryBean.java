package com.herongwang.p2p.model.loan;

/**
 * 网贷平台 对账接口 提现信息
 * 
 * @author
 * 
 */
public class LoanWithdrawsOrderQueryBean
{
	/*
	 * 提现人乾多多标识
	 */
	private String WithdrawMoneymoremore = "";
	
	/*
	 * 平台乾多多标识
	 */
	private String PlatformMoneymoremore = "";
	
	/*
	 * 乾多多流水号
	 */
	private String LoanNo = "";
	
	/*
	 * 平台订单号
	 */
	private String OrderNo = "";
	
	/*
	 * 金额
	 */
	private String Amount = "";
	
	/*
	 * 用户承担的最高手续费
	 */
	private String FeeMax = "";
	
	/*
	 * 用户实际承担的手续费金额
	 */
	private String FeeWithdraws = "";
	
	/*
	 * 平台承担的手续费比例
	 */
	private String FeePercent = "";
	
	/*
	 * 平台承担的手续费金额
	 */
	private String Fee = "";
	
	/*
	 * 平台扣除的免费提现额
	 */
	private String FreeLimit = "";
	
	/*
	 * 上浮费率
	 */
	private String FeeRate = "";
	
	/*
	 * 平台分润
	 */
	private String FeeSplitting = "";
	
	/*
	 * 平台分润结算状态
	 */
	private String SplittingSettleState = "";
	
	/*
	 * 提现状态
	 */
	private String WithdrawState = "";
	
	/*
	 * 提现时间
	 */
	private String WithdrawsTime = "";
	
	/*
	 * 平台审核状态
	 */
	private String PlatformAuditState = "";
	
	/*
	 * 平台审核时间
	 */
	private String PlatformAuditTime = "";
	
	/*
	 * 退回时间
	 */
	private String WithdrawsBackTime = "";
	
	public String getWithdrawMoneymoremore()
	{
		return WithdrawMoneymoremore;
	}
	
	public void setWithdrawMoneymoremore(String withdrawMoneymoremore)
	{
		WithdrawMoneymoremore = withdrawMoneymoremore;
	}
	
	public String getPlatformMoneymoremore()
	{
		return PlatformMoneymoremore;
	}
	
	public void setPlatformMoneymoremore(String platformMoneymoremore)
	{
		PlatformMoneymoremore = platformMoneymoremore;
	}
	
	public String getLoanNo()
	{
		return LoanNo;
	}
	
	public void setLoanNo(String loanNo)
	{
		LoanNo = loanNo;
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
	
	public String getFeeMax()
	{
		return FeeMax;
	}
	
	public void setFeeMax(String feeMax)
	{
		FeeMax = feeMax;
	}
	
	public String getFeeWithdraws()
	{
		return FeeWithdraws;
	}
	
	public void setFeeWithdraws(String feeWithdraws)
	{
		FeeWithdraws = feeWithdraws;
	}
	
	public String getFeePercent()
	{
		return FeePercent;
	}
	
	public void setFeePercent(String feePercent)
	{
		FeePercent = feePercent;
	}
	
	public String getFee()
	{
		return Fee;
	}
	
	public void setFee(String fee)
	{
		Fee = fee;
	}
	
	public String getFreeLimit()
	{
		return FreeLimit;
	}
	
	public void setFreeLimit(String freeLimit)
	{
		FreeLimit = freeLimit;
	}
	
	public String getWithdrawState()
	{
		return WithdrawState;
	}
	
	public void setWithdrawState(String withdrawState)
	{
		WithdrawState = withdrawState;
	}
	
	public String getWithdrawsTime()
	{
		return WithdrawsTime;
	}
	
	public void setWithdrawsTime(String withdrawsTime)
	{
		WithdrawsTime = withdrawsTime;
	}
	
	public String getWithdrawsBackTime()
	{
		return WithdrawsBackTime;
	}
	
	public void setWithdrawsBackTime(String withdrawsBackTime)
	{
		WithdrawsBackTime = withdrawsBackTime;
	}
	
	public String getPlatformAuditState()
	{
		return PlatformAuditState;
	}
	
	public void setPlatformAuditState(String platformAuditState)
	{
		PlatformAuditState = platformAuditState;
	}
	
	public String getPlatformAuditTime()
	{
		return PlatformAuditTime;
	}
	
	public void setPlatformAuditTime(String platformAuditTime)
	{
		PlatformAuditTime = platformAuditTime;
	}
	
	public String getFeeRate()
	{
		return FeeRate;
	}
	
	public void setFeeRate(String feeRate)
	{
		FeeRate = feeRate;
	}
	
	public String getFeeSplitting()
	{
		return FeeSplitting;
	}
	
	public void setFeeSplitting(String feeSplitting)
	{
		FeeSplitting = feeSplitting;
	}
	
	public String getSplittingSettleState()
	{
		return SplittingSettleState;
	}
	
	public void setSplittingSettleState(String splittingSettleState)
	{
		SplittingSettleState = splittingSettleState;
	}
	
}
