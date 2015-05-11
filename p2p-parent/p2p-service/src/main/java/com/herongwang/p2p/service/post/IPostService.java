package com.herongwang.p2p.service.post;

import com.herongwang.p2p.model.order.OrderMember;
import com.sxj.util.exception.ServiceException;

public interface IPostService
{
    /**
     * 充值
     * @param apply
     */
    public void Post(String deal) throws ServiceException;
    
    /**
     * 生成签名
     * @return
     * @throws ServiceException
     */
    public String getSignMsg(OrderMember orderMember) throws ServiceException;
}
