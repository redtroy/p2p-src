package com.herongwang.p2p.website.auth;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter;

import com.sxj.util.common.StringUtils;

public class P2pWebsiteAuthenticationFilter extends
        PermissionsAuthorizationFilter
{
    
    // @Resource
    // private CacheManager shiroCacheManager;
    
    @Override
    public boolean isAccessAllowed(ServletRequest request,
            ServletResponse response, Object mappedValue) throws IOException
    {
        HttpServletRequest req = (HttpServletRequest) request;
        req.setCharacterEncoding("UTF-8");
        Subject subject = getSubject(request, response);
        String uri = req.getRequestURI();
        String contextPath = req.getContextPath() + "/";
        int i = uri.indexOf(contextPath);
        if (i > -1)
        {
            uri = uri.substring(i + contextPath.length());
        }
        if (StringUtils.isBlank(uri))
        {
            uri = "/";
        }
        i = uri.indexOf(";");
        if (i > 0)
        {
            uri = uri.substring(0, i);
        }
        boolean permitted = false;
        if ("/".equals(uri))
        {
            permitted = true;
        }
        else
        {
            if (uri.startsWith("/"))
                uri = uri.substring(1);
            permitted = subject.isPermitted(uri);
        }
        return permitted;
        
    }
    
}
