package com.herongwang.p2p.website.controller.profit;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

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
    @RequestMapping("bdList")
    public String bdList(ModelMap map,String debtId,String amount) throws WebException
    {
        try
        {
            ProfitModel pm =profitService.calculatingProfit(debtId, new BigDecimal(amount));
            map.put("pm", pm);
        }
        catch (Exception e)
        {
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new WebException("计算收益错误", e);
        }
        return "site/profit/profit";
    }
}
