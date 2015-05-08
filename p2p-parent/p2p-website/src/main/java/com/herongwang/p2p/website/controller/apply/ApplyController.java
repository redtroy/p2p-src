package com.herongwang.p2p.website.controller.apply;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.herongwang.p2p.entity.apply.ApplyForEntity;
import com.herongwang.p2p.service.apply.IApplyForService;
import com.sxj.util.exception.WebException;
import com.sxj.util.logger.SxjLogger;

@Controller
@RequestMapping("/apply")
public class ApplyController
{
    @Autowired
    IApplyForService applyForService;
    
    /**
     * 跳转申请融资
     * @param session
     * @param map
     * @return
     */
    @RequestMapping("toApply")
    public String accountList(HttpSession session, ModelMap map)
    {
        //会员信息传到页面
        return "site/apply/apply";
    }
    
    @RequestMapping("saveApply")
    public @ResponseBody Map<String, String> saveApply(HttpSession session,
            ApplyForEntity apply) throws WebException
    {
        try
        {
            //获取登陆会员ID 
            Map<String, String> map = new HashMap<String, String>();
            apply.setMemberId("M999999");
            apply.setForTime(new Date());//申请时间
            apply.setStatus(0);
            applyForService.addApplyFor(apply);
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
