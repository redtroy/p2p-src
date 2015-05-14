package com.herongwang.p2p.website.controller.profit;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.herongwang.p2p.entity.profitlist.ProfitListEntity;
import com.herongwang.p2p.model.profit.ProfitModel;
import com.herongwang.p2p.service.profit.IProfitService;
import com.sxj.util.exception.WebException;
import com.sxj.util.logger.SxjLogger;

@Controller
@RequestMapping("/profit")
public class ProfitController
{
    @Autowired
    private IProfitService profitService;
    
    /**
     * 预算收益
     * @param map
     * @param debtId
     * @param amount
     * @return
     * @throws WebException
     */
    @RequestMapping("profit")
    public String profit(ModelMap map, String debtId, String amount)
            throws WebException
    {
        try
        {
            ProfitModel pm = profitService.calculatingProfit("a44apymRNEWYenNmTc5MBNdhLGxdwQo3",
                    new BigDecimal("1000"));
            map.put("pm", pm);
        }
        catch (Exception e)
        {
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new WebException("计算收益错误", e);
        }
        return "site/profit/profit";
    }
    /**
     * 收益详情
     * @param map
     * @param debtId
     * @param amount
     * @return
     * @throws WebException
     */
    @RequestMapping("queryProfit")
    public String queryProfit(ModelMap map, String orderId, String amount)
            throws WebException
    {
        try
        {
           List<ProfitListEntity> list= profitService.queryProfit(orderId);
            map.put("list", list);
        }
        catch (Exception e)
        {
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new WebException("计算收益错误", e);
        }
        return "site/profit/profitList";
    }
    
}