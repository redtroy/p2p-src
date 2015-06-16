package com.herongwang.p2p.service.impl.apply;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.herongwang.p2p.dao.apply.IDebtApplicationDao;
import com.herongwang.p2p.entity.apply.DebtApplicationEntity;
import com.herongwang.p2p.service.apply.IDebtApplicationService;
import com.sxj.util.exception.ServiceException;
import com.sxj.util.logger.SxjLogger;
import com.sxj.util.persistent.QueryCondition;

@Service
@Transactional
public class DebtApplicationImpl implements IDebtApplicationService
{
    
    @Autowired
    private IDebtApplicationDao debtApplicationDao;
    
    /**
     * 申请融资
     */
    @Override
    @Transactional
    public void addApply(DebtApplicationEntity apply) throws ServiceException
    {
        try
        {
            debtApplicationDao.addApply(apply);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            SxjLogger.error(e.getMessage(), e, this.getClass());
        }
        
    }
    
    @Override
    @Transactional
    public void updateApply(DebtApplicationEntity apply)
            throws ServiceException
    {
        try
        {
            debtApplicationDao.updateApply(apply);
        }
        catch (Exception e)
        {
            SxjLogger.error(e.getMessage(), e, this.getClass());
            e.printStackTrace();
        }
        
    }
    
    @Override
    @Transactional(readOnly = true)
    public DebtApplicationEntity getApplyForEntity(String id)
            throws ServiceException
    {
        
        DebtApplicationEntity debtApplicationEntity = debtApplicationDao.getApply(id);
        return debtApplicationEntity;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<DebtApplicationEntity> queryApply(DebtApplicationEntity query)
            throws ServiceException
    {
        QueryCondition<DebtApplicationEntity> condition = new QueryCondition<DebtApplicationEntity>();
        condition.addCondition("name", query.getName());
        condition.setPage(query);
        List<DebtApplicationEntity> contractList = debtApplicationDao.query(condition);
        query.setPage(condition);
        return contractList;
    }
    
    @Override
    @Transactional
    public void delApply(String id) throws ServiceException
    {
        try
        {
            debtApplicationDao.delApply(id);
        }
        catch (Exception e)
        {
            SxjLogger.error(e.getMessage(), e, this.getClass());
            e.printStackTrace();
        }
        
    }
    
}
