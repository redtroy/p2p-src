package com.herongwang.p2p.service.admin;

import com.herongwang.p2p.entity.admin.AdminEntity;

public interface IAdminService
{
    /**
     * 根据名称获取管理员信息
     * @param name
     * @return
     */
    public AdminEntity getAdminEntityByName(String name);
    
    /**
     * 根据id获取管理员信息
     * @param id
     * @return
     */
    public AdminEntity gitAdminEntity(String id);
    
    public void updateAdmin(AdminEntity admin);
}
