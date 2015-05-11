package com.herongwang.p2p.service.impl.investOrder;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.herongwang.p2p.dao.investOrder.IInvestOrderDao;
import com.herongwang.p2p.entity.investOrder.InvestOrderEntity;
import com.herongwang.p2p.service.investOrder.IInvestOrderService;
import com.sxj.util.exception.ServiceException;
import com.sxj.util.persistent.QueryCondition;
@Service
@Transactional
public class InvestOrderServiceImpl implements IInvestOrderService
{
    @Autowired
    IInvestOrderDao investOrderDao;
    
    @Override
    public void addOrder(InvestOrderEntity order) throws ServiceException
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void updateOrder(InvestOrderEntity order) throws ServiceException
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public InvestOrderEntity getInvestOrderEntity(String id)
            throws ServiceException
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public List<InvestOrderEntity> queryorderList(InvestOrderEntity query)
            throws ServiceException
    {
        QueryCondition<InvestOrderEntity> condition = new QueryCondition<InvestOrderEntity>();
        condition.addCondition("debtId", query.getDebtId());
        condition.setPage(query);
         List<InvestOrderEntity> contractList = investOrderDao.query(condition);
        return contractList;
    }
    
    @Override
    public void delOrder(String id) throws ServiceException
    {
        // TODO Auto-generated method stub
        
    }
    
}
