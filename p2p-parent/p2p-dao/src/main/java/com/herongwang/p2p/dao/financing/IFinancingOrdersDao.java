package com.herongwang.p2p.dao.financing;

import java.util.List;

import com.herongwang.p2p.entity.financing.FinancingOrdersEntity;
import com.sxj.mybatis.orm.annotations.Delete;
import com.sxj.mybatis.orm.annotations.Get;
import com.sxj.mybatis.orm.annotations.Insert;
import com.sxj.mybatis.orm.annotations.Update;
import com.sxj.util.persistent.QueryCondition;

/**
 * 生成融资订单
 * @author nishaotang
 *
 */
public interface IFinancingOrdersDao
{
    /**
     * 添加融资订单
     * @param Order
     */
    @Insert
    public void addOrder(FinancingOrdersEntity order);
    
    /**
     * 修改融资订单
     * @param Order
     */
    @Update
    public void updateOrder(FinancingOrdersEntity order);
    
    /**
     * 删除融资订单
     * @param id
     */
    @Delete
    public void delOrder(String id);
    
    /**
     * 查询融资订单
     * @param id
     */
    @Get
    public FinancingOrdersEntity getOrder(String id);
    
    /**
     * 融资订单高级查询
     * @param query
     * @return
     */
    public List<FinancingOrdersEntity> query(
            QueryCondition<FinancingOrdersEntity> query);
    
    /**
     * 根据标的ID 查询融资订单
     * @return
     */
    public FinancingOrdersEntity getOrderByDebtId(String debtId);
}
