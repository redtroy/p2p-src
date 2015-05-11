package com.herongwang.p2p.website.controller.investorder;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.herongwang.p2p.service.investorder.IInvestOrderService;
import com.sxj.util.exception.WebException;
import com.sxj.util.logger.SxjLogger;

@Controller
@RequestMapping("invest")
public class InvestOrderController
{
    @Autowired
    private IInvestOrderService ivestService;
    
    /**
     * 订单列表
     * @return
     */
    @RequestMapping("list")
    public String list(ModelMap map, InvestOrderEntity invest)
            throws WebException
    {
        try
        {
            List<InvestOrderEntity> investList = ivestService.queryorderList(invest);
            map.put("list", investList);
        }
        catch (Exception e)
        {
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new WebException("查询订单列表信息错误", e);
        }
        return "site/invest/invest-list";
    }
}
