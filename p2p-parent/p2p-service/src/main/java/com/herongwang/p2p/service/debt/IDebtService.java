package com.herongwang.p2p.service.debt;

import java.util.List;

import com.herongwang.p2p.entity.debt.DebtEntity;
import com.sxj.util.exception.ServiceException;

public interface IDebtService
{
    /**
     * 新增借款标
     * aa
     * @param Debt
     */
    public void addDebt(DebtEntity Debt) throws ServiceException;
    
    /**
     * 更新借款标
     * 
     * @param Debt
     */
    public void updateDebt(DebtEntity Debt) throws ServiceException;
    
    /**
     * 获取借款标信息
     * 
     * @param id
     * @return
     */
    public DebtEntity getDebtEntity(String id) throws ServiceException;
    
    /**
     * 获取借款标列表
     * 
     * @param query
     * @return
     */
    public List<DebtEntity> queryDebtList(DebtEntity query)
            throws ServiceException;
    
    /**
     * 删除借款标
     * 
     * @param id
     */
    public void delDebt(String id) throws ServiceException;
    
    /**
     * 查询前五条标的
     */
    public List<DebtEntity> queryTop5();
    
    /**
     * 满标审核
     */
    public String audit(String debtId);
}
