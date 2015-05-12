package com.herongwang.p2p.service.impl.repayplan;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.herongwang.p2p.dao.repayPlan.IRepayPlanDao;
import com.herongwang.p2p.entity.repayPlan.RepayPlanEntity;
import com.herongwang.p2p.service.repayplan.IRepayPlanService;
import com.sxj.util.exception.ServiceException;
import com.sxj.util.logger.SxjLogger;
import com.sxj.util.persistent.QueryCondition;

@Service
@Transactional
public class RepayPlanServiceImpl implements IRepayPlanService
{
    
    @Autowired
    IRepayPlanDao repayPlanDao;
    
    @Override
    public void addRepayPlan(RepayPlanEntity plan) throws ServiceException
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void updateRepayPlan(RepayPlanEntity plan) throws ServiceException
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public RepayPlanEntity getRepayPlan(String id) throws ServiceException
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public List<RepayPlanEntity> queryRepayPlan(RepayPlanEntity query)
            throws ServiceException
    {
        try
        {
            
            List<RepayPlanEntity> planList;
            QueryCondition<RepayPlanEntity> condition = new QueryCondition<RepayPlanEntity>();
            condition.addCondition("orderId", query.getOrderId());//订单id
            planList = repayPlanDao.queryRepayPlan(condition);
            return planList;
        }
        catch (ServiceException e)
        {
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new ServiceException(e.getMessage());
        }
        catch (Exception e)
        {
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new ServiceException("查询还款计划错误", e);
        }
    }
    
    @Override
    public void delRepayPlan(String id) throws ServiceException
    {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * 还款
     */
    @Override
    @Transactional
    public void repayment(String[] ids) throws ServiceException
    {
        try
        {
            //获取到还款计划
            List<RepayPlanEntity> planlist=repayPlanDao.getRepayPlanList(ids);
            BigDecimal monthAmount=null;
            //统计所有还款总价格
            for (RepayPlanEntity repayPlanEntity : planlist)
            {
                monthAmount.add(repayPlanEntity.getMonthAmount()).doubleValue();
            }
        }
        catch (ServiceException e)
        {
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new ServiceException(e.getMessage());
        }
        catch (Exception e)
        {
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new ServiceException("查询还款计划错误", e);
        }
    }
    
}
