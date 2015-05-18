package com.herongwang.p2p.service.impl.repayplan;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.herongwang.p2p.dao.account.IAccountDao;
import com.herongwang.p2p.dao.debt.IDebtDao;
import com.herongwang.p2p.dao.financing.IFinancingOrdersDao;
import com.herongwang.p2p.dao.repayPlan.IRepayPlanDao;
import com.herongwang.p2p.entity.account.AccountEntity;
import com.herongwang.p2p.entity.debt.DebtEntity;
import com.herongwang.p2p.entity.financing.FinancingOrdersEntity;
import com.herongwang.p2p.entity.investorder.InvestOrderEntity;
import com.herongwang.p2p.entity.repayPlan.RepayPlanEntity;
import com.herongwang.p2p.model.profit.ProfitModel;
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
    
    @Autowired
    IAccountDao accountDao;
    
    @Autowired
    IFinancingOrdersDao financingOrdersDao;
    
    @Autowired
    IDebtDao debtDao;
    
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
    public List<RepayPlanEntity> queryRepayPlan(FinancingOrdersEntity order)
            throws ServiceException
    {
        try
        {
            List<RepayPlanEntity> planList;
            QueryCondition<RepayPlanEntity> condition = new QueryCondition<RepayPlanEntity>();
            condition.addCondition("orderId", order.getOrderId());//订单id
            condition.addCondition("debtId", order.getDebtId());//订单id
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
     * 验证余额是否充足
     */
    @Override
    @Transactional
    public String getBalance(String[] ids, String orderId, String debtId)
            throws ServiceException
    {
        try
        {
            //获取到还款计划
            List<RepayPlanEntity> planlist = repayPlanDao.getRepayPlanList(ids);
            BigDecimal monthAmount = new BigDecimal(0);
            //统计所有还款总价格
            for (RepayPlanEntity repayPlanEntity : planlist)
            {
                monthAmount = monthAmount.add(repayPlanEntity.getMonthAmount());
            }
            AccountEntity account = accountDao.getAccountByOrderId(orderId);
            int flag = account.getBalance().compareTo(monthAmount);
            if (flag == -1)
            {
                return "no";
            }
            else
            {
                //余额足够直接还款
                account.setBalance(account.getBalance().subtract(monthAmount));//减去余额
                accountDao.updateAccount(account);
                repayPlanDao.updateRepayPlanStatus(ids);//还款状态
                Integer num = repayPlanDao.getRepayPlanCount(orderId);
                if (num == 0)
                {
                    DebtEntity db = new DebtEntity();
                    db.setDebtId(debtId);
                    db.setStatus(5);
                    debtDao.updateDebt(db);
                }
                return "ok";
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
            throw new ServiceException("验证余额错误", e);
        }
    }
    
    @Override
    public String saveRepayPlan(String[] ids, String orderId, String debtId)
            throws ServiceException
    {
        try
        {
            //获取到还款计划
            List<RepayPlanEntity> planlist = repayPlanDao.getRepayPlanList(ids);
            for (RepayPlanEntity repayPlanEntity : planlist)
            {
                AccountEntity account = accountDao.getAccountByOrderId(orderId);//账户余额
                int flag = account.getBalance()
                        .compareTo(repayPlanEntity.getMonthAmount());//0 相等 1大于  -1 小于
                if (flag >= 0)
                {
                    //扣除款项
                    BigDecimal b = account.getBalance()
                            .subtract(repayPlanEntity.getMonthAmount());
                    account.setBalance(b);//减去余额
                    accountDao.updateAccount(account);//更新账户余额
                    repayPlanEntity.setStatus(1);//
                    repayPlanDao.updateRepayPlan(repayPlanEntity);//更新状态
                    Integer num = repayPlanDao.getRepayPlanCount(orderId);
                    if (num == 0)
                    {
                        DebtEntity db = new DebtEntity();
                        db.setDebtId(debtId);
                        db.setStatus(5);
                        debtDao.updateDebt(db);
                    }
                }
                else
                {
                    //账户余额不足以还款计划
                    account.setDebtAmount(account.getDebtAmount()
                            .add(repayPlanEntity.getMonthAmount()));//负债总额
                    accountDao.updateAccount(account);//更新账户余额
                    repayPlanEntity.setStatus(1);//
                    repayPlanEntity.setPrepaidStatus(1);//0:为垫付1:垫付
                    repayPlanDao.updateRepayPlan(repayPlanEntity);//更新状态
                    Integer num = repayPlanDao.getRepayPlanCount(orderId);
                    if (num == 0)
                    {
                        DebtEntity db = new DebtEntity();
                        db.setDebtId(debtId);
                        db.setStatus(5);
                        debtDao.updateDebt(db);
                    }
                    //生成还款资金明细
                    //更新对应账单
                }
            }
            return "ok";
        }
        catch (ServiceException e)
        {
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new ServiceException(e.getMessage());
        }
        catch (Exception e)
        {
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new ServiceException("验证余额错误", e);
        }
    }
}
