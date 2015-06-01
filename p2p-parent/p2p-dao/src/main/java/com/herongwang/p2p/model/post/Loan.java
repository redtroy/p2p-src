package com.herongwang.p2p.model.post;

/**
 * 双乾参数
 * @author nishaotang
 *
 */
public class Loan
{
    private String privatekey;//私钥 
    
    private String publickey;//公钥
    
    private String moremoreId;//平台乾多多标识
    
    private String submitURL;//双乾地址
    
    private Double feePercent;//平台承担的手续费比例
    
    private Double feeMax;//用户承担的最高手续费
    
    private Double feeRate;//上浮费率
    
    public String getPrivatekey()
    {
        return privatekey;
    }
    
    public void setPrivatekey(String privatekey)
    {
        this.privatekey = privatekey;
    }
    
    public String getPublickey()
    {
        return publickey;
    }
    
    public void setPublickey(String publickey)
    {
        this.publickey = publickey;
    }
    
    public String getMoremoreId()
    {
        return moremoreId;
    }
    
    public void setMoremoreId(String moremoreId)
    {
        this.moremoreId = moremoreId;
    }
    
    public String getSubmitURL()
    {
        return submitURL;
    }
    
    public void setSubmitURL(String submitURL)
    {
        this.submitURL = submitURL;
    }
    
    public Double getFeePercent()
    {
        return feePercent;
    }
    
    public void setFeePercent(Double feePercent)
    {
        this.feePercent = feePercent;
    }
    
    public Double getFeeMax()
    {
        return feeMax;
    }
    
    public void setFeeMax(Double feeMax)
    {
        this.feeMax = feeMax;
    }
    
    public Double getFeeRate()
    {
        return feeRate;
    }
    
    public void setFeeRate(Double feeRate)
    {
        this.feeRate = feeRate;
    }
    
}
