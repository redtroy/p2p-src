package com.herongwang.p2p.website.controller.post;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import util.RsaHelper;

import com.herongwang.p2p.entity.account.AccountEntity;
import com.herongwang.p2p.entity.users.UsersEntity;
import com.herongwang.p2p.model.order.OrderModel;
import com.herongwang.p2p.model.post.LoanModel;
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
    public static final String privateKeyPKCS8 = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAJxTqfs4pxfcpD6+lS1P/GpnYLS0"
            + "hfrgxq/XE8WAVN/njS5TFt6cvic98GFgb0Q7H//esOGdJ5vowHg8ohHTjkvBDkiWKRzlA+Mpasir"
            + "1hyGBu4tcsG5WwYdT4ZjSpF2FDXADDS8un6FS9FHjPCagTp6fpFm3MTdesf7eWiLlcEzAgMBAAEC"
            + "gYAlAh18ruXH3WE4xW+VgZkVK5IWVaJeSTZgTH+OwxnUxAGFVQBWBS9zJNOyidztfz3NGlAvqT/G"
            + "RizCikAoDjhiUK98MIqM+l7S43Q/ASEZ8/w1iTXbX6HsYb8aDGdIWpDiQajEPIqcQXoMZZR2942r"
            + "Y8+aObuSbrk5IVmIK8uzkQJBAOxuSTcK9O5dy68J0g99CSadPkUUXYovm0r1ruiyYQRbaL6i6L4p"
            + "3o0Vyy63Vj56qhe2tsRyC8abYoCMVehMi/kCQQCpRA+KkSdWd++9trUEMr6Tsv0fHBm+c7y74uEB"
            + "9NM8wJ/zfBvXgvdR6tnmMpr4PRkU7twdKjidPTf/DGXkagmLAkEA2bx7etCBXuBMi4fMx2zMN56K"
            + "UU3/ExritjbqfOyCAmQ4Y5BeLXsbtOzEMOKw71tCOBKR4Ppys9Y38dDL8OJF6QJBAJgPP5LxIZDJ"
            + "gENDLs0NtS1Ev6ZB/VKd8LAteoviYB4Uwdzf4rcxvXMG8yec0KEvaifnCTDeLCv9wh9LCQIwzE8C"
            + "QC/K4FmKZf51Y6HmSxLI/0FtzOHPjTc3z2v6JdBTur5fQRXHUB86JcztiDMi3+5LZVg8gTJ1fzM8"
            + "/S93FKFLK84=";
    
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
        return "site/loan/recharge";
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
            String ReturnURL = basePath + "loan/returnURL.htm";
            String NotifyURL = basePath + "loan/notifyURL.htm";
            String RechargeMoneymoremore = "m31333";
            String PlatformMoneymoremore = "p1190";
            String OrderNo = map1.get("orderNo").toString();
            String Amount = order.getOrderAmount();
            String RechargeType = "";
            String FeeType = "";
            String CardNo = "";
            String RandomTimeStamp = "";
            String Remark1 = "";
            String Remark2 = "";
            String Remark3 = "";
            map.put("SubmitURL", SubmitURL);
            map.put("ReturnURL", ReturnURL);
            map.put("NotifyURL", NotifyURL);
            map.put("merchantId", map1.get("merchantId"));
            map.put("OrderNo", OrderNo);
            map.put("Amount", Amount);
            map.put("RandomTimeStamp", RandomTimeStamp);
            map.put("RechargeMoneymoremore", RechargeMoneymoremore);
            map.put("PlatformMoneymoremore", PlatformMoneymoremore);
            RsaHelper rsa = RsaHelper.getInstance();
            String dataStr = RechargeMoneymoremore + PlatformMoneymoremore
                    + OrderNo + Amount + RechargeType + FeeType + CardNo
                    + RandomTimeStamp + Remark1 + Remark2 + Remark3 + ReturnURL
                    + NotifyURL;
            String SignInfo = rsa.signData(dataStr, privateKeyPKCS8);
            map.put("SignInfo", SignInfo);
            
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
        
        return "site/loan/recharge-list";
    }
    
    @RequestMapping("/returnURL")
    public String returnURL(ModelMap map, LoanModel result) throws Exception
    {
        
        UsersEntity user = this.getUsersEntity();
        if (user == null)
        {
            return LOGIN;
        }
        //获取账户信息
        AccountEntity account = accountService.getAccountByCustomerId(user.getCustomerId());
        double orderAmount = 0;
        double payAmount = 0;
        
        map.put("orderNo", result.getOrderNo());
        map.put("orderAmount", orderAmount);
        map.put("payAmount", payAmount);
        map.put("balance", this.divide(account.getBalance()));
        return "site/loan/results";
    }
    
    @ResponseBody
    @RequestMapping("/notifyURL")
    public void notifyURL(ModelMap map, LoanModel result) throws Exception
    {
        System.out.println(result.getMessage());
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
