package com.herongwang.p2p.service.impl.post;

import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;

import com.allinpay.ets.client.PaymentResult;
import com.allinpay.ets.client.RequestOrder;
import com.allinpay.ets.client.SecurityUtil;
import com.allinpay.ets.client.StringUtil;
import com.herongwang.p2p.entity.account.AccountEntity;
import com.herongwang.p2p.entity.debt.DebtEntity;
import com.herongwang.p2p.entity.funddetail.FundDetailEntity;
import com.herongwang.p2p.entity.investorder.InvestOrderEntity;
import com.herongwang.p2p.entity.orders.OrdersEntity;
import com.herongwang.p2p.entity.parameters.ParametersEntity;
import com.herongwang.p2p.entity.tl.TLBillEntity;
import com.herongwang.p2p.entity.users.UsersEntity;
import com.herongwang.p2p.model.order.OrderModel;
import com.herongwang.p2p.model.order.ResultsModel;
import com.herongwang.p2p.service.account.IAccountService;
import com.herongwang.p2p.service.debt.IDebtService;
import com.herongwang.p2p.service.funddetail.IFundDetailService;
import com.herongwang.p2p.service.investorder.IInvestOrderService;
import com.herongwang.p2p.service.orders.IOrdersService;
import com.herongwang.p2p.service.parameters.IParametersService;
import com.herongwang.p2p.service.post.IPostService;
import com.herongwang.p2p.service.tl.ITLBillService;

@Service
public class PostServiceImpl implements IPostService
{
    @Autowired
    IOrdersService ordersService;
    
    @Autowired
    IParametersService parametersService;
    
    @Autowired
    ITLBillService tlBillService;
    
    @Autowired
    IFundDetailService fundDetailService;
    
    @Autowired
    IAccountService accountService;
    
    @Autowired
    private IDebtService debtService;
    
    @Autowired
    IInvestOrderService investOrderService;
    
