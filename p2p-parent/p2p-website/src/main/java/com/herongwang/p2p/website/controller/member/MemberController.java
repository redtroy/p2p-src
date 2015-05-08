package com.herongwang.p2p.website.controller.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.herongwang.p2p.entity.account.AccountEntity;
import com.herongwang.p2p.entity.member.MemberEntity;
import com.herongwang.p2p.model.member.MemberModel;
import com.herongwang.p2p.service.account.IAccountService;
import com.herongwang.p2p.service.member.IMemberService;
import com.sxj.util.exception.WebException;
import com.sxj.util.logger.SxjLogger;

@Controller
@RequestMapping("member")
public class MemberController
{
    @Autowired
    private IMemberService memberservice;
    
    @Autowired
    private IAccountService accountService;
    
    @RequestMapping("register")
    public String register(ModelMap map, MemberEntity member)
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
    public String saveMember(MemberEntity member, ModelMap map)
            throws WebException
    {
        try
        {
            MemberEntity info = memberservice.addMember(member);
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
            MemberModel memberInfo = accountService.addAccount(account);
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
