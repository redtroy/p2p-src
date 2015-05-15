package com.herongwang.p2p.manage.controller.repayplan;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.herongwang.p2p.dao.financing.IFinancingOrdersDao;
import com.herongwang.p2p.entity.apply.DebtApplicationEntity;
import com.herongwang.p2p.entity.debt.DebtEntity;
import com.herongwang.p2p.entity.financing.FinancingOrdersEntity;
import com.herongwang.p2p.entity.repayPlan.RepayPlanEntity;
import com.herongwang.p2p.service.financing.IFinancingOrdersService;
import com.herongwang.p2p.service.repayplan.IRepayPlanService;
import com.sxj.util.exception.WebException;
import com.sxj.util.logger.SxjLogger;

@Controller
@RequestMapping("/repayPlan")
public class RepayPlanController
{
    
    @Autowired
    IRepayPlanService repayPlanService;
    
    @Autowired
    IFinancingOrdersService financingOrdersService;
    
    /**
     * 还款计划
     * @param entity
     * @return
     */
    @RequestMapping("/queryRepayPlan")
    public String queryRepayPlan(String  debtId, ModelMap map)
            throws WebException
    {
        try
        {
           FinancingOrdersEntity order = financingOrdersService.getOrderByDebtId(debtId);
            List<RepayPlanEntity> list = repayPlanService.queryRepayPlan(order);
            map.put("repayPlan", list);
            //map.put("orderId", entity.getOrderId());
            map.put("orderId", order.getOrderId());
            map.put("debtId", debtId);
            return "manage/repayPlan/repayPlan";
        }
        catch (Exception e)
        {
            e.printStackTrace();
            SxjLogger.error("查询还款计划信息错误", e, this.getClass());
            throw new WebException("查询还款计划信息错误");
        }
    }
    
    /**
     * 验证余额
     * @param session
     * @param ids
     * @return
     * @throws WebException
     */
    @RequestMapping("getBalance")
    public @ResponseBody Map<String, Object> getBalance(
            @RequestParam("ids[]")String[] ids, String orderId,String debtId) throws WebException
    {
        try
        {
            Map<String, Object> map = new HashMap<String, Object>();
            String flag = repayPlanService.getBalance(ids, orderId,debtId);
            map.put("flag", flag);
            return map;
        }
        catch (Exception e)
        {
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new WebException("验证余额错误", e);
        }
        
    }
    /**
     * 还款
     * @param session
     * @param ids
     * @return
     * @throws WebException
     */
    @RequestMapping("saveRepayPlan")
    public @ResponseBody Map<String, Object> saveRepayPlan(
            @RequestParam("ids[]")String[] ids, String orderId,String debtId) throws WebException
    {
        try
        {
            Map<String, Object> map = new HashMap<String, Object>();
            String flag = repayPlanService.saveRepayPlan(ids, orderId,debtId);
            map.put("flag", flag);
            return map;
        }
        catch (Exception e)
        {
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new WebException("还款错误", e);
        }
        
    }
}
