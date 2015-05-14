package com.herongwang.p2p.website.controller.post;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.herongwang.p2p.entity.account.AccountEntity;
import com.herongwang.p2p.entity.funddetail.FundDetailEntity;
import com.herongwang.p2p.entity.orders.OrdersEntity;
import com.herongwang.p2p.entity.parameters.ParametersEntity;
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
    public String rechargeList(HttpSession session, ModelMap map,
            OrderModel order) throws WebException
    {
        UsersEntity user = this.getUsersEntity();
        try
        {
            BigDecimal amount = new BigDecimal(order.getOrderAmount());
            BigDecimal b2 = new BigDecimal(100);
            BigDecimal b = amount.multiply(b2);
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            
            //生成充值订单
            OrdersEntity orders = new OrdersEntity();
            orders.setCustomerId(user.getCustomerId());
            orders.setAmount(b);
            orders.setCreateTime(new Date());
            orders.setStatus(0);
            orders.setOrderType(1);
            ordersService.addOrders(orders);
            
            //返回到页面的参数
            ParametersEntity entity = new ParametersEntity();
            entity.setType("postType");
            List<ParametersEntity> postList = parametersService.queryParameters(entity);
            for (int i = 0; i < postList.size(); i++)
            {
                ParametersEntity p = postList.get(i);
                if (p.getValue().equals("serverip"))
                {
                    map.put("serverip", p.getText());
                }
                if (p.getValue().equals("pickupUrl"))
                {
                    map.put("pickupUrl", p.getText());
                }
                if (p.getValue().equals("receiveUrl"))
                {
                    map.put("receiveUrl", p.getText());
                }
                if (p.getValue().equals("merchantId"))
                {
                    map.put("merchantId", p.getText());
                }
                if (p.getValue().equals("orderExpireDatetime"))
                {
                    map.put("orderExpireDatetime", p.getText());
                }
                if (p.getValue().equals("productName"))
                {
                    map.put("productName", p.getText());
                }
                if (p.getValue().equals("payType"))
                {
                    map.put("payType", p.getText());
                }
                if (p.getValue().equals("key"))
                {
                    map.put("key", p.getText());
                }
            }
            
            //生成签名
            OrderModel orderMember = new OrderModel();
            orderMember.setInputCharset("1");
            orderMember.setPickupUrl(String.valueOf(map.get("pickupUrl")));
            orderMember.setReceiveUrl(String.valueOf(map.get("receiveUrl")));
            orderMember.setVersion("v1.0");
            orderMember.setLanguage("1");
            orderMember.setSignType("1");
            orderMember.setMerchantId(String.valueOf(map.get("merchantId")));
            orderMember.setOrderNo(orders.getOrdersNo());
            orderMember.setOrderAmount(b.toString());
            orderMember.setOrderCurrency("0");
            orderMember.setOrderDatetime(sf.format(orders.getCreateTime()));
            orderMember.setOrderExpireDatetime(String.valueOf(map.get("orderExpireDatetime")));
            orderMember.setProductName(String.valueOf(map.get("productName")));
            orderMember.setPayType(String.valueOf(map.get("payType")));
            orderMember.setKey(String.valueOf(map.get("key")));
            String strSignMsg = postService.getSignMsg(orderMember);
            orders.setStrSignMsg(strSignMsg);
            
            //添加签名到订单表
            ordersService.updateOrders(orders);
            map.put("amount", amount);
            map.put("orderAmount", b);
            map.put("orderName", "充值");
            map.put("signMsg", strSignMsg);
            map.put("balance", "100");
            map.put("orderNo", orders.getOrdersNo());
            map.put("createTime", orders.getCreateTime());
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
        double amount = Double.valueOf(result.getOrderAmount()) / 100;
        double payAmount = Double.valueOf(result.getPayAmount()) / 100;
        map.put("orderNo", result.getOrderNo());
        map.put("orderAmount", amount);
        map.put("payAmount", payAmount);
        return "site/post/results";
    }
    
    @ResponseBody
    @RequestMapping("/receive")
    public void receive(ModelMap map, ResultsModel result) throws Exception
    {
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
        String newDate = sf.format(new Date());
        //获取配置信息
        ParametersEntity entity = new ParametersEntity();
        entity.setType("postType");
        List<ParametersEntity> postList = parametersService.queryParameters(entity);
        for (int i = 0; i < postList.size(); i++)
        {
            ParametersEntity p = postList.get(i);
            if (p.getValue().equals("serverip"))
            {
                map.put("serverip", p.getText());
            }
            if (p.getValue().equals("key"))
            {
                map.put("key", p.getText());
            }
        }
        
        //生成查询签名
        OrderModel orderModel = new OrderModel();
        orderModel.setMerchantId(result.getMerchantId());
        orderModel.setOrderNo(result.getOrderNo());
        orderModel.setOrderDatetime(result.getOrderDatetime());
        orderModel.setKey(map.get("key").toString());
        orderModel.setServerip(map.get("serverip").toString());
        orderModel.setSignType(result.getSignType());
        orderModel.setQueryTime(newDate);
        orderModel.setOrderDatetime(result.getOrderDatetime());
        String queryMsg = postService.getQuerySignMsg(orderModel);
        
        //查询账单
        orderModel.setSignMsg(queryMsg);
        TLBillEntity tl = postService.getBIll(orderModel);
        
        //添加账单
        tlBillService.addBill(tl);
        
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
        }
        
    }
}
