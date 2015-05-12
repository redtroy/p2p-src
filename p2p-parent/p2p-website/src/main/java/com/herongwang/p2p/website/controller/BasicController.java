package com.herongwang.p2p.website.controller;

import java.util.Calendar;
import java.util.List;

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

import com.herongwang.p2p.entity.debt.DebtEntity;
import com.herongwang.p2p.entity.users.UsersEntity;
import com.herongwang.p2p.service.debt.IDebtService;
import com.herongwang.p2p.service.users.IUserService;
import com.herongwang.p2p.website.login.SupervisorShiroRedisCache;
import com.sxj.redis.core.pubsub.RedisTopics;
import com.sxj.util.exception.WebException;
import com.sxj.util.logger.SxjLogger;

@Controller
public class BasicController extends BaseController
{
    
    @Autowired
    private RedisTopics topics;
    
    @Autowired
    private IUserService userService;
    
    @Autowired
    private IDebtService debtService;
    
    @RequestMapping("to_login")
    public String ToLogin()
    {
        return LOGIN;
    }
    
    @RequestMapping("logout")
    public String logout(HttpServletRequest request)
    {
        Subject currentUser = SecurityUtils.getSubject();
        currentUser.logout();
        return "redirect:" + getBasePath(request) + "to_login.htm";
        
    }
    
    @RequestMapping("error")
    public String ToError()
    {
        return "site/500";
    }
    
    @RequestMapping("404")
    public String To404()
    {
        return "site/404";
    }
    
    @RequestMapping("login")
    public String login(String account, String password, HttpSession session,
            HttpServletRequest request, ModelMap map)
    {
        UsersEntity user = userService.getUserByAccount(account);
        if (user == null)
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
            String userNo = user.getCustomerNo();
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
            session.setAttribute("userInfo", user);
            UsersEntity userInfo = userService.getUserByUserId(user.getCustomerId());
            map.put("user", userInfo);
            return "site/member/member-center";
            // return "redirect:" + getBasePath(request) + "member/memberInfo.htm";
        }
        else
        {
            map.put("account", account);
            map.put("message", "登陆失败");
            return LOGIN;
        }
    }
    
    private String stringDate()
    {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);//获取年份         
        int month = cal.get(Calendar.MONTH);//获取月份         
        int day = cal.get(Calendar.DATE);//获取日         
        int hour = cal.get(Calendar.HOUR);//小时          
        int minute = cal.get(Calendar.MINUTE);//分                     
        int second = cal.get(Calendar.SECOND);//秒     
        String date = year + "" + month + "" + day + "" + hour + "" + minute
                + "" + second;
        
        return date;
    }
    
    @RequestMapping("leftMenu")
    public String leftMenu()
    {
        return "site/leftMenu";
    }
    
    @RequestMapping("index")
    public String ToIndex(HttpServletRequest request, ModelMap map)
            throws WebException
    {
        try
        {
            List<DebtEntity> list = debtService.queryTop5();
            map.put("list", list);
        }
        catch (Exception e)
        {
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new WebException("主页查询标的TOP5列表出错", e);
        }
        return INDEX;
    }
    
    public static String getIpAddr(HttpServletRequest request)
    {
        String strUserIp = null;
        /** * */
        // Apache 代理 解决IP地址问题
        strUserIp = request.getHeader("X-Forwarded-For");
        if (strUserIp == null || strUserIp.length() == 0
                || "unknown".equalsIgnoreCase(strUserIp))
        {
            strUserIp = request.getHeader("Proxy-Client-IP");
        }
        if (strUserIp == null || strUserIp.length() == 0
                || "unknown".equalsIgnoreCase(strUserIp))
        {
            strUserIp = request.getHeader("WL-Proxy-Client-IP");
        }
        if (strUserIp == null || strUserIp.length() == 0
                || "unknown".equalsIgnoreCase(strUserIp))
        {
            strUserIp = request.getRemoteAddr();
        }
        // 解决获取多网卡的IP地址问题
        if (strUserIp != null)
        {
            strUserIp = strUserIp.split(",")[0];
        }
        else
        {
            strUserIp = "127.0.0.1";
        }
        // 解决获取IPv6地址 暂时改为本机地址模式
        if (strUserIp.length() > 16)
        {
            strUserIp = "127.0.0.1";
        }
        return strUserIp;
        // 没有IP Apache 代理 解决IP地址问题
        // strUserIp=request.getRemoteAddr();
        // if (strUserIp != null) {strUserIp = strUserIp.split(",")[0];}
        // return strUserIp;
    }
    
}
