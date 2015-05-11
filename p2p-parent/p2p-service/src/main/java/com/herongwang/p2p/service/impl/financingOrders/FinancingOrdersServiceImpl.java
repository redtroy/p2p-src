package com.herongwang.p2p.service.impl.financingOrders;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.herongwang.p2p.dao.financing.IFinancingOrdersDao;
import com.herongwang.p2p.entity.financing.FinancingOrdersEntity;
import com.sxj.util.exception.ServiceException;

@Service
public class FinancingOrdersServiceImpl implements IFinancingOrdersService
{
    @Autowired
    private IFinancingOrdersDao financingOrdersDao;
    
    @Override
    public void addOrder(FinancingOrdersEntity order) throws ServiceException
    {
        financingOrdersDao.addOrder(order);
    }
    
    @Override
    public void updateOrder(FinancingOrdersEntity order)
            throws ServiceException
    {
        financingOrdersDao.updateOrder(order);
        
    }
    
    @Override
    public FinancingOrdersEntity getFinancingOrdersEntity(String id)
            throws ServiceException
    {
        return financingOrdersDao.getOrder(id);
    }
    
    @Override
    public List<FinancingOrdersEntity> queryOrderList(
            FinancingOrdersEntity query) throws ServiceException
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public void delOrder(String id) throws ServiceException
    {
        // TODO Auto-generated method stub
        
    }
    
}
