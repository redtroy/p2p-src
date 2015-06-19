package com.herongwang.p2p.service.impl.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.herongwang.p2p.dao.admin.IAdminDao;
import com.herongwang.p2p.entity.admin.AdminEntity;
import com.herongwang.p2p.service.admin.IAdminService;
import com.sxj.util.exception.ServiceException;
import com.sxj.util.logger.SxjLogger;

@Service
public class AdminServiceImpl implements IAdminService
{
    @Autowired
    private IAdminDao adminDao;
    
    @Override
    public AdminEntity getAdminEntityByName(String name)
            throws ServiceException
    {
        try
        {
            return adminDao.getAdminByName(name);
        }
        catch (Exception e)
        {
            SxjLogger.error(e.getMessage(), e, this.getClass());
            e.printStackTrace();
        }
        return null;
    }
    
    @Override
    public AdminEntity gitAdminEntity(String id)
    {
        try
        {
            return adminDao.getAdminEntity(id);
        }
        catch (Exception e)
        {
            SxjLogger.error(e.getMessage(), e, this.getClass());
            e.printStackTrace();
        }
        return null;
    }
    
    @Override
    public void updateAdmin(AdminEntity admin)
    {
        adminDao.updateAdminEntity(admin);
        
    }
    
}
