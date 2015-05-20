package com.herongwang.p2p.service.profit;

import java.math.BigDecimal;
import java.util.List;

import com.herongwang.p2p.entity.profitlist.ProfitListEntity;
import com.herongwang.p2p.model.profit.ProfitModel;
import com.sxj.util.exception.ServiceException;

/**
 *  收益接口
 * @author Administrator
 *
 */
public interface IProfitService
{
    
    ProfitModel calculatingProfit(String debtId, BigDecimal amount,String customerId)
            throws ServiceException;
    
    List<ProfitListEntity> queryProfit(String debtId);
    
}
