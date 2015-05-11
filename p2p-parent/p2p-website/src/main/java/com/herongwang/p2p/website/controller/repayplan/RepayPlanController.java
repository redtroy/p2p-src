package com.herongwang.p2p.website.controller.repayplan;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.herongwang.p2p.entity.funddetail.FundDetailEntity;
import com.herongwang.p2p.entity.repayPlan.RepayPlanEntity;
import com.herongwang.p2p.service.repayplan.IRepayPlanService;
import com.sxj.util.exception.WebException;
import com.sxj.util.logger.SxjLogger;

@Controller
@RequestMapping("/repayPlan")
public class RepayPlanController
{
    
    @Autowired
    IRepayPlanService repayPlanService;
    
    /**
     * 还款计划
     * @param session
     * @param map
     * @return
     * @throws WebException 
     */
    @RequestMapping("queryRepayPlan")
    public String queryRepayPlan(HttpSession session, ModelMap map,
            RepayPlanEntity query) throws WebException
    {
        //会员信息传到页面
        try
        {
            query.setPagable(true);
            List<RepayPlanEntity> repayPlanList = repayPlanService.queryRepayPlan(query);
            map.put("repayPlan", repayPlanList);
            map.put("query", query);
            return "site/repayPlan/repayPlan";
        }
        catch (Exception e)
        {
            SxjLogger.error("查询资金明细错误", e, this.getClass());
            throw new WebException("查询资金明细错误");
        }
    }
}
