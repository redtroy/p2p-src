package com.herongwang.p2p.service.impl.fee;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.herongwang.p2p.dao.fee.IDiscountDAO;
import com.herongwang.p2p.entity.fee.DiscountEntity;
import com.herongwang.p2p.service.fee.IDiscountService;
import com.sxj.util.exception.ServiceException;
import com.sxj.util.logger.SxjLogger;

@Service
@Transactional
public class DiscountServiceImpl implements IDiscountService
{
    @Autowired
    IDiscountDAO discountDAO;
    
    @Override
    public List<DiscountEntity> getDiscountByCustomerId(String customerId)
            throws ServiceException
    {
        List<DiscountEntity> list;
        try
        {
            list = discountDAO.getDiscountByCustomerId(customerId);
        }
        catch (Exception e)
        {
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new ServiceException("获取折扣错误", e);
        }
        return list;
    }
    
}
