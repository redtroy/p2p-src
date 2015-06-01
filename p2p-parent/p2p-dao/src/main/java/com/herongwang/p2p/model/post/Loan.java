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
    
}
