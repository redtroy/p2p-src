package com.herongwang.p2p.website.controller.post;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.herongwang.p2p.entity.apply.DebtApplicationEntity;
import com.herongwang.p2p.entity.member.MemberEntity;
import com.herongwang.p2p.service.apply.IDebtApplicationService;
import com.sxj.util.exception.WebException;
import com.sxj.util.logger.SxjLogger;

@Controller
@RequestMapping("/post")
public class PoastController
{
    @Autowired
    IDebtApplicationService applyForService;
     
    @RequestMapping("recharge")
    public String recharge(ModelMap map, MemberEntity member)
            throws WebException
    {
        return "site/post/recharge";
    }
    @RequestMapping("recharge")
    public String register(ModelMap map, MemberEntity member)
            throws WebException
    {
        return "site/post/recharge";
    }
    /**
     * 新增融资申请
     * @param session
     * @param apply
     * @return
     * @throws WebException
     */
    @RequestMapping("saveApply")
    public @ResponseBody Map<String, String> saveApply(HttpSession session,
            DebtApplicationEntity apply) throws WebException
    {
        try
        {
            //获取登陆会员ID 
            Map<String, String> map = new HashMap<String, String>();
            apply.setCustomerId("M999999");
            apply.setApplyTime(new Date());//申请时间
            apply.setStatus(0);
            applyForService.addApply(apply);
            map.put("isOK", "ok");
            return map;
        }
        catch (Exception e)
        {
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new WebException("新增融资申请错误", e);
        }
        
    }
}
