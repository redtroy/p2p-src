package com.herongwang.p2p.website.controller.investOrder;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sxj.util.exception.WebException;

@Controller
@RequestMapping("invest")
public class InvestOrderController
{
    
    /**
     * 订单列表
     * @return
     */
    @RequestMapping("list")
    public String list(ModelMap map) throws WebException
    {
        try
        {
            
        }
        catch (Exception e)
        {
            // TODO: handle exception
        }
        return "site/invest/invest-list";
    }
}
