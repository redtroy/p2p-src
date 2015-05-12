package com.herongwang.p2p.service.impl.tl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.herongwang.p2p.dao.tl.ITLBillDao;
import com.herongwang.p2p.entity.tl.TLBillEntity;
import com.herongwang.p2p.service.tl.ITLBillService;
import com.sxj.util.exception.ServiceException;

@Service
public class TLBillServiceImpl implements ITLBillService
{
    @Autowired
    private ITLBillDao tlBillDao;
    
    @Override
    public void addBill(TLBillEntity bill) throws ServiceException
    {
        tlBillDao.addBill(bill);
        
    }
    
    @Override
    public void updateBill(TLBillEntity bill) throws ServiceException
    {
        tlBillDao.updateBill(bill);
        
    }
    
    @Override
    public TLBillEntity getTLBillEntity(String id) throws ServiceException
    {
        return tlBillDao.getOrders(id);
    }
    
    @Override
    public TLBillEntity getTLBillEntityByNo(String ordersNo)
            throws ServiceException
    {
        return tlBillDao.getTLBIllByNo(ordersNo);
    }
    
    @Override
    public List<TLBillEntity> queryOrdersList(TLBillEntity query)
            throws ServiceException
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public void delBill(String id) throws ServiceException
    {
        tlBillDao.delBill(id);
        
    }
    
}
