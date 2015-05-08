package com.herongwang.p2p.service.admin;

import com.herongwang.p2p.entity.admin.AdminEntity;

public interface IAdminService
{
    public AdminEntity getAdminEntityByName(String name);
}
