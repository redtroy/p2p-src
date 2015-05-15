package com.herongwang.p2p.website.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.herongwang.p2p.entity.users.UsersEntity;
import com.herongwang.p2p.service.account.IAccountService;
import com.herongwang.p2p.service.users.IUserService;
import com.herongwang.p2p.website.controller.BaseController;
import com.sxj.util.exception.WebException;
import com.sxj.util.logger.SxjLogger;

@Controller
@RequestMapping("/user")
public class UserController extends BaseController
{
    @Autowired
    private IUserService userService;
    
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
            if (getUsersEntity() == null)
            {
                return LOGIN;
            }
            UsersEntity user = getUsersEntity();
            user = userService.getUserById(user.getCustomerId());
            map.put("user", user);
        }
        catch (Exception e)
        {
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new WebException("查询用户信息出错", e);
        }
        return "site/member/member-center";
    }
    
    @RequestMapping("saveUser")
    public String saveUser(UsersEntity user, ModelMap map) throws WebException
    {
        try
        {
            UsersEntity info = userService.addUser(user);
            map.put("user", info);
        }
        catch (Exception e)
        {
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new WebException("保存用户信息出错", e);
        }
        return "site/member/member-center";
    }
    
    @RequestMapping("updateUser")
    public String updateUser(ModelMap map, UsersEntity user)
            throws WebException
    {
        try
        {
            UsersEntity info = userService.updateUser(user);
            map.put("user", info);
        }
        catch (Exception e)
        {
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new WebException("保存用户信息出错", e);
        }
        return "site/member/member-center";
    }
}
