package com.herongwang.p2p.service.impl.investorder;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.herongwang.p2p.dao.debt.IDebtDao;
import com.herongwang.p2p.dao.investorder.IInvestOrderDao;
import com.herongwang.p2p.entity.debt.DebtEntity;
import com.herongwang.p2p.entity.investorder.InvestOrderEntity;
import com.herongwang.p2p.service.investorder.IInvestOrderService;
import com.sxj.util.exception.ServiceException;
import com.sxj.util.logger.SxjLogger;
import com.sxj.util.persistent.QueryCondition;

@Service
@Transactional
public class InvestOrderServiceImpl implements IInvestOrderService
{
    @Autowired
    IInvestOrderDao investOrderDao;
    
    @Autowired
    IDebtDao debtDao;
    
    /**
     * 生成投资订单
     */
    @Override
    public void addOrder(String debtId, String amount) throws ServiceException
    {
        try
        {
            DebtEntity debt = debtDao.getDebtFor(debtId);//标的
            InvestOrderEntity io = new InvestOrderEntity();//投资订单
            io.setDebtId(debtId);
            
        }
        catch (Exception e)
        {
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new ServiceException("查询投资订单列表信息错误", e);
        }
        
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
        try
        {
            QueryCondition<InvestOrderEntity> condition = new QueryCondition<InvestOrderEntity>();
            List<InvestOrderEntity> investList = new ArrayList<InvestOrderEntity>();
            if (query == null)
            {
                return investList;
            }
            condition.addCondition("debtId", query.getDebtId());
            investList = investOrderDao.query(condition);
            return investList;
        }
        catch (Exception e)
        {
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new ServiceException("查询投资订单列表信息错误", e);
        }
    }
    
    @Override
    public void delOrder(String id) throws ServiceException
    {
        // TODO Auto-generated method stub
        
    }
    
}
