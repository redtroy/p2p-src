package com.herongwang.p2p.service.orders;

import java.util.List;

import com.herongwang.p2p.entity.orders.OrdersEntity;
import com.sxj.util.exception.ServiceException;

public interface IOrdersService
{
    /**
     * 新增订单
     * aa
     * @param Orders
     */
    public void addOrders(OrdersEntity orders) throws ServiceException;
    
    /**
     * 更新订单
     * 
     * @param Orders
     */
    public void updateOrders(OrdersEntity orders) throws ServiceException;
    
    /**
     * 获取订单信息
     * 
     * @param id
     * @return
     */
    public OrdersEntity getOrdersEntity(String id) throws ServiceException;
    
    /**
     * 根据订单号获取订单信息
     * 
     * @param id
     * @return
     */
    public OrdersEntity getOrdersEntityByNo(String ordersNo)
            throws ServiceException;
    
    /**
     * 获取订单列表
     * 
     * @param query
     * @return
     */
    public List<OrdersEntity> queryOrdersList(OrdersEntity query)
            throws ServiceException;
    
    /**
     * 删除订单
     * 
     * @param id
     */
    public void delOrders(String id) throws ServiceException;
    
}
