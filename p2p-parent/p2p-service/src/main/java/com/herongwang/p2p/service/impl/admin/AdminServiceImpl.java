package com.herongwang.p2p.service.impl.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.herongwang.p2p.dao.admin.IAdminDao;
import com.herongwang.p2p.entity.admin.AdminEntity;
import com.herongwang.p2p.service.admin.IAdminService;
import com.sxj.util.exception.ServiceException;

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
            e.printStackTrace();
        }
        return null;
    }
    
}
