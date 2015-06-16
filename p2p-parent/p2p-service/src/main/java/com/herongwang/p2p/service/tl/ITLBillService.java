package com.herongwang.p2p.service.tl;

import java.util.List;

import com.herongwang.p2p.entity.tl.TLBillEntity;
import com.sxj.util.exception.ServiceException;

public interface ITLBillService
{
    /**
     * 新增通联账单
     * @param Orders
     */
    public void addBill(TLBillEntity bill) throws ServiceException;
    
    /**
     * 更新通联账单
     * 
     * @param Orders
     */
    public void updateBill(TLBillEntity bill) throws ServiceException;
    
    /**
     * 获取通联账单信息
     * 
     * @param id
     * @return
     */
    public TLBillEntity getTLBillEntity(String id) throws ServiceException;
    
    /**
     * 根据通联账单号获取通联账单信息
     * 
     * @param id
     * @return
     */
    public TLBillEntity getTLBillEntityByNo(String ordersNo)
            throws ServiceException;
    
    /**
     * 获取通联账单列表
     * 
     * @param query
     * @return
     */
    public List<TLBillEntity> queryOrdersList(TLBillEntity query)
            throws ServiceException;
    
    /**
     * 删除通联账单
     * 
     * @param id
     */
    public void delBill(String id) throws ServiceException;
    
}
