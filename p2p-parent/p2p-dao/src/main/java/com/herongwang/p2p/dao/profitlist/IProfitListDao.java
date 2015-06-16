package com.herongwang.p2p.dao.profitlist;

import java.sql.SQLException;
import java.util.List;

import com.herongwang.p2p.entity.profitlist.ProfitListEntity;
import com.sxj.mybatis.orm.annotations.BatchInsert;
import com.sxj.mybatis.orm.annotations.Get;
import com.sxj.mybatis.orm.annotations.Insert;
import com.sxj.mybatis.orm.annotations.Update;
import com.sxj.util.persistent.QueryCondition;

public interface IProfitListDao
{
    /**
     * 添加收益明细
     * @param list
     * @throws SQLException
     */
    @BatchInsert
    public void addProfitList(List<ProfitListEntity> list) throws SQLException;
    
    /**
     * 添加单个明细
     * @param profit
     */
    @Insert
    public void addProfit(ProfitListEntity profit);
    
    /**
     * 查询收益
     * @param query
     * @return
     * @throws SQLException
     */
    public List<ProfitListEntity> query(QueryCondition<ProfitListEntity> query)
            throws SQLException;
    
    /**
     * 根据 序号和 订单ID查询实体
     * @param se
     * @param orderId
     * @return
     */
    public ProfitListEntity getEntityBySeAndOrderId(String se, String orderId);
    
    /**
     * 更新受益明细
     */
    @Update
    public void updateProfitList(ProfitListEntity pro);
    
    /**
     * 查询收益明细
     * @param id
     */
    @Get
    public ProfitListEntity getProfitListEntity(String id);
}
