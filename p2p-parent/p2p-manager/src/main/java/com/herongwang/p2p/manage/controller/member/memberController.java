package com.herongwang.p2p.manage.controller.member;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.herongwang.p2p.manage.controller.BaseController;
import com.herongwang.p2p.model.member.MemberModel;
import com.herongwang.p2p.service.member.IMemberService;
import com.sxj.util.exception.WebException;
import com.sxj.util.logger.SxjLogger;

@Controller
@RequestMapping("member")
public class memberController extends BaseController
{
    @Autowired
    private IMemberService memberService;
    
    @RequestMapping("manage")
    public String manage(ModelMap map, MemberModel member) throws WebException
    {
        try
        {
            List<MemberModel> memberList = memberService.queryMemberInfo(member);
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
