package com.herongwang.p2p.dao.investorder;

import java.util.List;

import com.herongwang.p2p.entity.investorder.InvestorderEntity;
import com.sxj.util.persistent.QueryCondition;

public interface IInvestorderDao
{
    /**
     * 查询订单列表
     */
    public List<InvestorderEntity> queryInvestorder(
            QueryCondition<InvestorderEntity> query);
}
