package com.herongwang.p2p.dao;

import java.math.BigDecimal;

import org.junit.After;
import org.junit.Test;

public class BigDecimalTest
{
    
    @After
    public void tearDown() throws Exception
    {
    }
    
    @Test
    public void test()
    {
        BigDecimal yearRatio = new BigDecimal("0.22");
        BigDecimal monthRatio = yearRatio.divide(new BigDecimal("12"),
                6,
                BigDecimal.ROUND_HALF_UP);
        /*
         * 计算应还总额
         */
        BigDecimal money = new BigDecimal(10000);
        
        BigDecimal operand = monthRatio.add(new BigDecimal(1)).pow(12);
        BigDecimal value = money.multiply(monthRatio)
                .multiply(operand)
                .divide(operand.subtract(new BigDecimal(1)),
                        2,
                        BigDecimal.ROUND_HALF_UP);
        System.out.println(value.doubleValue());
        /**
         * 计算当月利息
         */
        BigDecimal profit = money.multiply(monthRatio);
        BigDecimal fee = profit.multiply(new BigDecimal("0.09"));
        BigDecimal subtract = profit.subtract(fee);
        System.out.println(subtract.doubleValue());
        
        System.out.println("当期应还总额:" + value.subtract(fee).doubleValue()
                + ",当期应还利息:" + subtract.doubleValue() + ",当期应还本金:"
                + value.subtract(profit));
        money = money.subtract(value.subtract(profit));
        
    }
}
