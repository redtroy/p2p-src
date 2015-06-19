package com.herongwang.p2p.dao.admin;

import com.herongwang.p2p.entity.admin.AdminEntity;
import com.sxj.mybatis.orm.annotations.Get;
import com.sxj.mybatis.orm.annotations.Update;

public interface IAdminDao
{
    /**
     * 更具用户名查询管理员信息
     * @param USER_NO
     * @return
     */
    public AdminEntity getAdminByName(String name);
    
    @Get
    public AdminEntity getAdminEntity(String id);
    
    @Update
    public void updateAdminEntity(AdminEntity adminEntity);
}
