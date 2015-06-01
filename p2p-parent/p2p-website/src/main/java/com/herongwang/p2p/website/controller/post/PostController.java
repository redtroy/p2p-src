package com.herongwang.p2p.website.controller.post;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.herongwang.p2p.entity.account.AccountEntity;
import com.herongwang.p2p.entity.funddetail.FundDetailEntity;
import com.herongwang.p2p.entity.investorder.InvestOrderEntity;
import com.herongwang.p2p.entity.orders.OrdersEntity;
import com.herongwang.p2p.entity.tl.TLBillEntity;
import com.herongwang.p2p.entity.users.UsersEntity;
import com.herongwang.p2p.model.order.OrderModel;
import com.herongwang.p2p.model.order.ResultsModel;
import com.herongwang.p2p.service.account.IAccountService;
import com.herongwang.p2p.service.debt.IDebtService;
import com.herongwang.p2p.service.funddetail.IFundDetailService;
import com.herongwang.p2p.service.investorder.IInvestOrderService;
import com.herongwang.p2p.service.orders.IOrdersService;
import com.herongwang.p2p.service.post.IPostService;
import com.herongwang.p2p.service.users.IUserService;
import com.herongwang.p2p.website.controller.BaseController;
import com.sxj.util.common.EncryptUtil;
import com.sxj.util.common.StringUtils;
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
    
    @RequestMapping("/withdraw")
    public String withdraw(ModelMap map) throws WebException
    {
        UsersEntity user = this.getUsersEntity();
        if (user == null)
        {
            return LOGIN;
        }
        OrdersEntity query = new OrdersEntity();
        AccountEntity account = accountService.getAccountByCustomerId(user.getCustomerId());
        UsersEntity u = userService.getUserById(user.getCustomerId());
        query.setCustomerId(user.getCustomerId());
        query.setOrderType(2);
        query.setStatus(0);
        int num = ordersService.queryOrdersList(query).size();
        if (StringUtils.isEmpty(u.getCardNo()))
        {
            num = 999;
        }
        map.put("type", num);
        map.put("balance", this.divide(account.getBalance()));
        return "site/post/withdraw";
    }
    
    @RequestMapping("/withdrawList")
    public @ResponseBody Map<String, String> withdrawList(HttpSession session,
            OrderModel order) throws WebException
    {
        Map<String, String> map = new HashMap<String, String>();
        try
        {
            BigDecimal m = this.multiply(new BigDecimal(order.getOrderAmount()));
            UsersEntity user = this.getUsersEntity();
            if (StringUtils.isEmpty(order.getSignMsg()))
            {
                map.put("isOK", "提现失败，密码为空！");
                return map;
            }
            String signMsg = EncryptUtil.md5Hex(order.getSignMsg());
            if (!signMsg.equals(user.getPassword()))
            {
                map.put("isOK", "提现失败，输入密码错误！");
                return map;
            }
            int flag = accountService.updateAccountBalance(user.getCustomerId(),
                    m,
                    null);
            if (flag > 0)
            {
                AccountEntity account = accountService.getAccountByCustomerId(user.getCustomerId());
                account.setFozenAmount(account.getFozenAmount().add(m));
                //生成提现订单
                OrdersEntity orders = new OrdersEntity();
                orders.setCustomerId(user.getCustomerId());
                orders.setAmount(m);
                orders.setCreateTime(new Date());
                orders.setStatus(0);
                orders.setOrderType(2);
                ordersService.addOrders(orders);
                FundDetailEntity deal = new FundDetailEntity();
                deal.setCustomerId(user.getCustomerId());
                deal.setAccountId(account.getAccountId());
                deal.setOrderId(orders.getOrderId());
                deal.setType(2);
                deal.setCreateTime(new Date());
                deal.setStatus(1);
                deal.setAmount(m);
                deal.setBalance(account.getBalance());
                deal.setDueAmount(account.getDueAmount());
                deal.setFrozenAmount(m);
                deal.setRemark("申请提现，冻结对应的账户余额。");
                fundDetailService.addFundDetail(deal);//生成资金明细
                
                map.put("isOK", "ok");
            }
            else
            {
                map.put("isOK", "提现失败，余额不足");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            map.put("isOK", "提现失败，余额不足");
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new WebException("生成充值订单失败", e);
        }
        
        return map;
    }
    
    @SuppressWarnings("finally")
    @RequestMapping("/investPost")
    public String investPost(ModelMap map, InvestOrderEntity order)
            throws WebException
    {
        
        UsersEntity user = this.getUsersEntity();
        if (user == null)
        {
            return LOGIN;
        }
        int flag = accountService.updateAccountBalance(user.getCustomerId(),
                order.getAmount(),
                order.getOrderId());
        if (flag == 1)
        {
            try
            {
                //支付成功
                InvestOrderEntity io = new InvestOrderEntity();
                io.setOrderId(order.getOrderId());
                io.setAmount(order.getAmount());
                io.setStatus(1);
                io.setChannel(2);
                io.setCreateTime(new Date());
                io.setArriveTime(new Date());
                investOrderService.finishOrder(io);
                AccountEntity account = accountService.getAccountByCustomerId(user.getCustomerId());
                map.put("title", "投资成功");
                map.put("orderName", "投资");
                map.put("cz", 0);
                map.put("tz", this.divide(order.getAmount()));
                map.put("sxj", 0);
                map.put("zj", 0);
                map.put("yve", this.divide(account.getBalance()));
            }
            catch (Exception e)
            {
                e.printStackTrace();
                map.put("title", "投资失败");
                SxjLogger.error(e.getMessage(), e, this.getClass());
            }
            finally
            {
                return "site/post/resultsin";
            }
        }
        else
        {
            map.put("amount", this.divide(order.getAmount()));
            map.put("orderId", order.getOrderId());
        }
        return "site/post/rechargein";
    }
    
    @RequestMapping("/rechargeInList")
    public String rechargeInList(HttpServletRequest request, ModelMap map,
            InvestOrderEntity order) throws WebException
    {
        BigDecimal m = order.getAmount().multiply(new BigDecimal(100));//投标订单金额
        BigDecimal m1 = order.getTotalFee().multiply(new BigDecimal(100));//充值金额
        
        UsersEntity user = this.getUsersEntity();
        if (user == null)
        {
            return LOGIN;
        }
        AccountEntity account = accountService.getAccountByCustomerId(user.getCustomerId());
        try
        {
            String basePath = this.getBasePath(request);
            ModelMap map1 = postService.Post(m1, user);
            
            String pickupUrl = map1.get("pickupUrl").toString();
            String receiveUrl = map1.get("receiveUrl").toString();
            
            map.put("pickupUrl", basePath + "post/pickupin.htm");
            map.put("receiveUrl", basePath + "post/receivein.htm");
            map.put("serverip", map1.get("serverip"));
            /*map.put("pickupUrl", pickupUrl.substring(0, pickupUrl.length() - 4)
                    + "in.htm");
            map.put("receiveUrl",
                    receiveUrl.substring(0, receiveUrl.length() - 4) + "in.htm");*/
            map.put("merchantId", map1.get("merchantId"));
            map.put("orderNo", map1.get("orderNo"));
            map.put("orderAmount", map1.get("orderAmount"));
            map.put("orderDatetime", map1.get("orderDatetime"));
            map.put("orderExpireDatetime", map1.get("orderExpireDatetime"));
            map.put("productName", map1.get("productName"));
            map.put("payType", map1.get("payType"));
            map.put("signMsg", map1.get("strSignMsg"));
            map.put("ext1", order.getOrderId());
            map.put("ext2", m);
            
            map.put("amount", order.getTotalFee());
            map.put("orderName", "充值");
            map.put("balance", this.divide(account.getBalance()));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            SxjLogger.error(e.getMessage(), e, this.getClass());
        }
        
        return "site/post/rechargein-list";
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
