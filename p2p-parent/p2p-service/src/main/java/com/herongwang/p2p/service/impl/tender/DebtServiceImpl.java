package com.herongwang.p2p.service.impl.tender;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.herongwang.p2p.dao.debt.IDebtDao;
import com.herongwang.p2p.entity.debt.DebtEntity;
import com.herongwang.p2p.service.tender.IDebtService;
import com.sxj.util.exception.ServiceException;
import com.sxj.util.logger.SxjLogger;
import com.sxj.util.persistent.QueryCondition;

@Service
@Transactional
public class DebtServiceImpl implements IDebtService
{
    
    @Autowired
    private IDebtDao DebtDao;
    
    @Override
    public void addDebt(DebtEntity Debt) throws ServiceException
    {
        DebtDao.addDebt(Debt);
        
    }
    
    @Override
    public void updateDebt(DebtEntity Debt) throws ServiceException
    {
        DebtDao.updateDebt(Debt);
        
    }
    
    @Override
    public DebtEntity getDebtEntity(String id) throws ServiceException
    {
        return DebtDao.getDebtFor(id);
    }
    
    @Override
    public List<DebtEntity> queryDebtList(DebtEntity query)
            throws ServiceException
    {
   try
        {
            
            QueryCondition<DebtEntity> condition = new QueryCondition<DebtEntity>();
            List<DebtEntity> debtList = new ArrayList<DebtEntity>();
            if (query == null)
            {
                return debtList;
            }
            condition.addCondition("name", query.getName());
            condition.setPage(query);
            debtList = DebtDao.query(condition);
            query.setPage(condition);
            return debtList;
        }
        catch (Exception e)
        {
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new ServiceException("查询标的列表信息错误", e);
        }
    }
    
    @Override
    public void delDebt(String id) throws ServiceException
    {
        DebtDao.delDebt(id);
        
    }
    

    @Override
    public List<DebtEntity> queryTop5()
    {
        try
        {
            return DebtDao.queryTop5();
        }
        catch (Exception e)
        {
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new ServiceException("查询标的前5条列表信息错误", e);
        }
    }
    

}
