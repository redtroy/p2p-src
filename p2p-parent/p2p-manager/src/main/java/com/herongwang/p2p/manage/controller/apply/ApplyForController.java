package com.herongwang.p2p.manage.controller.apply;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.util.StringUtils;
import com.herongwang.p2p.entity.apply.DebtApplicationEntity;
import com.herongwang.p2p.manage.controller.BaseController;
import com.herongwang.p2p.service.apply.IDebtApplicationService;
import com.sxj.util.exception.WebException;
import com.sxj.util.logger.SxjLogger;

@Controller
@RequestMapping("/apply")
public class ApplyForController extends BaseController
{
    
    @Autowired
    private IDebtApplicationService debtApplicationService;
    
    /**
     * 融资申请页面
     * @param entity
     * @return
     */
    @RequestMapping("/applyList")
    public String developerList(DebtApplicationEntity entity, ModelMap map)
            throws WebException
    {
        try
        {
            if (entity != null)
            {
                entity.setPagable(true);
            }
            List<DebtApplicationEntity> list = debtApplicationService.queryApply(entity);
            for (int i = 0; i < list.size(); i++)
            {
                DebtApplicationEntity debt = list.get(i);
                debt.setAmount(debt.getAmount().divide(new BigDecimal(100), 2));
            }
            map.put("list", list);
            map.put("query", entity);
            return "manage/apply/apply-list";
        }
        catch (Exception e)
        {
            e.printStackTrace();
            SxjLogger.error("查询融资申请错误", e, this.getClass());
            throw new WebException("查询融资申请错误");
        }
    }
    
    @RequestMapping("/toEdit")
    public String toEdit(String id, ModelMap map) throws WebException
    {
        if (StringUtils.isEmpty(id))
        {
            return "manage/apply/new-tender";
        }
        else
        {
            DebtApplicationEntity info = debtApplicationService.getApplyForEntity(id);
            map.put("info", info);
            return "manage/apply/new-tender";
        }
        
    }
    
    @RequestMapping("edit")
    public @ResponseBody Map<String, String> addApply(String applicationId)
            throws WebException
    {
        DebtApplicationEntity entity = new DebtApplicationEntity();
        entity.setApplicationId(applicationId);
        try
        {
            if (null == applicationId || applicationId.isEmpty())
            {
                debtApplicationService.addApply(entity);
            }
            else
            {
                entity.setApplicationId(applicationId);
                debtApplicationService.updateApply(entity);
            }
            Map<String, String> map = new HashMap<String, String>();
            map.put("isOK", "ok");
            return map;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new WebException(e);
        }
    }
    
    @RequestMapping("update")
    public @ResponseBody Map<String, String> update(String id)
            throws WebException
    {
        DebtApplicationEntity info = debtApplicationService.getApplyForEntity(id);
        info.setStatus(2);
        debtApplicationService.updateApply(info);
        Map<String, String> map = new HashMap<String, String>();
        map.put("isOK", "ok");
        return map;
    }
    
    @RequestMapping("delete")
    public @ResponseBody Map<String, String> delApply(String id)
            throws WebException
    {
        try
        {
            debtApplicationService.delApply(id);
            Map<String, String> map = new HashMap<String, String>();
            map.put("isOK", "ok");
            return map;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new WebException(e);
        }
    }
}
