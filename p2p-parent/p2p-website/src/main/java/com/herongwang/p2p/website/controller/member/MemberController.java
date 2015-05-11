package com.herongwang.p2p.website.controller.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.herongwang.p2p.entity.account.AccountEntity;
import com.herongwang.p2p.entity.users.UsersEntity;
import com.herongwang.p2p.model.users.UserModel;
import com.herongwang.p2p.service.account.IAccountService;
import com.herongwang.p2p.service.users.IUserService;
import com.sxj.util.exception.WebException;
import com.sxj.util.logger.SxjLogger;

@Controller
@RequestMapping("member")
public class MemberController
{
    @Autowired
    private IUserService memberservice;
    
    @Autowired
    private IAccountService accountService;
    
    @RequestMapping("register")
    public String register(ModelMap map, UsersEntity member)
            throws WebException
    {
        return "site/member/register";
    }
    
    @RequestMapping("memberInfo")
    public String memberInfo(ModelMap map) throws WebException
    {
        try
        {
            
        }
        catch (Exception e)
        {
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new WebException("查询用户信息出错", e);
        }
        return "site/member/member-center";
    }
    
    @RequestMapping("saveMember")
    public String saveMember(UsersEntity member, ModelMap map)
            throws WebException
    {
        try
        {
            UsersEntity info = memberservice.addUser(member);
            map.put("member", info);
        }
        catch (Exception e)
        {
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new WebException("保存用户信息出错", e);
        }
        return "site/member/member-center";
    }
    
    @RequestMapping("saveAccount")
    public String saveAccount(AccountEntity account, ModelMap map)
            throws WebException
    {
        try
        {
            UserModel memberInfo = accountService.addAccount(account);
            map.put("member", memberInfo);
        }
        catch (Exception e)
        {
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new WebException("保存账户信息出错", e);
        }
        return "site/member/member-center";
    }
}
