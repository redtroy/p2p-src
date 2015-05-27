package com.herongwang.p2p.service.investorder;

import java.util.List;

import com.herongwang.p2p.entity.investorder.InvestOrderEntity;
import com.herongwang.p2p.model.invest.InvestModel;
import com.sxj.util.exception.ServiceException;

public interface IInvestOrderService
{
    /**
     * 新增投标订单
     * aa
     * @param order 
     */
    public InvestOrderEntity addOrder(String debtId, String amount,
            String customerId) throws ServiceException;
    
    /**
     * 更新投标订单
     * 
     * @param order
     */
    public void updateOrder(InvestOrderEntity order) throws ServiceException;
    
    /**
     * 获取投标订单信息
     * 
     * @param id
     * @return
     */
    public InvestOrderEntity getInvestOrderEntity(String id)
            throws ServiceException;
    
    /**
     * 获取投标订单列表
     * 
     * @param query
     * @return
     */
    public List<InvestOrderEntity> queryorderList(InvestOrderEntity query)
            throws ServiceException;
    
    /**
     * 删除投标订单
     * 
     * @param id
     */
    public void delOrder(String id) throws ServiceException;
    
    void finishOrder(InvestOrderEntity io) throws ServiceException;
    
    /**
    * 查询订单，标的详情
    */
    public List<InvestModel> queryInvestModel(InvestOrderEntity query);
    
    /**
     * 查询所有收益人 总收益
     */
    public int queryDueProfitAmount();
    
    /**
     * 查询投资总额
     */
    public int queryAllAmount();
    
}
