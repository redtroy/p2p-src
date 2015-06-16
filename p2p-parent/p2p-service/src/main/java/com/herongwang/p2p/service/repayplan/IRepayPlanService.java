package com.herongwang.p2p.service.repayplan;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.herongwang.p2p.entity.financing.FinancingOrdersEntity;
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
    public List<RepayPlanEntity> queryRepayPlan(FinancingOrdersEntity order)
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
    String saveRepayPlan(String[] ids, String orderId, String debtId)
            throws ServiceException;
    
    /**
     * 生成还款明细接口
     * @param ids
     * @param orderId
     * @param debtId
     * @return
     */
    public String repay(String ids, String orderId, String debtId);
    
    /**
     * 验证余额
     * @param ids
     * @param orderId
     * @return
     * @throws ServiceException
     */
    String getBalance(String ids, String orderId, String debtId, String Url)
            throws ServiceException;
    
    /**
     * 获取转账会员的 乾多多平台号 和 金额
     */
    public List<List<Map<String, String>>> getTransferList(String ids,
            String orderId, String debtId);
    
    /**
     * 自动审核所有到时间的还款
     * @param url
     * @return
     */
    public String refundAudit(String url, String debtId);
}
