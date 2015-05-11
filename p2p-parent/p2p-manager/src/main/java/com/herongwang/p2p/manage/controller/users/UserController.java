package com.herongwang.p2p.manage.controller.users;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.herongwang.p2p.entity.users.UsersEntity;
import com.herongwang.p2p.manage.controller.BaseController;
import com.herongwang.p2p.service.users.IUserService;
import com.sxj.util.exception.WebException;
import com.sxj.util.logger.SxjLogger;

@Controller
@RequestMapping("user")
public class UserController extends BaseController
{
    @Autowired
    private IUserService userService;
    
    @RequestMapping("manage")
    public String manage(ModelMap map, UsersEntity user) throws WebException
    {
        try
        {
            List<UsersEntity> memberList = userService.queryUsers(user);
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
