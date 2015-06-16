package com.herongwang.p2p.dao.admin;

import com.herongwang.p2p.entity.admin.AdminEntity;

public interface IAdminDao
{
    /**
     * 更具用户名查询管理员信息
     * @param USER_NO
     * @return
     */
    public AdminEntity getAdminByName(String name);
}
