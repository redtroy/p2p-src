package com.herongwang.p2p.service.impl.profit;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.herongwang.p2p.dao.debt.IDebtDao;
import com.herongwang.p2p.entity.debt.DebtEntity;
import com.herongwang.p2p.model.profit.MonthProfit;
import com.herongwang.p2p.model.profit.ProfitModel;
import com.herongwang.p2p.service.profit.IProfitService;
import com.sxj.util.exception.ServiceException;

public class ProfitServiceImpl implements IProfitService
{
    @Autowired
    IDebtDao debtDao;
    
    /**
     * 计算收益
     */
    @Override
    public ProfitModel calculatingProfit(String debtId, BigDecimal money)
            throws ServiceException
    {
        
        //获取到标的详情
        DebtEntity debt = debtDao.getDebtFor(debtId);
        BigDecimal yearRatio = new BigDecimal(debt.getAnnualizedRate()).divide(new BigDecimal(
                100));//年利率/100
        BigDecimal monthRatio = yearRatio.divide(new BigDecimal(
                debt.getMonths()));//月利率=年利率/12
        DecimalFormat df = new DecimalFormat(".00");
        List<MonthProfit> monthProfits = new ArrayList<MonthProfit>();
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
                BigDecimal fee = profit.multiply(new BigDecimal("0.09"));//手续费
                BigDecimal subtract = profit.subtract(fee);
                money = money.subtract(monthMoney.subtract(profit));
                MonthProfit pm = new MonthProfit();
                pm.setTotal(monthMoney.subtract(fee).setScale(2,
                        BigDecimal.ROUND_HALF_UP));//月总额
                pm.setProfit(subtract.setScale(2, BigDecimal.ROUND_HALF_UP));//月利息
                pm.setCapital(monthMoney.subtract(subtract)
                        .subtract(fee)
                        .setScale(2, BigDecimal.ROUND_HALF_UP));//本金
                pm.setMonth(month);
                pm.setFee(fee);
                monthProfits.add(pm);
            }
            BigDecimal totalMoney = new BigDecimal(0);
            for (MonthProfit monthProfit : monthProfits)
            {
                totalMoney = totalMoney.add(monthProfit.getProfit());
            }
            profits.setTotalInterest(totalMoney);
            profits.setAmount(money.add(totalMoney));
            profits.setInvestment(money);
            //            System.out.println("总利息" + df.format(money));
            //            System.out.println("总额" + df.format(new BigDecimal(10000).add(money)));
            
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
                BigDecimal fee = profit.multiply(new BigDecimal("0.09"));//手续费
                BigDecimal subtract = profit.subtract(fee);
                MonthProfit mp = new MonthProfit();
                if (i != 12)
                {
                    mp.setTotal(subtract);
                    mp.setProfit(subtract);
                    mp.setCapital(new BigDecimal(0));
                }
                else
                {
                    mp.setTotal(subtract.add(money));
                    mp.setProfit(subtract);
                    mp.setCapital(money);
                }
                mp.setMonth(month);
                mp.setFee(fee);
                monthProfits.add(mp);
            }
            BigDecimal totalMoney = new BigDecimal(0);
            for (MonthProfit monthProfit : monthProfits)
            {
                totalMoney = totalMoney.add(monthProfit.getProfit());
            }
            profits.setTotalInterest(totalMoney);
            profits.setAmount(money.add(totalMoney));
            profits.setInvestment(money);
            
        }
        return profits;
    }
    
}
