package com.herongwang.p2p.model.post;

public class Info
{
    private String TRX_CODE = "";//交易代码
    
    private String VERSION = "";//版本
    
    private String DATA_TYPE = "";//数据格式
    
    private String LEVEL = "";//处理级别
    
    private String MERCHANT_ID;//商户代码
    
    private String USER_NAME = "";//用户名
    
    private String USER_PASS = "";//用户密码
    
    /**
     * 必须全局唯一，商户提交的批次号必须以商户号开头以保证与其他商户不冲突，一旦冲突交易将无法提交
     *建议格式：商户号+时间+固定位数顺序流水号。该字段值用于后续的查询交易、对账文件等的唯一标识，对应通联
     *系统中的交易文件名，可以在通联系统交易查询页面查询到该值
     */
    private String REQ_SN = "";//交易批次号
    
    private String REQTIME;//请求时间
    
    private String SIGNED_MSG = "";//签名信息
    
    public String getTRX_CODE()
    {
        return TRX_CODE;
    }
    
    public void setTRX_CODE(String tRX_CODE)
    {
        TRX_CODE = tRX_CODE;
    }
    
    public String getVERSION()
    {
        return VERSION;
    }
    
    public void setVERSION(String vERSION)
    {
        VERSION = vERSION;
    }
    
    public String getDATA_TYPE()
    {
        return DATA_TYPE;
    }
    
    public void setDATA_TYPE(String dATA_TYPE)
    {
        DATA_TYPE = dATA_TYPE;
    }
    
    public String getLEVEL()
    {
        return LEVEL;
    }
    
    public void setLEVEL(String lEVEL)
    {
        LEVEL = lEVEL;
    }
    
    public String getMERCHANT_ID()
    {
        return MERCHANT_ID;
    }
    
    public void setMERCHANT_ID(String mERCHANT_ID)
    {
        MERCHANT_ID = mERCHANT_ID;
    }
    
    public String getUSER_NAME()
    {
        return USER_NAME;
    }
    
    public void setUSER_NAME(String uSER_NAME)
    {
        USER_NAME = uSER_NAME;
    }
    
    public String getUSER_PASS()
    {
        return USER_PASS;
    }
    
    public void setUSER_PASS(String uSER_PASS)
    {
        USER_PASS = uSER_PASS;
    }
    
    public String getREQ_SN()
    {
        return REQ_SN;
    }
    
    public void setREQ_SN(String rEQ_SN)
    {
        REQ_SN = rEQ_SN;
    }
    
    public String getREQTIME()
    {
        return REQTIME;
    }
    
    public void setREQTIME(String rEQTIME)
    {
        REQTIME = rEQTIME;
    }
    
    public String getSIGNED_MSG()
    {
        return SIGNED_MSG;
    }
    
    public void setSIGNED_MSG(String sIGNED_MSG)
    {
        SIGNED_MSG = sIGNED_MSG;
    }
}
