package com.herongwang.p2p.model.order;

import java.io.Serializable;

public class ResultsModel implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = -9143503533936611917L;
    
    private String merchantId;//商户号
    
    private String version;//网关返回支付结果接口版本
    
    private String language;//网页显示语言种类
    
    private String signType;//签名类型
    
    private String payType;//支付方式
    
    private String issuerId;//发卡方机构代码
    
    private String paymentOrderId;//通联订单号
    
    private String orderNo;//商户订单号
    
    private String orderDatetime;//商户订单提交时间
    
    private String orderAmount;//商户订单金额
    
    private String payDatetime;//支付完成时间
    
    private String payAmount;//订单实际支付金额
    
    private String ext1;//扩展字段1
    
    private String ext2;//扩展字段2
    
    private String payResult;//处理结果
    
    private String errorCode;//错误代码
    
    private String returnDatetime;//结果返回时间
    
    private String signMsg;//签名字符串
    
    public String getMerchantId()
    {
        return merchantId;
    }
    
    public void setMerchantId(String merchantId)
    {
        this.merchantId = merchantId;
    }
    
    public String getVersion()
    {
        return version;
    }
    
    public void setVersion(String version)
    {
        this.version = version;
    }
    
    public String getLanguage()
    {
        return language;
    }
    
    public void setLanguage(String language)
    {
        this.language = language;
    }
    
    public String getSignType()
    {
        return signType;
    }
    
    public void setSignType(String signType)
    {
        this.signType = signType;
    }
    
    public String getPayType()
    {
        return payType;
    }
    
    public void setPayType(String payType)
    {
        this.payType = payType;
    }
    
    public String getIssuerId()
    {
        return issuerId;
    }
    
    public void setIssuerId(String issuerId)
    {
        this.issuerId = issuerId;
    }
    
    public String getPaymentOrderId()
    {
        return paymentOrderId;
    }
    
    public void setPaymentOrderId(String paymentOrderId)
    {
        this.paymentOrderId = paymentOrderId;
    }
    
    public String getOrderNo()
    {
        return orderNo;
    }
    
    public void setOrderNo(String orderNo)
    {
        this.orderNo = orderNo;
    }
    
    public String getOrderDatetime()
    {
        return orderDatetime;
    }
    
    public void setOrderDatetime(String orderDatetime)
    {
        this.orderDatetime = orderDatetime;
    }
    
    public String getOrderAmount()
    {
        return orderAmount;
    }
    
    public void setOrderAmount(String orderAmount)
    {
        this.orderAmount = orderAmount;
    }
    
    public String getPayDatetime()
    {
        return payDatetime;
    }
    
    public void setPayDatetime(String payDatetime)
    {
        this.payDatetime = payDatetime;
    }
    
    public String getPayAmount()
    {
        return payAmount;
    }
    
    public void setPayAmount(String payAmount)
    {
        this.payAmount = payAmount;
    }
    
    public String getExt1()
    {
        return ext1;
    }
    
    public void setExt1(String ext1)
    {
        this.ext1 = ext1;
    }
    
    public String getExt2()
    {
        return ext2;
    }
    
    public void setExt2(String ext2)
    {
        this.ext2 = ext2;
    }
    
    public String getPayResult()
    {
        return payResult;
    }
    
    public void setPayResult(String payResult)
    {
        this.payResult = payResult;
    }
    
    public String getErrorCode()
    {
        return errorCode;
    }
    
    public void setErrorCode(String errorCode)
    {
        this.errorCode = errorCode;
    }
    
    public String getReturnDatetime()
    {
        return returnDatetime;
    }
    
    public void setReturnDatetime(String returnDatetime)
    {
        this.returnDatetime = returnDatetime;
    }
    
    public String getSignMsg()
    {
        return signMsg;
    }
    
    public void setSignMsg(String signMsg)
    {
        this.signMsg = signMsg;
    }
    
}
