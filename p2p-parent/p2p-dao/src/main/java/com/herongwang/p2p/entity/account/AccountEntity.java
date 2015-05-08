package com.herongwang.p2p.entity.account;

import java.io.Serializable;

import com.herongwang.p2p.dao.account.IAccountDao;
import com.sxj.mybatis.orm.annotations.Column;
import com.sxj.mybatis.orm.annotations.Entity;
import com.sxj.mybatis.orm.annotations.GeneratedValue;
import com.sxj.mybatis.orm.annotations.GenerationType;
import com.sxj.mybatis.orm.annotations.Id;
import com.sxj.mybatis.orm.annotations.Table;

@Entity(mapper = IAccountDao.class)
@Table(name = "ACCOUNT")
public class AccountEntity implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = -3102751924185282943L;
    
    /**
     * 主键ID
     */
    @Id(column = "ACCOUNT_ID")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String accountId;
    
    /**
     * 会员ID
     */
    @Column(name = "MEMBER_ID")
    private String memberId;
    
    /**
     * 用户卡号
     */
    @Column(name = "BANK_CODE")
    private String bankCode;
    
    /**
     * 持卡姓名
     */
    @Column(name = "CODE_NAME")
    private String codeName;
    
    /**
     * 开户行
     */
    @Column(name = "OPENING_BANK")
    private String openingBank;
    
    /**
     * 开户行支行
     */
    @Column(name = "BRANCH_BANK")
    private String branchBank;
    
    /**
     * 可用金额
     */
    @Column(name = "AVAILABLE_M")
    private Double availableM;
    
    /**
     * 状态
     */
    @Column(name = "STSTUS")
    private Integer ststus;
    
    public String getAccountId()
    {
        return accountId;
    }
    
    public void setAccountId(String accountId)
    {
        this.accountId = accountId;
    }
    
    public String getMemberId()
    {
        return memberId;
    }
    
    public void setMemberId(String memberId)
    {
        this.memberId = memberId;
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
    
    public String getOpeningBank()
    {
        return openingBank;
    }
    
    public void setOpeningBank(String openingBank)
    {
        this.openingBank = openingBank;
    }
    
    public String getBranchBank()
    {
        return branchBank;
    }
    
    public void setBranchBank(String branchBank)
    {
        this.branchBank = branchBank;
    }
    
    public Double getAvailableM()
    {
        return availableM;
    }
    
    public void setAvailableM(Double availableM)
    {
        this.availableM = availableM;
    }
    
    public Integer getStstus()
    {
        return ststus;
    }
    
    public void setStstus(Integer ststus)
    {
        this.ststus = ststus;
    }
    
}
