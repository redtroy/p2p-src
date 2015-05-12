package com.herongwang.p2p.service.post;

import com.herongwang.p2p.entity.tl.TLBillEntity;
import com.herongwang.p2p.model.order.OrderModel;
import com.sxj.util.exception.ServiceException;

public interface IPostService
{
    /**
     * 充值
     * @param apply
     */
    public void Post(String deal) throws Exception;
    
    /**
     * 生成支付签名
     * @return
     * @throws ServiceException
     */
    public String getSignMsg(OrderModel orderMember) throws Exception;
    
    /**
     * 生成查询签名
     * @return
     * @throws ServiceException
     */
    public String getQuerySignMsg(OrderModel orderMember) throws Exception;
    
    /**
     * 查询订单
     * @param orderMember
     * @return
     * @throws Exception
     */
    public TLBillEntity getBIll(OrderModel orderMember) throws Exception;
}
