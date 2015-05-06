package com.herongwang.p2p.website.auth;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter;

public class P2pWebsiteAuthenticationFilter extends
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
