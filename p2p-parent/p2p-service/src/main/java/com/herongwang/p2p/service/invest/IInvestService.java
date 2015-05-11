package com.herongwang.p2p.service.invest;

import java.util.List;

import com.herongwang.p2p.entity.investorder.InvestorderEntity;

public interface IInvestService
{
    /**
     * 查询投资订单列表
     */
    public List<InvestorderEntity> queryInvest(InvestorderEntity invest);
}
