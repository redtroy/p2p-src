package com.herongwang.p2p.service.impl.profit;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.herongwang.p2p.dao.debt.IDebtDao;
import com.herongwang.p2p.entity.debt.DebtEntity;
import com.herongwang.p2p.entity.profitlist.ProfitListEntity;
import com.herongwang.p2p.model.profit.MonthProfit;
import com.herongwang.p2p.model.profit.ProfitModel;
import com.herongwang.p2p.service.profit.IProfitService;
import com.sxj.util.exception.ServiceException;

@Service
@Transactional
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
        BigDecimal money1 = money;
        //获取到标的详情
        DebtEntity debt = debtDao.getDebtFor(debtId);
        BigDecimal yearRatio = new BigDecimal(debt.getAnnualizedRate());//年利率/100
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
                BigDecimal fee = profit.multiply(new BigDecimal("0.09"));//手续费
                BigDecimal subtract = profit.subtract(fee);
                money = money.subtract(monthMoney.subtract(profit));
                ProfitListEntity pm = new ProfitListEntity();
                pm.setMonthAmount(monthMoney.subtract(fee).setScale(2,
                        BigDecimal.ROUND_HALF_UP));//月总额
                pm.setMonthProfit(subtract.setScale(2, BigDecimal.ROUND_HALF_UP));//月利息
                pm.setMonthCapital(monthMoney.subtract(subtract)
                        .subtract(fee)
                        .setScale(2, BigDecimal.ROUND_HALF_UP));//本金
                pm.setSequence(month);
                pm.setFee(fee);
                monthProfits.add(pm);
            }
            BigDecimal totalMoney = new BigDecimal(0);
            BigDecimal totalFee = new BigDecimal(0);
            for (ProfitListEntity monthProfit : monthProfits)
            {
                totalMoney = totalMoney.add(monthProfit.getMonthProfit());
                totalFee = totalFee.add(monthProfit.getFee());
            }
            profits.setTotalInterest(new BigDecimal(df.format(totalMoney)));
            profits.setAmount(money1.add(totalMoney));
            profits.setInvestment(money1);
            profits.setTotalFee(totalFee);
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
                BigDecimal fee = profit.multiply(new BigDecimal("0.09"));//手续费
                BigDecimal subtract = profit.subtract(fee);
                ProfitListEntity mp = new ProfitListEntity();
                if (i != 12)
                {
                    mp.setMonthAmount(subtract);
                    mp.setMonthProfit(subtract);
                    mp.setMonthCapital(new BigDecimal(0));
                }
                else
                {
                    mp.setMonthAmount(subtract.add(money));
                    mp.setMonthProfit(subtract);
                    mp.setMonthCapital(money);
                }
                mp.setSequence(month);
                mp.setFee(fee);
                monthProfits.add(mp);
            }
            BigDecimal totalMoney = new BigDecimal(0);
            BigDecimal totalFee = new BigDecimal(0);
            for (ProfitListEntity monthProfit : monthProfits)
            {
                totalMoney = totalMoney.add(monthProfit.getMonthProfit());
                totalFee = totalFee.add(monthProfit.getFee());
            }
            profits.setTotalInterest(totalMoney);
            profits.setAmount(money1.add(totalMoney));
            profits.setInvestment(money1);
            profits.setMonthProfit(monthProfits);
            profits.setTotalFee(totalFee);
            
        }
        return profits;
    }
    
}
