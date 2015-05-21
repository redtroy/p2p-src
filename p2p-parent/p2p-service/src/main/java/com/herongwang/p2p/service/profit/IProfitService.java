package com.herongwang.p2p.service.profit;

import java.math.BigDecimal;
import java.util.List;

import com.herongwang.p2p.entity.profitlist.ProfitListEntity;
import com.herongwang.p2p.model.profit.ProfitModel;
import com.herongwang.p2p.model.repayplan.RepayPlanModel;
import com.sxj.util.exception.ServiceException;

/**
 *  收益接口
 * @author Administrator
 *
 */
public interface IProfitService
{
    
    ProfitModel calculatingProfit(String debtId, BigDecimal amount,
            String customerId) throws ServiceException;
    
    List<ProfitListEntity> queryProfit(String debtId);
    
    /**
     * 计算融资还款明细
     * @param debtId
     * @param amount
     * @param customerId
     * @return
     */
    public RepayPlanModel FinancingProfit(String debtId, BigDecimal amount,
            String customerId);
}
