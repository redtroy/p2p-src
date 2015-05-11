package com.herongwang.p2p.service.post;

import com.sxj.util.exception.ServiceException;

public interface IPostService
{
    /**
     * 充值
     * @param apply
     */
    public void Post(String deal) throws ServiceException;
    
}
