package com.herongwang.p2p.website.controller.post;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.herongwang.p2p.entity.account.AccountEntity;
import com.herongwang.p2p.entity.users.UsersEntity;
import com.herongwang.p2p.model.order.OrderModel;
import com.herongwang.p2p.service.account.IAccountService;
import com.herongwang.p2p.service.debt.IDebtService;
import com.herongwang.p2p.service.funddetail.IFundDetailService;
import com.herongwang.p2p.service.investorder.IInvestOrderService;
import com.herongwang.p2p.service.orders.IOrdersService;
import com.herongwang.p2p.service.post.IPostService;
import com.herongwang.p2p.website.controller.BaseController;
import com.sxj.util.exception.WebException;
import com.sxj.util.logger.SxjLogger;

@Controller
@RequestMapping("/loan")
public class LoanController extends BaseController
{
    
    @Autowired
    IOrdersService ordersService;
    
    @Autowired
    IPostService postService;
    
    @Autowired
    IAccountService accountService;
    
    @Autowired
    IInvestOrderService investOrderService;
    
    @Autowired
    IFundDetailService fundDetailService;
    
    @Autowired
    private IDebtService debtService;
    
    @RequestMapping("/recharge")
    public String recharge(ModelMap map) throws WebException
    {
        return "site/post/recharge";
    }
    
    @RequestMapping("/rechargeList")
    public String rechargeList(HttpServletRequest request, ModelMap map,
            OrderModel order) throws WebException
    {
        BigDecimal m = this.multiply(new BigDecimal(order.getOrderAmount()));
        UsersEntity user = this.getUsersEntity();
        if (user == null)
        {
            return LOGIN;
        }
        AccountEntity account = accountService.getAccountByCustomerId(user.getCustomerId());
        try
        {
            String basePath = this.getBasePath(request);
            ModelMap map1 = postService.Post(m, user);
            String SubmitURL = "http://218.4.234.150:88/main/loan/toloanrecharge.action";
            String ReturnURL = basePath + "loan/testloanrechargereturn.action";
            String NotifyURL = basePath + "loan/testloanrechargenotify.action";
            map.put("SubmitURL", SubmitURL);
            map.put("ReturnURL", ReturnURL);
            map.put("NotifyURL", NotifyURL);
            map.put("merchantId", map1.get("merchantId"));
            map.put("orderNo", map1.get("orderNo"));
            map.put("orderAmount", map1.get("orderAmount"));
            map.put("orderDatetime", map1.get("orderDatetime"));
            map.put("orderExpireDatetime", map1.get("orderExpireDatetime"));
            map.put("productName", map1.get("productName"));
            map.put("payType", map1.get("payType"));
            map.put("signMsg", map1.get("strSignMsg"));
            
            map.put("amount", order.getOrderAmount());
            map.put("orderName", "充值");
            map.put("balance", this.divide(account.getBalance()));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new WebException("生成充值订单失败", e);
        }
        
        return "site/post/recharge-list";
    }
    
    /**
     * 除100
     * @param m
     * @return
     */
    private BigDecimal divide(BigDecimal m)
    {
        if (m == null)
        {
            return new BigDecimal(0);
        }
        BigDecimal b2 = new BigDecimal(100);
        return m.divide(b2, 2, BigDecimal.ROUND_HALF_UP);
    }
    
    /**
     * 乘100
     * @param m
     * @return
     */
    private BigDecimal multiply(BigDecimal m)
    {
        if (m == null)
        {
            return new BigDecimal(0);
        }
        BigDecimal b2 = new BigDecimal(100);
        return m.multiply(b2);
    }
}
