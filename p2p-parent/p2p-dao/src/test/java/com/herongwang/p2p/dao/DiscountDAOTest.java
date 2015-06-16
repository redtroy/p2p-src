package com.herongwang.p2p.dao;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.herongwang.p2p.dao.fee.IDiscountDAO;
import com.herongwang.p2p.dao.fee.IFeeDAO;
import com.herongwang.p2p.entity.fee.DiscountEntity;
import com.herongwang.p2p.entity.fee.FeeEntity;

public class DiscountDAOTest extends TestBase
{
    @Autowired
    IDiscountDAO discountDAO;
    
    @Autowired
    IFeeDAO feeDAO;
    
    @Test
    public void testCreateDiscount()
    {
        FeeEntity fee = feeDAO.getFeeById("SR0g0yTvR22OLZQyEoeNYRMn4EBEAs0i");
        DiscountEntity discount = new DiscountEntity();
        discount.setFeeEntity(fee);
        discount.setDiscount(1.2f);
        System.out.println(discount.getFeeId());
        // discount.setFee(1.0f);
        discountDAO.createDiscount(discount);
    }
}
