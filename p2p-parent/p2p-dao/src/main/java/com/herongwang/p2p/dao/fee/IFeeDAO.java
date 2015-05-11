package com.herongwang.p2p.dao.fee;

import com.herongwang.p2p.entity.fee.Fee;
import com.sxj.mybatis.orm.annotations.Delete;
import com.sxj.mybatis.orm.annotations.Get;
import com.sxj.mybatis.orm.annotations.Insert;
import com.sxj.mybatis.orm.annotations.Update;

public interface IFeeDAO
{
    @Insert
    public Fee createFee(Fee fee);
    
    @Delete
    public void deleteFeeById(String feeId);
    
    @Update
    public void updateFee(Fee newFee);
    
    @Get
    public Fee getFeeById(String feeId);
    
}
