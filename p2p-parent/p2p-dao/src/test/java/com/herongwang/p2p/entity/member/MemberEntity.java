package com.herongwang.p2p.entity.member;

import java.io.Serializable;

import com.herongwang.p2p.dao.member.IMemberDao;
import com.sxj.mybatis.orm.annotations.Column;
import com.sxj.mybatis.orm.annotations.Entity;
import com.sxj.mybatis.orm.annotations.GeneratedValue;
import com.sxj.mybatis.orm.annotations.GenerationType;
import com.sxj.mybatis.orm.annotations.Id;
import com.sxj.mybatis.orm.annotations.Table;

@Entity(mapper = IMemberDao.class)
@Table(name = "MEMBER")
public class MemberEntity implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 2828188766764116079L;
    
    /**
     * 会员ID
     */
    @Id(column = "MEMBER_ID")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String memberId;
    
    /**
     * 登录名
     */
    @Column(name = "MEMBER_CODE")
    private String memberCode;
    
    /**
     * 密码
     */
    @Column(name = "PASSWORD")
    private String password;
    
    /**
     * 姓名
     */
    @Column(name = "MEMBER_NAME")
    private String memberName;
    
    /**
     * 可用金额
     */
    @Column(name = "AVAILABLE_M")
    private String availableM;
    
    /**
     * 注册时间
     */
    @Column(name = "REGISTER_TIME")
    private String registerTime;
    
    /**
     * 邮箱
     */
    @Column(name = "EMAIL")
    private String email;
    
    /**
     * 手机号
     */
    @Column(name = "TEL")
    private String tel;
    
    /**
     * 身份证号
     */
    @Column(name = "CARD_NUM")
    private String cardNum;
    
    /**
     * 状态
     */
    @Column(name = "TYPE")
    private Integer type;
    
    public String getMemberId()
    {
        return memberId;
    }
    
    public void setMemberId(String memberId)
    {
        this.memberId = memberId;
    }
    
    public String getMemberCode()
    {
        return memberCode;
    }
    
    public void setMemberCode(String memberCode)
    {
        this.memberCode = memberCode;
    }
    
    public String getPassword()
    {
        return password;
    }
    
    public void setPassword(String password)
    {
        this.password = password;
    }
    
    public String getMemberName()
    {
        return memberName;
    }
    
    public void setMemberName(String memberName)
    {
        this.memberName = memberName;
    }
    
    public String getAvailableM()
    {
        return availableM;
    }
    
    public void setAvailableM(String availableM)
    {
        this.availableM = availableM;
    }
    
    public String getRegisterTime()
    {
        return registerTime;
    }
    
    public void setRegisterTime(String registerTime)
    {
        this.registerTime = registerTime;
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
    
    public String getCardNum()
    {
        return cardNum;
    }
    
    public void setCardNum(String cardNum)
    {
        this.cardNum = cardNum;
    }
    
    public Integer getType()
    {
        return type;
    }
    
    public void setType(Integer type)
    {
        this.type = type;
    }
    
}
