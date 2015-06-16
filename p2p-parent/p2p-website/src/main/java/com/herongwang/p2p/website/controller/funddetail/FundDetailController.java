package com.herongwang.p2p.website.controller.funddetail;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.herongwang.p2p.entity.account.AccountEntity;
import com.herongwang.p2p.entity.funddetail.FundDetailEntity;
import com.herongwang.p2p.entity.users.UsersEntity;
import com.herongwang.p2p.service.account.IAccountService;
import com.herongwang.p2p.service.funddetail.IFundDetailService;
import com.herongwang.p2p.website.controller.BaseController;
import com.sxj.util.exception.WebException;
import com.sxj.util.logger.SxjLogger;

@Controller
@RequestMapping("/fundDetail")
public class FundDetailController extends BaseController
{
    @Autowired
    IFundDetailService fundDetailService;
    
    @Autowired
    IAccountService accountService;
    
    /**
     * 资金明细
     * @param session
     * @param map
     * @return
     * @throws WebException 
     */
    @RequestMapping("queryFundDetail")
    public String queryFundDetail(HttpSession session, ModelMap map,
            FundDetailEntity query) throws WebException
    {
        //会员信息传到页面
        try
        {
            UsersEntity user = getUsersEntity();
            if(user==null){
                return LOGIN;
            }
            AccountEntity account = accountService.getAccountByCustomerId(user.getCustomerId());
            query.setCustomerId(user.getCustomerId());
            query.setPagable(true);
            List<FundDetailEntity> fundList = fundDetailService.queryFundDetail(query);
            map.put("fundList", fundList);
            map.put("query", query);
            map.put("account", account);
            return "site/fundDetail/fundDetail";
        }
        catch (Exception e)
        {
            SxjLogger.error("查询资金明细错误", e, this.getClass());
            throw new WebException("查询资金明细错误");
        }
    }
}
