package com.herongwang.p2p.service.impl.profit;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.herongwang.p2p.dao.debt.IDebtDao;
import com.herongwang.p2p.dao.profitlist.IProfitListDao;
import com.herongwang.p2p.entity.debt.DebtEntity;
import com.herongwang.p2p.entity.fee.DiscountEntity;
import com.herongwang.p2p.entity.profitlist.ProfitListEntity;
import com.herongwang.p2p.entity.repayPlan.RepayPlanEntity;
import com.herongwang.p2p.model.profit.ProfitModel;
import com.herongwang.p2p.model.repayplan.RepayPlanModel;
import com.herongwang.p2p.service.fee.IDiscountService;
import com.herongwang.p2p.service.profit.IProfitService;
import com.sxj.util.exception.ServiceException;
import com.sxj.util.logger.SxjLogger;
import com.sxj.util.persistent.QueryCondition;

@Service
@Transactional
public class ProfitServiceImpl implements IProfitService
{
    @Autowired
    IDebtDao debtDao;
    
    @Autowired
    IProfitListDao profitListDao;
    
    @Autowired
    IDiscountService discountService;
    
    /**
     * 计算收益
     */
    @Override
    @Transactional
    public ProfitModel calculatingProfit(String debtId, BigDecimal money,
            String customerId) throws ServiceException
    {
        float InvestFee = 0;
        List<DiscountEntity> list = discountService.getDiscountByCustomerId(customerId);
        if (!CollectionUtils.isEmpty(list))
        {
            for (DiscountEntity discountEntity : list)
            {
                if (discountEntity.getType() == 1
                        && discountEntity.getFee() != 0)
                {
                    InvestFee = discountEntity.getFee();
                }
            }
        }
        BigDecimal money1 = money;
        //获取到标的详情
        DebtEntity debt = debtDao.getDebtFor(debtId);
        BigDecimal yearRatio = new BigDecimal(debt.getAnnualizedRate()).divide(new BigDecimal(
                100));//年利率/100
        BigDecimal monthRatio = yearRatio.divide(new BigDecimal(
                debt.getMonths()),
                6,
                BigDecimal.ROUND_HALF_UP);//月利率=年利率/12
        DecimalFormat df = new DecimalFormat(".00");
        List<ProfitListEntity> monthProfits = new ArrayList<ProfitListEntity>();
        ProfitModel profits = new ProfitModel();
        int month = 0;
        if (debt.getRepayType() == 1)//按月分期还款
        {
            
            /*
             * 计算应还总额
             */
            int num = debt.getMonths();
            for (int i = num; i >= 1; i--)
            {
                month++;
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
                BigDecimal fee = profit.multiply(new BigDecimal(InvestFee).divide(new BigDecimal(
                        100)));//手续费
                BigDecimal subtract = profit.subtract(fee);
                money = money.subtract(monthMoney.subtract(profit));
                ProfitListEntity pm = new ProfitListEntity();
                pm.setMonthAmount(monthMoney.subtract(fee).setScale(2,
                        BigDecimal.ROUND_HALF_UP));//月总额
                pm.setMonthProfit(profit.setScale(2, BigDecimal.ROUND_HALF_UP));//月利息
                pm.setMonthCapital(monthMoney.subtract(subtract)
                        .subtract(fee)
                        .setScale(2, BigDecimal.ROUND_HALF_UP));//本金
                pm.setSequence(month);
                pm.setFee(fee.setScale(2, BigDecimal.ROUND_HALF_UP));
                monthProfits.add(pm);
            }
            BigDecimal totalMoney = new BigDecimal(0);
            BigDecimal totalFee = new BigDecimal(0);
            for (ProfitListEntity monthProfit : monthProfits)
            {
                totalMoney = totalMoney.add(monthProfit.getMonthProfit()
                        .setScale(2, BigDecimal.ROUND_HALF_UP));
                totalFee = totalFee.add(monthProfit.getFee().setScale(2,
                        BigDecimal.ROUND_HALF_UP));
            }
            profits.setTotalInterest(new BigDecimal(df.format(totalMoney)));
            profits.setAmount(money1.add(totalMoney));
            profits.setInvestment(money1);
            profits.setTotalFee(totalFee);
            profits.setType(debt.getRepayType());
            //            System.out.println("总利息" + df.format(money));
            //            System.out.println("总额" + df.format(new BigDecimal(10000).add(money)));
            profits.setMonthProfit(monthProfits);
            
        }
        else
        {
            /*
             *每月还息到期还本
             */
            
            int num = debt.getMonths();
            for (int i = num; i >= 1; i--)
            {
                month++;
                BigDecimal profit = money.multiply(monthRatio);
                BigDecimal fee = profit.multiply(new BigDecimal(InvestFee).divide(new BigDecimal(
                        100)));//手续费
                BigDecimal subtract = profit.subtract(fee);
                ProfitListEntity mp = new ProfitListEntity();
                if (i != 1)
                {
                    mp.setMonthAmount(subtract.setScale(2,
                            BigDecimal.ROUND_HALF_UP));
                    mp.setMonthProfit(profit.setScale(2,
                            BigDecimal.ROUND_HALF_UP));
                    mp.setMonthCapital(new BigDecimal(0));
                }
                else
                {
                    mp.setMonthAmount(subtract.add(money).setScale(2,
                            BigDecimal.ROUND_HALF_UP));
                    mp.setMonthProfit(profit.setScale(2,
                            BigDecimal.ROUND_HALF_UP));
                    mp.setMonthCapital(money.setScale(2,
                            BigDecimal.ROUND_HALF_UP));
                }
                mp.setSequence(month);
                mp.setFee(fee.setScale(2, BigDecimal.ROUND_HALF_UP));
                monthProfits.add(mp);
            }
            BigDecimal totalMoney = new BigDecimal(0);
            BigDecimal totalFee = new BigDecimal(0);
            for (ProfitListEntity monthProfit : monthProfits)
            {
                totalMoney = totalMoney.add(monthProfit.getMonthProfit());
                totalFee = totalFee.add(monthProfit.getFee());
            }
            profits.setTotalInterest(totalMoney.setScale(2,
                    BigDecimal.ROUND_HALF_UP));
            profits.setAmount(money1.add(totalMoney).setScale(2,
                    BigDecimal.ROUND_HALF_UP));
            profits.setInvestment(money1.setScale(2, BigDecimal.ROUND_HALF_UP));
            profits.setMonthProfit(monthProfits);
            profits.setTotalFee(totalFee.setScale(2, BigDecimal.ROUND_HALF_UP));
            profits.setType(debt.getRepayType());
        }
        return profits;
    }
    
