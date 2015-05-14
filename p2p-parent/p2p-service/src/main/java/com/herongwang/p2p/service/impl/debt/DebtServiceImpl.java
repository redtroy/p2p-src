package com.herongwang.p2p.service.impl.debt;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.herongwang.p2p.dao.debt.IDebtDao;
import com.herongwang.p2p.dao.financing.IFinancingOrdersDao;
import com.herongwang.p2p.entity.debt.DebtEntity;
import com.herongwang.p2p.entity.financing.FinancingOrdersEntity;
import com.herongwang.p2p.entity.repayPlan.RepayPlanEntity;
import com.herongwang.p2p.model.profit.ProfitModel;
import com.herongwang.p2p.service.debt.IDebtService;
import com.herongwang.p2p.service.profit.IProfitService;
import com.sxj.util.exception.ServiceException;
import com.sxj.util.logger.SxjLogger;
import com.sxj.util.persistent.QueryCondition;

@Service
@Transactional
public class DebtServiceImpl implements IDebtService
{
    
    @Autowired
    private IDebtDao DebtDao;
    
    @Autowired
    private IProfitService profitService;
    
    @Autowired
    private IFinancingOrdersDao financingOrder;
    
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
            condition.addCondition("customerId", query.getCustomerId());//会员ID
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
    
    @Override
    public String audit(String debtId) throws ServiceException
    {
        try
        {
            DecimalFormat df = new DecimalFormat(".00");
            //更新标的信息
            DebtEntity debt = DebtDao.getDebtFor(debtId); //获取标的信息
            debt.setStatus(4);
            DebtDao.updateDebt(debt);
            //---
            BigDecimal amout = debt.getAmount();
            ProfitModel prift = profitService.calculatingProfit(debtId, amout);//获取利息，总额
            //查询更新融资订单
            FinancingOrdersEntity financeOrder = financingOrder.getOrderByDebtId(debtId);//查询订单
            financeOrder.setLoanAmount(debt.getAmount());
            financeOrder.setActualAmount(debt.getAmount());
            financeOrder.setTotalFee(prift.getAmount());
            financeOrder.setTotalAmount(prift.getInvestment());
            financeOrder.setProfitAmount(prift.getTotalInterest());
            financingOrder.updateOrder(financeOrder);
            //生成还款计划表
            RepayPlanEntity repayPlan = new RepayPlanEntity(); //
            repayPlan.setOrderId(financeOrder.getOrderId());
            // repayPlan.setSequence(sequence);  
            repayPlan.setMonthCapital(new BigDecimal(df.format(debt.getAmount()
                    .divide(new BigDecimal(debt.getMonths()))))); //月本金
            repayPlan.setMonthProfit(new BigDecimal(
                    df.format(prift.getTotalInterest().divide(new BigDecimal(
                            debt.getMonths()))))); //月利息
            repayPlan.setMonthAmount(repayPlan.getMonthCapital()
                    .add(repayPlan.getMonthProfit())); //月总额
            repayPlan.setLeftAmount(repayPlan.getLeftAmount()); //剩余本息总额
        }
        catch (Exception e)
        {
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new ServiceException("满标审核错误", e);
        }
        return null;
    }
    
}
