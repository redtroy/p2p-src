package com.herongwang.p2p.model.order;

import java.io.Serializable;

/**
 * 构造订单请求对象，生成signMsg。
 * @author nishaotang
 *
 */
public class OrderModel implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = -6070019104070277683L;
    
    //字符集
    private String inputCharset;
    
    //取货地址
    private String pickupUrl;
    
    //商户系统通知地址
    private String receiveUrl;
    
    //版本号
    private String version;
    
    //语言
    private String language;
    
    //签名类型
    private String signType;
    
    //支付方式
    private String payType;
    
    //发卡方代码
    private String issuerId;
    
    //商户号
    private String merchantId;
    
    //付款人姓名
    private String payerName;
    
    //付款人联系email
    private String payerEmail;
    
    //付款人电话
    private String payerTelephone;
    
    //付款人证件号
    private String payerIDCard;
    
    //合作伙伴商户号
    private String pid;
    
    //商户系统订单号
    private String orderNo;
    
    //订单金额(单位分)
    private String orderAmount;
    
    //订单金额币种类型
    private String orderCurrency;
    
    //商户的订单提交时间
    private String orderDatetime;
    
    //订单过期时间
    private String orderExpireDatetime;
    
    //商品名称
    private String productName;
    
    //商品单价
    private String productPrice;
    
    //商品数量
    private String productNum;
    
    //商品标识
    private String productId;
    
    //商品描述
    private String productDesc;
    
    //扩展字段1
    private String ext1;
    
    //扩展字段2
    private String ext2;
    
    //业务扩展字段
    private String extTL;
    
    //付款人支付卡号
    private String pan;
    
    //贸易类型
    private String tradeNature;
    
    //key
    private String key;
    
    public String getKey()
    {
        return key;
    }
    
    public void setKey(String key)
    {
        this.key = key;
    }
    
    public String getInputCharset()
    {
        return inputCharset;
    }
    
    public void setInputCharset(String inputCharset)
    {
        this.inputCharset = inputCharset;
    }
    
    public String getPickupUrl()
    {
        return pickupUrl;
    }
    
    public void setPickupUrl(String pickupUrl)
    {
        this.pickupUrl = pickupUrl;
    }
    
    public String getReceiveUrl()
    {
        return receiveUrl;
    }
    
    public void setReceiveUrl(String receiveUrl)
    {
        this.receiveUrl = receiveUrl;
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
    
    public String getMerchantId()
    {
        return merchantId;
    }
    
    public void setMerchantId(String merchantId)
    {
        this.merchantId = merchantId;
    }
    
    public String getPayerName()
    {
        return payerName;
    }
    
    public void setPayerName(String payerName)
    {
        this.payerName = payerName;
    }
    
    public String getPayerEmail()
    {
        return payerEmail;
    }
    
    public void setPayerEmail(String payerEmail)
    {
        this.payerEmail = payerEmail;
    }
    
    public String getPayerTelephone()
    {
        return payerTelephone;
    }
    
    public void setPayerTelephone(String payerTelephone)
    {
        this.payerTelephone = payerTelephone;
    }
    
    public String getPayerIDCard()
    {
        return payerIDCard;
    }
    
    public void setPayerIDCard(String payerIDCard)
    {
        this.payerIDCard = payerIDCard;
    }
    
    public String getPid()
    {
        return pid;
    }
    
    public void setPid(String pid)
    {
        this.pid = pid;
    }
    
    public String getOrderNo()
    {
        return orderNo;
    }
    
    public void setOrderNo(String orderNo)
    {
        this.orderNo = orderNo;
    }
    
    public String getOrderAmount()
    {
        return orderAmount;
    }
    
    public void setOrderAmount(String orderAmount)
    {
        this.orderAmount = orderAmount;
    }
    
    public String getOrderCurrency()
    {
        return orderCurrency;
    }
    
    public void setOrderCurrency(String orderCurrency)
    {
        this.orderCurrency = orderCurrency;
    }
    
    public String getOrderDatetime()
    {
        return orderDatetime;
    }
    
    public void setOrderDatetime(String orderDatetime)
    {
        this.orderDatetime = orderDatetime;
    }
    
    public String getOrderExpireDatetime()
    {
        return orderExpireDatetime;
    }
    
    public void setOrderExpireDatetime(String orderExpireDatetime)
    {
        this.orderExpireDatetime = orderExpireDatetime;
    }
    
    public String getProductName()
    {
        return productName;
    }
    
    public void setProductName(String productName)
    {
        this.productName = productName;
    }
    
    public String getProductPrice()
    {
        return productPrice;
    }
    
    public void setProductPrice(String productPrice)
    {
        this.productPrice = productPrice;
    }
    
    public String getProductNum()
    {
        return productNum;
    }
    
    public void setProductNum(String productNum)
    {
        this.productNum = productNum;
    }
    
    public String getProductId()
    {
        return productId;
    }
    
    public void setProductId(String productId)
    {
        this.productId = productId;
    }
    
    public String getProductDesc()
    {
        return productDesc;
    }
    
    public void setProductDesc(String productDesc)
    {
        this.productDesc = productDesc;
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
    
    public String getExtTL()
    {
        return extTL;
    }
    
    public void setExtTL(String extTL)
    {
        this.extTL = extTL;
    }
    
    public String getPan()
    {
        return pan;
    }
    
    public void setPan(String pan)
    {
        this.pan = pan;
    }
    
    public String getTradeNature()
    {
        return tradeNature;
    }
    
    public void setTradeNature(String tradeNature)
    {
        this.tradeNature = tradeNature;
    }
    
}
