package com.herongwang.p2p.dao.funddetail;

import java.sql.SQLException;
import java.util.List;

import com.herongwang.p2p.entity.funddetail.FundDetailEntity;
import com.sxj.mybatis.orm.annotations.BatchInsert;
import com.sxj.mybatis.orm.annotations.Delete;
import com.sxj.mybatis.orm.annotations.Get;
import com.sxj.mybatis.orm.annotations.Insert;
import com.sxj.mybatis.orm.annotations.Update;
import com.sxj.util.persistent.QueryCondition;

public interface IFundDetailDao
{
    /**
     * 添加交易明细
     * @param Deal
     */
    @Insert
    public void addFundDetail(FundDetailEntity deal);
    
    /**
     * 修改交易明细
     * @param Deal
     */
    @Update
    public void updateFundDetail(FundDetailEntity deal);
    
    /**
     * 删除交易明细
     * @param id
     */
    @Delete
    public void delFundDetail(String id);
    
    /**
     * 查询交易明细
     * @param id
     */
    @Get
    public FundDetailEntity getFundDetail(String id);
    
    /**
     * 交易明细高级查询
     * @param query
     * @return
     */
    public List<FundDetailEntity> queryFundDetail(
            QueryCondition<FundDetailEntity> query) throws SQLException;
    
    /**
     * 批量插入明细
     * @param deal
     */
    @BatchInsert
    public void addFundDetailList(List<FundDetailEntity> deal);
}
