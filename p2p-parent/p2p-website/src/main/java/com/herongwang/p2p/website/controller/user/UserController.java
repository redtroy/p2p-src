package com.herongwang.p2p.website.controller.user;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.herongwang.p2p.entity.admin.AdminEntity;
import com.herongwang.p2p.entity.users.UsersEntity;
import com.herongwang.p2p.loan.util.Common;
import com.herongwang.p2p.service.account.IAccountService;
import com.herongwang.p2p.service.admin.IAdminService;
import com.herongwang.p2p.service.users.IUserService;
import com.herongwang.p2p.website.controller.BaseController;
import com.herongwang.p2p.website.login.SupervisorShiroRedisCache;
import com.sxj.cache.manager.CacheLevel;
import com.sxj.cache.manager.HierarchicalCacheManager;
import com.sxj.redis.core.RAtomicLong;
import com.sxj.redis.core.concurrent.RedisConcurrent;
import com.sxj.util.common.DateTimeUtils;
import com.sxj.util.common.EncryptUtil;
import com.sxj.util.common.StringUtils;
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
    
    @Autowired
    private IAdminService adminService;
    
    @Autowired
    private RedisConcurrent redisConcurrent;
    
    @RequestMapping("register")
    public String register(ModelMap map, UsersEntity member)
            throws WebException
    {
        return "site/member/register";
    }
    
    @RequestMapping("memberInfo")
    public String memberInfo(ModelMap map, String message) throws WebException
    {
        try
        {
            AdminEntity admin = adminService.gitAdminEntity("1");
            if (message != null && !"".equals(message))
            {
                message = Common.UrlDecoder(message, "utf-8");
            }
            if (getUsersEntity() == null)
            {
                return LOGIN;
            }
            UsersEntity user = getUsersEntity();
            if (user == null)
            {
                return LOGIN;
            }
            user = userService.getUserById(user.getCustomerId());
            map.put("user", user);
            map.put("message", message);
            map.put("type", admin.getStatus());
        }
        catch (Exception e)
        {
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new WebException("查询用户信息出错", e);
        }
        return "site/member/member-center";
    }
    
    @RequestMapping("saveUser")
    public String saveUser(UsersEntity user, ModelMap map, String ms,
            HttpServletRequest request) throws WebException
    {
        AdminEntity admin = adminService.gitAdminEntity("1");
        map.put("type", admin.getStatus());
        Map<String, String> mappost = new HashMap<String, String>();
        String password = user.getPassword();
        try
        {
            String message = (String) HierarchicalCacheManager.get(CacheLevel.REDIS,
                    "p2pCheckMs",
                    user.getCellphone() + "_checkMs");
            if (StringUtils.isEmpty(message))
            {
                return "site/member/login";
            }
            if (!message.equals(ms))
            {
                return "site/member/login";
            }
            UsersEntity info = userService.addUser(user);
            map.put("user", info);
            Subject currentUser = SecurityUtils.getSubject();
            UsernamePasswordToken token = new UsernamePasswordToken(
                    user.getEmail(), password);
            currentUser.login(token);
            PrincipalCollection principals = SecurityUtils.getSubject()
                    .getPrincipals();
            String userNo = info.getCustomerNo();
            SupervisorShiroRedisCache.addToMap(userNo, principals);
        }
        catch (Exception e)
        {
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new WebException("保存用户信息出错", e);
        }
        // return "redirect:" + getBasePath(request) + "login.htm?account=";
        return "site/member/member-center";
    }
    
    @RequestMapping("updateUser")
    public @ResponseBody String updateUser(ModelMap map, UsersEntity user)
            throws WebException
    {
        try
        {
            UsersEntity info = userService.updateUser(user);
            return "ok";
        }
        catch (Exception e)
        {
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new WebException("保存用户信息出错", e);
        }
    }
    
    @RequestMapping("editPassword")
    public @ResponseBody String editPassword(String id, String password,
            String oldPwd) throws WebException
    {
        try
        {
            UsersEntity u = getUsersEntity();
            if (u == null)
            {
                return LOGIN;
            }
            password = EncryptUtil.md5Hex(password);
            oldPwd = EncryptUtil.md5Hex(oldPwd);
            if (oldPwd.equals(u.getPassword()))
            {
                UsersEntity user = new UsersEntity();
                user.setCustomerId(id);
                user.setPassword(password);
                userService.updateUser(user);
                return "ok";
            }
            else
            {
                return "X";
            }
            
        }
        catch (Exception e)
        {
            
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new WebException("查询会员信息错误", e);
        }
    }
    
    /**
     * 发送验证短信
     */
    @RequestMapping("checkMs")
    public @ResponseBody Map<String, String> checkMs(String phoneNo)
            throws WebException
    {
        Map<String, String> map = new HashMap<String, String>();
        try
        {
            if (StringUtils.isEmpty(phoneNo))
            {
                map.put("status", "n");
                map.put("info", "手机号不能为空");
                return map;
            }
            phoneNo = phoneNo.trim();
            RAtomicLong num = redisConcurrent.getAtomicLong("p2pNum_" + phoneNo,
                    59);// 记录次数59秒只能发送一次
            if (num.incrementAndGet() == 1)
            {
                RAtomicLong sendMax = redisConcurrent.getAtomicLong("p2pSendMax_"
                        + phoneNo,
                        DateTimeUtils.getNextZeroTime());
                if (sendMax.incrementAndGet() <= 5)
                {
                    String message = "";
                    message = userService.sendMs(phoneNo);
                    if (message.equals("erro"))
                    {
                        map.put("status", "n");
                        map.put("info", "发送失败!");
                    }
                    else
                    {
                        HierarchicalCacheManager.set(CacheLevel.REDIS,
                                "p2pCheckMs",
                                phoneNo + "_checkMs",
                                message,
                                600);
                    }
                }
                else
                {
                    map.put("status", "n");
                    map.put("info", "每个号码每天限制发送5次");
                }
            }
            else
            {
                map.put("status", "n");
                map.put("info", " 每一分钟发送一次");
            }
        }
        catch (Exception e)
        {
            SxjLogger.error("信息发送错误", e, this.getClass());
            throw new WebException("发送信息错误");
        }
        return map;
    }
    
    /**
     * 检查电话号码是否存在
     * @param cellphone
     * @return
     * @throws WebException
     */
    @RequestMapping("checkPhone")
    public @ResponseBody Map<String, String> checkPhone(String param)
            throws WebException
    {
        Map<String, String> map = new HashMap<String, String>();
        try
        {
            if (userService.checkPhone(param))
            {
                map.put("status", "y");
                map.put("info", "");
            }
            else
            {
                map.put("status", "n");
                map.put("info", "手机号码已存在！");
            }
            return map;
        }
        catch (Exception e)
        {
            SxjLogger.error("手机号验证出错", e, this.getClass());
            throw new WebException("手机号验证出错");
        }
    }
    
    /**
     * 检查邮箱是否存在
     * @param cellphone
     * @return
     * @throws WebException
     */
    @RequestMapping("checkEmail")
    public @ResponseBody Map<String, String> checkEmail(String param)
            throws WebException
    {
        Map<String, String> map = new HashMap<String, String>();
        try
        {
            if (userService.checkEmail(param))
            {
                map.put("status", "y");
                map.put("info", "可以注册");
            }
            else
            {
                map.put("status", "n");
                map.put("info", "邮箱已存在");
            }
            return map;
        }
        catch (Exception e)
        {
            SxjLogger.error("验证邮箱出错", e, this.getClass());
            throw new WebException("验证邮箱出错");
        }
    }
    
    /**
     * 验证码校验
     */
    @RequestMapping("verification")
    public @ResponseBody Map<String, String> verification(String phone,
            String ms) throws WebException
    {
        Map<String, String> map = new HashMap<String, String>();
        try
        {
            String message = (String) HierarchicalCacheManager.get(CacheLevel.REDIS,
                    "p2pCheckMs",
                    phone + "_checkMs");
            if (ms.equals(message))
            {
                map.put("status", "ok");
            }
            return map;
        }
        catch (Exception e)
        {
            SxjLogger.error("验证码校验出错", e, this.getClass());
            throw new WebException("验证码校验出错");
        }
    }
}
