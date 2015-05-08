package com.herongwang.p2p.website.controller.capital;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/capital")
public class CapitalController
{
    /**
     * 资金明细
     * @param session
     * @param map
     * @return
     */
    @RequestMapping("queryCapital")
    public String toCapital(HttpSession session, ModelMap map)
    {
        //会员信息传到页面
        return "site/capital/capital";
    }
}
