package com.herongwang.p2p.dao;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.herongwang.p2p.dao.fee.IUserFeeDAO;
import com.herongwang.p2p.entity.fee.UserFeeEntity;

public class UserFeeDAOTest extends TestBase
{
    @Autowired
    IUserFeeDAO userFeeDAO;
    
    @Test
    public void test()
    {
        UserFeeEntity userFee = new UserFeeEntity();
        userFee.setCustomerId("cid");
        userFee.setDiscountId("did");
        userFee.setFeeId("fid");
        userFeeDAO.createUserFee(userFee);
    }
    
}
