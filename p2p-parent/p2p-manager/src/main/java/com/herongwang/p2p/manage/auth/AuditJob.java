package com.herongwang.p2p.manage.auth;

import org.springframework.beans.factory.annotation.Autowired;

import com.herongwang.p2p.service.repayplan.IRepayPlanService;
import com.sxj.util.logger.SxjLogger;

public class AuditJob
{
    @Autowired
    private IRepayPlanService replan;
    
    protected void execute()
    {
        try
        {
            replan.refundAudit("test", "4rOKGlQhaCif8cOkhcxcm6IwCxqJRNnB");
        }
        catch (Exception e)
        {
            SxjLogger.error(e.getMessage(), e, this.getClass());
            e.printStackTrace();
            
        }
    }
}
