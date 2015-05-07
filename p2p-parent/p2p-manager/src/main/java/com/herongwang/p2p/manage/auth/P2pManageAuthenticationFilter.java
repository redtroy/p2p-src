package com.herongwang.p2p.manage.auth;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter;

public class P2pManageAuthenticationFilter extends
        PermissionsAuthorizationFilter
{
    
    // @Resource
    // private CacheManager shiroCacheManager;
    
    @Override
    public boolean isAccessAllowed(ServletRequest request,
            ServletResponse response, Object mappedValue) throws IOException
    {
        return true;
        
    }
    
}
