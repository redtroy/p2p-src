package com.herongwang.p2p.manage.controller.member;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.herongwang.p2p.manage.controller.BaseController;
import com.sxj.util.exception.WebException;

@Controller
@RequestMapping("member")
public class memberController extends BaseController
{
    
    @RequestMapping("manage")
    public String manage() throws WebException
    {
        try
        {
            
        }
        catch (Exception e)
        {
            // TODO: handle exception
        }
        return "manage/memberManage/member-center";
    }
}
