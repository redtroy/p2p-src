package com.herongwang.p2p.model.loan;

/**
 * 网贷平台 充值返回 操作信息
 * 
 * @author
 * 
 */
public class LoanRechargeReturnBean
{
	/*
	 * 充值人乾多多标识
	 */
	private String RechargeMoneymoremore;
	
	/*
	 * 平台乾多多标识
	 */
	private String PlatformMoneymoremore = "";
	
	/*
	 * 乾多多流水号
	 */
	private String LoanNo = "";
	
	/*
	 * 平台的充值订单号
	 */
	private String OrderNo = "";
	
	/*
	 * 金额
	 */
	private String Amount = "";
	
	/*
	 * 充值类型
	 */
	private String RechargeType = "";
	
	/*
	 * 当前绑定的银行卡号列表
	 */
	private String CardNoList = "";
	
	/*
	 * 随机时间戳
	 */
	private String RandomTimeStamp = "";
	
	/*
	 * 自定义备注
	 */
	private String Remark1 = "";
	
	/*
	 * 自定义备注
	 */
	private String Remark2 = "";
	
	/*
	 * 自定义备注
	 */
	private String Remark3 = "";
	
	/*
	 * 返回码
	 */
	private String ResultCode = "";
	
	/*
	 * 返回信息
	 */
	private String Message = "";
	
	/*
	 * 签名信息
	 */
	private String SignInfo = "";
	
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
	
	public String getRechargeType()
	{
		return RechargeType;
	}
	
	public void setRechargeType(String rechargeType)
	{
		RechargeType = rechargeType;
	}
	
	public String getCardNoList()
	{
		return CardNoList;
	}
	
	public void setCardNoList(String cardNoList)
	{
		CardNoList = cardNoList;
	}
	
	public String getRandomTimeStamp()
	{
		return RandomTimeStamp;
	}
	
	public void setRandomTimeStamp(String randomTimeStamp)
	{
		RandomTimeStamp = randomTimeStamp;
	}
	
	public String getRemark1()
	{
		return Remark1;
	}
	
	public void setRemark1(String remark1)
	{
		Remark1 = remark1;
	}
	
	public String getRemark2()
	{
		return Remark2;
	}
	
	public void setRemark2(String remark2)
	{
		Remark2 = remark2;
	}
	
	public String getRemark3()
	{
		return Remark3;
	}
	
	public void setRemark3(String remark3)
	{
		Remark3 = remark3;
	}
	
	public String getResultCode()
	{
		return ResultCode;
	}
	
	public void setResultCode(String resultCode)
	{
		ResultCode = resultCode;
	}
	
	public String getMessage()
	{
		return Message;
	}
	
	public void setMessage(String message)
	{
		Message = message;
	}
	
	public String getSignInfo()
	{
		return SignInfo;
	}
	
	public void setSignInfo(String signInfo)
	{
		SignInfo = signInfo;
	}
	
}
