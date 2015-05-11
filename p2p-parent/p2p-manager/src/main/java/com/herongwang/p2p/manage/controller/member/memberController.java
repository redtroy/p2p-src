package com.herongwang.p2p.manage.controller.member;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.herongwang.p2p.manage.controller.BaseController;
import com.herongwang.p2p.model.users.UserModel;
import com.herongwang.p2p.service.users.IUserService;
import com.sxj.util.exception.WebException;
import com.sxj.util.logger.SxjLogger;

@Controller
@RequestMapping("member")
public class memberController extends BaseController
{
    @Autowired
    private IUserService memberService;
    
    @RequestMapping("manage")
    public String manage(ModelMap map, UserModel member) throws WebException
    {
        try
        {
            List<UserModel> memberList = memberService.queryUsers(member);
            map.put("list", memberList);
        }
        catch (Exception e)
        {
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new WebException("查询会员信息错误", e);
        }
        return "manage/memberManage/member-center";
    }
}
