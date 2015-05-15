package com.herongwang.p2p.website.controller.debt;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.herongwang.p2p.entity.debt.DebtEntity;
import com.herongwang.p2p.service.debt.IDebtService;
import com.herongwang.p2p.website.controller.BaseController;

@Controller
@RequestMapping("/debt")
public class DebtController extends BaseController
{
    @Autowired
    IDebtService debtService;
    
    /**
     * 跳转申请融资
     * @param session
     * @param map
     * @return
     */
    @RequestMapping("queryDebt")
    public String queryDebt(HttpSession session, ModelMap map, DebtEntity query)
    {
        
        //获取会员信息
        query.setCustomerId(getUsersEntity().getCustomerId());
        query.setPagable(true);
        List<DebtEntity> debtList = debtService.queryDebtList(query);
        map.put("debtList", debtList);
        map.put("query", query);
        
        return "site/debt/debtList";
    }
}
