package com.herongwang.p2p.website.controller.prod;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.herongwang.p2p.entity.account.AccountEntity;
import com.herongwang.p2p.entity.debt.DebtEntity;
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
            AccountEntity account = null;
            BigDecimal amountMax;
            DebtEntity debt = debtService.getDebtEntity(debtId);
            if (getUsersEntity() == null)
            {
                amountMax = new BigDecimal(0);
            }
            else
            {
                account = acountService.getAccountByCustomerId(getUsersEntity().getCustomerId());
                BigDecimal balance = bigDecimalIsNull(debt.getAmount()).subtract(bigDecimalIsNull(debt.getFinance()));//剩余融资金额
                if (balance.intValue() <= bigDecimalIsNull(account.getBalance()).intValue())
                {
                    amountMax = balance;
                }
                else
                {
                    amountMax = account.getBalance();
                }
            }
            map.put("model", debt);
            map.put("account", account);
            map.put("amountMax", amountMax);
        }
        catch (Exception e)
        {
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new WebException("查询标的详情错误", e);
        }
        return "site/prod/prodetails";
    }
}
