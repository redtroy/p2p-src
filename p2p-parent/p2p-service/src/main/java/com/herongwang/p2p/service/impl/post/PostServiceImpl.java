package com.herongwang.p2p.service.impl.post;

import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;

import com.allinpay.ets.client.PaymentResult;
import com.allinpay.ets.client.RequestOrder;
import com.allinpay.ets.client.SecurityUtil;
import com.allinpay.ets.client.StringUtil;
import com.herongwang.p2p.dao.financing.IFinancingOrdersDao;
import com.herongwang.p2p.dao.users.IUsersDao;
import com.herongwang.p2p.entity.account.AccountEntity;
import com.herongwang.p2p.entity.funddetail.FundDetailEntity;
import com.herongwang.p2p.entity.orders.OrdersEntity;
import com.herongwang.p2p.entity.parameters.ParametersEntity;
import com.herongwang.p2p.entity.profitlist.ProfitListEntity;
import com.herongwang.p2p.entity.tl.TLBillEntity;
import com.herongwang.p2p.entity.users.UsersEntity;
import com.herongwang.p2p.loan.util.Common;
import com.herongwang.p2p.loan.util.HttpClientUtil;
import com.herongwang.p2p.loan.util.RsaHelper;
import com.herongwang.p2p.model.loan.LoanInfoSecondaryBean;
import com.herongwang.p2p.model.loan.LoanOrderQueryBean;
import com.herongwang.p2p.model.loan.LoanRechargeOrderQueryBean;
import com.herongwang.p2p.model.loan.LoanRegisterBindReturnBean;
import com.herongwang.p2p.model.loan.LoanReturnInfoBean;
import com.herongwang.p2p.model.loan.LoanTransferReturnBean;
import com.herongwang.p2p.model.loan.LoanWithdrawsOrderQueryBean;
import com.herongwang.p2p.model.loan.transferauditreturnBean;
import com.herongwang.p2p.model.order.OrderModel;
import com.herongwang.p2p.model.order.ResultsModel;
import com.herongwang.p2p.model.post.Loan;
import com.herongwang.p2p.model.post.LoanModel;
import com.herongwang.p2p.model.post.LoanReleaseModel;
import com.herongwang.p2p.model.post.LoanTransferAuditModel;
import com.herongwang.p2p.model.post.RegisterModel;
import com.herongwang.p2p.model.post.TransferModel;
import com.herongwang.p2p.service.account.IAccountService;
import com.herongwang.p2p.service.debt.IDebtService;
import com.herongwang.p2p.service.funddetail.IFundDetailService;
import com.herongwang.p2p.service.investorder.IInvestOrderService;
import com.herongwang.p2p.service.loan.ILoanService;
import com.herongwang.p2p.service.orders.IOrdersService;
import com.herongwang.p2p.service.parameters.IParametersService;
import com.herongwang.p2p.service.post.IPostService;
import com.herongwang.p2p.service.profit.IProfitService;
import com.herongwang.p2p.service.tl.ITLBillService;
import com.sxj.util.exception.ServiceException;
import com.sxj.util.logger.SxjLogger;

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
    
    @Autowired
    private IFinancingOrdersDao financingOrder;
    
    @Autowired
    private IUsersDao userDao;
    
    @Autowired
    private IProfitService proftService;
    
    @Autowired
    private ILoanService loanService;
    
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
            SxjLogger.error(e.getMessage(), e, this.getClass());
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
        if (incomeStatus == 1)//充值
        {
            entity.setBalance(sr);
            deal.setAmount(amount);
            deal.setBalance(entity.getBalance());
            deal.setDueAmount(entity.getDueAmount());
            deal.setFrozenAmount(entity.getFozenAmount());
            accountService.updateAccount(entity);
            deal.setStatus(1);
            deal.setRemark("充值" + amount.divide(new BigDecimal(100)) + "元成功！");
            fundDetailService.addFundDetail(deal);
        }
        else if (incomeStatus == 2)//提现
        {
            entity.setBalance(zc);
            deal.setAmount(amount);
            deal.setBalance(entity.getBalance());
            deal.setDueAmount(new BigDecimal(0));
            deal.setFrozenAmount(amount);
            
            accountService.updateAccountBalance(entity.getCustomerId(),
                    amount,
                    orderId,
                    "");
        }
        else
        {
            return;
        }
    }
    
    @Override
    public String register(RegisterModel rg)
    {
        try
        {
            Loan loan = parametersService.getLoan();
            String SubmitURL = loan.getSubmitURL()
                    + "loan/toloanregisterbind.action";
            
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
            
            if (rg.getRegisterType().equals("1"))
            {
                Map<String, String> req = new TreeMap<String, String>();
                req.put("RegisterType", rg.getRegisterType());
                req.put("AccountType", rg.getAccountType());
                req.put("Mobile", rg.getMobile());
                req.put("Email", rg.getEmail());
                req.put("RealName", rg.getRealName());
                req.put("IdentificationNo", rg.getIdentificationNo());
                req.put("Image1", rg.getImage1());
                req.put("Image2", rg.getImage2());
                req.put("LoanPlatformAccount", rg.getLoanPlatformAccount());
                req.put("PlatformMoneymoremore", rg.getPlatformMoneymoremore());
                req.put("RandomTimeStamp", rg.getRandomTimeStamp());
                req.put("Remark1", rg.getRemark1());
                req.put("Remark2", rg.getRemark2());
                req.put("Remark3", rg.getRemark3());
                req.put("ReturnURL", rg.getReturnURL());
                req.put("NotifyURL", rg.getNotifyURL());
                req.put("SignInfo", SignInfo);
                
                String[] resultarr = HttpClientUtil.doPostQueryCmd(SubmitURL,
                        req);
                System.out.println(resultarr[1]);
                
                if (StringUtils.isNotBlank(resultarr[1])
                        && resultarr[1].startsWith("{"))
                {
                    LoanRegisterBindReturnBean lrbrb = (LoanRegisterBindReturnBean) Common.JSONDecode(resultarr[1],
                            LoanRegisterBindReturnBean.class);
                    if (lrbrb != null)
                    {
                        String publickey = loan.getPublickey();
                        
                        dataStr = lrbrb.getAccountType()
                                + lrbrb.getAccountNumber() + lrbrb.getMobile()
                                + lrbrb.getEmail() + lrbrb.getRealName()
                                + lrbrb.getIdentificationNo()
                                + lrbrb.getLoanPlatformAccount()
                                + lrbrb.getMoneymoremoreId()
                                + lrbrb.getPlatformMoneymoremore()
                                + lrbrb.getRandomTimeStamp()
                                + lrbrb.getRemark1() + lrbrb.getRemark2()
                                + lrbrb.getRemark3() + lrbrb.getResultCode();
                        // System.out.println(dataStr);
                        // 签名
                        boolean verifySignature = rsa.verifySignature(lrbrb.getSignInfo(),
                                dataStr,
                                publickey);
                        System.out.println(verifySignature);
                    }
                }
                return null;
            }
        }
        catch (Exception e)
        {
            SxjLogger.error(e.getMessage(), e, this.getClass());
            e.printStackTrace();
        }
        return null;
    }
    
    @Override
    public String transfer(TransferModel tf)
    {
        try
        {
            Loan loan = parametersService.getLoan();
            String SubmitURL = loan.getSubmitURL() + "loan/loan.action";
            
            String dataStr = "";
            // 签名
            
            RsaHelper rsa = RsaHelper.getInstance();
            
            String SignInfo = tf.getSignInfo();
            
            if (tf.getAction().equals("2"))
            {
                Map<String, String> req = new TreeMap<String, String>();
                req.put("LoanJsonList", tf.getLoanJsonList());
                req.put("PlatformMoneymoremore", tf.getPlatformMoneymoremore());
                req.put("TransferAction", tf.getTransferAction());
                req.put("Action", tf.getAction());
                req.put("TransferType", tf.getTransferType());
                req.put("NeedAudit", tf.getNeedAudit());
                req.put("RandomTimeStamp", tf.getRandomTimeStamp());
                req.put("Remark1", tf.getRemark1());
                req.put("Remark2", tf.getRemark2());
                req.put("Remark3", tf.getRemark3());
                req.put("ReturnURL", tf.getReturnURL());
                req.put("NotifyURL", tf.getNotifyURL());
                req.put("SignInfo", SignInfo);
                
                String[] resultarr = HttpClientUtil.doPostQueryCmd(SubmitURL,
                        req);
                loanService.addOrder(Common.JSONEncode(resultarr),
                        "LoanTransferReturnBean",
                        "转账页面返回Model");
                if (StringUtils.isNotBlank(resultarr[1])
                        && (resultarr[1].startsWith("[") || resultarr[1].startsWith("{")))
                {
                    // 转账
                    List<Object> loanobjectlist = Common.JSONDecodeList(resultarr[1],
                            LoanTransferReturnBean.class);
                    if (loanobjectlist != null && loanobjectlist.size() > 0)
                    {
                        for (int i = 0; i < loanobjectlist.size(); i++)
                        {
                            if (loanobjectlist.get(i) instanceof LoanTransferReturnBean)
                            {
                                LoanTransferReturnBean ltrb = (LoanTransferReturnBean) loanobjectlist.get(i);
                                ltrb.setLoanJsonList(Common.UrlDecoder(ltrb.getLoanJsonList(),
                                        "utf-8"));
                                
                                String publickey = loan.getPublickey();
                                if (!"88".equals(ltrb.getResultCode()))
                                {
                                    return "false";
                                }
                                dataStr = ltrb.getLoanJsonList()
                                        + ltrb.getPlatformMoneymoremore()
                                        + ltrb.getAction()
                                        + ltrb.getRandomTimeStamp()
                                        + ltrb.getRemark1() + ltrb.getRemark2()
                                        + ltrb.getRemark3()
                                        + ltrb.getResultCode();
                                
                                // 签名
                                boolean verifySignature = rsa.verifySignature(ltrb.getSignInfo(),
                                        dataStr,
                                        publickey);
                                System.out.println(verifySignature);
                                
                                if (verifySignature)
                                {
                                    // 转账列表
                                    if (StringUtils.isNotBlank(ltrb.getLoanJsonList()))
                                    {
                                        List<Object> loaninfolist = Common.JSONDecodeList(ltrb.getLoanJsonList(),
                                                LoanReturnInfoBean.class);
                                        if (loaninfolist != null
                                                && loaninfolist.size() > 0)
                                        {
                                            for (int j = 0; j < loaninfolist.size(); j++)
                                            {
                                                if (loaninfolist.get(j) instanceof LoanReturnInfoBean)
                                                {
                                                    LoanReturnInfoBean lrib = (LoanReturnInfoBean) loaninfolist.get(j);
                                                    ProfitListEntity entity = new ProfitListEntity();
                                                    entity.setProfitId(lrib.getOrderNo());
                                                    entity.setLoanNo(lrib.getLoanNo());
                                                    proftService.update(entity);
                                                    // 二次分配列表
                                                    if (StringUtils.isNotBlank(lrib.getSecondaryJsonList()))
                                                    {
                                                        List<Object> loansecondarylist = Common.JSONDecodeList(lrib.getSecondaryJsonList(),
                                                                LoanInfoSecondaryBean.class);
                                                        if (loansecondarylist != null
                                                                && loansecondarylist.size() > 0)
                                                        {
                                                            for (int k = 0; k < loansecondarylist.size(); k++)
                                                            {
                                                                if (loansecondarylist.get(k) instanceof LoanInfoSecondaryBean)
                                                                {
                                                                    LoanInfoSecondaryBean lisb = (LoanInfoSecondaryBean) loansecondarylist.get(k);
                                                                    System.out.println(lisb);
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    return "ok";
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            SxjLogger.error(e.getMessage(), e, this.getClass());
        }
        return "false";
    }
    
    @Override
    public String loanRelease(LoanReleaseModel lr)
    {
        return null;
    }
    
    @Override
    public List<LoanOrderQueryBean> orderQuery(LoanModel loan, String privatekey)
            throws Exception
    {
        Loan l = parametersService.getLoan();
        List<LoanOrderQueryBean> list = new ArrayList<LoanOrderQueryBean>();
        String SubmitURL = l.getSubmitURL() + "loan/loanorderquery.action";
        
        String PlatformMoneymoremore = loan.getPlatformMoneymoremore();
        String Action = "";
        String LoanNo = null == loan.getLoanNo() ? "" : loan.getLoanNo();
        String OrderNo = null == loan.getOrderNo() ? "" : loan.getOrderNo();
        String BatchNo = null == loan.getBatchNo() ? "" : loan.getBatchNo();
        String BeginTime = null == loan.getBeginTime() ? ""
                : loan.getBeginTime();
        String EndTime = null == loan.getEndTime() ? "" : loan.getEndTime();
        String dataStr = PlatformMoneymoremore + Action + LoanNo + OrderNo
                + BatchNo + BeginTime + EndTime;
        RsaHelper rsa = RsaHelper.getInstance();
        String SignInfo = rsa.signData(dataStr, privatekey);
        
        Map<String, String> req = new TreeMap<String, String>();
        req.put("PlatformMoneymoremore", PlatformMoneymoremore);
        req.put("Action", Action);
        req.put("LoanNo", LoanNo);
        req.put("OrderNo", OrderNo);
        req.put("BatchNo", BatchNo);
        req.put("BeginTime", BeginTime);
        req.put("EndTime", EndTime);
        req.put("SignInfo", SignInfo);
        
        String[] resultarr = HttpClientUtil.doPostQueryCmd(SubmitURL, req);
        if (StringUtils.isNotBlank(resultarr[1])
                && (resultarr[1].startsWith("[") || resultarr[1].startsWith("{")))
        {
            
            list = new ArrayList<LoanOrderQueryBean>();
            // 转账
            List<Object> loanobjectlist = Common.JSONDecodeList(resultarr[1],
                    LoanOrderQueryBean.class);
            if (loanobjectlist != null && loanobjectlist.size() > 0)
            {
                for (int i = 0; i < loanobjectlist.size(); i++)
                {
                    if (loanobjectlist.get(i) instanceof LoanOrderQueryBean)
                    {
                        LoanOrderQueryBean loqb = (LoanOrderQueryBean) loanobjectlist.get(i);
                        list.add(loqb);
                    }
                }
            }
        }
        return list;
    }
    
    @Override
    public List<LoanRechargeOrderQueryBean> rechargeOrderQuery(LoanModel loan,
            String submitURLPrefix) throws Exception
    {
        Loan l = parametersService.getLoan();
        List<LoanRechargeOrderQueryBean> list = new ArrayList<LoanRechargeOrderQueryBean>();
        String SubmitURL = l.getSubmitURL() + "loan/loanorderquery.action";
        
        String privatekey = l.getPrivatekey();
        String PlatformMoneymoremore = loan.getPlatformMoneymoremore();
        String Action = "1";
        String LoanNo = null == loan.getLoanNo() ? "" : loan.getLoanNo();
        String OrderNo = null == loan.getOrderNo() ? "" : loan.getOrderNo();
        String BatchNo = null == loan.getBatchNo() ? "" : loan.getBatchNo();
        String BeginTime = null == loan.getBeginTime() ? ""
                : loan.getBeginTime();
        String EndTime = null == loan.getEndTime() ? "" : loan.getEndTime();
        String dataStr = PlatformMoneymoremore + Action + LoanNo + OrderNo
                + BatchNo + BeginTime + EndTime;
        RsaHelper rsa = RsaHelper.getInstance();
        String SignInfo = rsa.signData(dataStr, privatekey);
        
        Map<String, String> req = new TreeMap<String, String>();
        req.put("PlatformMoneymoremore", PlatformMoneymoremore);
        req.put("Action", Action);
        req.put("LoanNo", LoanNo);
        req.put("OrderNo", OrderNo);
        req.put("BatchNo", BatchNo);
        req.put("BeginTime", BeginTime);
        req.put("EndTime", EndTime);
        req.put("SignInfo", SignInfo);
        
        String[] resultarr = HttpClientUtil.doPostQueryCmd(SubmitURL, req);
        if (StringUtils.isNotBlank(resultarr[1])
                && (resultarr[1].startsWith("[") || resultarr[1].startsWith("{")))
        {
            // 充值
            List<Object> loanobjectlist = Common.JSONDecodeList(resultarr[1],
                    LoanRechargeOrderQueryBean.class);
            if (loanobjectlist != null && loanobjectlist.size() > 0)
            {
                for (int i = 0; i < loanobjectlist.size(); i++)
                {
                    if (loanobjectlist.get(i) instanceof LoanRechargeOrderQueryBean)
                    {
                        LoanRechargeOrderQueryBean lroqb = (LoanRechargeOrderQueryBean) loanobjectlist.get(i);
                        list.add(lroqb);
                    }
                }
            }
            
        }
        return list;
    }
    
    @Override
    public List<LoanWithdrawsOrderQueryBean> withdrawsOrderQuery(
            LoanModel loan, String submitURLPrefix) throws Exception
    {
        List<LoanWithdrawsOrderQueryBean> list = new ArrayList<LoanWithdrawsOrderQueryBean>();
        Loan l = parametersService.getLoan();
        String SubmitURL = l.getSubmitURL() + "loan/loanorderquery.action";
        
        String privatekey = l.getPrivatekey();
        String PlatformMoneymoremore = loan.getPlatformMoneymoremore();
        String Action = "2";
        String LoanNo = null == loan.getLoanNo() ? "" : loan.getLoanNo();
        String OrderNo = null == loan.getOrderNo() ? "" : loan.getOrderNo();
        String BatchNo = null == loan.getBatchNo() ? "" : loan.getBatchNo();
        String BeginTime = null == loan.getBeginTime() ? ""
                : loan.getBeginTime();
        String EndTime = null == loan.getEndTime() ? "" : loan.getEndTime();
        String dataStr = PlatformMoneymoremore + Action + LoanNo + OrderNo
                + BatchNo + BeginTime + EndTime;
        RsaHelper rsa = RsaHelper.getInstance();
        String SignInfo = rsa.signData(dataStr, privatekey);
        
        Map<String, String> req = new TreeMap<String, String>();
        req.put("PlatformMoneymoremore", PlatformMoneymoremore);
        req.put("Action", Action);
        req.put("LoanNo", LoanNo);
        req.put("OrderNo", OrderNo);
        req.put("BatchNo", BatchNo);
        req.put("BeginTime", BeginTime);
        req.put("EndTime", EndTime);
        req.put("SignInfo", SignInfo);
        
        String[] resultarr = HttpClientUtil.doPostQueryCmd(SubmitURL, req);
        if (StringUtils.isNotBlank(resultarr[1])
                && (resultarr[1].startsWith("[") || resultarr[1].startsWith("{")))
        {
            
            // 提现
            List<Object> loanobjectlist = Common.JSONDecodeList(resultarr[1],
                    LoanWithdrawsOrderQueryBean.class);
            if (loanobjectlist != null && loanobjectlist.size() > 0)
            {
                for (int i = 0; i < loanobjectlist.size(); i++)
                {
                    if (loanobjectlist.get(i) instanceof LoanWithdrawsOrderQueryBean)
                    {
                        LoanWithdrawsOrderQueryBean lwoqb = (LoanWithdrawsOrderQueryBean) loanobjectlist.get(i);
                        list.add(lwoqb);
                    }
                }
            }
            
        }
        return list;
    }
    
    @Override
    public String[] balanceQuery(String PlatformId, String platformType)
            throws Exception
    {
        String[] resultarr = null;
        Loan l = parametersService.getLoan();
        String SubmitURL = l.getSubmitURL() + "loan/balancequery.action";
        String PlatformMoneymoremore = l.getMoremoreId();
        String privatekey = l.getPrivatekey();
        
        String dataStr = PlatformId + platformType + PlatformMoneymoremore;
        // 签名
        RsaHelper rsa = RsaHelper.getInstance();
        String SignInfo = rsa.signData(dataStr, privatekey);
        
        Map<String, String> req = new TreeMap<String, String>();
        req.put("PlatformId", PlatformId);
        req.put("PlatformType", platformType);
        req.put("PlatformMoneymoremore", PlatformMoneymoremore);
        req.put("SignInfo", SignInfo);
        
        resultarr = HttpClientUtil.doPostQueryCmd(SubmitURL, req);
        return resultarr;
    }
    
    @Override
    public String audit(LoanTransferAuditModel ltsa) throws ServiceException
    {
        try
        {
            Loan l = parametersService.getLoan();
            Map<String, String> req = new TreeMap<String, String>();
            req.put("LoanNoList", ltsa.getLoanNoList());
            req.put("PlatformMoneymoremore", ltsa.getPlatformMoneymoremore());
            req.put("AuditType", ltsa.getAuditType());
            req.put("ReturnURL", ltsa.getReturnURL());
            req.put("NotifyURL", ltsa.getNotifyURL());
            req.put("SignInfo", ltsa.getSignInfo());
            req.put("Remark3", ltsa.getRemark3());
            String[] resultarr = HttpClientUtil.doPostQueryCmd(l.getSubmitURL()
                    + "loan/toloantransferaudit.action", req);
            loanService.addOrder(Common.JSONEncode(resultarr),
                    "transferauditreturnBean",
                    "审核页面返回Model");
            if (StringUtils.isNotBlank(resultarr[1])
                    && (resultarr[1].startsWith("[") || resultarr[1].startsWith("{")))
            {
                
                transferauditreturnBean tfb = (transferauditreturnBean) Common.JSONDecode(resultarr[1],
                        transferauditreturnBean.class);
                if ("88".equals(tfb.getResultCode()))
                {
                    return "ok";
                }
                
            }
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
            SxjLogger.error(e.getMessage(), e, this.getClass());
            e.printStackTrace();
        }
        return "false";
    }
}
