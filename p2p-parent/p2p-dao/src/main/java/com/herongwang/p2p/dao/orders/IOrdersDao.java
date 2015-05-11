package com.herongwang.p2p.dao.orders;

import java.util.List;

import com.herongwang.p2p.entity.orders.OrdersEntity;
import com.sxj.mybatis.orm.annotations.Delete;
import com.sxj.mybatis.orm.annotations.Get;
import com.sxj.mybatis.orm.annotations.Insert;
import com.sxj.mybatis.orm.annotations.Update;
import com.sxj.util.persistent.QueryCondition;

/**
 * 订单Dao
 * @author nishaotang
 *
 */
public interface IOrdersDao
{
    /**
     * 添加订单
     * @param Orders
     */
    @Insert
    public void addOrders(OrdersEntity orders);
    
    /**
     * 修改订单
     * @param Orders
     */
    @Update
    public void updateOrders(OrdersEntity orders);
    
    /**
     * 删除订单
     * @param id
     */
    @Delete
    public void delOrders(String id);
    
    /**
     * 查询订单
     * @param id
     */
    @Get
    public OrdersEntity getOrders(String id);
    
    /**
     * 订单高级查询
     * @param query
     * @return
     */
    public List<OrdersEntity> query(QueryCondition<OrdersEntity> query);
    
}
