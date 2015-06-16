package com.herongwang.p2p.model.loan;

/**
 * 网贷平台 对账接口 二次分配
 * 
 * @author
 * 
 */
public class LoanOrderQuerySecondaryBean
{
	/*
	 * 二次收款人标识
	 */
	private String LoanInMoneymoremore = "";
	
	/*
	 * 流水号
	 */
	private String LoanNo = "";
	
	/*
	 * 金额
	 */
	private String Amount = "";
	
	/*
	 * 用途
	 */
	private String TransferName = "";
	
	/*
	 * 备注
	 */
	private String Remark = "";
	
	public String getLoanInMoneymoremore()
	{
		return LoanInMoneymoremore;
	}
	
	public void setLoanInMoneymoremore(String loanInMoneymoremore)
	{
		LoanInMoneymoremore = loanInMoneymoremore;
	}
	
	public String getAmount()
	{
		return Amount;
	}
	
	public void setAmount(String amount)
	{
		Amount = amount;
	}
	
	public String getTransferName()
	{
		return TransferName;
	}
	
	public void setTransferName(String transferName)
	{
		TransferName = transferName;
	}
	
	public String getRemark()
	{
		return Remark;
	}
	
	public void setRemark(String remark)
	{
		Remark = remark;
	}
	
	public String getLoanNo()
	{
		return LoanNo;
	}
	
	public void setLoanNo(String loanNo)
	{
		LoanNo = loanNo;
	}
	
}
