package com.herongwang.p2p.entity.admin;

import java.io.Serializable;

import com.herongwang.p2p.dao.admin.IAdminDao;
import com.sxj.mybatis.orm.annotations.Column;
import com.sxj.mybatis.orm.annotations.Entity;
import com.sxj.mybatis.orm.annotations.GeneratedValue;
import com.sxj.mybatis.orm.annotations.GenerationType;
import com.sxj.mybatis.orm.annotations.Id;
import com.sxj.mybatis.orm.annotations.Table;

@Entity(mapper = IAdminDao.class)
@Table(name = "USER_INFO")
public class AdminEntity implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = -8561589767072456782L;
    
    /**
     * 主键ID
     */
    @Id(column = "USER_ID")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String userId;
    
    /**
     * 登录名
     */
    @Column(name = "USER_NO")
    private String userNo;
    
    /**
     * 姓名
     */
    @Column(name = "USER_NAME")
    private String userName;
    
    /**
     * 密码
     */
    @Column(name = "PASSWORD")
    private String password;
    
    /**
     * 电话
     */
    @Column(name = "TEL")
    private String tel;
    
    /**
     * 状态
     * 0 代表启用，1代表禁用
     */
    @Column(name = "STATUS")
    private Integer status;
    
    public String getUserId()
    {
        return userId;
    }
    
    public void setUserId(String userId)
    {
        this.userId = userId;
    }
    
    public String getUserNo()
    {
        return userNo;
    }
    
    public void setUserNo(String userNo)
    {
        this.userNo = userNo;
    }
    
    public String getUserName()
    {
        return userName;
    }
    
    public void setUserName(String userName)
    {
        this.userName = userName;
    }
    
    public String getPassword()
    {
        return password;
    }
    
    public void setPassword(String password)
    {
        this.password = password;
    }
    
    public String getTel()
    {
        return tel;
    }
    
    public void setTel(String tel)
    {
        this.tel = tel;
    }
    
    public Integer getStatus()
    {
        return status;
    }
    
    public void setStatus(Integer status)
    {
        this.status = status;
    }
    
}
