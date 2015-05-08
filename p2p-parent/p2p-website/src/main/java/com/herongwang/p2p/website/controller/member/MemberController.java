package com.herongwang.p2p.website.controller.member;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.herongwang.p2p.entity.member.MemberEntity;
import com.sxj.util.exception.WebException;

@Controller
@RequestMapping("member")
public class MemberController
{
    
    @RequestMapping("register")
    public String register(ModelMap map, MemberEntity member)
            throws WebException
    {
        try
        {
            
        }
        catch (Exception e)
        {
            // TODO: handle exception
        }
        return null;
    }
    
    @RequestMapping("memberInfo")
    public String memberInfo(ModelMap map) throws WebException
    {
        try
        {
            
        }
        catch (Exception e)
        {
            // TODO: handle exception
        }
        return "site/member/member-center";
    }
    
    @RequestMapping("saveMember")
    public String saveMember(MemberEntity member) throws WebException
    {
        try
        {
            
        }
        catch (Exception e)
        {
            // TODO: handle exception
        }
        return null;
    }
}
