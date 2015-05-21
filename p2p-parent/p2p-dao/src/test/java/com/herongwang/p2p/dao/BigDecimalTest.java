package com.herongwang.p2p.dao;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Test;

import com.herongwang.p2p.model.MonthProfit;

public class BigDecimalTest
{
    
    @After
    public void tearDown() throws Exception
    {
    }
    
    public void testInvest()
    {
        BigDecimal yearRatio = new BigDecimal("0.22");
        BigDecimal monthRatio = yearRatio.divide(new BigDecimal("12"),
                128,
                BigDecimal.ROUND_HALF_UP);
        System.out.println(monthRatio);
        DecimalFormat df = new DecimalFormat(".00");
        List<MonthProfit> profits = new ArrayList<MonthProfit>();
        /*
         * 计算应还总额
         */
        int num = 12;
        BigDecimal money = new BigDecimal(10000);//总投资
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
            BigDecimal fee = profit.multiply(new BigDecimal("0.09"));
            BigDecimal subtract = profit.subtract(fee);
            money = money.subtract(monthMoney.subtract(profit)).setScale(2,
                    BigDecimal.ROUND_HALF_UP);
            profits.add(new MonthProfit(monthMoney.subtract(fee).setScale(2,
                    BigDecimal.ROUND_HALF_UP), subtract.setScale(2,
                    BigDecimal.ROUND_HALF_UP), monthMoney.subtract(subtract)
                    .subtract(fee)
                    .setScale(2, BigDecimal.ROUND_HALF_UP)));
        }
        money = new BigDecimal(0);
        for (MonthProfit profit : profits)
        {
            System.out.println("当期应还总额:" + df.format(profit.getTotal())
                    + ",当期应还利息:" + (profit.getProfit()) + ",当期应还本金:"
                    + df.format(profit.getCapital()));
            //System.out.println(profit.getProfit());
            money = money.add(profit.getProfit());
        }
        System.out.println("总利息" + df.format(money));
        System.out.println("总额" + df.format(new BigDecimal(10000).add(money)));
        
    }
    
    @Test
    public void testLoan()
    {
        BigDecimal yearRatio = new BigDecimal("0.12");
        BigDecimal monthRatio = yearRatio.divide(new BigDecimal("12"),
                128,
                BigDecimal.ROUND_HALF_UP);
        System.out.println(monthRatio);
        DecimalFormat df = new DecimalFormat(".00");
        List<MonthProfit> profits = new ArrayList<MonthProfit>();
        /*
         * 计算应还总额
         */
        int num = 12;
        BigDecimal money = new BigDecimal(200000);//总投资
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
        for (MonthProfit profit : profits)
        {
            System.out.println("当期应还总额:" + df.format(profit.getTotal())
                    + ",当期应还利息:" + (profit.getProfit()) + ",当期应还本金:"
                    + df.format(profit.getCapital()));
            //            System.out.println(profit.getProfit());
            money = money.add(profit.getProfit());
        }
        System.out.println("总利息" + df.format(money));
        System.out.println("总额" + df.format(new BigDecimal(10000).add(money)));
    }
}
