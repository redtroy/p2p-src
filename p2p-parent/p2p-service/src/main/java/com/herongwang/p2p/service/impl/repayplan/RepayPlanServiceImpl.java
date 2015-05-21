package com.herongwang.p2p.service.impl.repayplan;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.herongwang.p2p.dao.account.IAccountDao;
import com.herongwang.p2p.dao.debt.IDebtDao;
import com.herongwang.p2p.dao.financing.IFinancingOrdersDao;
import com.herongwang.p2p.dao.investorder.IInvestOrderDao;
import com.herongwang.p2p.dao.profitlist.IProfitListDao;
import com.herongwang.p2p.dao.repayPlan.IRepayPlanDao;
import com.herongwang.p2p.entity.account.AccountEntity;
import com.herongwang.p2p.entity.debt.DebtEntity;
import com.herongwang.p2p.entity.financing.FinancingOrdersEntity;
import com.herongwang.p2p.entity.funddetail.FundDetailEntity;
import com.herongwang.p2p.entity.investorder.InvestOrderEntity;
import com.herongwang.p2p.entity.profitlist.ProfitListEntity;
import com.herongwang.p2p.entity.repayPlan.RepayPlanEntity;
import com.herongwang.p2p.service.funddetail.IFundDetailService;
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
    IFundDetailService fundDetailService;
    
    @Autowired
    private IInvestOrderDao investOrderDao;
    
    @Autowired
    private IProfitListDao profitListDao;
    
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
            List<Integer> xhlist = new ArrayList<Integer>();//获取还款序号
            //统计所有还款总价格
            for (RepayPlanEntity repayPlanEntity : planlist)
            {
                monthAmount = monthAmount.add(repayPlanEntity.getMonthAmount());
                xhlist.add(repayPlanEntity.getSequence());
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
                fundDetailService.repayPlanFundDetail(planlist);//还款资金明细
                //投资方收款
                investGetMoney(debtId, xhlist);
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
            List<Integer> xhlist = new ArrayList<Integer>();//获取还款序号
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
                }
                xhlist.add(repayPlanEntity.getSequence());
            }
            fundDetailService.repayPlanFundDetail(planlist);//还款资金明细
            //投资方收款
            investGetMoney(debtId, xhlist);
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
    
    //投资方收款并生成明细
    public void investGetMoney(String debtId, List<Integer> xhlist)
    {
        //投资方收款
        List<InvestOrderEntity> orderList = investOrderDao.queryInvestorderList(debtId);//根据标的ID获取投资订单详情
        for (InvestOrderEntity orderEntity : orderList)
        {
            for (Integer se : xhlist)
            {
                if (orderEntity.getStatus() == 0)
                {
                    continue;
                }
                ProfitListEntity entity = profitListDao.getEntityBySeAndOrderId(se.toString(),
                        orderEntity.getOrderId());//通过订单ID和序号获取投资收益
                AccountEntity account = accountDao.getAcoountByCustomerId(orderEntity.getCustomerId());//获取账户信息
                account.setBalance(account.getBalance()
                        .add(entity.getMonthCapital()));//账户增加月本金
                account.setDueAmount(account.getDueAmount()
                        .subtract(entity.getMonthCapital()));
                FundDetailEntity fund1 = new FundDetailEntity();
                fund1.setCustomerId(orderEntity.getCustomerId());
                fund1.setAccountId(account.getAccountId());
                fund1.setOrderId(orderEntity.getOrderId());
                fund1.setAmount(entity.getMonthCapital());//金额 
                fund1.setBalance(account.getBalance());//账户可用额
                fund1.setFrozenAmount(account.getFozenAmount());
                fund1.setDueAmount(account.getDueAmount());//代收金额
                fund1.setCreateTime(new Date());
                fund1.setStatus(1);
                fund1.setType(7);
                fundDetailService.addFundDetail(fund1);
                account.setBalance(account.getBalance()
                        .add(entity.getMonthProfit()));
                account.setDueAmount(account.getDueAmount()
                        .subtract(entity.getMonthProfit()));
                FundDetailEntity fund2 = new FundDetailEntity();
                fund2.setCustomerId(orderEntity.getCustomerId());
                fund2.setAccountId(account.getAccountId());
                fund2.setOrderId(orderEntity.getOrderId());
                fund2.setAmount(entity.getMonthProfit());//金额 
                fund2.setBalance(account.getBalance());//账户可用额
                fund2.setFrozenAmount(account.getFozenAmount());
                fund2.setDueAmount(account.getDueAmount());//代收金额
                fund2.setCreateTime(new Date());
                fund2.setStatus(1);
                fund2.setType(8);
                fundDetailService.addFundDetail(fund2);
                if (entity.getFee() == null)
                {
                    entity.setFee(new BigDecimal(0));
                }
                account.setBalance(account.getBalance()
                        .subtract(entity.getFee()));
                FundDetailEntity fund3 = new FundDetailEntity();
                fund3.setCustomerId(orderEntity.getCustomerId());
                fund3.setAccountId(account.getAccountId());
                fund3.setOrderId(orderEntity.getOrderId());
                fund3.setAmount(entity.getFee());//金额 
                fund3.setBalance(account.getBalance());//账户可用额
                fund3.setFrozenAmount(account.getFozenAmount());
                fund3.setDueAmount(account.getDueAmount());//代收金额
                fund3.setCreateTime(new Date());
                fund3.setStatus(0);
                fund3.setType(11);
                fundDetailService.addFundDetail(fund3);
                accountDao.updateAccount(account);
            }
        }
    }
}
