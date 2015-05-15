package com.herongwang.p2p.service.impl.orders;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.herongwang.p2p.dao.orders.IOrdersDao;
import com.herongwang.p2p.entity.orders.OrdersEntity;
import com.herongwang.p2p.service.orders.IOrdersService;
import com.sxj.util.exception.ServiceException;
import com.sxj.util.persistent.QueryCondition;

@Service
public class OrdersServiceImpl implements IOrdersService
{
    @Autowired
    private IOrdersDao ordersDao;
    
    @Override
    public void addOrders(OrdersEntity orders) throws ServiceException
    {
        ordersDao.addOrders(orders);
        
    }
    
    @Override
    public void updateOrders(OrdersEntity orders) throws ServiceException
    {
        ordersDao.updateOrders(orders);
        
    }
    
    @Override
    public OrdersEntity getOrdersEntity(String id) throws ServiceException
    {
        return ordersDao.getOrders(id);
    }
    
    @Override
    public List<OrdersEntity> queryOrdersList(OrdersEntity query)
            throws ServiceException
    {
        QueryCondition<OrdersEntity> condition = new QueryCondition<OrdersEntity>();
        condition.addCondition("customerId", query.getCustomerId());
        condition.addCondition("orderType", query.getOrderType());
        condition.addCondition("orderId", query.getOrderId());
        List<OrdersEntity> ordersList = ordersDao.query(condition);
        return ordersList;
    }
    
    @Override
    public void delOrders(String id) throws ServiceException
    {
        ordersDao.delOrders(id);
        
    }
    
    @Override
    public OrdersEntity getOrdersEntityByNo(String ordersNo)
            throws ServiceException
    {
        return ordersDao.getOrdersByNo(ordersNo);
    }
    
}
