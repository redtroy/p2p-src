package com.herongwang.p2p.manage.auth;

import org.springframework.beans.factory.annotation.Autowired;

import com.herongwang.p2p.entity.admin.AdminEntity;
import com.herongwang.p2p.service.admin.IAdminService;
import com.herongwang.p2p.service.repayplan.IRepayPlanService;
import com.sxj.util.logger.SxjLogger;

public class AuditJob
{
    @Autowired
    private IRepayPlanService replan;
    
    @Autowired
    private IAdminService adminService;
    
    protected void execute()
    {
        try
        {
            AdminEntity user = adminService.gitAdminEntity("1");
            if (null != user && user.getStatus().equals("1"))
            {
                replan.refundAudit("test", null);
            }
        }
        catch (Exception e)
        {
            SxjLogger.error(e.getMessage(), e, this.getClass());
            e.printStackTrace();
            
        }
    }
}
