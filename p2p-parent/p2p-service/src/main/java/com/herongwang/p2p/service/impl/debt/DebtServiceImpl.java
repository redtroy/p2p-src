package com.herongwang.p2p.service.impl.debt;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.herongwang.p2p.dao.account.IAccountDao;
import com.herongwang.p2p.dao.debt.IDebtDao;
import com.herongwang.p2p.dao.financing.IFinancingOrdersDao;
import com.herongwang.p2p.dao.funddetail.IFundDetailDao;
import com.herongwang.p2p.dao.investorder.IInvestOrderDao;
import com.herongwang.p2p.dao.repayPlan.IRepayPlanDao;
import com.herongwang.p2p.entity.account.AccountEntity;
import com.herongwang.p2p.entity.debt.DebtEntity;
import com.herongwang.p2p.entity.fee.DiscountEntity;
import com.herongwang.p2p.entity.financing.FinancingOrdersEntity;
import com.herongwang.p2p.entity.funddetail.FundDetailEntity;
import com.herongwang.p2p.entity.investorder.InvestOrderEntity;
import com.herongwang.p2p.entity.repayPlan.RepayPlanEntity;
import com.herongwang.p2p.model.MonthProfit;
import com.herongwang.p2p.model.profit.ProfitModel;
import com.herongwang.p2p.service.debt.IDebtService;
import com.herongwang.p2p.service.fee.IDiscountService;
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
    
    @Autowired
    private IRepayPlanDao repayDao;
    
    @Autowired
    private IAccountDao accountDao;
    
    @Autowired
    private IFundDetailDao fundDetailDao;
    
    @Autowired
    private IInvestOrderDao investDao;
    
    @Autowired
    private IDiscountService discountService;
    
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
            List<RepayPlanEntity> reList = new ArrayList<RepayPlanEntity>();
            BigDecimal monthCapital = new BigDecimal(df.format(debt.getAmount()
                    .divide(new BigDecimal(debt.getMonths()),
                            2,
                            BigDecimal.ROUND_HALF_UP)));
            BigDecimal monthProfit = new BigDecimal(
                    df.format(prift.getTotalInterest().divide(new BigDecimal(
                            debt.getMonths()),
                            2,
                            BigDecimal.ROUND_HALF_UP)));
            for (int i = 0; i < prift.getMonthProfit().size(); i++)
            {
                RepayPlanEntity repayPlan = new RepayPlanEntity(); //
                repayPlan.setOrderId(financeOrder.getOrderId());
                repayPlan.setDebtId(debtId);
                repayPlan.setSequence(i + 1);
                repayPlan.setMonthCapital(prift.getMonthProfit()
                        .get(i)
                        .getMonthCapital()); //月本金
                repayPlan.setMonthProfit(prift.getMonthProfit()
                        .get(i)
                        .getMonthProfit()); //月利息
                repayPlan.setMonthAmount(prift.getMonthProfit()
                        .get(i)
                        .getMonthAmount()); //月总额
                repayPlan.setLeftAmount(prift.getMonthProfit()
                        .get(i)
                        .getLeftAmount()); //剩余本息总额
                repayPlan.setStatus(0);
                repayPlan.setCreateTime(new Date());
                repayPlan.setUpdateTime(new Date());
                repayPlan.setPrepaidStatus(0);
                reList.add(repayPlan);
            }
            repayDao.addRepayPlanList(reList);
            //根据会员ID 查询账户
            AccountEntity account = accountDao.getAcoountByCustomerId(debt.getCustomerId());
            //更新账户信息
            account.setBalance(financeOrder.getAmount()
                    .subtract((financeOrder.getAmount().multiply(new BigDecimal(
                            0.03)))));
            accountDao.updateAccount(account);
            //生成融资方资金明细
            //获取到融资方费率
            FundDetailEntity fd = new FundDetailEntity();
            
            fd.setCustomerId(debt.getCustomerId());//用户ID
            fd.setAccountId(account.getAccountId());
            fd.setOrderId(financeOrder.getOrderId());
            fd.setAmount(debt.getAmount());
            fd.setBalance(account.getBalance());
            fd.setFrozenAmount(account.getFozenAmount());
            fd.setDueAmount(account.getDueAmount());
            fd.setCreateTime(new Date());
            fd.setStatus(1);//收入
            fd.setType(8);//投标
            fd.setRemark("融资" + debt.getTitle() + ",完成");
            fundDetailDao.addFundDetail(fd);
            List<DiscountEntity> list = discountService.getDiscountByCustomerId(debt.getCustomerId());
            //重新设置对象
            if (!CollectionUtils.isEmpty(list))
            {
                for (DiscountEntity discountEntity : list)
                {
                    if (discountEntity.getType() == 0
                            && discountEntity.getFee() != 0)
                    {
                        fd.setDetailId(null);
                        BigDecimal fee = new BigDecimal(discountEntity.getFee()).divide(new BigDecimal(
                                100),
                                2,
                                BigDecimal.ROUND_HALF_UP);
                        fd.setAmount(debt.getAmount().multiply(fee));
                        fd.setType(9);
                        fd.setRemark("融资" + debt.getTitle() + "完成");
                        fundDetailDao.addFundDetail(fd);//插入手续费
                    }
                }
            }
            
            //生成投资方 资金明细
            List<InvestOrderEntity> investList = investDao.queryInvestorderList(debtId);
            for (int i = 0; i < investList.size(); i++)
            {
                //查询用户账户
                FundDetailEntity fundDetail = new FundDetailEntity();
                AccountEntity account1 = accountDao.getAcoountByCustomerId(investList.get(i)
                        .getCustomerId());
                //更新账户
                account1.setFozenAmount(account1.getFozenAmount()
                        .subtract(investList.get(i).getAmount()));//账户冻结资金减去账单资金
                ProfitModel prift2 = profitService.calculatingProfit(debtId,
                        investList.get(i).getAmount());//获取利息，总额
                account1.setDueAmount(account1.getDueAmount()
                        .add(prift2.getAmount()));//更新代收金额
                accountDao.updateAccount(account1);
                //生成明细
                fundDetail.setCustomerId(investList.get(i).getCustomerId());//用户ID
                fundDetail.setAccountId(account1.getAccountId());
                fundDetail.setOrderId(investList.get(i).getOrderId());
                fundDetail.setAmount(investList.get(i).getAmount());
                fundDetail.setBalance(account1.getBalance());
                fundDetail.setFrozenAmount(account1.getFozenAmount());
                fundDetail.setDueAmount(account1.getDueAmount());
                fundDetail.setCreateTime(new Date());
                fundDetail.setStatus(0);//收入
                fundDetail.setType(6);//解冻
                fundDetail.setRemark("投资" + debt.getTitle() + "完成,资金解冻");
                fundDetailDao.addFundDetail(fundDetail);
                List<DiscountEntity> disList = discountService.getDiscountByCustomerId(investList.get(i)
                        .getCustomerId());
                if (!CollectionUtils.isEmpty(disList))
                {
                    for (DiscountEntity discountEntity : disList)
                    {
                        if (discountEntity.getType() == 0
                                && discountEntity.getFee() != 0)
                        {
                            fundDetail.setDetailId(null);
                            BigDecimal fee = new BigDecimal(
                                    discountEntity.getFee()).divide(new BigDecimal(
                                    100),
                                    2,
                                    BigDecimal.ROUND_HALF_UP);
                            fundDetail.setAmount(debt.getAmount().multiply(fee));
                            fundDetail.setType(7);
                            fundDetail.setRemark("投资" + debt.getTitle()
                                    + "完成,手续费解冻");
                            fundDetailDao.addFundDetail(fundDetail);//插入手续费
                        }
                    }
                }
            }
            return "ok";
        }
        catch (Exception e)
        {
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new ServiceException("满标审核错误", e);
        }
    }
    
    public Map<String, String> testLoan(BigDecimal money, int num)
    {
        Map<String, Object> map = new HashMap<String, Object>();
        BigDecimal yearRatio = new BigDecimal("0.22");
        BigDecimal monthRatio = yearRatio.divide(new BigDecimal(num),
                128,
                BigDecimal.ROUND_HALF_UP);
        System.out.println(monthRatio);
        DecimalFormat df = new DecimalFormat(".00");
        List<MonthProfit> profits = new ArrayList<MonthProfit>();
        /*
         * 计算应还总额
         */
        //  BigDecimal money = new BigDecimal(10000);//总投资
        for (int i = num; i >= 1; i--)
        {
            BigDecimal operand = monthRatio.add(new BigDecimal(1)).pow(i);
            BigDecimal monthMoney = money.multiply(monthRatio)
                    .multiply(operand)
                    .divide(operand.subtract(new BigDecimal(1)),
                            6,
                            BigDecimal.ROUND_HALF_UP);
            /**
             * 计算当月利息
             */
            BigDecimal profit = money.multiply(monthRatio);
            //BigDecimal fee = profit.multiply(new BigDecimal("0.09"));
            //BigDecimal subtract = profit.subtract(fee);
            money = money.subtract(monthMoney.subtract(profit)).setScale(2,
                    BigDecimal.ROUND_HALF_UP);
            profits.add(new MonthProfit(monthMoney.setScale(2,
                    BigDecimal.ROUND_HALF_UP), profit.setScale(2,
                    BigDecimal.ROUND_HALF_UP), monthMoney.subtract(profit)
                    .setScale(2, BigDecimal.ROUND_HALF_UP)));
        }
        money = new BigDecimal(0);
        List<Map<String, BigDecimal>> list = new ArrayList<Map<String, BigDecimal>>();
        for (MonthProfit profit : profits)
        {
            /* System.out.println("当期应还总额:" + df.format(profit.getTotal())
                     + ",当期应还利息:" + (profit.getProfit()) + ",当期应还本金:"
                     + df.format(profit.getCapital()));*/
            //            System.out.println(profit.getProfit());
            money = money.add(profit.getProfit());
            Map<String, BigDecimal> map1 = new HashMap<String, BigDecimal>();
            
        }
        System.out.println("总利息" + df.format(money));
        System.out.println("总额" + df.format(new BigDecimal(10000).add(money)));
        return null;
        
    }
}
