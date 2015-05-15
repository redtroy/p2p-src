package com.herongwang.p2p.website.controller.post;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.herongwang.p2p.entity.account.AccountEntity;
import com.herongwang.p2p.entity.investorder.InvestOrderEntity;
import com.herongwang.p2p.entity.orders.OrdersEntity;
import com.herongwang.p2p.entity.tl.TLBillEntity;
import com.herongwang.p2p.entity.users.UsersEntity;
import com.herongwang.p2p.model.order.OrderModel;
import com.herongwang.p2p.model.order.ResultsModel;
import com.herongwang.p2p.service.account.IAccountService;
import com.herongwang.p2p.service.investorder.IInvestOrderService;
import com.herongwang.p2p.service.orders.IOrdersService;
import com.herongwang.p2p.service.post.IPostService;
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
    
    @RequestMapping("/recharge")
    public String recharge(ModelMap map) throws WebException
    {
        return "site/post/recharge";
    }
    
    @RequestMapping("/rechargeList")
    public String rechargeList(ModelMap map, OrderModel order)
            throws WebException
    {
        BigDecimal m = this.multiply(new BigDecimal(order.getOrderAmount()));
        UsersEntity user = this.getUsersEntity();
        AccountEntity account = accountService.getAccountByCustomerId(user.getCustomerId());
        try
        {
            ModelMap map1 = postService.Post(m, user);
            
            map.put("serverip", map1.get("serverip"));
            map.put("pickupUrl", map1.get("pickupUrl"));
            map.put("receiveUrl", map1.get("receiveUrl"));
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
        }
        
        return "site/post/recharge-list";
    }
    
    @RequestMapping("/pickup")
    public String pickup(ModelMap map, ResultsModel result) throws Exception
    {
        
        UsersEntity user = this.getUsersEntity();
        //获取账户信息
        AccountEntity account = accountService.getAccountByCustomerId(user.getCustomerId());
        
        double orderAmount = Double.valueOf(result.getOrderAmount()) / 100;
        double payAmount = Double.valueOf(result.getPayAmount()) / 100;
        TLBillEntity tl = postService.QueryTLBill(result);
        
        OrdersEntity orders = ordersService.getOrdersEntityByNo(result.getOrderNo());
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
        }
        map.put("orderNo", result.getOrderNo());
        map.put("orderAmount", orderAmount);
        map.put("payAmount", payAmount);
        map.put("balance", this.divide(account.getBalance()));
        return "site/post/results";
    }
    
    @ResponseBody
    @RequestMapping("/receive")
    public void receive(ModelMap map, ResultsModel result) throws Exception
    {
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
        }
        
    }
    
    @SuppressWarnings("finally")
    @RequestMapping("/investPost")
    public String investPost(ModelMap map, InvestOrderEntity order)
            throws WebException
    {
        
        UsersEntity user = this.getUsersEntity();
        int flag = accountService.updateAccountBalance(user.getCustomerId(),
                order.getAmount());
        if (flag == 1)
        {
            
            AccountEntity account = accountService.getAccountByCustomerId(user.getCustomerId());
            //支付成功
            try
            {
                InvestOrderEntity io = new InvestOrderEntity();
                io.setOrderId(order.getOrderId());
                io.setAmount(order.getAmount());
                io.setStatus(flag);
                io.setChannel(1);
                io.setCreateTime(new Date());
                io.setArriveTime(new Date());
                investOrderService.finishOrder(io);
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
            }
            finally
            {
                return "site/post/resultsIn";
            }
        }
        else
        {
            map.put("amount", this.divide(order.getAmount()));
            map.put("orderId", order.getOrderId());
        }
        return "site/post/rechargeIn";
    }
    
    @RequestMapping("/rechargeInList")
    public String rechargeInList(ModelMap map, InvestOrderEntity order)
            throws WebException
    {
        BigDecimal m = order.getAmount().multiply(new BigDecimal(100));//投标订单金额
        BigDecimal m1 = order.getTotalFee().multiply(new BigDecimal(100));//充值金额
        
        UsersEntity user = this.getUsersEntity();
        AccountEntity account = accountService.getAccountByCustomerId(user.getCustomerId());
        try
        {
            ModelMap map1 = postService.Post(m1, user);
            
            String pickupUrl = map1.get("pickupUrl").toString();
            String receiveUrl = map1.get("receiveUrl").toString();
            map.put("serverip", map1.get("serverip"));
            map.put("pickupUrl", pickupUrl.substring(0, pickupUrl.length() - 4)
                    + "in.htm");
            map.put("receiveUrl",
                    receiveUrl.substring(0, receiveUrl.length() - 4) + "in.htm");
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
        }
        
        return "site/post/rechargeIn-list";
    }
    
    @SuppressWarnings("finally")
    @RequestMapping("/pickupin")
    public String pickupin(ModelMap map, ResultsModel result) throws Exception
    {
        UsersEntity user = this.getUsersEntity();
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
            map.put("orderName", "充值失败");
            map.put("cz", 0);
            map.put("sxj", 0);
            map.put("zj", 0);
            map.put("yve", this.divide(account.getBalance()));
            if (tl.getStarus() == 1)
            {
                map.put("title", "充值成功但投资失败");
            }
            else
            {
                map.put("title", "充值并投资失败");
            }
        }
        finally
        {
            return "site/post/resultsIn";
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
        BigDecimal b2 = new BigDecimal(100);
        return m.multiply(b2);
    }
}