    /**
     * 收益
     */
    @Override
    public List<ProfitListEntity> queryProfit(String orderId)
    {
        
        try
        {
            List<ProfitListEntity> list;
            QueryCondition<ProfitListEntity> condition = new QueryCondition<ProfitListEntity>();
            condition.addCondition("orderId", orderId);//会员id
            list = profitListDao.query(condition);
            return list;
        }
        catch (ServiceException e)
        {
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new ServiceException(e.getMessage());
        }
        catch (Exception e)
        {
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new ServiceException("查询收益明细错误", e);
        }
    }
    
    @Override
    public RepayPlanModel FinancingProfit(String debtId, BigDecimal money,
            String customerId)
    {
        float InvestFee = 0;
        List<DiscountEntity> list = discountService.getDiscountByCustomerId(customerId);
        if (!CollectionUtils.isEmpty(list))
        {
            for (DiscountEntity discountEntity : list)
            {
                if (discountEntity.getType() == 1
                        && discountEntity.getFee() != 0)
                {
                    InvestFee = discountEntity.getFee();
                }
            }
        }
        BigDecimal money1 = money;
        //获取到标的详情
        DebtEntity debt = debtDao.getDebtFor(debtId);
        BigDecimal yearRatio = new BigDecimal(debt.getAnnualizedRate()).divide(new BigDecimal(
                100));//年利率/100
        BigDecimal monthRatio = yearRatio.divide(new BigDecimal(
                debt.getMonths()),
                6,
                BigDecimal.ROUND_HALF_UP);//月利率=年利率/12
        DecimalFormat df = new DecimalFormat(".00");
        List<RepayPlanEntity> monthProfits = new ArrayList<RepayPlanEntity>();
        RepayPlanModel profits = new RepayPlanModel();
        int month = 0;
        if (debt.getRepayType() == 1)//按月分期还款
        {
            
            /*
             * 计算应还总额
             */
            int num = debt.getMonths();
            for (int i = num; i >= 1; i--)
            {
                month++;
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
                //                BigDecimal fee = profit.multiply(new BigDecimal(InvestFee));//手续费
                //                BigDecimal subtract = profit.subtract(fee);
                money = money.subtract(monthMoney.subtract(profit)).setScale(2,
                        BigDecimal.ROUND_HALF_UP);
                RepayPlanEntity pm = new RepayPlanEntity();
                pm.setMonthAmount(monthMoney.setScale(2,
                        BigDecimal.ROUND_HALF_UP));//月总额
                pm.setMonthProfit(profit.setScale(2, BigDecimal.ROUND_HALF_UP));//月利息
                pm.setMonthCapital(monthMoney.subtract(profit).setScale(2,
                        BigDecimal.ROUND_HALF_UP));//本金
                pm.setSequence(month);
                monthProfits.add(pm);
                
            }
            BigDecimal totalMoney = new BigDecimal(0);
            BigDecimal totalFee = new BigDecimal(0);
            for (RepayPlanEntity monthProfit : monthProfits)
            {
                totalMoney = totalMoney.add(monthProfit.getMonthProfit()
                        .setScale(2, BigDecimal.ROUND_HALF_UP));
            }
            profits.setTotalInterest(new BigDecimal(df.format(totalMoney)));
            profits.setAmount(money1.add(totalMoney));
            profits.setInvestment(money1);
            // profits.setTotalFee(totalFee);//管理费
            profits.setType(debt.getRepayType());
            //            System.out.println("总利息" + df.format(money));
            //            System.out.println("总额" + df.format(new BigDecimal(10000).add(money)));
            profits.setMonthProfit(monthProfits);
            
        }
        else
        {
            /*
             *每月还息到期还本
             */
            
            int num = debt.getMonths();
            for (int i = num; i >= 1; i--)
            {
                month++;
                BigDecimal profit = money.multiply(monthRatio);
                //  BigDecimal fee = profit.multiply(new BigDecimal(InvestFee));//手续费
                //  BigDecimal subtract = profit.subtract(fee);
                RepayPlanEntity mp = new RepayPlanEntity();
                if (i != 1)
                {
                    mp.setMonthAmount(profit.setScale(2,
                            BigDecimal.ROUND_HALF_UP));
                    mp.setMonthProfit(profit.setScale(2,
                            BigDecimal.ROUND_HALF_UP));
                    mp.setMonthCapital(new BigDecimal(0));
                }
                else
                {
                    mp.setMonthAmount(profit.add(money).setScale(2,
                            BigDecimal.ROUND_HALF_UP));
                    mp.setMonthProfit(profit.setScale(2,
                            BigDecimal.ROUND_HALF_UP));
                    mp.setMonthCapital(money.setScale(2,
                            BigDecimal.ROUND_HALF_UP));
                }
                mp.setSequence(month);
                //   mp.setFee(fee.setScale(2, BigDecimal.ROUND_HALF_UP));
                monthProfits.add(mp);
            }
            BigDecimal totalMoney = new BigDecimal(0);
            BigDecimal totalFee = new BigDecimal(0);
            for (RepayPlanEntity monthProfit : monthProfits)
            {
                totalMoney = totalMoney.add(monthProfit.getMonthProfit());
                // totalFee = totalFee.add(monthProfit.getFee());
            }
            profits.setTotalInterest(totalMoney.setScale(2,
                    BigDecimal.ROUND_HALF_UP));
            profits.setAmount(money1.add(totalMoney).setScale(2,
                    BigDecimal.ROUND_HALF_UP));
            profits.setInvestment(money1.setScale(2, BigDecimal.ROUND_HALF_UP));
            profits.setMonthProfit(monthProfits);
            profits.setTotalFee(totalFee.setScale(2, BigDecimal.ROUND_HALF_UP));
            profits.setType(debt.getRepayType());
        }
        return profits;
    }
    
    @Override
    public ProfitListEntity getProfitListEntity(String profitId)
    {
        return profitListDao.getProfitListEntity(profitId);
    }
}
