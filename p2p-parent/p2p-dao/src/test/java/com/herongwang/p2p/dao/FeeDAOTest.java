package com.herongwang.p2p.dao;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.herongwang.p2p.dao.fee.IFeeDAO;
import com.herongwang.p2p.entity.fee.Fee;
import com.herongwang.p2p.entity.fee.FeeStatus;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/applicationContext.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
public class FeeDAOTest
{
    @Autowired
    IFeeDAO feeDAO;
    
    @Test
    public void testCreateFee()
    {
        Fee fee = new Fee();
        fee.setStatus(FeeStatus.UNAVAILABLE);
        feeDAO.createFee(fee);
        Assert.assertNotNull(fee.getFeeId());
    }
    
}
