package com.herongwang.p2p.manage.controller.users;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.herongwang.p2p.entity.users.UsersEntity;
import com.herongwang.p2p.manage.controller.BaseController;
import com.herongwang.p2p.service.users.IUserService;
import com.sxj.util.common.EncryptUtil;
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
            user.setPagable(true);
            List<UsersEntity> memberList = userService.queryUsers(user);
            map.put("list", memberList);
            map.put("query", user);
        }
        catch (Exception e)
        {
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new WebException("查询会员信息错误", e);
        }
        return "manage/memberManage/member-center";
    }
    
    @RequestMapping("editPassword")
    public @ResponseBody String editPassword(String id, String password)
            throws WebException
    {
        try
        {
            password = EncryptUtil.md5Hex(password);
            UsersEntity user = new UsersEntity();
            user.setCustomerId(id);
            user.setPassword(password);
            userService.updateUser(user);
            return "ok";
        }
        catch (Exception e)
        {
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new WebException("查询会员信息错误", e);
        }
    }
    
}
