package com.herongwang.p2p.manage.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.herongwang.p2p.entity.admin.AdminEntity;
import com.herongwang.p2p.manage.login.SupervisorShiroRedisCache;
import com.herongwang.p2p.service.admin.IAdminService;

@Controller
public class BasicController extends BaseController
{
    @Autowired
    private IAdminService adminService;
    
    private static final HttpServletRequest DefaultMultipartHttpServletRequest = null;
    
    @RequestMapping("login")
    public String login(String account, String password, HttpSession session,
            HttpServletRequest request, ModelMap map)
    {
        AdminEntity admin = adminService.getAdminEntityByName(account);
        if (admin == null)
        {
            map.put("message", "用户名不存在");
            return LOGIN;
        }
        Subject currentUser = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(account,
                password);
        try
        {
            currentUser.login(token);
            PrincipalCollection principals = SecurityUtils.getSubject()
                    .getPrincipals();
            String userNo = admin.getUserNo();
            SupervisorShiroRedisCache.addToMap(userNo, principals);
        }
        catch (AuthenticationException e)
        {
            map.put("account", account);
            map.put("message", "用户名或密码错误");
            return LOGIN;
            
        }
        if (currentUser.isAuthenticated())
        {
            session.setAttribute("adminInfo", admin);
            
            return "redirect:" + getBasePath(request) + "member/manage.htm";
        }
        else
        {
            map.put("account", account);
            map.put("message", "登陆失败");
            return LOGIN;
        }
    }
    
}
