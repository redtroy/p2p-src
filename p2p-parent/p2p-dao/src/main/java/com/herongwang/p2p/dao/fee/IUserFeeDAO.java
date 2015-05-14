package com.herongwang.p2p.dao.fee;

import com.herongwang.p2p.entity.fee.UserFeeEntity;

public interface IUserFeeDAO
{
    public void createUserFee(UserFeeEntity userFee);
    
    public void deleteUserFeeByCustomerId(String customerId);
    
    public void deleteuserFeeByDiscountId(String discountId);
    
}
