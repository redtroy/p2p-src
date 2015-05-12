package com.herongwang.p2p.service.repayplan;

import java.sql.SQLException;
import java.util.List;

import com.herongwang.p2p.entity.repayPlan.RepayPlanEntity;
import com.sxj.util.exception.ServiceException;

public interface IRepayPlanService
{
    /**
     * 新增还款计划
     * @param apply
     */
    public void addRepayPlan(RepayPlanEntity plan) throws ServiceException;
    
    /**
     * 更新还款计划
     * 
     * @param apply
     */
    public void updateRepayPlan(RepayPlanEntity plan) throws ServiceException;
    
    /**
     * 获取还款计划信息
     * 
     * @param id
     * @return
     */
    public RepayPlanEntity getRepayPlan(String id) throws ServiceException;
    
    /**
     * 获取还款计划列表
     * 
     * @param query
     * @return
     * @throws SQLException 
     */
    public List<RepayPlanEntity> queryRepayPlan(RepayPlanEntity query)
            throws ServiceException;
    
    /**
     * 删除还款计划
     * 
     * @param id
     */
    public void delRepayPlan(String id) throws ServiceException;

    /**
     * 还款
     * @param ids
     * @throws ServiceException
     */
    void repayment(String[] ids) throws ServiceException;
}
