package com.herongwang.p2p.service.impl.post;

import org.springframework.stereotype.Service;

import com.herongwang.p2p.model.order.OrderMember;
import com.herongwang.p2p.service.post.IPostService;
import com.sxj.util.exception.ServiceException;
import com.allinpay.ets.client.*;

@Service
public class PostServiceImpl implements IPostService
{

    @Override
    public void Post(String deal) throws ServiceException
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public String getSignMsg(OrderMember orderMember) throws ServiceException
    {
        RequestOrder requestOrder = new RequestOrder();
        if(null!=orderMember.getInputCharset()&&!"".equals(orderMember.getInputCharset())){
            requestOrder.setInputCharset(Integer.parseInt(orderMember.getInputCharset()));
        }
        requestOrder.setPickupUrl(orderMember.getPickupUrl());
        requestOrder.setReceiveUrl(orderMember.getReceiveUrl());
        requestOrder.setVersion(orderMember.getVersion());
        if(null!=orderMember.getLanguage()&&!"".equals(orderMember.getLanguage())){
            requestOrder.setLanguage(Integer.parseInt(orderMember.getLanguage()));
        }
        requestOrder.setSignType(Integer.parseInt(orderMember.getSignType()));
        requestOrder.setPayType(Integer.parseInt(orderMember.getPayType()));
        requestOrder.setIssuerId(orderMember.getIssuerId());
        requestOrder.setMerchantId(orderMember.getMerchantId());
        requestOrder.setPayerName(orderMember.getPayerName());
        requestOrder.setPayerEmail(orderMember.getPayerEmail());
        requestOrder.setPayerTelephone(orderMember.getPayerTelephone());
        requestOrder.setPayerIDCard(orderMember.getPayerIDCard());
        requestOrder.setPid(orderMember.getPid());
        requestOrder.setOrderNo(orderMember.getOrderNo());
        requestOrder.setOrderAmount(Long.parseLong(orderMember.getOrderAmount()));
        requestOrder.setOrderCurrency(orderMember.getOrderCurrency());
        requestOrder.setOrderDatetime(orderMember.getOrderDatetime());
        requestOrder.setOrderExpireDatetime(orderMember.getOrderExpireDatetime());
        requestOrder.setProductName(orderMember.getProductName());
        if(null!=orderMember.getProductPrice()&&!"".equals(orderMember.getProductPrice())){
            requestOrder.setProductPrice(Long.parseLong(orderMember.getProductPrice()));
        }
        if(null!=orderMember.getProductNum()&&!"".equals(orderMember.getProductNum())){
            requestOrder.setProductNum(Integer.parseInt(orderMember.getProductNum()));
        }   
        requestOrder.setProductId(orderMember.getProductId());
        requestOrder.setProductDesc(orderMember.getProductDesc());
        requestOrder.setExt1(orderMember.getExt1());
        requestOrder.setExt2(orderMember.getExt2());
        requestOrder.setExtTL(orderMember.getExtTL());//通联商户拓展业务字段，在v2.2.0版本之后才使用到的，用于开通分账等业务
        requestOrder.setPan(orderMember.getPan());
        requestOrder.setTradeNature(orderMember.getTradeNature());
        requestOrder.setKey(orderMember.getKey()); //key为MD5密钥，密钥是在通联支付网关会员服务网站上设置。

        String strSrcMsg = requestOrder.getSrc(); // 此方法用于debug，测试通过后可注释。
        String strSignMsg = requestOrder.doSign(); // 签名，设为signMsg字段值。
        System.out.println(strSrcMsg+"======"+strSignMsg);
        return strSignMsg;
    }
    
}
