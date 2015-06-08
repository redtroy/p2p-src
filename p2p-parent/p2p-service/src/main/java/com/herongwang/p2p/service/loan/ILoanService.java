package com.herongwang.p2p.service.loan;

import com.sxj.util.exception.ServiceException;

public interface ILoanService
{
    /**
     * 新增双乾报文
     * @param message json报文
     * @param className 报文类型
     * @param remark 说明
     * @return
     * @throws ServiceException
     */
    public void addOrder(String message, String className, String remark)
            throws ServiceException;
    
}
