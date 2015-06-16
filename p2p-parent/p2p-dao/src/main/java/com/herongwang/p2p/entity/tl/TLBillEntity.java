package com.herongwang.p2p.entity.tl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.herongwang.p2p.dao.tl.ITLBillDao;
import com.sxj.mybatis.orm.annotations.Column;
import com.sxj.mybatis.orm.annotations.Entity;
import com.sxj.mybatis.orm.annotations.GeneratedValue;
import com.sxj.mybatis.orm.annotations.GenerationType;
import com.sxj.mybatis.orm.annotations.Id;
import com.sxj.mybatis.orm.annotations.Table;

@Entity(mapper = ITLBillDao.class)
@Table(name = "TL_BILL")
public class TLBillEntity implements Serializable
{
    
    /**
     * 
     */
    private static final long serialVersionUID = -1420073084751502868L;
    
    /**
     * 账单id
     */
    @Id(column = "BILL_ID")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String billId;
    
    /**
     * 订单号
     */
    @Column(name = "MERCHANT_BILL_NO")
    private String merchantBillNo;
    
    /**
     * 商户号
     */
    @Column(name = "MERCHANT_NO")
    private String merchantNo;
    
    /**
     * 通联订单号
     */
    @Column(name = "TL_BILL_NO")
    private String tlBillNo;
    
    /**
     * 商户订单提交时间
     */
    @Column(name = "SUBMIT_TIME")
    private Date submitTime;
    
    /**
     * 商户订单金额
     */
    @Column(name = "BILL_MONEY")
    private BigDecimal billMoney;
    
    /**
     * 支付完成时间
     */
    @Column(name = "FINISH_TIME")
    private Date finishTime;
    
    /**
     * 订单实际支付金额
     */
    @Column(name = "ACTUAL_MONEY")
    private BigDecimal actualMoney;
    
    /**
     * 扩展字段1
     */
    @Column(name = "REMARK1")
    private String remark1;
    
    /**
     * 扩展字段2
     */
    @Column(name = "REMARK2")
    private String remark2;
    
    /**
     * 处理结果
     * 1代表支付成功；0代表未支付
     */
    @Column(name = "STARUS")
    private Integer starus;
    
    public String getBillId()
    {
        return billId;
    }
    
    public void setBillId(String billId)
    {
        this.billId = billId;
    }
    
    public String getMerchantBillNo()
    {
        return merchantBillNo;
    }
    
    public void setMerchantBillNo(String merchantBillNo)
    {
        this.merchantBillNo = merchantBillNo;
    }
    
    public String getMerchantNo()
    {
        return merchantNo;
    }
    
    public void setMerchantNo(String merchantNo)
    {
        this.merchantNo = merchantNo;
    }
    
    public String getTlBillNo()
    {
        return tlBillNo;
    }
    
    public void setTlBillNo(String tlBillNo)
    {
        this.tlBillNo = tlBillNo;
    }
    
    public Date getSubmitTime()
    {
        return submitTime;
    }
    
    public void setSubmitTime(Date submitTime)
    {
        this.submitTime = submitTime;
    }
    
    public BigDecimal getBillMoney()
    {
        return billMoney;
    }
    
    public void setBillMoney(BigDecimal billMoney)
    {
        this.billMoney = billMoney;
    }
    
    public Date getFinishTime()
    {
        return finishTime;
    }
    
    public void setFinishTime(Date finishTime)
    {
        this.finishTime = finishTime;
    }
    
    public BigDecimal getActualMoney()
    {
        return actualMoney;
    }
    
    public void setActualMoney(BigDecimal actualMoney)
    {
        this.actualMoney = actualMoney;
    }
    
    public String getRemark1()
    {
        return remark1;
    }
    
    public void setRemark1(String remark1)
    {
        this.remark1 = remark1;
    }
    
    public String getRemark2()
    {
        return remark2;
    }
    
    public void setRemark2(String remark2)
    {
        this.remark2 = remark2;
    }
    
    public Integer getStarus()
    {
        return starus;
    }
    
    public void setStarus(Integer starus)
    {
        this.starus = starus;
    }
    
}
