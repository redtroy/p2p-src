package com.herongwang.p2p.dao;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.herongwang.p2p.dao.fee.IFeeDAO;
import com.herongwang.p2p.entity.fee.FeeEntity;
import com.herongwang.p2p.entity.fee.FeeStatus;

public class FeeDAOTest extends TestBase
{
    @Autowired
    IFeeDAO feeDAO;
    
    @Test
    public void testCreateFee()
    {
        FeeEntity fee = new FeeEntity();
        fee.setName("测试费率");
        fee.setStatus(FeeStatus.UNAVAILABLE);
        feeDAO.createFee(fee);
        Assert.assertNotNull(fee.getFeeId());
    }
    
    public void testGetFeeById()
    {
        FeeEntity feeById = feeDAO.getFeeById("SR0g0yTvR22OLZQyEoeNYRMn4EBEAs0i");
        Assert.assertNotNull(feeById);
        
    }
}
