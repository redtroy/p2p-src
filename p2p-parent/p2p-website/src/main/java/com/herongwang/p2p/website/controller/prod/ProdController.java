package com.herongwang.p2p.website.controller.prod;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.herongwang.p2p.entity.account.AccountEntity;
import com.herongwang.p2p.entity.debt.DebtEntity;
import com.herongwang.p2p.entity.users.UsersEntity;
import com.herongwang.p2p.service.account.IAccountService;
import com.herongwang.p2p.service.debt.IDebtService;
import com.herongwang.p2p.website.controller.BaseController;
import com.sxj.util.exception.WebException;
import com.sxj.util.logger.SxjLogger;

@Controller
@RequestMapping("prod")
public class ProdController extends BaseController
{
    @Autowired
    private IDebtService debtService;
    
    @Autowired
    private IAccountService acountService;
    
    @RequestMapping("bdList")
    public String bdList(ModelMap map, DebtEntity debt) throws WebException
    {
        try
        {
            debt.setPagable(true);
            List<DebtEntity> list = debtService.queryDebtList(debt);
            map.put("list", list);
            map.put("query", debt);
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
            UsersEntity user = getUsersEntity();
            if (user == null)
            {
                return LOGIN;
            }
            AccountEntity account = null;
            BigDecimal amountMax;
            DebtEntity debt = debtService.getDebtEntity(debtId);
            if ((bigDecimalIsNull(debt.getAmount()).subtract(bigDecimalIsNull(debt.getFinance()))).intValue() >= debt.getMaxInvest()
                    .intValue())
            {
                amountMax = debt.getMaxInvest();
            }
            else
            {
                amountMax = bigDecimalIsNull(debt.getAmount()).subtract(bigDecimalIsNull(debt.getFinance()));
            }
            if (getUsersEntity() != null)
            {
                account = acountService.getAccountByCustomerId(getUsersEntity().getCustomerId());
            }
            if (user.getCustomerId().equals(debt.getCustomerId()))
            {
                
                map.put("type", "true");
            }
            else
            {
                map.put("type", "false");
            }
            map.put("model", debt);
            map.put("account", account);
            map.put("amountMax", amountMax);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new WebException("查询标的详情错误", e);
        }
        return "site/prod/prodetails";
    }
    
    /* @RequestMapping("prod")
     public String prod(String amount) throws WebException
     {
         try
         {
             
         }
         catch (Exception e)
         {
             SxjLogger.error(e.getMessage(), e, this.getClass());
             throw new WebException("投资失败", e); // TODO: handle exception
         }
         return "";
     }*/
}
