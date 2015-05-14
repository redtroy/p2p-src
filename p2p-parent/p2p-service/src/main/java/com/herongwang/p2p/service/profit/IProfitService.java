package com.herongwang.p2p.service.profit;

import java.math.BigDecimal;

import com.herongwang.p2p.model.profit.ProfitModel;
import com.sxj.util.exception.ServiceException;

/**
 *  收益接口
 * @author Administrator
 *
 */
public interface IProfitService
{

    ProfitModel calculatingProfit(String debtId, BigDecimal amount)
            throws ServiceException;
    
}
