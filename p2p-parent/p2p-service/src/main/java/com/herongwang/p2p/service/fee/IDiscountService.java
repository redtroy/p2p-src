package com.herongwang.p2p.service.fee;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.herongwang.p2p.entity.fee.DiscountEntity;
import com.sxj.util.exception.ServiceException;

public interface IDiscountService
{
    public List<DiscountEntity> getDiscountByCustomerId(String customerId)
            throws ServiceException;
}
