package com.herongwang.p2p.entity.users;

import java.io.Serializable;
import java.util.Date;

import com.herongwang.p2p.dao.users.IUsersDao;
import com.sxj.mybatis.orm.annotations.Column;
import com.sxj.mybatis.orm.annotations.Entity;
import com.sxj.mybatis.orm.annotations.GeneratedValue;
import com.sxj.mybatis.orm.annotations.GenerationType;
import com.sxj.mybatis.orm.annotations.Id;
import com.sxj.mybatis.orm.annotations.Sn;
import com.sxj.mybatis.orm.annotations.Table;
import com.sxj.mybatis.pagination.Pagable;

@Entity(mapper = IUsersDao.class)
@Table(name = "Users")
public class UsersEntity extends Pagable implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 2828188766764116079L;
    
    /**
     * 会员ID
     */
    @Id(column = "customerId")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String customerId;
    
    /**
     * 客户编号
     */
    @Sn(pattern = "000000", step = 1, table = "T_SN", stub = "F_SN_NAME", sn = "F_SN_NUMBER", stubValue = "M")
    @Column(name = "customerNo")
    private String customerNo;
    
    /**
     * 密码
     */
    @Column(name = "password")
    private String password;
    
    /**
     * 姓名
     */
    @Column(name = "name")
    private String name;
    
    /**
     * 注册时间
     */
    @Column(name = "registerTime")
    private Date registerTime;
    
    /**
     * 邮箱
     */
    @Column(name = "email")
    private String email;
    
    /**
     * 手机号
     */
    @Column(name = "cellphone")
    private String cellphone;
    
    /**
     * 身份证号
     */
    @Column(name = "cardNum")
    private String cardNum;
    
    /**
     *持卡人姓名
     */
    @Column(name = "cardHolder")
    private String cardHolder;
    
    /**
     * 银行卡号
     */
    @Column(name = "cardNo")
    private String cardNo;
    
    /**
     * 状态
     * 0代表启用，1代表禁用
     */
    @Column(name = "status")
    private Integer status;
    
    /**
     * 更新时间
     */
    @Column(name = "updateTime")
    private Date updateTime;
    
    /**
     * 
     */
    @Column(name = "levelId")
    private String levelId;
    
    /**
     * 多多号
     */
    @Column(name = "accountNumber")
    private String accountNumber;
    
    /**
     * 用户的乾多多标识
     */
    @Column(name = "moneymoremoreId")
    private String moneymoremoreId;
    
    /**
     * 姓名匹配手续费
     */
    @Column(name = "authFee")
    private Double authFee;
    
    /**
     * 实名认证状态
     * 1.未实名认证
       2.快捷支付认证
       3.其他认证
     */
    @Column(name = "authState")
    private Integer authState;
    
    public String getAccountNumber()
    {
        return accountNumber;
    }
    
    public void setAccountNumber(String accountNumber)
    {
        this.accountNumber = accountNumber;
    }
    
    public String getMoneymoremoreId()
    {
        return moneymoremoreId;
    }
    
    public void setMoneymoremoreId(String moneymoremoreId)
    {
        this.moneymoremoreId = moneymoremoreId;
    }
    
    public Double getAuthFee()
    {
        return authFee;
    }
    
    public void setAuthFee(Double authFee)
    {
        authFee = authFee;
    }
    
    public Integer getAuthState()
    {
        return authState;
    }
    
    public void setAuthState(Integer authState)
    {
        authState = authState;
    }
    
    public String getCustomerId()
    {
        return customerId;
    }
    
    public void setCustomerId(String customerId)
    {
        this.customerId = customerId;
    }
    
    public String getCustomerNo()
    {
        return customerNo;
    }
    
    public void setCustomerNo(String customerNo)
    {
        this.customerNo = customerNo;
    }
    
    public String getPassword()
    {
        return password;
    }
    
    public void setPassword(String password)
    {
        this.password = password;
    }
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public Date getRegisterTime()
    {
        return registerTime;
    }
    
    public void setRegisterTime(Date registerTime)
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
    
    public String getCellphone()
    {
        return cellphone;
    }
    
    public void setCellphone(String cellphone)
    {
        this.cellphone = cellphone;
    }
    
    public String getCardNum()
    {
        return cardNum;
    }
    
    public void setCardNum(String cardNum)
    {
        this.cardNum = cardNum;
    }
    
    public String getCardHolder()
    {
        return cardHolder;
    }
    
    public void setCardHolder(String cardHolder)
    {
        this.cardHolder = cardHolder;
    }
    
    public String getCardNo()
    {
        return cardNo;
    }
    
    public void setCardNo(String cardNo)
    {
        this.cardNo = cardNo;
    }
    
    public Integer getStatus()
    {
        return status;
    }
    
    public void setStatus(Integer status)
    {
        this.status = status;
    }
    
    public Date getUpdateTime()
    {
        return updateTime;
    }
    
    public void setUpdateTime(Date updateTime)
    {
        this.updateTime = updateTime;
    }
    
    public String getLevelId()
    {
        return levelId;
    }
    
    public void setLevelId(String levelId)
    {
        this.levelId = levelId;
    }
    
}
