package com.herongwang.p2p.model.loan;

/**
 * 网贷平台 对账接口 充值信息
 * 
 * @author
 * 
 */
public class LoanRechargeOrderQueryBean
{
	/*
	 * 充值人标识
	 */
	private String RechargeMoneymoremore = "";
	
	/*
	 * 平台标识
	 */
	private String PlatformMoneymoremore = "";
	
	/*
	 * 流水号
	 */
	private String LoanNo = "";
	
	/*
	 * 订单号
	 */
	private String OrderNo = "";
	
	/*
	 * 金额
	 */
	private String Amount = "";
	
	/*
	 * 手续费
	 */
	private String Fee = "";
	
	/*
	 * 平台手续费
	 */
	private String FeePlatform = "";
	
	/*
	 * 充值时间
	 */
	private String RechargeTime = "";
	
	/*
	 * 充值类型
	 */
	private String RechargeType = "";
	
	/*
	 * 手续费类型
	 */
	private String FeeType = "";
	
	/*
	 * 状态
	 */
	private String RechargeState = "";
	
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
	
	public String getRechargeMoneymoremore()
	{
		return RechargeMoneymoremore;
	}
	
	public void setRechargeMoneymoremore(String rechargeMoneymoremore)
	{
		RechargeMoneymoremore = rechargeMoneymoremore;
	}
	
	public String getPlatformMoneymoremore()
	{
		return PlatformMoneymoremore;
	}
	
	public void setPlatformMoneymoremore(String platformMoneymoremore)
	{
		PlatformMoneymoremore = platformMoneymoremore;
	}
	
	public String getRechargeTime()
	{
		return RechargeTime;
	}
	
	public void setRechargeTime(String rechargeTime)
	{
		RechargeTime = rechargeTime;
	}
	
	public String getRechargeType()
	{
		return RechargeType;
	}
	
	public void setRechargeType(String rechargeType)
	{
		RechargeType = rechargeType;
	}
	
	public String getRechargeState()
	{
		return RechargeState;
	}
	
	public void setRechargeState(String rechargeState)
	{
		RechargeState = rechargeState;
	}
	
	public String getFee()
	{
		return Fee;
	}
	
	public void setFee(String fee)
	{
		Fee = fee;
	}
	
	public String getFeePlatform()
	{
		return FeePlatform;
	}
	
	public void setFeePlatform(String feePlatform)
	{
		FeePlatform = feePlatform;
	}
	
	public String getFeeType()
	{
		return FeeType;
	}
	
	public void setFeeType(String feeType)
	{
		FeeType = feeType;
	}
	
}
