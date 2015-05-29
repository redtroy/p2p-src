package com.herongwang.p2p.website.controller.post;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import util.RsaHelper;

import com.herongwang.p2p.entity.account.AccountEntity;
import com.herongwang.p2p.entity.funddetail.FundDetailEntity;
import com.herongwang.p2p.entity.orders.OrdersEntity;
import com.herongwang.p2p.entity.users.UsersEntity;
import com.herongwang.p2p.loan.util.Common;
import com.herongwang.p2p.model.order.OrderModel;
import com.herongwang.p2p.model.post.LoanModel;
import com.herongwang.p2p.model.post.RegisterModel;
import com.herongwang.p2p.service.account.IAccountService;
import com.herongwang.p2p.service.funddetail.IFundDetailService;
import com.herongwang.p2p.service.orders.IOrdersService;
import com.herongwang.p2p.service.post.IPostService;
import com.herongwang.p2p.service.users.IUserService;
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
    
    public static final String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCcU6n7OKcX3KQ+vpUtT/xqZ2C0tIX64Mav1xPF"
            + "gFTf540uUxbenL4nPfBhYG9EOx//3rDhnSeb6MB4PKIR045LwQ5Ilikc5QPjKWrIq9YchgbuLXLB"
            + "uVsGHU+GY0qRdhQ1wAw0vLp+hUvRR4zwmoE6en6RZtzE3XrH+3loi5XBMwIDAQAB";
    
    @Autowired
    IOrdersService ordersService;
    
    @Autowired
    IPostService postService;
    
    @Autowired
    IAccountService accountService;
    
    @Autowired
    IFundDetailService fundDetailService;
    
    @Autowired
    private IUserService userService;
    
    /*----------------------------------------------充值--------------------------------*/
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
            String NotifyURL = "http://61.132.53.150:7001/p2p-website/loan/notifyURL.htm";
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
        LoanModel loan = new LoanModel();
        loan.setPlatformMoneymoremore("p1190");
        loan.setAction("1");
        loan.setLoanNo(result.getLoanNo());
        String submitURLPrefix = "";
        /*List<LoanRechargeOrderQueryBean> list = postService.rechargeOrderQuery(loan,
                submitURLPrefix);
        for (int i = 0; i < list.size(); i++)
        {
            LoanRechargeOrderQueryBean bean = list.get(i);
            System.out.println("钱多多查询返回：" + bean.getOrderNo());
        }
        System.out.println("钱多多返回：" + result.getOrderNo());*/
        map.put("title", "账户充值");
        map.put("orderNo", result.getOrderNo());
        map.put("orderAmount", result.getAmount());
        map.put("Fee", result.getFee() == null ? 0 : result.getFee());
        map.put("payAmount", result.getAmount());
        map.put("balance", this.divide(account.getBalance()));
        return "site/loan/results";
    }
    
    @ResponseBody
    @RequestMapping("/notifyURL")
    public void notifyURL(ModelMap map, LoanModel result) throws Exception
    {
        System.out.println(result.getMessage());
    }
    
    /*----------------------------------------------提现--------------------------------*/
    @RequestMapping("/withdraw")
    public String withdraw(ModelMap map) throws WebException
    {
        UsersEntity user = this.getUsersEntity();
        if (user == null)
        {
            return LOGIN;
        }
        AccountEntity account = accountService.getAccountByCustomerId(user.getCustomerId());
        map.put("balance", this.divide(account.getBalance()));
        return "site/loan/withdraw";
    }
    
    @RequestMapping("/withdrawList")
    public String withdrawList(HttpServletRequest request, ModelMap map,
            OrderModel order) throws WebException
    {
        String basePath = this.getBasePath(request);
        try
        {
            BigDecimal m = this.multiply(new BigDecimal(order.getOrderAmount()));
            UsersEntity user = userService.getUserById(this.getUsersEntity()
                    .getCustomerId());
            AccountEntity account = accountService.getAccountByCustomerId(user.getCustomerId());
            
            //生成提现订单
            OrdersEntity orders = new OrdersEntity();
            orders.setCustomerId(user.getCustomerId());
            orders.setAmount(m);
            orders.setCreateTime(new Date());
            orders.setStatus(0);
            orders.setOrderType(2);
            ordersService.addOrders(orders);
            
            //生成资金明细
            FundDetailEntity deal = new FundDetailEntity();
            deal.setCustomerId(user.getCustomerId());
            deal.setAccountId(account.getAccountId());
            deal.setOrderId(orders.getOrderId());
            deal.setType(2);
            deal.setCreateTime(new Date());
            deal.setStatus(0);
            deal.setAmount(m);
            deal.setBalance(account.getBalance());
            deal.setDueAmount(account.getDueAmount());
            deal.setFrozenAmount(account.getFozenAmount());
            deal.setRemark("提现" + order.getOrderAmount() + "元失败！");
            fundDetailService.addFundDetail(deal);
            
            //双乾信息
            String SubmitURL = "http://218.4.234.150:88/main/loan/toloanwithdraws.action";
            String ReturnURL = basePath + "loan/withdrawReturnURL.htm";
            String NotifyURL = basePath + "loan/withdrawNotifyURL.htm";
            String WithdrawMoneymoremore = "m31333";
            String PlatformMoneymoremore = "p1190";
            String OrderNo = orders.getOrdersNo();//平台的提现订单号
            String Amount = order.getOrderAmount();//金额
            String FeePercent = "0";//平台承担的手续费比例
            String FeeMax = "5";//用户承担的最高手续费
            String FeeRate = "";//上浮费率
            String CardNo = user.getCardNo();//银行卡号
            String CardType = "0";//银行卡类型
            String BankCode = "1";//银行代码
            String BranchBankName = "中国银行工业园区支行";//开户行支行名称
            String Province = "10";//开户行省份
            String City = "1078";
            String RandomTimeStamp = "";
            String Remark1 = "";
            String Remark2 = "";
            String Remark3 = "";
            
            String dataStr = WithdrawMoneymoremore + PlatformMoneymoremore
                    + OrderNo + Amount + FeePercent + FeeMax + FeeRate + CardNo
                    + CardType + BankCode + BranchBankName + Province + City
                    + RandomTimeStamp + Remark1 + Remark2 + Remark3 + ReturnURL
                    + NotifyURL;
            // 签名
            RsaHelper rsa = RsaHelper.getInstance();
            String SignInfo = rsa.signData(dataStr, privateKeyPKCS8);
            
            CardNo = rsa.encryptData(CardNo, publicKey);
            map.put("SubmitURL", SubmitURL);
            map.put("WithdrawMoneymoremore", WithdrawMoneymoremore);
            map.put("PlatformMoneymoremore", PlatformMoneymoremore);
            map.put("OrderNo", OrderNo);
            map.put("FeePercent", FeePercent);
            map.put("FeeMax", FeeMax);
            map.put("FeeRate", FeeRate);
            map.put("Amount", Amount);
            map.put("CardNo", CardNo);
            map.put("CardType", CardType);
            map.put("BankCode", BankCode);
            map.put("BranchBankName", BranchBankName);
            map.put("Province", Province);
            map.put("City", City);
            map.put("RandomTimeStamp", RandomTimeStamp);
            map.put("Remark1", Remark1);
            map.put("Remark2", Remark2);
            map.put("Remark3", Remark3);
            map.put("ReturnURL", ReturnURL);
            map.put("NotifyURL", NotifyURL);
            map.put("SignInfo", SignInfo);
            map.put("balance", this.divide(account.getBalance()).toString());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new WebException("生成提现订单失败", e);
        }
        
        return "site/loan/withdraw-list";
    }
    
    @RequestMapping("/withdrawReturnURL")
    public String withdrawReturnURL(ModelMap map, LoanModel result)
            throws Exception
    {
        
        UsersEntity user = this.getUsersEntity();
        if (user == null)
        {
            return LOGIN;
        }
        //获取账户信息
        AccountEntity account = accountService.getAccountByCustomerId(user.getCustomerId());
        RsaHelper rsa = RsaHelper.getInstance();
        String dataStr = result.getWithdrawMoneymoremore()
                + result.getPlatformMoneymoremore() + result.getLoanNo()
                + result.getOrderNo() + result.getAmount() + result.getFeeMax()
                + result.getFeeWithdraws() + result.getFeePercent()
                + result.getFee() + result.getFreeLimit() + result.getFeeRate()
                + result.getFeeSplitting() + result.getRandomTimeStamp()
                + result.getRemark1() + result.getRemark2()
                + result.getRemark3() + result.getResultCode();
        boolean verifySignature = rsa.verifySignature(result.getSignInfo(),
                dataStr,
                publicKey);
        if (verifySignature)
        {
            OrdersEntity order = ordersService.getOrdersEntityByNo(result.getOrderNo());
            if (null != order && order.getStatus() == 0)
            {
                order.setStatus(1);
                ordersService.updateOrders(order);
                FundDetailEntity query = new FundDetailEntity();
                query.setOrderId(order.getOrderId());
                query.setType(2);
                List<FundDetailEntity> list = fundDetailService.queryFundDetail(query);
                if (null != list && list.size() > 0)
                {
                    FundDetailEntity deal = list.get(0);
                    deal.setStatus(1);
                    deal.setRemark("提现" + result.getAmount() + "元成功！");
                    fundDetailService.updateFundDetail(deal);
                }
                account.setBalance(account.getBalance()
                        .subtract(this.multiply(new BigDecimal(
                                result.getAmount()))));
                accountService.updateAccount(account);
            }
        }
        map.put("title", "账户提现");
        map.put("orderNo", result.getOrderNo());
        map.put("orderAmount", result.getAmount());
        map.put("Fee", result.getFeeWithdraws());
        map.put("payAmount", result.getAmount() - result.getFeeWithdraws());
        map.put("balance", this.divide(account.getBalance()));
        return "site/loan/results";
    }
    
    @ResponseBody
    @RequestMapping("/withdrawNotifyURL")
    public void withdrawNotifyURL(LoanModel result) throws Exception
    {
        
        UsersEntity user = this.getUsersEntity();
        if (user == null)
        {
            return;
        }
        //获取账户信息
        AccountEntity account = accountService.getAccountByCustomerId(user.getCustomerId());
        RsaHelper rsa = RsaHelper.getInstance();
        String dataStr = result.getWithdrawMoneymoremore()
                + result.getPlatformMoneymoremore() + result.getLoanNo()
                + result.getOrderNo() + result.getAmount() + result.getFeeMax()
                + result.getFeeWithdraws() + result.getFeePercent()
                + result.getFee() + result.getFreeLimit() + result.getFeeRate()
                + result.getFeeSplitting() + result.getRandomTimeStamp()
                + result.getRemark1() + result.getRemark2()
                + result.getRemark3() + result.getResultCode();
        boolean verifySignature = rsa.verifySignature(result.getSignInfo(),
                dataStr,
                publicKey);
        if (verifySignature)
        {
            OrdersEntity order = ordersService.getOrdersEntityByNo(result.getOrderNo());
            if (null != order && order.getStatus() == 0)
            {
                order.setStatus(1);
                ordersService.updateOrders(order);
                FundDetailEntity query = new FundDetailEntity();
                query.setOrderId(order.getOrderId());
                query.setType(2);
                List<FundDetailEntity> list = fundDetailService.queryFundDetail(query);
                if (null != list && list.size() > 0)
                {
                    FundDetailEntity deal = list.get(0);
                    deal.setStatus(1);
                    deal.setRemark("提现" + result.getAmount() + "元成功！");
                    fundDetailService.updateFundDetail(deal);
                }
                account.setBalance(account.getBalance()
                        .subtract(this.multiply(new BigDecimal(
                                result.getAmount()))));
                accountService.updateAccount(account);
            }
            
        }
    }
    
    /*----------------------------------------------授权--------------------------------*/
    @RequestMapping("/authorize")
    public String authorize(HttpServletRequest request, ModelMap map)
            throws WebException
    {
        UsersEntity user = this.getUsersEntity();
        if (user == null)
        {
            return LOGIN;
        }
        String basePath = this.getBasePath(request);
        //获取授权状态
        String authorizeType1 = "0";
        String authorizeType2 = "0";
        String authorizeType3 = "1";
        String ReturnURL = basePath + "loan/authorizeReturnURL.htm";
        String NotifyURL = basePath + "loan/authorizeNotifyURL.htm";
        String SubmitURL = "http://218.4.234.150:88/main/loan/toloanauthorize.action";
        String MoneymoremoreId = "m31333";
        String PlatformMoneymoremore = "p1190";
        
        map.put("authorizeType1", authorizeType1);
        map.put("authorizeType2", authorizeType2);
        map.put("authorizeType3", authorizeType3);
        map.put("SubmitURL", SubmitURL);
        map.put("MoneymoremoreId", MoneymoremoreId);
        map.put("PlatformMoneymoremore", PlatformMoneymoremore);
        map.put("ReturnURL", ReturnURL);
        map.put("NotifyURL", NotifyURL);
        return "site/loan/authorize";
    }
    
    /**
     * 生成授权签名
     * @param session
     * @param apply
     * @return
     * @throws WebException
     */
    @RequestMapping("authorizeList")
    public @ResponseBody Map<String, String> saveApply(
            HttpServletRequest request, String type) throws WebException
    {
        String basePath = this.getBasePath(request);
        try
        {
            Map<String, String> map = new HashMap<String, String>();
            String ReturnURL = basePath + "loan/authorizeReturnURL.htm";
            String NotifyURL = basePath + "loan/authorizeNotifyURL.htm";
            String MoneymoremoreId = "m31333";
            String PlatformMoneymoremore = "p1190";
            String AuthorizeTypeOpen = type.substring(2);
            String dataStr = MoneymoremoreId + PlatformMoneymoremore
                    + AuthorizeTypeOpen + "" + "" + "" + "" + "" + ReturnURL
                    + NotifyURL;
            // 签名
            RsaHelper rsa = RsaHelper.getInstance();
            String SignInfo = rsa.signData(dataStr, privateKeyPKCS8);
            map.put("AuthorizeTypeOpen", AuthorizeTypeOpen);
            map.put("SignInfo", SignInfo);
            return map;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new WebException("生成授权签名失败", e);
        }
        
    }
    
    @RequestMapping("/authorizeReturnURL")
    public String authorizeReturnURL(ModelMap map, LoanModel result)
            throws Exception
    {
        
        UsersEntity user = this.getUsersEntity();
        if (user == null)
        {
            return LOGIN;
        }
        //获取账户信息
        map.put("title", result.getMessage());
        map.put("orderNo", result.getOrderNo());
        map.put("orderAmount", result.getAmount());
        map.put("Fee", result.getFeeWithdraws());
        map.put("payAmount", "");
        return "site/loan/results";
    }
    
    @ResponseBody
    @RequestMapping("/authorizeNotifyURL")
    public void authorizeNotifyURL(LoanModel result) throws Exception
    {
        
        UsersEntity user = this.getUsersEntity();
        if (user == null)
        {
            return;
        }
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
    
    @RequestMapping("register")
    public String register() throws WebException
    {
        return "site/test/register";
    }
    
    @RequestMapping("saveregister")
    public String saveregister(RegisterModel rg) throws WebException
    {
        try
        {
            rg.setReturnURL("http://127.0.0.1:8080/p2p-website/loan/test.htm");
            rg.setNotifyURL("http://127.0.0.1:8080/p2p-website/loan/test2.htm");
            String privatekey = Common.privateKeyPKCS8;
            String dataStr = rg.getRegisterType() + rg.getAccountType()
                    + rg.getMobile() + rg.getEmail() + rg.getRealName()
                    + rg.getIdentificationNo() + rg.getImage1()
                    + rg.getImage2() + rg.getLoanPlatformAccount()
                    + rg.getPlatformMoneymoremore() + rg.getRandomTimeStamp()
                    + rg.getRemark1() + rg.getRemark2() + rg.getRemark3()
                    + rg.getReturnURL() + rg.getNotifyURL();
            // 签名
            RsaHelper rsa = RsaHelper.getInstance();
            String SignInfo = rsa.signData(dataStr, privatekey);
            rg.setSignInfo(SignInfo);
            postService.register(rg);
        }
        catch (Exception e)
        {
            // TODO: handle exception
        }
        return "site/test/jump";
    }
    
    @RequestMapping("test")
    public String test()
    {
        System.out.println("ok");
        return "site/test/jump";
    }
    
    @RequestMapping("test2")
    public @ResponseBody String test2()
    {
        System.out.println("ok");
        return "";
    }
}