    @Override
    public String getSignMsg(OrderModel orderModel) throws Exception
    {
        String strSignMsg = "";
        try
        {
            RequestOrder requestOrder = new RequestOrder();
            if (null != orderModel.getInputCharset()
                    && !"".equals(orderModel.getInputCharset()))
            {
                requestOrder.setInputCharset(Integer.parseInt(orderModel.getInputCharset()));
            }
            requestOrder.setPickupUrl(orderModel.getPickupUrl());
            requestOrder.setReceiveUrl(orderModel.getReceiveUrl());
            requestOrder.setVersion(orderModel.getVersion());
            if (null != orderModel.getLanguage()
                    && !"".equals(orderModel.getLanguage()))
            {
                requestOrder.setLanguage(Integer.parseInt(orderModel.getLanguage()));
            }
            requestOrder.setSignType(Integer.parseInt(orderModel.getSignType()));
            requestOrder.setPayType(Integer.parseInt(orderModel.getPayType()));
            requestOrder.setIssuerId(orderModel.getIssuerId());
            requestOrder.setMerchantId(orderModel.getMerchantId());
            requestOrder.setPayerName(orderModel.getPayerName());
            requestOrder.setPayerEmail(orderModel.getPayerEmail());
            requestOrder.setPayerTelephone(orderModel.getPayerTelephone());
            requestOrder.setPayerIDCard(orderModel.getPayerIDCard());
            requestOrder.setPid(orderModel.getPid());
            requestOrder.setOrderNo(orderModel.getOrderNo());
            requestOrder.setOrderAmount(Long.parseLong(orderModel.getOrderAmount()));
            requestOrder.setOrderCurrency(orderModel.getOrderCurrency());
            requestOrder.setOrderDatetime(orderModel.getOrderDatetime()
                    .replace("-", "")
                    .replace(" ", "")
                    .replace(":", ""));
            requestOrder.setOrderExpireDatetime(orderModel.getOrderExpireDatetime());
            requestOrder.setProductName(orderModel.getProductName());
            if (null != orderModel.getProductPrice()
                    && !"".equals(orderModel.getProductPrice()))
            {
                requestOrder.setProductPrice(Long.parseLong(orderModel.getProductPrice()));
            }
            if (null != orderModel.getProductNum()
                    && !"".equals(orderModel.getProductNum()))
            {
                requestOrder.setProductNum(Integer.parseInt(orderModel.getProductNum()));
            }
            requestOrder.setProductId(orderModel.getProductId());
            requestOrder.setProductDesc(orderModel.getProductDesc());
            requestOrder.setExt1(orderModel.getExt1());
            requestOrder.setExt2(orderModel.getExt2());
            requestOrder.setExtTL(orderModel.getExtTL());//通联商户拓展业务字段，在v2.2.0版本之后才使用到的，用于开通分账等业务
            requestOrder.setPan(orderModel.getPan());
            requestOrder.setTradeNature(orderModel.getTradeNature());
            requestOrder.setKey(orderModel.getKey()); //key为MD5密钥，密钥是在通联支付网关会员服务网站上设置。
            
            String strSrcMsg = requestOrder.getSrc(); // 此方法用于debug，测试通过后可注释。
            strSignMsg = requestOrder.doSign(); // 签名，设为signMsg字段值。
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return strSignMsg;
    }
    
    @Override
    public String getQuerySignMsg(OrderModel orderMember) throws Exception
    {
        StringBuffer bufSignSrc = new StringBuffer();
        StringUtil.appendSignPara(bufSignSrc,
                "merchantId",
                orderMember.getMerchantId());
        StringUtil.appendSignPara(bufSignSrc, "version", "v1.5");
        StringUtil.appendSignPara(bufSignSrc,
                "signType",
                orderMember.getSignType());
        StringUtil.appendSignPara(bufSignSrc,
                "orderNo",
                orderMember.getOrderNo());
        StringUtil.appendSignPara(bufSignSrc,
                "orderDatetime",
                orderMember.getOrderDatetime());
        StringUtil.appendSignPara(bufSignSrc,
                "queryDatetime",
                orderMember.getQueryTime());
        StringUtil.appendLastSignPara(bufSignSrc, "key", orderMember.getKey());
        String signSrc = bufSignSrc.toString();
        String sign = SecurityUtil.MD5Encode(bufSignSrc.toString());
        return sign;
    }
    
    @Override
    @Transactional
    public TLBillEntity getBIll(OrderModel orderMember) throws Exception
    {
        TLBillEntity tl = new TLBillEntity();
        HttpClient httpclient = new HttpClient();
        PostMethod postmethod = new PostMethod(orderMember.getServerip());
        NameValuePair[] date = {
                new NameValuePair("merchantId", orderMember.getMerchantId()),
                new NameValuePair("version", "v1.5"),
                new NameValuePair("signType", orderMember.getSignType()),
                new NameValuePair("orderNo", orderMember.getOrderNo()),
                new NameValuePair("orderDatetime",
                        orderMember.getOrderDatetime()),
                new NameValuePair("queryDatetime", orderMember.getQueryTime()),
                new NameValuePair("signMsg", orderMember.getSignMsg()) };
        
        postmethod.setRequestBody(date);
        int responseCode = httpclient.executeMethod(postmethod);
        
        Map<String, String> results = new HashMap<String, String>();
        String resultMsg = "";
        if (responseCode == HttpURLConnection.HTTP_OK)
        {
            String strResponse = postmethod.getResponseBodyAsString();
            
            // 解析查询返回结果
            strResponse = URLDecoder.decode(strResponse);
            //  Map<String, String> result = new HashMap<String, String>();
            String[] parameters = strResponse.split("&");
            for (int i = 0; i < parameters.length; i++)
            {
                String msg = parameters[i];
                int index = msg.indexOf('=');
                if (index > 0)
                {
                    String name = msg.substring(0, index);
                    String value = msg.substring(index + 1);
                    results.put(name, value);
                }
            }
            
            // 查询结果会以Server方式通知商户(同支付返回)；
            // 若无法取得Server通知结果，可以通过解析查询返回结果，更新订单状态(参考如下).
            if (null != results.get("ERRORCODE"))
            {
                // 未查询到订单
                System.out.println("ERRORCODE=" + results.get("ERRORCODE"));
                System.out.println("ERRORMSG=" + results.get("ERRORMSG"));
                resultMsg = results.get("ERRORMSG");
                
            }
            else
            {
                // 查询到订单
                String payResult = results.get("payResult");
                if (payResult.equals("1"))
                {
                    System.out.println("订单付款成功！");
                    
                    // 支付成功，验证签名
                    PaymentResult paymentResult = new PaymentResult();
                    paymentResult.setMerchantId(results.get("merchantId"));
                    paymentResult.setVersion(results.get("version"));
                    paymentResult.setLanguage(results.get("language"));
                    paymentResult.setSignType(results.get("signType"));
                    paymentResult.setPayType(results.get("payType"));
                    paymentResult.setIssuerId(results.get("issuerId"));
                    paymentResult.setPaymentOrderId(results.get("paymentOrderId"));
                    paymentResult.setOrderNo(results.get("orderNo"));
                    paymentResult.setOrderDatetime(results.get("orderDatetime"));
                    paymentResult.setOrderAmount(results.get("orderAmount"));
                    paymentResult.setPayAmount(results.get("payAmount"));
                    paymentResult.setPayDatetime(results.get("payDatetime"));
                    paymentResult.setExt1(results.get("ext1"));
                    paymentResult.setExt2(results.get("ext2"));
                    paymentResult.setPayResult(results.get("payResult"));
                    paymentResult.setErrorCode(results.get("errorCode"));
                    paymentResult.setReturnDatetime(results.get("returnDatetime"));
                    paymentResult.setKey(orderMember.getKey());
                    paymentResult.setSignMsg(results.get("signMsg"));
                    paymentResult.setCertPath("d:\\cert\\TLCert-test.cer");
                    
                    tl.setMerchantBillNo(results.get("orderNo"));
                    tl.setMerchantNo(results.get("merchantId"));
                    tl.setTlBillNo(results.get("paymentOrderId"));
                    tl.setSubmitTime(new SimpleDateFormat("yyyyMMddHHmmss").parse(results.get("orderDatetime")));
                    tl.setBillMoney(new BigDecimal(results.get("orderAmount")));
                    tl.setFinishTime(new SimpleDateFormat("yyyyMMddHHmmss").parse(results.get("payDatetime")));
                    tl.setActualMoney(new BigDecimal(results.get("payAmount")));
                    tl.setRemark1(results.get("ext1"));
                    tl.setRemark2(results.get("ext2"));
                    tl.setStarus(Integer.valueOf(results.get("payResult")));
                    
                    boolean verifyResult = paymentResult.verify();
                    
                    if (verifyResult)
                    {
                        System.out.println("验签成功！商户更新订单状态。");
                        resultMsg = "订单支付成功，验签成功！商户更新订单状态。";
                    }
                    else
                    {
                        System.out.println("验签失败！");
                        resultMsg = "订单支付成功，验签失败！";
                    }
                    
                }
                else
                {
                    System.out.println("订单尚未付款！");
                    resultMsg = "订单尚未付款！";
                }
            }
            
        }
        return tl;
    }
    
    @Override
    @Transactional
    public String PostWithdraw(String url, boolean isFront) throws Exception
    {
        return null;
    }
    
    @Override
    @Transactional
    public ModelMap Post(BigDecimal amount, UsersEntity user) throws Exception
    {
        ModelMap map = new ModelMap();
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
        
        //生成充值订单
        OrdersEntity orders = new OrdersEntity();
        orders.setCustomerId(user.getCustomerId());
        orders.setAmount(amount);
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
        orderMember.setOrderAmount(amount.toString());
        orderMember.setOrderCurrency("0");
        orderMember.setOrderDatetime(sf.format(orders.getCreateTime()));
        orderMember.setOrderExpireDatetime(String.valueOf(map.get("orderExpireDatetime")));
        orderMember.setProductName(String.valueOf(map.get("productName")));
        orderMember.setPayType(String.valueOf(map.get("payType")));
        orderMember.setKey(String.valueOf(map.get("key")));
        String strSignMsg = this.getSignMsg(orderMember);
        orders.setStrSignMsg(strSignMsg);
        
        map.put("strSignMsg", strSignMsg);
        map.put("orderNo", orders.getOrdersNo());
        map.put("orderAmount", orders.getAmount());
        map.put("orderDatetime", orderMember.getOrderDatetime());
        
        //添加签名到订单表
        ordersService.updateOrders(orders);
        return map;
    }
    
    @Override
    @Transactional
    public TLBillEntity QueryTLBill(ResultsModel result) throws Exception
    {
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
        String newDate = sf.format(new Date());
        //获取配置信息
        ModelMap map = new ModelMap();
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
        String queryMsg = this.getQuerySignMsg(orderModel);
        
        orderModel.setSignMsg(queryMsg);
        //查询远程通联账单
        TLBillEntity tl = new TLBillEntity();
        tl.setMerchantNo(result.getMerchantId());
        tl.setTlBillNo("TL" + result.getExt1());
        tl.setMerchantBillNo(result.getExt1());
        tl.setSubmitTime(new Date());
        tl.setBillMoney(new BigDecimal(result.getOrderAmount()));
        tl.setFinishTime(new Date());
        tl.setActualMoney(new BigDecimal(result.getOrderAmount()));
        tl.setStarus(1);
        //        TLBillEntity tl = this.getBIll(orderModel);
        //查询本地通联账单
        TLBillEntity tl2 = tlBillService.getTLBillEntityByNo(tl.getMerchantBillNo());
        if (null != tl2)
        {
            //添加账单
            tlBillService.addBill(tl);
        }
        return tl;
    }
    
    @Override
    @Transactional
    public void updateAccount(BigDecimal amount, AccountEntity entity,
            String orderId, int incomeStatus) throws Exception
    {
        BigDecimal zc = entity.getBalance().subtract(amount);
        BigDecimal sr = entity.getBalance().add(amount);
        FundDetailEntity deal = new FundDetailEntity();
        deal.setCustomerId(entity.getCustomerId());
        deal.setAccountId(entity.getAccountId());
        deal.setOrderId(orderId);
        deal.setType(incomeStatus);
        deal.setCreateTime(new Date());
        deal.setStatus(1);
        if (incomeStatus == 1 || incomeStatus == 3)
        {
            entity.setBalance(sr);
            deal.setAmount(amount);
            deal.setBalance(entity.getBalance());
            deal.setDueAmount(new BigDecimal(0));
            deal.setFrozenAmount(new BigDecimal(0));
            accountService.updateAccount(entity);
        }
        else if (incomeStatus == 2 || incomeStatus == 4)
        {
            entity.setBalance(zc);
            deal.setAmount(amount);
            deal.setBalance(entity.getBalance());
            deal.setDueAmount(new BigDecimal(0));
            deal.setFrozenAmount(amount);
            
            accountService.updateAccountBalance(entity.getCustomerId(),
                    amount,
                    orderId);
        }
        else
        {
            return;
        }
        if (incomeStatus == 2)
        {
            InvestOrderEntity order = investOrderService.getInvestOrderEntity(orderId);
            DebtEntity debt = debtService.getDebtEntity(order.getDebtId());
            deal.setRemark("投资" + debt.getTitle() + "成功！");
            
        }
        else if (incomeStatus == 1)
        {
            deal.setRemark("充值" + amount.divide(new BigDecimal(100)) + "元成功！");
        }
        fundDetailService.addFundDetail(deal);
        
    }
}
