package com.herongwang.p2p.website.controller.post;

import java.math.BigDecimal;

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
        UsersEntity user = this.getUsersEntity();
        AccountEntity account = accountService.getAccountByCustomerId(user.getCustomerId());
        BigDecimal b2 = new BigDecimal(100);
        try
        {
            ModelMap map1 = postService.Post(new BigDecimal(
                    order.getOrderAmount()),
                    user);
            
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
            map.put("balance",
                    account.getBalance()
                            .divide(b2, 2, BigDecimal.ROUND_HALF_UP));
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
        BigDecimal b2 = new BigDecimal(100);
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
        map.put("balance",
                account.getBalance().divide(b2, 2, BigDecimal.ROUND_HALF_UP));
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
    
    @RequestMapping("/investPost")
    public String investPost(ModelMap map, InvestOrderEntity order)
            throws WebException
    {
        UsersEntity user = this.getUsersEntity();
        int flag = accountService.updateAccountBalance(user.getCustomerId(),
                order.getAmount());
        if (flag == 1)
        {
            //支付成功
        }
        else
        {
            map.put("amount", order.getAmount());
            map.put("orderId", order.getOrderId());
        }
        return "site/post/rechargeIn";
    }
    
    @RequestMapping("/rechargeInList")
    public String rechargeInList(ModelMap map, InvestOrderEntity order)
            throws WebException
    {
        UsersEntity user = this.getUsersEntity();
        AccountEntity account = accountService.getAccountByCustomerId(user.getCustomerId());
        BigDecimal b2 = new BigDecimal(100);
        try
        {
            ModelMap map1 = postService.Post(order.getAmount(), user);
            
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
            map.put("ext1", order.getOrderId());
            map.put("ext2", order.getAmount());
            
            map.put("amount",
                    order.getAmount().divide(b2, 2, BigDecimal.ROUND_HALF_UP));
            map.put("orderName", "充值");
            map.put("balance",
                    account.getBalance()
                            .divide(b2, 2, BigDecimal.ROUND_HALF_UP));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        return "site/post/recharge-list";
    }
    
    @RequestMapping("/pickupin")
    public String pickupin(ModelMap map, ResultsModel result) throws Exception
    {
        BigDecimal b2 = new BigDecimal(100);
        UsersEntity user = this.getUsersEntity();
        AccountEntity account = accountService.getAccountByCustomerId(user.getCustomerId());
        double orderAmount = Double.valueOf(result.getOrderAmount()) / 100;
        double payAmount = Double.valueOf(result.getPayAmount()) / 100;
        TLBillEntity tl = postService.QueryTLBill(result);
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
        
        map.put("orderNo", result.getOrderNo());
        map.put("orderAmount", orderAmount);
        map.put("payAmount", payAmount);
        map.put("balance",
                account.getBalance().divide(b2, 2, BigDecimal.ROUND_HALF_UP));
        return "site/post/results";
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
        
    }
}
