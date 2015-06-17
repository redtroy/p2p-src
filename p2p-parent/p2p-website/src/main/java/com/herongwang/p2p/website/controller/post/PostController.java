package com.herongwang.p2p.website.controller.post;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.herongwang.p2p.entity.account.AccountEntity;
import com.herongwang.p2p.entity.debt.DebtEntity;
import com.herongwang.p2p.entity.investorder.InvestOrderEntity;
import com.herongwang.p2p.entity.users.UsersEntity;
import com.herongwang.p2p.loan.util.Common;
import com.herongwang.p2p.model.loan.LoanInfoBean;
import com.herongwang.p2p.service.account.IAccountService;
import com.herongwang.p2p.service.debt.IDebtService;
import com.herongwang.p2p.service.funddetail.IFundDetailService;
import com.herongwang.p2p.service.investorder.IInvestOrderService;
import com.herongwang.p2p.service.orders.IOrdersService;
import com.herongwang.p2p.service.post.IPostService;
import com.herongwang.p2p.service.users.IUserService;
import com.herongwang.p2p.website.controller.BaseController;
import com.sxj.util.exception.WebException;

@Controller
@RequestMapping("/post")
public class PostController extends BaseController
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
    
    @Autowired
    private IUserService userService;
    
    @RequestMapping("/investPost")
    public String investPost(ModelMap map, String orderId, RedirectAttributes ra)
            throws WebException
    {
        
        UsersEntity user = this.getUsersEntity();
        if (user == null)
        {
            return LOGIN;
        }
        InvestOrderEntity order = investOrderService.getInvestOrderEntity(orderId);
        DebtEntity debt = debtService.getDebtEntity(order.getDebtId());
        //获取双乾参数
        AccountEntity account = accountService.getAccountByCustomerId(user.getCustomerId());
        if (account.getBalance().compareTo(order.getAmount()) < 0)
        {
            map.put("title", "余额不足，请充值");
            if (StringUtils.isEmpty(user.getMoneymoremoreId()))
            {
                map.put("moneyType", 0);
            }
            else
            {
                map.put("moneyType", 1);
            }
            map.put("ext1", orderId);//投资订单id
            map.put("moneyAmount", order.getAmount());//订单金额
            
            return "site/loan/recharge";
        }
        List<LoanInfoBean> listmlib = new ArrayList<LoanInfoBean>();
        LoanInfoBean mlib = new LoanInfoBean();
        UsersEntity inUser = userService.getUserById(debt.getCustomerId());
        mlib.setLoanOutMoneymoremore(user.getMoneymoremoreId());//付款人
        mlib.setLoanInMoneymoremore(inUser.getMoneymoremoreId());//收款人
        mlib.setOrderNo(order.getOrderNo());//订单号
        mlib.setBatchNo(debt.getDebtNo());//标号
        mlib.setAmount(this.divide(order.getAmount()).toString());
        mlib.setFullAmount(this.divide(debt.getAmount()).toString());
        mlib.setTransferName(Common.UrlEncoder("投标", "utf-8"));
        mlib.setRemark(Common.UrlEncoder("对" + debt.getTitle() + "标投资。",
                "utf-8"));
        mlib.setSecondaryJsonList("");
        listmlib.add(mlib);
        String LoanJsonList = Common.JSONEncode(listmlib);
        ra.addAttribute("LoanJsonList", LoanJsonList);
        return "redirect:/loan/transfer.htm";
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
    
}
