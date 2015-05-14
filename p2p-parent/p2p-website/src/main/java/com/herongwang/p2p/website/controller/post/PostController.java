package com.herongwang.p2p.website.controller.post;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.herongwang.p2p.entity.account.AccountEntity;
import com.herongwang.p2p.entity.funddetail.FundDetailEntity;
import com.herongwang.p2p.entity.orders.OrdersEntity;
import com.herongwang.p2p.entity.tl.TLBillEntity;
import com.herongwang.p2p.entity.users.UsersEntity;
import com.herongwang.p2p.model.order.OrderModel;
import com.herongwang.p2p.model.order.ResultsModel;
import com.herongwang.p2p.service.account.IAccountService;
import com.herongwang.p2p.service.funddetail.IFundDetailService;
import com.herongwang.p2p.service.orders.IOrdersService;
import com.herongwang.p2p.service.parameters.IParametersService;
import com.herongwang.p2p.service.post.IPostService;
import com.herongwang.p2p.service.tl.ITLBillService;
import com.herongwang.p2p.website.controller.BaseController;
import com.sxj.util.exception.WebException;

@Controller
@RequestMapping("/post")
public class PostController extends BaseController
{
    @Autowired
    IParametersService parametersService;
    
    @Autowired
    IOrdersService ordersService;
    
    @Autowired
    IPostService postService;
    
    @Autowired
    ITLBillService tlBillService;
    
    @Autowired
    IFundDetailService fundDetailService;
    
    @Autowired
    IAccountService accountService;
    
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
            ModelMap map1 = postService.Post(order, user);
            
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
        AccountEntity account = accountService.getAccountByCustomerId(user.getCustomerId());
        double amount = Double.valueOf(result.getOrderAmount()) / 100;
        double payAmount = Double.valueOf(result.getPayAmount()) / 100;
        map.put("orderNo", result.getOrderNo());
        map.put("orderAmount", amount);
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
        //支付成功更新支付订单状态
        if (result.getPayResult().equals("1") && tl.getStarus() == 1)
        {
            
            OrdersEntity orders = ordersService.getOrdersEntityByNo(result.getOrderNo());
            orders.setStatus(1);
            orders.setArriveTime(tl.getFinishTime());
            ordersService.updateOrders(orders);
            
            //获取账户信息
            AccountEntity account = accountService.getAccountByCustomerId(orders.getCustomerId());
            //插入资金明细表
            FundDetailEntity deal = new FundDetailEntity();
            deal.setCustomerId(orders.getCustomerId());
            deal.setAccountId(account.getAccountId());
            deal.setOrderId(orders.getOrderId());
            deal.setType(1);
            deal.setAmount(orders.getAmount());
            deal.setBalance(account.getBalance());
            deal.setDueAmount(new BigDecimal(0));
            deal.setFrozenAmount(new BigDecimal(0));
            deal.setCreateTime(new Date());
            deal.setStatus(1);
            fundDetailService.addFundDetail(deal);
            account.setBalance(account.getBalance().add(orders.getAmount()));
            accountService.updateAccount(account);
        }
        
    }
}
