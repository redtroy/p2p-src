package com.herongwang.p2p.website.controller.post;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
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
import com.herongwang.p2p.model.loan.LoanInfoBean;
import com.herongwang.p2p.model.loan.LoanRegisterBindReturnBean;
import com.herongwang.p2p.model.loan.LoanTransferReturnBean;
import com.herongwang.p2p.model.loan.ReleaseBean;
import com.herongwang.p2p.model.loan.transferauditreturnBean;
import com.herongwang.p2p.model.order.OrderModel;
import com.herongwang.p2p.model.post.Loan;
import com.herongwang.p2p.model.post.LoanModel;
import com.herongwang.p2p.model.post.LoanReleaseModel;
import com.herongwang.p2p.model.post.LoanTransferAuditModel;
import com.herongwang.p2p.model.post.RegisterModel;
import com.herongwang.p2p.model.post.TransferModel;
import com.herongwang.p2p.service.account.IAccountService;
import com.herongwang.p2p.service.funddetail.IFundDetailService;
import com.herongwang.p2p.service.orders.IOrdersService;
import com.herongwang.p2p.service.parameters.IParametersService;
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
    
    @Autowired
    IParametersService parametersService;
    
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
        Loan loan = parametersService.getLoan();
        try
        {
            String basePath = this.getBasePath(request);
            
            //生成充值订单
            OrdersEntity orders = new OrdersEntity();
            orders.setCustomerId(user.getCustomerId());
            orders.setAmount(m);
            orders.setCreateTime(new Date());
            orders.setStatus(0);
            orders.setOrderType(1);
            ordersService.addOrders(orders);
            
            //生成签名
            String SubmitURL = loan.getSubmitURL()
                    + "loan/toloanrecharge.action";
            String ReturnURL = basePath + "loan/returnURL.htm";
            String NotifyURL = "http://61.132.53.150:7001/p2p-website/loan/notifyURL.htm";
            String RechargeMoneymoremore = "m31333";//用户钱多多标志
            String PlatformMoneymoremore = loan.getMoremoreId();
            String OrderNo = orders.getOrdersNo();
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
            String SignInfo = rsa.signData(dataStr, loan.getPrivatekey());
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
        DecimalFormat df = new DecimalFormat("######0.00");
        UsersEntity user = this.getUsersEntity();
        if (user == null)
        {
            return LOGIN;
        }
        //获取账户信息
        AccountEntity account = accountService.getAccountByCustomerId(user.getCustomerId());
        //获取双乾参数
        Loan loan = parametersService.getLoan();
        String fee = result.getFee() == null ? "" : result.getFee().toString();
        String amount = df.format(result.getAmount());
        //生成返回签名
        String dataStr = result.getRechargeMoneymoremore()
                + result.getPlatformMoneymoremore() + result.getLoanNo()
                + result.getOrderNo() + amount + fee + result.getRechargeType()
                + result.getFeeType() + result.getCardNoList()
                + result.getRandomTimeStamp() + result.getRemark1()
                + result.getRemark2() + result.getRemark3()
                + result.getResultCode();
        // 验证签名签名
        RsaHelper rsa = RsaHelper.getInstance();
        boolean verifySignature = rsa.verifySignature(result.getSignInfo(),
                dataStr,
                loan.getPublickey());
        if (verifySignature)
        {
            OrdersEntity orders = ordersService.getOrdersEntityByNo(result.getOrderNo());
            if (orders.getStatus() != 1)
            {
                //更新订单支付成功！
                orders.setStatus(1);
                orders.setLoanNo(result.getLoanNo());
                orders.setArriveTime(new Date());
                ordersService.updateOrders(orders);
                
                //添加资金明细
                FundDetailEntity deal = new FundDetailEntity();
                deal.setCustomerId(user.getCustomerId());
                deal.setAccountId(account.getAccountId());
                deal.setOrderId(orders.getOrderId());
                deal.setType(1);
                deal.setCreateTime(new Date());
                deal.setStatus(1);
                deal.setAmount(orders.getAmount());
                deal.setBalance(account.getBalance());
                deal.setDueAmount(account.getDueAmount());
                deal.setFrozenAmount(account.getFozenAmount());
                deal.setRemark("充值" + amount + "元成功！");
                fundDetailService.addFundDetail(deal);
                
                //更新账户金额
                account.setBalance(account.getBalance().add(orders.getAmount()));
                accountService.updateAccount(account);
            }
        }
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
        DecimalFormat df = new DecimalFormat("######0.00");
        //获取双乾参数
        Loan loan = parametersService.getLoan();
        String amount = df.format(result.getAmount());
        //生成返回签名
        String dataStr = result.getRechargeMoneymoremore()
                + result.getPlatformMoneymoremore() + result.getLoanNo()
                + result.getOrderNo() + amount + result.getFee()
                + result.getRechargeType() + result.getFeeType()
                + result.getCardNoList() + result.getRandomTimeStamp()
                + result.getRemark1() + result.getRemark2()
                + result.getRemark3() + result.getResultCode();
        // 验证签名签名
        RsaHelper rsa = RsaHelper.getInstance();
        boolean verifySignature = rsa.verifySignature(result.getSignInfo(),
                dataStr,
                loan.getPublickey());
        if (verifySignature)
        {
            OrdersEntity orders = ordersService.getOrdersEntityByNo(result.getOrderNo());
            //获取账户信息
            AccountEntity account = accountService.getAccountByCustomerId(orders.getCustomerId());
            if (orders.getStatus() != 1)
            {
                //更新订单未支付成功！
                orders.setStatus(1);
                orders.setLoanNo(result.getLoanNo());
                orders.setArriveTime(new Date());
                ordersService.updateOrders(orders);
                
                //添加资金明细
                FundDetailEntity deal = new FundDetailEntity();
                deal.setCustomerId(orders.getCustomerId());
                deal.setAccountId(account.getAccountId());
                deal.setOrderId(orders.getOrderId());
                deal.setType(1);
                deal.setCreateTime(new Date());
                deal.setStatus(1);
                deal.setAmount(orders.getAmount());
                deal.setBalance(account.getBalance());
                deal.setDueAmount(account.getDueAmount());
                deal.setFrozenAmount(account.getFozenAmount());
                deal.setRemark("充值" + result.getAmount() + "元成功！");
                fundDetailService.addFundDetail(deal);
                
                //更新账户金额
                account.setBalance(account.getBalance().add(orders.getAmount()));
                accountService.updateAccount(account);
            }
        }
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
        UsersEntity users = this.getUsersEntity();
        String basePath = this.getBasePath(request);
        
        try
        {
            //获取双乾参数
            Loan loan = parametersService.getLoan();
            BigDecimal m = this.multiply(new BigDecimal(order.getOrderAmount()));
            UsersEntity user = userService.getUserById(users.getCustomerId());
            AccountEntity account = accountService.getAccountByCustomerId(user.getCustomerId());
            
            //生成提现订单
            OrdersEntity orders = new OrdersEntity();
            orders.setCustomerId(user.getCustomerId());
            orders.setAmount(m);
            orders.setCreateTime(new Date());
            orders.setStatus(0);
            orders.setOrderType(2);
            ordersService.addOrders(orders);
            
            //双乾信息
            //生成签名
            String SubmitURL = loan.getSubmitURL()
                    + "loan/toloanwithdraws.action";
            String ReturnURL = basePath + "loan/withdrawReturnURL.htm";
            String NotifyURL = "http://61.132.53.150:7001/p2p-website/loan/withdrawNotifyURL.htm";
            String WithdrawMoneymoremore = "m31333";
            String PlatformMoneymoremore = loan.getMoremoreId();
            String OrderNo = orders.getOrdersNo();//平台的提现订单号
            String Amount = order.getOrderAmount();//金额
            String FeePercent = loan.getFeePercent() == null ? ""
                    : loan.getFeePercent().toString();//平台承担的手续费比例
            String FeeMax = loan.getFeeMax() == null ? "" : loan.getFeeMax()
                    .toString();//用户承担的最高手续费
            String FeeRate = loan.getFeeRate() == null ? "" : loan.getFeeRate()
                    .toString();//上浮费率
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
            String SignInfo = rsa.signData(dataStr, loan.getPrivatekey());
            
            CardNo = rsa.encryptData(CardNo, loan.getPublickey());
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
        DecimalFormat df = new DecimalFormat("######0.00");
        UsersEntity user = this.getUsersEntity();
        if (user == null)
        {
            return LOGIN;
        }
        //获取账户信息
        AccountEntity account = accountService.getAccountByCustomerId(user.getCustomerId());
        //获取双乾参数
        Loan loan = parametersService.getLoan();
        RsaHelper rsa = RsaHelper.getInstance();
        
        String amount = df.format(result.getAmount());
        String feeSplitting = result.getFeeSplitting() == null ? ""
                : result.getFeeSplitting().toString();//平台承担的手续费比例
        String feeMax = result.getFeeMax() == null ? "" : result.getFeeMax()
                .toString();//用户承担的最高手续费
        String feeRate = result.getFeeRate() == null ? "" : result.getFeeRate()
                .toString();//上浮费率
        String dataStr = result.getWithdrawMoneymoremore()
                + result.getPlatformMoneymoremore() + result.getLoanNo()
                + result.getOrderNo() + amount + feeMax
                + df.format(result.getFeeWithdraws())
                + df.format(result.getFeePercent())
                + df.format(result.getFee()) + df.format(result.getFreeLimit())
                + feeRate + feeSplitting + result.getRandomTimeStamp()
                + result.getRemark1() + result.getRemark2()
                + result.getRemark3() + result.getResultCode();
        boolean verifySignature = rsa.verifySignature(result.getSignInfo(),
                dataStr,
                loan.getPublickey());
        if (verifySignature)
        {
            OrdersEntity order = ordersService.getOrdersEntityByNo(result.getOrderNo());
            if (null != order && order.getStatus() == 0)
            {
                order.setStatus(1);
                ordersService.updateOrders(order);
                
                //添加资金明细
                FundDetailEntity deal = new FundDetailEntity();
                deal.setCustomerId(order.getCustomerId());
                deal.setAccountId(account.getAccountId());
                deal.setOrderId(order.getOrderId());
                deal.setType(1);
                deal.setCreateTime(new Date());
                deal.setStatus(1);
                deal.setAmount(order.getAmount());
                deal.setBalance(account.getBalance());
                deal.setDueAmount(account.getDueAmount());
                deal.setFrozenAmount(account.getFozenAmount());
                deal.setRemark("提现" + result.getAmount() + "元成功！");
                fundDetailService.addFundDetail(deal);
                
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
        DecimalFormat df = new DecimalFormat("######0.00");
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
    
    /*----------------------------------------------余额查询--------------------------------*/
    /**
     * 余额查询
     * @param session
     * @param apply
     * @return
     * @throws WebException
     */
    @RequestMapping("balanceQuery")
    public @ResponseBody Map<String, String> balanceQuery(
            HttpServletRequest request, String type) throws WebException
    {
        try
        {
            Map<String, String> map = new HashMap<String, String>();
            String PlatformId = "m31333";
            String platformType = "1";//1.托管账户 2.自有账户
            
            String[] result = postService.balanceQuery(PlatformId, platformType);
            String[] balance = result[1].split("\\|");
            map.put("balance1", balance[0]);
            map.put("balance2", balance[1]);
            map.put("balance3", balance[2]);
            return map;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new WebException("生成查询余额失败", e);
        }
        
    }
    
    /*-------------------------认证、提现银行卡绑定、代扣授权三合一接口--------------------------------*/
    @RequestMapping("/fastPay")
    public String fastPay(HttpServletRequest request, ModelMap map)
            throws WebException
    {
        UsersEntity user = this.getUsersEntity();
        if (user == null)
        {
            return LOGIN;
        }
        String basePath = this.getBasePath(request);
        String SubmitURL = "http://218.4.234.150:88/main/loan/toloanauthorize.action";
        String ReturnURL = basePath + "loan/fastPayReturnURL.htm";
        String NotifyURL = basePath + "loan/fastPayNotifyURL.htm";
        
        String privatekey = privateKeyPKCS8;
        String publickey = publicKey;
        String MoneymoremoreId = "m31333";
        String PlatformMoneymoremore = "p1190";
        String Action = "2";
        String CardNo = "6222024301056658220";
        String WithholdBeginDate = "";
        String WithholdEndDate = "";
        String SingleWithholdLimit = "";
        String TotalWithholdLimit = "";
        String RandomTimeStamp = "";
        String Remark1 = "";
        String Remark2 = "";
        String Remark3 = "";
        String dataStr = MoneymoremoreId + PlatformMoneymoremore + Action
                + CardNo + WithholdBeginDate + WithholdEndDate
                + SingleWithholdLimit + TotalWithholdLimit + RandomTimeStamp
                + Remark1 + Remark2 + Remark3 + ReturnURL + NotifyURL;
        // 签名
        RsaHelper rsa = RsaHelper.getInstance();
        String SignInfo = rsa.signData(dataStr, privatekey);
        
        if (StringUtils.isNotBlank(CardNo))
        {
            CardNo = rsa.encryptData(CardNo, publickey);
        }
        
        map.put("SubmitURL", SubmitURL);
        map.put("MoneymoremoreId", MoneymoremoreId);
        map.put("PlatformMoneymoremore", PlatformMoneymoremore);
        map.put("Action", Action);
        map.put("CardNo", CardNo);
        map.put("WithholdBeginDate", WithholdBeginDate);
        map.put("WithholdEndDate", WithholdEndDate);
        map.put("SingleWithholdLimit", SingleWithholdLimit);
        map.put("TotalWithholdLimit", TotalWithholdLimit);
        map.put("RandomTimeStamp", RandomTimeStamp);
        map.put("Remark1", Remark1);
        map.put("Remark2", Remark2);
        map.put("Remark3", Remark3);
        map.put("ReturnURL", ReturnURL);
        map.put("NotifyURL", NotifyURL);
        map.put("SignInfo", SignInfo);
        return "site/loan/fastpay";
    }
    
    @RequestMapping("/fastPayReturnURL")
    public String fastPayReturnURL(ModelMap map, LoanModel result)
            throws Exception
    {
        
        UsersEntity user = this.getUsersEntity();
        if (user == null)
        {
            return LOGIN;
        }
        map.put("title", "代扣");
        map.put("orderNo", "0");
        map.put("orderAmount", "0");
        map.put("Fee", "0");
        map.put("payAmount", "0");
        map.put("balance", "0");
        return "site/loan/results";
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
    
    /**
     * 开户页面
     * @return
     * @throws WebException
     */
    @RequestMapping("register")
    public String register() throws WebException
    {
        return "site/test/register";
    }
    
    /**
     * 开户
     * @param rg
     * @param map
     * @return
     * @throws WebException
     */
    @RequestMapping("saveregister")
    public String saveregister(RegisterModel rg, ModelMap map)
            throws WebException
    {
        try
        {
            UsersEntity user = getUsersEntity();
            rg.setRegisterType("2");
            rg.setMobile(user.getCellphone());
            rg.setEmail(user.getEmail());
            rg.setRealName(user.getName());
            rg.setIdentificationNo(user.getCardNum());
            rg.setLoanPlatformAccount(user.getCustomerNo());
            rg.setPlatformMoneymoremore("p1190");
            rg.setReturnURL("http://127.0.0.1:8080/p2p-website/loan/registerbindreturn.htm");
            rg.setNotifyURL("http://127.0.0.1:8080/p2p-website/loan/test2.htm");
            String privatekey = privateKeyPKCS8;
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
            if (rg.getRegisterType().equals("1"))
            {
                postService.register(rg);
            }
            else
            {
                map.put("model", rg);
                return "site/test/loanregister";
            }
        }
        catch (Exception e)
        {
            SxjLogger.error("注册失败", this.getClass());
            throw new WebException("注册会员失败！", e);
        }
        return "site/member/member-center";
    }
    
    /**
     * 开户成功返回页面
     * @param lb
     * @param map
     * @return
     */
    @RequestMapping("registerbindreturn")
    public String registerbindreturn(LoanRegisterBindReturnBean lb, ModelMap map)
    {
        map.put("model", lb);
        return "site/test/registerbindreturn";
    }
    
    /**
     * 开户成功通知后台操作
     * @return
     */
    @RequestMapping("test2")
    public @ResponseBody String test2()
    {
        System.out.println("ok===========================================");
        return "";
    }
    
    /**
     * 转账
     */
    @RequestMapping("transfer")
    public String transfer(ModelMap map) throws WebException
    {
        try
        {
            String privatekey = privateKeyPKCS8;
            List<LoanInfoBean> listmlib = new ArrayList<LoanInfoBean>();
            LoanInfoBean mlib = new LoanInfoBean();
            mlib.setLoanOutMoneymoremore("m31333");//付款人
            mlib.setLoanInMoneymoremore("m37679");//收款人
            mlib.setOrderNo(Common.getRandomNum(10));//订单号
            mlib.setBatchNo(Common.getRandomNum(10));//标号
            mlib.setAmount("10");
            mlib.setFullAmount("20");
            mlib.setTransferName("手续费");
            mlib.setRemark("测试");
            mlib.setSecondaryJsonList("");
            listmlib.add(mlib);
            String LoanJsonList = Common.JSONEncode(listmlib);
            
            TransferModel tf = new TransferModel();
            tf.setPlatformMoneymoremore("p1190");
            tf.setTransferAction("1");
            tf.setAction("1");
            tf.setTransferType("2");
            tf.setReturnURL("http://127.0.0.1:8080/p2p-website/loan/transferReturn.htm");
            tf.setNotifyURL("http://127.0.0.1:8080/p2p-website/loan/transferNotify.htm");
            String dataStr = LoanJsonList + tf.getPlatformMoneymoremore()
                    + tf.getTransferAction() + tf.getAction()
                    + tf.getTransferType() + tf.getNeedAudit()
                    + tf.getRandomTimeStamp() + tf.getRemark1()
                    + tf.getRemark2() + tf.getRemark3() + tf.getReturnURL()
                    + tf.getNotifyURL();
            RsaHelper rsa = RsaHelper.getInstance();
            String SignInfo = rsa.signData(dataStr, privatekey);
            LoanJsonList = Common.UrlEncoder(LoanJsonList, "utf-8");
            tf.setLoanJsonList(LoanJsonList);
            tf.setSignInfo(SignInfo);
            map.put("model", tf);
            
        }
        catch (Exception e)
        {
            // TODO: handle exception
        }
        return "site/test/loantransfer";
    }
    
    /**
     * 转账返回页面
     */
    @RequestMapping("transferReturn")
    public String transferReturn(LoanTransferReturnBean lr, ModelMap map)
            throws WebException
    {
        try
        {
            map.put("model", lr);
        }
        catch (Exception e)
        {
            // TODO: handle exception
        }
        return "site/test/transferreturn";
    }
    
    /**
     * 转账后台通知
     */
    @RequestMapping("transferNotify")
    public @ResponseBody String transferNotify() throws WebException
    {
        try
        {
            
        }
        catch (Exception e)
        {
            // TODO: handle exception
        }
        return "";
    }
    
    /**
     * 资金释放
     */
    @RequestMapping("loanRelease")
    public String loanRelease(ModelMap map) throws WebException
    {
        try
        {
            LoanReleaseModel lr = new LoanReleaseModel();
            lr.setAmount("100");
            lr.setMoneymoremoreId("m31333");
            lr.setPlatformMoneymoremore("p1190");
            lr.setOrderNo("D2015060110048102");
            lr.setReturnURL("http://127.0.0.1:8080/p2p-website/loan/loanReleaseReturn.htm");
            lr.setNotifyURL("http://127.0.0.1:8080/p2p-website/loan/loanReleaseNotif.htm");
            String dataStr = lr.getMoneymoremoreId()
                    + lr.getPlatformMoneymoremore() + lr.getOrderNo()
                    + lr.getAmount() + lr.getRandomTimeStamp()
                    + lr.getRemark1() + lr.getRemark2() + lr.getRemark3()
                    + lr.getReturnURL() + lr.getNotifyURL();
            String privatekey = privateKeyPKCS8;
            RsaHelper rsa = RsaHelper.getInstance();
            String SignInfo = rsa.signData(dataStr, privatekey);
            lr.setSignInfo(SignInfo);
            map.put("model", lr);
        }
        catch (Exception e)
        {
            // TODO: handle exception
        }
        return "site/test/loanrelease";
    }
    
    /**
     * 资金释放返回页面
     */
    @RequestMapping("loanReleaseReturn")
    public String loanReleaseReturn(ReleaseBean rb, ModelMap map)
            throws WebException
    {
        try
        {
            map.put("model", rb);
        }
        catch (Exception e)
        {
            // TODO: handle exception
        }
        return "site/test/releasereturn";
    }
    
    /**
     * 资金释放后台接收
     */
    @RequestMapping("loanReleaseNotif")
    public @ResponseBody String loanReleaseNotif(ReleaseBean rb)
            throws WebException
    {
        try
        {
            
        }
        catch (Exception e)
        {
            // TODO: handle exception
        }
        return "";
    }
    
    /**
     * 审核
     */
    @RequestMapping("loanTransferAudit")
    public String loanTransferAuditModel(ModelMap map) throws WebException
    {
        try
        {
            LoanTransferAuditModel ltsa = new LoanTransferAuditModel();
            ltsa.setPlatformMoneymoremore("p1190");
            ltsa.setAuditType("1");
            ltsa.setLoanNoList("LN11372141506011010551770858");
            String privatekey = privateKeyPKCS8;
            ltsa.setReturnURL("http://127.0.0.1:8080/p2p-website/loan/loanTransferAuditModelReturn.htm");
            ltsa.setNotifyURL("http://127.0.0.1:8080/p2p-website/loan/loanTransferAuditModelNotify.htm");
            String dataStr = ltsa.getLoanNoList()
                    + ltsa.getPlatformMoneymoremore() + ltsa.getAuditType()
                    + ltsa.getRandomTimeStamp() + ltsa.getRemark1()
                    + ltsa.getRemark2() + ltsa.getRemark3()
                    + ltsa.getReturnURL() + ltsa.getNotifyURL();
            RsaHelper rsa = RsaHelper.getInstance();
            String SignInfo = rsa.signData(dataStr, privatekey);
            ltsa.setSignInfo(SignInfo);
            map.put("model", ltsa);
        }
        catch (Exception e)
        {
            // TODO: handle exception
        }
        return "site/test/loantransferaudit";
    }
    
    /**
     * 审核页面返回信息
     */
    @RequestMapping("loanTransferAuditModelReturn")
    public String loanTransferAuditModelReturn(transferauditreturnBean tfb,
            ModelMap map) throws WebException
    {
        try
        {
            map.put("model", tfb);
        }
        catch (Exception e)
        {
            // TODO: handle exception
        }
        return "site/test/transferauditreturn";
    }
    
    /**
     * 审核后台通知信息
     */
    @RequestMapping("loanTransferAuditModelNotify")
    public @ResponseBody String loanTransferAuditModelNotify(
            transferauditreturnBean tfb) throws WebException
    {
        try
        {
            
        }
        catch (Exception e)
        {
            // TODO: handle exception
        }
        return "";
    }
}
