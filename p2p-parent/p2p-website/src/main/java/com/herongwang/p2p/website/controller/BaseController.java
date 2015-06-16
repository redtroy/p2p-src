package com.herongwang.p2p.website.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import com.herongwang.p2p.entity.users.UsersEntity;
import com.herongwang.p2p.service.users.IUserService;
import com.sxj.util.exception.SystemException;
import com.sxj.util.logger.SxjLogger;

public class BaseController
{
    
    public static final String LOGIN = "site/member/login";
    
    public static final String INDEX = "site/index";
    
    public static final String SPRING_SECURITY_CONTEXT = "SPRING_SECURITY_CONTEXT";
    
    @Autowired
    private IUserService userService;
    
    @InitBinder
    public void initBinder(WebDataBinder binder)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(
                dateFormat, false));
    }
    
    protected String getBasePath(HttpServletRequest request)
    {
        return request.getScheme() + "://" + request.getServerName() + ":"
                + request.getServerPort() + request.getContextPath() + "/";
    }
    
    //    protected SupervisorPrincipal getLoginInfo(HttpSession session)
    //    {
    //        Object object = session.getAttribute("userinfo");
    //        if (object != null)
    //        {
    //            SupervisorPrincipal userBean = (SupervisorPrincipal) object;
    //            return userBean;
    //        }
    //        else
    //        {
    //            return null;
    //        }
    //        
    //    }
    
    protected void getValidError(BindingResult result) throws SystemException
    {
        if (result.hasErrors())
        {
            String message = "";
            List<ObjectError> errors = result.getAllErrors();
            for (ObjectError error : errors)
            {
                if (error == null)
                {
                    continue;
                }
                message = message + error.getDefaultMessage();
            }
            SxjLogger.error("Nested path [" + result.getNestedPath()
                    + "] has errors [" + message + "]", this.getClass());
            throw new SystemException(message);
        }
    }
    
    public UsersEntity getUsersEntity()
    {
        Subject user = SecurityUtils.getSubject();
        if (((UsersEntity) user.getPrincipal()) == null)
        {
            return null;
        }
        return userService.getUserById(((UsersEntity) user.getPrincipal()).getCustomerId());
    }
    
    /**
     * 如果BigDecimal为null转成0
     * @param big
     * @return
     */
    public BigDecimal bigDecimalIsNull(BigDecimal big)
    {
        if (big == null)
        {
            return new BigDecimal(0);
        }
        return big;
    }
}
