package com.herongwang.p2p.website.controller.investorder;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.herongwang.p2p.entity.investorder.InvestOrderEntity;
import com.herongwang.p2p.entity.users.UsersEntity;
import com.herongwang.p2p.model.invest.InvestModel;
import com.herongwang.p2p.service.debt.IDebtService;
import com.herongwang.p2p.service.investorder.IInvestOrderService;
import com.herongwang.p2p.website.controller.BaseController;
import com.sxj.util.exception.WebException;
import com.sxj.util.logger.SxjLogger;

@Controller
@RequestMapping("invest")
public class InvestOrderController extends BaseController
{
    @Autowired
    private IInvestOrderService ivestService;
    
    @Autowired
    private IDebtService debtService;
    
    /**
     * 投资列表
     * @return
     */
    @RequestMapping("list")
    public String list(ModelMap map, InvestOrderEntity invest)
            throws WebException
    {
        try
        {
            if (getUsersEntity() == null)
            {
                return "site/invest/invest-list";
            }
            invest.setCustomerId(getUsersEntity().getCustomerId());
            List<InvestModel> investList = ivestService.queryInvestModel(invest);
            map.put("list", investList);
            map.put("query", invest);
        }
        catch (Exception e)
        {
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new WebException("查询投资列表信息错误", e);
        }
        return "site/invest/invest-list";
    }
    
    @RequestMapping("orders")
    public String orders(ModelMap map, String debtId, String amount,
            RedirectAttributes ra) throws WebException
    {
        try
        {
            UsersEntity user = getUsersEntity();
            if (user == null || user.getStatus() != 0)
            {
                return LOGIN;
            }
            InvestOrderEntity io = ivestService.addOrder(debtId,
                    amount,
                    user.getCustomerId());
            //加MD5
            ra.addAttribute("orderId", io.getOrderId());
            ra.addAttribute("amount", io.getAmount().doubleValue());
        }
        catch (Exception e)
        {
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new WebException("生成订单错误", e);
        }
        return "redirect:/post/investPost.htm";
    }
}
