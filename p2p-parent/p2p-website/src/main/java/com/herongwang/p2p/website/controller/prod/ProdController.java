package com.herongwang.p2p.website.controller.prod;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.herongwang.p2p.entity.debt.DebtEntity;
import com.herongwang.p2p.service.debt.IDebtService;
import com.sxj.util.exception.WebException;
import com.sxj.util.logger.SxjLogger;

@Controller
@RequestMapping("prod")
public class ProdController
{
    @Autowired
    private IDebtService debtService;
    
    @RequestMapping("bdList")
    public String bdList(ModelMap map) throws WebException
    {
        try
        {
            DebtEntity debt = new DebtEntity();
            List<DebtEntity> list = debtService.queryDebtList(debt);
            map.put("list", list);
        }
        catch (Exception e)
        {
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new WebException("查询标的列表错误", e);
        }
        return "site/prod/prod-list";
    }
    
    @RequestMapping("prodInfo")
    public String prodInfo(ModelMap map, String debtId) throws WebException
    {
        try
        {
            DebtEntity debt = debtService.getDebtEntity(debtId);
            Map<String, Object> modelMap = new HashMap<String, Object>();
            modelMap.put("createTime", debt.getCreateTime());
            map.put("model", debt);
            map.put("modelMap", modelMap);
        }
        catch (Exception e)
        {
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new WebException("查询标的详情错误", e);
        }
        return "site/prod/prodetails";
    }
}
