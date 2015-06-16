package com.herongwang.p2p.service.funddetail;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import com.herongwang.p2p.entity.funddetail.FundDetailEntity;
import com.herongwang.p2p.entity.investorder.InvestOrderEntity;
import com.herongwang.p2p.entity.orders.OrdersEntity;
import com.herongwang.p2p.entity.repayPlan.RepayPlanEntity;
import com.sxj.util.exception.ServiceException;

public interface IFundDetailService
{
    /**
     * 新增交易明细
     * @param apply
     */
    public void addFundDetail(FundDetailEntity deal) throws ServiceException;
    
    /**
     * 更新交易明细
     * 
     * @param apply
     */
    public void updateFundDetail(FundDetailEntity deal) throws ServiceException;
    
    /**
     * 获取交易明细信息
     * 
     * @param id
     * @return
     */
    public FundDetailEntity getFundDetail(String id) throws ServiceException;
    
    /**
     * 获取交易明细列表
     * 
     * @param query
     * @return
     * @throws SQLException 
     */
    public List<FundDetailEntity> queryFundDetail(FundDetailEntity query)
            throws ServiceException;
    
    /**
     * 删除交易明细
     * 
     * @param id
     */
    public void delFundDetail(String id) throws ServiceException;
    
    void investFundDetail(InvestOrderEntity io);
    
    void orderFundDetail(OrdersEntity order) throws ServiceException;
    
    void repayPlanFundDetail(List<RepayPlanEntity> planlist, BigDecimal blance)
            throws ServiceException;
    
    /**
     * 根据订单信息，添加充值提现交易明细
     * @param order
     * @throws ServiceException
     */
    public void addFunds(OrdersEntity order) throws ServiceException;
    
    /**
     * 第三方支付还款明细
     * @param planlist
     */
    public void repayPlanFundDetailAdvance(List<RepayPlanEntity> planlist);
}
