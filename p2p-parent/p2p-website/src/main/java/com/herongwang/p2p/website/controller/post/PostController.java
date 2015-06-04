package com.herongwang.p2p.website.controller.post;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.herongwang.p2p.entity.account.AccountEntity;
import com.herongwang.p2p.entity.debt.DebtEntity;
import com.herongwang.p2p.entity.investorder.InvestOrderEntity;
import com.herongwang.p2p.entity.orders.OrdersEntity;
import com.herongwang.p2p.entity.tl.TLBillEntity;
import com.herongwang.p2p.entity.users.UsersEntity;
import com.herongwang.p2p.loan.util.Common;
import com.herongwang.p2p.model.loan.LoanInfoBean;
import com.herongwang.p2p.model.order.ResultsModel;
import com.herongwang.p2p.service.account.IAccountService;
import com.herongwang.p2p.service.debt.IDebtService;
import com.herongwang.p2p.service.funddetail.IFundDetailService;
import com.herongwang.p2p.service.investorder.IInvestOrderService;
import com.herongwang.p2p.service.orders.IOrdersService;
import com.herongwang.p2p.service.post.IPostService;
import com.herongwang.p2p.service.users.IUserService;
import com.herongwang.p2p.website.controller.BaseController;
import com.sxj.util.exception.WebException;
import com.sxj.util.logger.SxjLogger;

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
        mlib.setTransferName("投标");
        mlib.setRemark("测试");
        mlib.setSecondaryJsonList("");
        listmlib.add(mlib);
        String LoanJsonList = Common.JSONEncode(listmlib);
        ra.addAttribute("LoanJsonList", LoanJsonList);
        return "redirect:/loan/transfer.htm";
    }
    
    @SuppressWarnings("finally")
    @RequestMapping("/pickupin")
    public String pickupin(ModelMap map, ResultsModel result) throws Exception
    {
        UsersEntity user = this.getUsersEntity();
        if (user == null)
        {
            return LOGIN;
        }
        AccountEntity account = accountService.getAccountByCustomerId(user.getCustomerId());
        TLBillEntity tl = postService.QueryTLBill(result); //测试借款
        try
        {
            
            //获取账户信息
            OrdersEntity orders = ordersService.getOrdersEntityByNo(result.getOrderNo());
            
            String investOrderId = result.getExt1();
            //支付成功更新支付订单状态
            if (result.getPayResult().equals("1") && tl.getStarus() == 1
                    && orders.getStatus() != 1)
            {
                
                orders.setStatus(1);
                orders.setArriveTime(tl.getFinishTime());
                ordersService.updateOrders(orders);
                
                //插入充值资金明细表
                postService.updateAccount(tl.getActualMoney(),
                        account,
                        orders.getOrderId(),
                        1);
                
                //投资资金明细
                //插入资金明细表
                
                postService.updateAccount(new BigDecimal(result.getExt2()),
                        account,
                        investOrderId,
                        2);
                
            }
            
            //支付成功
            InvestOrderEntity io = new InvestOrderEntity();
            io.setOrderId(investOrderId);
            io.setAmount(new BigDecimal(result.getExt2()));
            io.setStatus(tl.getStarus());
            io.setChannel(2);
            io.setCreateTime(new SimpleDateFormat("yyyyMMddHHmmss").parse(result.getOrderDatetime()));
            io.setArriveTime(new Date());
            investOrderService.finishOrder(io);
            
            account = accountService.getAccountByCustomerId(user.getCustomerId());
            map.put("title", "充值并投资成功");
            map.put("orderName", "投资");
            map.put("cz", this.divide(tl.getActualMoney()));
            map.put("tz", this.divide(new BigDecimal(result.getExt2())));
            map.put("sxj",
                    this.divide(tl.getBillMoney().subtract(tl.getActualMoney())));
            map.put("zj", this.divide((tl.getBillMoney())));
            map.put("yve", this.divide(account.getBalance()));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            account = accountService.getAccountByCustomerId(user.getCustomerId());
            if (tl.getStarus() == 1)
            {
                map.put("orderName", "充值成功");
            }
            else
            {
                map.put("orderName", "充值失败");
            }
            map.put("cz", 0);
            map.put("sxj", 0);
            map.put("zj", 0);
            map.put("yve", this.divide(account.getBalance()));
            map.put("title", "投资失败");
            SxjLogger.error(e.getMessage(), e, this.getClass());
        }
        finally
        {
            return "site/post/resultsin";
        }
    }
    
    @ResponseBody
    @RequestMapping("/receivein")
    public void receivein(ModelMap map, ResultsModel result) throws Exception
    {
        String investOrderId = result.getExt1();
        TLBillEntity tl = postService.QueryTLBill(result);
        OrdersEntity orders = ordersService.getOrdersEntityByNo(result.getOrderNo());
        //获取账户信息
        AccountEntity account = accountService.getAccountByCustomerId(orders.getCustomerId());
        //支付成功更新支付订单状态
        if (result.getPayResult().equals("1") && tl.getStarus() == 1
                && orders.getStatus() != 1)
        {
            
            orders.setStatus(1);
            orders.setArriveTime(tl.getFinishTime());
            ordersService.updateOrders(orders);
            
            //插入资金明细表
            postService.updateAccount(tl.getActualMoney(),
                    account,
                    orders.getOrderId(),
                    1);
            //投资资金明细
            //插入资金明细表
            
            postService.updateAccount(new BigDecimal(result.getExt2()),
                    account,
                    investOrderId,
                    2);
        }
        //支付成功
        InvestOrderEntity io = new InvestOrderEntity();
        io.setOrderId(investOrderId);
        io.setAmount(new BigDecimal(result.getExt2()));
        io.setStatus(tl.getStarus());
        io.setChannel(2);
        io.setCreateTime(new SimpleDateFormat("yyyyMMddHHmmss").parse(result.getOrderDatetime()));
        io.setArriveTime(new Date());
        investOrderService.finishOrder(io);
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
