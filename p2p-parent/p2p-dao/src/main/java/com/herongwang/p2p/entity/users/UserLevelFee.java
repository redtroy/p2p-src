package com.herongwang.p2p.entity.users;

import com.herongwang.p2p.dao.users.IUserLevelFeeDao;
import com.sxj.mybatis.orm.annotations.Column;
import com.sxj.mybatis.orm.annotations.Entity;
import com.sxj.mybatis.orm.annotations.Id;
import com.sxj.mybatis.orm.annotations.Table;

@Entity(mapper = IUserLevelFeeDao.class)
@Table(name = "UserLevelFee")
public class UserLevelFee
{
    @Id(column = "levelId")
    @Column(name = "levelId")
    private String userLevelId;
    
    @Column(name = "discountId")
    private String discountId;
    
    public String getUserLevelId()
    {
        return userLevelId;
    }
    
    public void setUserLevelId(String userLevelId)
    {
        this.userLevelId = userLevelId;
    }
    
    public String getDiscountId()
    {
        return discountId;
    }
    
    public void setDiscountId(String discountId)
    {
        this.discountId = discountId;
    }
    
}
