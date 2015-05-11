package com.herongwang.p2p.service.impl.invest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.herongwang.p2p.dao.investorder.IInvestorderDao;
import com.herongwang.p2p.entity.investorder.InvestorderEntity;
import com.herongwang.p2p.service.invest.IInvestService;
import com.sxj.util.exception.ServiceException;
import com.sxj.util.logger.SxjLogger;
import com.sxj.util.persistent.QueryCondition;

@Service
public class InvestServiceImpl implements IInvestService
{
    @Autowired
    private IInvestorderDao investDao;
    
    @Override
    public List<InvestorderEntity> queryInvest(InvestorderEntity invest)
    {
        try
        {
            QueryCondition<InvestorderEntity> condition = new QueryCondition<InvestorderEntity>();
            List<InvestorderEntity> investList = new ArrayList<InvestorderEntity>();
            if (invest == null)
            {
                return investList;
            }
            /* 
             *  condition.addCondition("memberCode", member.getMemberCode());// 姓名
            condition.setPage(query);*/
            investList = investDao.queryInvestorder(condition);
            // query.setPage(condition);
            return investList;
        }
        catch (Exception e)
        {
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new ServiceException("查询订单信息列表信息错误", e);
        }
    }
    
}
