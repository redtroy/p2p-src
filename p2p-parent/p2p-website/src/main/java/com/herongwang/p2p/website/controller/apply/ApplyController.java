package com.herongwang.p2p.website.controller.apply;

import java.math.BigDecimal;
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
import com.herongwang.p2p.service.apply.IDebtApplicationService;
import com.herongwang.p2p.website.controller.BaseController;
import com.sxj.util.exception.WebException;
import com.sxj.util.logger.SxjLogger;

@Controller
@RequestMapping("/apply")
public class ApplyController extends BaseController
{
    @Autowired
    IDebtApplicationService applyForService;
    
    /**
     * 跳转申请融资
     * @param session
     * @param map
     * @return
     */
    @RequestMapping("toApply")
    public String toApply(HttpSession session, ModelMap map)
    {
        //会员信息传到页面
        return "site/apply/apply";
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
            apply.setAmount(apply.getAmount().multiply(new BigDecimal(100)));
            Map<String, String> map = new HashMap<String, String>();
            apply.setApplyTime(new Date());//申请时间
            apply.setStatus(0);
            // apply.setCustomerId(user.getCustomerId());
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
