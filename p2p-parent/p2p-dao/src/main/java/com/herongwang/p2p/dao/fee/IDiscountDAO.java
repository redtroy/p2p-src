package com.herongwang.p2p.dao.fee;

import java.sql.SQLException;
import java.util.List;

import com.herongwang.p2p.entity.fee.DiscountEntity;
import com.sxj.mybatis.orm.annotations.Delete;
import com.sxj.mybatis.orm.annotations.Get;
import com.sxj.mybatis.orm.annotations.Insert;
import com.sxj.mybatis.orm.annotations.Update;

public interface IDiscountDAO
{
    @Insert
    public void createDiscount(DiscountEntity discount);
    
    @Get
    public DiscountEntity getDiscountById(String discountId);
    
    @Update
    public int updateDiscount(DiscountEntity newDiscount);
    
    @Delete
    public int deleteDiscountById(String discountId);
    
    public List<DiscountEntity> getDiscountByCustomerId(String customerId)
            throws SQLException;
}
