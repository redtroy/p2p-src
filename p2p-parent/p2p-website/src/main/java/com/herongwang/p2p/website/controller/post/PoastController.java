package com.herongwang.p2p.website.controller.post;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.herongwang.p2p.entity.apply.DebtApplicationEntity;
import com.herongwang.p2p.entity.parameters.ParametersEntity;
import com.herongwang.p2p.service.apply.IDebtApplicationService;
import com.herongwang.p2p.service.parameters.IParametersService;
import com.sxj.util.exception.WebException;
import com.sxj.util.logger.SxjLogger;

@Controller
@RequestMapping("/post")
public class PoastController
{
    @Autowired
    IParametersService parametersService;
     
    @RequestMapping("recharge")
    public String recharge(ModelMap map)throws WebException
    {
        return "site/post/recharge";
    }
    @RequestMapping("rechargeList")
    public String rechargeList(HttpSession session,ModelMap map, BigDecimal amount) throws WebException
    {
        //生成充值订单
        
        //返回到页面的参数
        ParametersEntity entity = new ParametersEntity();
        entity.setType("postType");
        List<ParametersEntity> postList = parametersService.queryParameters(entity);
        for(int i = 0;i<postList.size();i++){
            ParametersEntity p = postList.get(i);
            if(p.getValue().equals("serverip")){
                map.put("serverip", p.getText());
            }
            if(p.getValue().equals("pickupUrl")){
                map.put("pickupUrl", p.getText());
            }
            if(p.getValue().equals("receiveUrl")){
                map.put("receiveUrl", p.getText());
            }
            if(p.getValue().equals("merchantId")){
                map.put("merchantId", p.getText());
            }
            if(p.getValue().equals("orderExpireDatetime")){
                map.put("orderExpireDatetime", p.getText());
            }
            if(p.getValue().equals("productName")){
                map.put("productName", p.getText());
            }
            if(p.getValue().equals("payType")){
                map.put("payType", p.getText());
            }
        }
        return "site/post/recharge-list";
    }
}
