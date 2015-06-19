package com.herongwang.p2p.website.controller.post;

import java.math.BigDecimal;
import java.text.DecimalFormat;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import util.RsaHelper;

import com.herongwang.p2p.entity.account.AccountEntity;
import com.herongwang.p2p.entity.admin.AdminEntity;
import com.herongwang.p2p.entity.funddetail.FundDetailEntity;
import com.herongwang.p2p.entity.investorder.InvestOrderEntity;
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
import com.herongwang.p2p.service.admin.IAdminService;
import com.herongwang.p2p.service.funddetail.IFundDetailService;
import com.herongwang.p2p.service.investorder.IInvestOrderService;
import com.herongwang.p2p.service.loan.ILoanService;
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
    
    @Autowired
    IInvestOrderService investOrderService;
    
    @Autowired
    private IAdminService adminService;
    
    @Autowired
    ILoanService loanService;
    
    /*----------------------------------------------充值--------------------------------*/
    @RequestMapping("/recharge")
    public String recharge(ModelMap map) throws WebException
    {
        UsersEntity user = this.getUsersEntity();
        if (user == null)
        {
            return LOGIN;
        }
        map.put("title", "账户充值");
        if (StringUtils.isEmpty(user.getMoneymoremoreId()))
        {
            map.put("moneyType", 0);
        }
        else
        {
            map.put("moneyType", 1);
        }
        map.put("moneyAmount", "0");//订单金额
        return "site/loan/recharge";
    }
    
    @RequestMapping("/rechargeList")
    public String rechargeList(HttpServletRequest request, ModelMap map,
            OrderModel order) throws WebException
    {
        AdminEntity admin = adminService.gitAdminEntity("1");
        map.put("type", admin.getStatus());
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
            String NotifyURL = loan.getServiceIp()
                    + "p2p-website/loan/notifyURL.htm";
            String RechargeMoneymoremore = user.getMoneymoremoreId();//用户钱多多标志
            String PlatformMoneymoremore = loan.getMoremoreId();
            String OrderNo = orders.getOrdersNo();
            String Amount = order.getOrderAmount();
            String RechargeType = "";
            String FeeType = "";
            String CardNo = "";
            String RandomTimeStamp = "";
            String Remark1 = "";
            if (StringUtils.isNotEmpty(order.getExt1()))//投资跳转过来的
            {
                Remark1 = order.getExt1();
                ReturnURL = basePath + "loan/returnOrderURL.htm";
            }
            String Remark2 = "";
            String Remark3 = "";
            map.put("SubmitURL", SubmitURL);
            map.put("ReturnURL", ReturnURL);
            map.put("NotifyURL", NotifyURL);
            map.put("OrderNo", OrderNo);
            map.put("Amount", Amount);
            map.put("Remark1", Remark1);
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
        String message = Common.JSONEncode(result);
        loanService.addOrder(message, "LoanModel", "充值跳转页面返回报文");
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
                //更新订单未支付成功！
                orders.setStatus(1);
                orders.setLoanNo(result.getLoanNo());
                orders.setArriveTime(new Date());
                orders.setFeeWithdraws(multiply(new BigDecimal(
                        null == result.getFee() ? 0 : result.getFee())));
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
    
    /**
     * 投资余额不足充值回调方法
     * @param map
     * @param result
     * @return
     * @throws Exception
     */
    @RequestMapping("/returnOrderURL")
    public String returnOrderURL(ModelMap map, LoanModel result,
            RedirectAttributes ra) throws Exception
    {
        String message = Common.JSONEncode(result);
        loanService.addOrder(message, "LoanModel", "充值跳转页面返回报文");
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
                //更新订单未支付成功！
                orders.setStatus(1);
                orders.setLoanNo(result.getLoanNo());
                orders.setArriveTime(new Date());
                orders.setFeeWithdraws(multiply(new BigDecimal(
                        null == result.getFee() ? 0 : result.getFee())));
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
            ra.addAttribute("orderId", result.getRemark1());
            return "redirect:/post/investPost.htm";
        }
        else
        {
            map.put("title", result.getMessage());
            map.put("orderNo", result.getOrderNo());
            map.put("orderAmount", result.getAmount());
            map.put("Fee", result.getFee() == null ? 0 : result.getFee());
            map.put("payAmount", result.getAmount());
            map.put("balance", this.divide(account.getBalance()));
            return "site/loan/results";
        }
    }
    
    @ResponseBody
    @RequestMapping("/notifyURL")
    public void notifyURL(ModelMap map, LoanModel result) throws Exception
    {
        String message = Common.JSONEncode(result);
        loanService.addOrder(message, "LoanModel", "充值后台返回报文");
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
                orders.setFeeWithdraws(multiply(new BigDecimal(
                        null == result.getFee() ? 0 : result.getFee())));
                ordersService.updateOrders(orders);
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
        if (StringUtils.isEmpty(user.getMoneymoremoreId()))
        {
            map.put("moneyType", 0);
        }
        else
        {
            map.put("moneyType", 1);
        }
        return "site/loan/withdraw";
    }
    
    @RequestMapping("/withdrawList")
    public String withdrawList(HttpServletRequest request, ModelMap map,
            OrderModel order) throws WebException
    {
        UsersEntity users = this.getUsersEntity();
        String basePath = this.getBasePath(request);
        AdminEntity admin = adminService.gitAdminEntity("1");
        map.put("type", admin.getStatus());
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
            String NotifyURL = loan.getServiceIp()
                    + "p2p-website/loan/withdrawNotifyURL.htm";
            String WithdrawMoneymoremore = user.getMoneymoremoreId();
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
        String message = Common.JSONEncode(result);
        loanService.addOrder(message, "LoanModel", "提现跳转页面返回报文");
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
                //更新订单未支付成功！
                order.setStatus(1);
                order.setLoanNo(result.getLoanNo());
                order.setArriveTime(new Date());
                
                order.setFeeWithdraws(multiply(new BigDecimal(
                        result.getFeeWithdraws())));
                ordersService.updateOrders(order);
                
                //添加资金明细
                FundDetailEntity deal = new FundDetailEntity();
                deal.setCustomerId(order.getCustomerId());
                deal.setAccountId(account.getAccountId());
                deal.setOrderId(order.getOrderId());
                deal.setType(2);
                deal.setCreateTime(new Date());
                deal.setStatus(1);
                deal.setAmount(order.getAmount());
                deal.setBalance(account.getBalance());
                deal.setDueAmount(account.getDueAmount());
                deal.setFrozenAmount(account.getFozenAmount());
                deal.setRemark("提现" + result.getAmount() + "元成功！");
                fundDetailService.addFundDetail(deal);
                if (null != result.getFeeWithdraws()
                        && result.getFeeWithdraws() > 0)
                {
                    //添加资金明细
                    deal = new FundDetailEntity();
                    deal.setCustomerId(order.getCustomerId());
                    deal.setAccountId(account.getAccountId());
                    deal.setOrderId(order.getOrderId());
                    deal.setType(12);
                    deal.setCreateTime(new Date());
                    deal.setStatus(1);
                    deal.setAmount(this.multiply(new BigDecimal(
                            result.getFeeWithdraws())));
                    deal.setBalance(account.getBalance());
                    deal.setDueAmount(account.getDueAmount());
                    deal.setFrozenAmount(account.getFozenAmount());
                    deal.setRemark("提现手续费" + result.getFeeWithdraws()
                            + "元，该金额从提现金额中扣除。");
                    fundDetailService.addFundDetail(deal);
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
        String message = Common.JSONEncode(result);
        loanService.addOrder(message, "LoanModel", "提现后台返回报文");
        UsersEntity user = this.getUsersEntity();
        if (user == null)
        {
            return;
        }
        Loan loan = parametersService.getLoan();
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
                loan.getPublickey());
        if (verifySignature)
        {
            OrdersEntity order = ordersService.getOrdersEntityByNo(result.getOrderNo());
            if (null != order && order.getStatus() == 0)
            {
                //更新订单未支付成功！
                order.setStatus(1);
                order.setLoanNo(result.getLoanNo());
                order.setArriveTime(new Date());
                
                order.setFeeWithdraws(multiply(new BigDecimal(
                        result.getFeeWithdraws())));
                ordersService.updateOrders(order);
                
                //添加资金明细
                FundDetailEntity deal = new FundDetailEntity();
                deal.setCustomerId(order.getCustomerId());
                deal.setAccountId(account.getAccountId());
                deal.setOrderId(order.getOrderId());
                deal.setType(2);
                deal.setCreateTime(new Date());
                deal.setStatus(1);
                deal.setAmount(order.getAmount());
                deal.setBalance(account.getBalance());
                deal.setDueAmount(account.getDueAmount());
                deal.setFrozenAmount(account.getFozenAmount());
                deal.setRemark("提现" + result.getAmount() + "元成功！");
                fundDetailService.addFundDetail(deal);
                if (null != result.getFeeWithdraws()
                        && result.getFeeWithdraws() > 0)
                {
                    //添加资金明细
                    deal = new FundDetailEntity();
                    deal.setCustomerId(order.getCustomerId());
                    deal.setAccountId(account.getAccountId());
                    deal.setOrderId(order.getOrderId());
                    deal.setType(12);
                    deal.setCreateTime(new Date());
                    deal.setStatus(1);
                    deal.setAmount(this.multiply(new BigDecimal(
                            result.getFeeWithdraws())));
                    deal.setBalance(account.getBalance());
                    deal.setDueAmount(account.getDueAmount());
                    deal.setFrozenAmount(account.getFozenAmount());
                    deal.setRemark("提现手续费" + result.getFeeWithdraws()
                            + "元，该金额从提现金额中扣除。");
                    fundDetailService.addFundDetail(deal);
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
    public String authorize(HttpServletRequest request, ModelMap map,
            String type) throws WebException
    {
        AdminEntity admin = adminService.gitAdminEntity("1");
        map.put("type", admin.getStatus());
        UsersEntity user = this.getUsersEntity();
        if (user == null)
        {
            return LOGIN;
        }
        //获取双乾参数
        Loan loan = parametersService.getLoan();
        String basePath = this.getBasePath(request);
        //获取授权状态
        String authorizeType1 = user.getTenderStatus().toString();
        String authorizeType2 = user.getRepaymentStatus().toString();
        String authorizeType3 = user.getAllocationStatus().toString();
        String ReturnURL = basePath + "loan/authorizeReturnURL.htm";
        String NotifyURL = loan.getServiceIp()
                + "p2p-website/loan/authorizeNotifyURL.htm";
        String SubmitURL = loan.getSubmitURL() + "loan/toloanauthorize.action";
        String MoneymoremoreId = user.getMoneymoremoreId();
        String PlatformMoneymoremore = loan.getMoremoreId();
        
        map.put("authorizeType1", authorizeType1);
        map.put("authorizeType2", authorizeType2);
        map.put("authorizeType3", authorizeType3);
        map.put("SubmitURL", SubmitURL);
        map.put("MoneymoremoreId", MoneymoremoreId);
        map.put("PlatformMoneymoremore", PlatformMoneymoremore);
        map.put("ReturnURL", ReturnURL);
        map.put("NotifyURL", NotifyURL);
        map.put("type", type);
        if (StringUtils.isEmpty(user.getMoneymoremoreId()))
        {
            map.put("moneyType", 0);
        }
        else
        {
            map.put("moneyType", 1);
        }
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
        UsersEntity user = this.getUsersEntity();
        try
        {
            //获取双乾参数
            Loan loan = parametersService.getLoan();
            Map<String, String> map = new HashMap<String, String>();
            String ReturnURL = basePath + "loan/authorizeReturnURL.htm";
            String NotifyURL = loan.getServiceIp()
                    + "p2p-website/loan/authorizeNotifyURL.htm";
            String MoneymoremoreId = user.getMoneymoremoreId();
            String PlatformMoneymoremore = loan.getMoremoreId();
            String AuthorizeTypeOpen = type.substring(2);
            String dataStr = MoneymoremoreId + PlatformMoneymoremore
                    + AuthorizeTypeOpen + "" + "" + "" + "" + "" + ReturnURL
                    + NotifyURL;
            // 签名
            RsaHelper rsa = RsaHelper.getInstance();
            String SignInfo = rsa.signData(dataStr, loan.getPrivatekey());
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
        String message = Common.JSONEncode(result);
        loanService.addOrder(message, "LoanModel", "授权跳转页面返回报文");
        UsersEntity user = this.getUsersEntity();
        if (user == null)
        {
            return LOGIN;
        }
        //获取双乾参数
        Loan loan = parametersService.getLoan();
        String dataStr = result.getMoneymoremoreId()
                + result.getPlatformMoneymoremore()
                + result.getAuthorizeTypeOpen()
                + result.getAuthorizeTypeClose() + result.getAuthorizeType()
                + result.getRandomTimeStamp() + result.getRemark1()
                + result.getRemark2() + result.getRemark3()
                + result.getResultCode();
        RsaHelper rsa = RsaHelper.getInstance();
        // 签名
        boolean verifySignature = rsa.verifySignature(result.getSignInfo(),
                dataStr,
                loan.getPublickey());
        if (verifySignature)
        {
            int tenderStatus = 0;
            int repaymentStatus = 0;
            int allocationStatus = 0;
            
            String[] status = result.getAuthorizeType().split(",");
            for (int i = 0; i < status.length; i++)
            {
                if ("1".equals(status[i]))
                {
                    tenderStatus = 1;
                }
                if ("2".equals(status[i]))
                {
                    repaymentStatus = 1;
                }
                if ("3".equals(status[i]))
                {
                    allocationStatus = 1;
                }
            }
            //更新用户授权状态
            UsersEntity u = userService.getUserById(user.getCustomerId());
            u.setTenderStatus(tenderStatus);
            u.setRepaymentStatus(repaymentStatus);
            u.setAllocationStatus(allocationStatus);
            userService.updateUser(u);
        }
        //获取账户信息
        map.put("title", "授权：" + result.getMessage());
        map.put("orderNo", "无");
        map.put("orderAmount", 0);
        map.put("Fee", 0);
        map.put("payAmount", 0);
        return "site/loan/results";
    }
    
    @ResponseBody
    @RequestMapping("/authorizeNotifyURL")
    public void authorizeNotifyURL(LoanModel result) throws Exception
    {
        String message = Common.JSONEncode(result);
        loanService.addOrder(message, "LoanModel", "授权后台返回报文");
        UsersEntity user = this.getUsersEntity();
        if (user == null)
        {
            return;
        }
        //获取双乾参数
        Loan loan = parametersService.getLoan();
        String dataStr = result.getMoneymoremoreId()
                + result.getPlatformMoneymoremore()
                + result.getAuthorizeTypeOpen()
                + result.getAuthorizeTypeClose() + result.getAuthorizeType()
                + result.getRandomTimeStamp() + result.getRemark1()
                + result.getRemark2() + result.getRemark3()
                + result.getResultCode();
        RsaHelper rsa = RsaHelper.getInstance();
        // 签名
        boolean verifySignature = rsa.verifySignature(result.getSignInfo(),
                dataStr,
                loan.getPublickey());
        if (verifySignature)
        {
            int tenderStatus = 0;
            int repaymentStatus = 0;
            int allocationStatus = 0;
            
            String[] status = result.getAuthorizeTypeOpen().split(".");
            for (int i = 0; i < status.length; i++)
            {
                if ("1".equals(status[i]))
                {
                    tenderStatus = 1;
                }
                if ("2".equals(status[i]))
                {
                    repaymentStatus = 1;
                }
                if ("3".equals(status[i]))
                {
                    allocationStatus = 1;
                }
            }
            //更新用户授权状态
            UsersEntity u = userService.getUserById(user.getCustomerId());
            u.setTenderStatus(tenderStatus);
            u.setRepaymentStatus(repaymentStatus);
            u.setAllocationStatus(allocationStatus);
            userService.updateUser(u);
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
            UsersEntity user = this.getUsersEntity();
            if (StringUtils.isEmpty(user.getMoneymoremoreId()))
            {
                map.put("moneyType", "0");
                return map;
            }
            else
            {
                map.put("moneyType", "1");
            }
            AccountEntity account = accountService.getAccountByCustomerId(user.getCustomerId());
            String PlatformId = user.getMoneymoremoreId();
            String platformType = "1";//1.托管账户 2.自有账户
            
            String[] result = postService.balanceQuery(PlatformId, platformType);
            String message = Common.JSONEncode(result);
            loanService.addOrder(message, "String[]", "余额查询对账处理。");
            String[] balance = result[1].split("\\|");
            
            map.put("balance1", balance[0]);
            map.put("balance2", balance[1]);
            map.put("balance3", balance[2]);
            BigDecimal b1 = account.getBalance()
                    .subtract(multiply(new BigDecimal(balance[0])))
                    .abs();
            if (b1.compareTo(new BigDecimal(0)) > 0)
            {
                
                //添加资金明细
                FundDetailEntity deal = new FundDetailEntity();
                deal.setCustomerId(user.getCustomerId());
                deal.setAccountId(account.getAccountId());
                deal.setType(13);
                deal.setCreateTime(new Date());
                deal.setStatus(1);
                deal.setAmount((account.getBalance().subtract(multiply(new BigDecimal(
                        balance[0])))).abs());
                deal.setBalance(account.getBalance());
                deal.setDueAmount(account.getDueAmount());
                deal.setFrozenAmount(account.getFozenAmount());
                deal.setRemark("对账，资金以托管账户金额为准！");
                fundDetailService.addFundDetail(deal);
                account.setBalance(multiply(new BigDecimal(balance[0])));
                account.setFozenAmount(multiply(new BigDecimal(balance[2])));
                accountService.updateAccount(account);
            }
            else
            {
                map.put("moneyType", "999");
            }
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
        Loan loan = parametersService.getLoan();
        String basePath = this.getBasePath(request);
        String SubmitURL = loan.getSubmitURL()
                + "main/loan/toloanauthorize.action";
        String ReturnURL = basePath + "loan/fastPayReturnURL.htm";
        String NotifyURL = loan.getServiceIp()
                + "p2p-website/loan/fastPayNotifyURL.htm";
        
        String privatekey = loan.getPrivatekey();
        String publickey = loan.getPublickey();
        String MoneymoremoreId = user.getMoneymoremoreId();
        String PlatformMoneymoremore = loan.getMoremoreId();
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
    public String saveregister(RegisterModel rg, ModelMap map,
            HttpServletRequest request) throws WebException
    {
        try
        {
            Loan loan = parametersService.getLoan();
            UsersEntity user = getUsersEntity();
            rg.setRegisterType("2");
            rg.setMobile(user.getCellphone());
            rg.setEmail(user.getEmail());
            rg.setRealName(user.getName());
            rg.setIdentificationNo(user.getCardNum());
            rg.setLoanPlatformAccount(user.getCustomerNo());
            rg.setPlatformMoneymoremore(loan.getMoremoreId());
            rg.setReturnURL(getBasePath(request)
                    + "loan/registerbindreturn.htm");
            rg.setNotifyURL(loan.getServiceIp()
                    + "p2p-website/loan/registerbindInform.htm");
            String privatekey = loan.getPrivatekey();
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
    public String registerbindreturn(LoanRegisterBindReturnBean lb,
            ModelMap map, RedirectAttributes ra)
    {
        loanService.addOrder(Common.JSONEncode(lb),
                "LoanRegisterBindReturnBean",
                "开户页面返回Model");
        if ("88".equals(lb.getResultCode()))
        {
            map.put("model", lb);
            UsersEntity user = getUsersEntity();
            user.setAccountNumber(lb.getAccountNumber());
            user.setMoneymoremoreId(lb.getMoneymoremoreId());
            user.setAuthState(lb.getAuthState());
            userService.updateUser(user);
            FundDetailEntity fd = new FundDetailEntity();
            fd.setCustomerId(user.getCustomerId());
            fd.setAmount(new BigDecimal(lb.getAuthFee()));
            fd.setType(0);
            fundDetailService.updateFundDetail(fd);
            
        }
        else
        {
            ra.addAttribute("message",
                    Common.UrlEncoder(lb.getMessage(), "utf-8"));
        }
        return "redirect:/user/memberInfo.htm";
    }
    
    /**
     * 开户成功通知后台操作
     * @return
     */
    @RequestMapping("registerbindInform")
    public @ResponseBody String registerbindInform()
    {
        System.out.println("ok===========================================");
        return "";
    }
    
    /**
     * 转账
     */
    @RequestMapping("transfer")
    public String transfer(ModelMap map, String LoanJsonList,
            HttpServletRequest request) throws WebException
    {
        try
        {
            Loan loan = parametersService.getLoan();
            String privatekey = loan.getPrivatekey();
            /*List<LoanInfoBean> listmlib = new ArrayList<LoanInfoBean>();
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
            LoanJsonList = Common.JSONEncode(listmlib);*/
            
            TransferModel tf = new TransferModel();
            tf.setPlatformMoneymoremore(loan.getMoremoreId());
            tf.setTransferAction("1");
            tf.setAction("1");
            tf.setTransferType("2");
            tf.setReturnURL(getBasePath(request) + "loan/transferReturn.htm");
            tf.setNotifyURL(loan.getServiceIp()
                    + "p2p-website/loan/transferNotify.htm");
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
            e.printStackTrace();
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new WebException("投资转账失败", e);
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
        UsersEntity user = getUsersEntity();
        try
        {
            String message = Common.JSONEncode(lr);
            loanService.addOrder(message,
                    "LoanTransferReturnBean",
                    "转账跳转页面返回报文");
            String json = Common.UrlDecoder(lr.getLoanJsonList(), "utf-8");
            List<Object> list = Common.JSONDecodeList(json, LoanInfoBean.class);
            if (list.size() == 1 && "88".equals(lr.getResultCode()))
            {
                LoanInfoBean loan = (LoanInfoBean) list.get(0);
                InvestOrderEntity order = investOrderService.getInvestOrderEntityByOrderNo(loan.getOrderNo());
                accountService.updateAccountBalance(user.getCustomerId(),
                        multiply(new BigDecimal(loan.getAmount())),
                        order.getOrderId(),
                        loan.getLoanNo());
                
                AccountEntity account = accountService.getAccountByCustomerId(user.getCustomerId());
                map.put("title", "投资成功");
                map.put("orderNo", loan.getOrderNo());
                map.put("orderAmount", loan.getAmount());
                map.put("Fee", "0");
                map.put("payAmount", "0");
                map.put("balance", this.divide(account.getBalance()));
            }
            else
            {
                AccountEntity account = accountService.getAccountByCustomerId(user.getCustomerId());
                map.put("title", "投资失败:" + lr.getMessage());
                map.put("orderNo", "无");
                map.put("orderAmount", "0");
                map.put("Fee", "0");
                map.put("payAmount", "0");
                map.put("balance", this.divide(account.getBalance()));
            }
            
        }
        
        catch (Exception e)
        {
            e.printStackTrace();
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new WebException("投资返回信息失败", e);
        }
        return "site/loan/results";
    }
    
    /**
     * 转账后台通知
     */
    @RequestMapping("transferNotify")
    public @ResponseBody String transferNotify(LoanTransferReturnBean lr)
            throws WebException
    {
        UsersEntity user = getUsersEntity();
        try
        {
            String message = Common.JSONEncode(lr);
            loanService.addOrder(message, "LoanTransferReturnBean", "转账后台返回报文");
            /*String json = Common.UrlDecoder(lr.getLoanJsonList(), "utf-8");
            List<Object> list = Common.JSONDecodeList(json, LoanInfoBean.class);
            if (list.size() == 1 && "88".equals(lr.getResultCode()))
            {
                LoanInfoBean loan = (LoanInfoBean) list.get(0);
                InvestOrderEntity order = investOrderService.getInvestOrderEntityByOrderNo(loan.getOrderNo());
                accountService.updateAccountBalance(user.getCustomerId(),
                        multiply(new BigDecimal(loan.getAmount())),
                        order.getOrderId(),
                        loan.getLoanNo());
                
            }*/
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new WebException("投资返回信息失败", e);
        }
        return "";
    }
    
    /**
     * 资金释放
     */
    @RequestMapping("loanRelease")
    public String loanRelease(ModelMap map, HttpServletRequest request)
            throws WebException
    {
        UsersEntity user = getUsersEntity();
        if (null == user)
        {
            return LOGIN;
        }
        try
        {
            Loan loan = parametersService.getLoan();
            LoanReleaseModel lr = new LoanReleaseModel();
            lr.setAmount("100");
            lr.setMoneymoremoreId(user.getMoneymoremoreId());
            lr.setPlatformMoneymoremore(loan.getMoremoreId());
            lr.setOrderNo("D2015060110048102");
            lr.setReturnURL(getBasePath(request) + "loan/loanReleaseReturn.htm");
            lr.setNotifyURL(loan.getServiceIp()
                    + "p2p-website/loan/loanReleaseNotif.htm");
            String dataStr = lr.getMoneymoremoreId()
                    + lr.getPlatformMoneymoremore() + lr.getOrderNo()
                    + lr.getAmount() + lr.getRandomTimeStamp()
                    + lr.getRemark1() + lr.getRemark2() + lr.getRemark3()
                    + lr.getReturnURL() + lr.getNotifyURL();
            String privatekey = loan.getPrivatekey();
            RsaHelper rsa = RsaHelper.getInstance();
            String SignInfo = rsa.signData(dataStr, privatekey);
            lr.setSignInfo(SignInfo);
            map.put("model", lr);
        }
        catch (Exception e)
        {
            SxjLogger.error(e.getMessage(), e, this.getClass());
            e.printStackTrace();
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
            e.printStackTrace();
            SxjLogger.error(e.getMessage(), e, this.getClass());
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
            e.printStackTrace();
            SxjLogger.error(e.getMessage(), e, this.getClass());
        }
        return "";
    }
    
    /**
     * 审核
     */
    @RequestMapping("loanTransferAudit")
    public String loanTransferAuditModel(ModelMap map,
            HttpServletRequest request) throws WebException
    {
        try
        {
            Loan loan = parametersService.getLoan();
            LoanTransferAuditModel ltsa = new LoanTransferAuditModel();
            ltsa.setPlatformMoneymoremore(loan.getMoremoreId());
            ltsa.setAuditType("1");
            ltsa.setLoanNoList("LN11372141506011010551770858");
            String privatekey = loan.getPrivatekey();
            ltsa.setReturnURL(getBasePath(request)
                    + "loan/loanTransferAuditModelReturn.htm");
            ltsa.setNotifyURL(loan.getServiceIp()
                    + "p2p-website/loan/loanTransferAuditModelNotify.htm");
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
            e.printStackTrace();
            SxjLogger.error(e.getMessage(), e, this.getClass());
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
            e.printStackTrace();
            SxjLogger.error(e.getMessage(), e, this.getClass());
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
            e.printStackTrace();
            SxjLogger.error(e.getMessage(), e, this.getClass());
        }
        return "";
    }
    
    /**
     * 审核后台通知信息
     */
    @RequestMapping("receive")
    public @ResponseBody void receive(LoanModel result) throws WebException
    {
        try
        {
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
            SxjLogger.error(e.getMessage(), e, this.getClass());
        }
    }
}
