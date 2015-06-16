package com.herongwang.p2p.model.users;

import java.io.Serializable;
import java.util.Date;

public class UserModel implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 810402437840491323L;
    
    private String memberCode;
    
    private String memberName;
    
    private String email;
    
    private String tel;
    
    private String bankCode;
    
    private String codeName;
    
    private Date registerTime;
    
    public String getMemberCode()
    {
        return memberCode;
    }
    
    public void setMemberCode(String memberCode)
    {
        this.memberCode = memberCode;
    }
    
    public String getMemberName()
    {
        return memberName;
    }
    
    public void setMemberName(String memberName)
    {
        this.memberName = memberName;
    }
    
    public String getEmail()
    {
        return email;
    }
    
    public void setEmail(String email)
    {
        this.email = email;
    }
    
    public String getTel()
    {
        return tel;
    }
    
    public void setTel(String tel)
    {
        this.tel = tel;
    }
    
    public String getBankCode()
    {
        return bankCode;
    }
    
    public void setBankCode(String bankCode)
    {
        this.bankCode = bankCode;
    }
    
    public String getCodeName()
    {
        return codeName;
    }
    
    public void setCodeName(String codeName)
    {
        this.codeName = codeName;
    }
    
    public Date getRegisterTime()
    {
        return registerTime;
    }
    
    public void setRegisterTime(Date registerTime)
    {
        this.registerTime = registerTime;
    }
    
}
