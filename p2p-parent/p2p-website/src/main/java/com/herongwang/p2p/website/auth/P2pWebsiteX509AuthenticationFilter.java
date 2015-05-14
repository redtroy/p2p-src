package com.herongwang.p2p.website.auth;

import java.security.cert.X509Certificate;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.AuthenticationToken;

import com.sxj.spring.modules.shiro.x509.web.X509AuthenticationFilter;

public class P2pWebsiteX509AuthenticationFilter extends
        X509AuthenticationFilter
{
    
    @Override
    protected AuthenticationToken createToken(ServletRequest request,
            ServletResponse response) throws Exception
    {
        X509Certificate[] clientCertChain = (X509Certificate[]) request.getAttribute("javax.servlet.request.X509Certificate");
        if (clientCertChain == null || clientCertChain.length < 1)
        {
            throw new ShiroException(
                    "Request do not contain any X509Certificate");
        }
        P2pWebsiteX509AuthenticationToken token = new P2pWebsiteX509AuthenticationToken(
                clientCertChain, getHost(request));
        token.setPassword(request.getParameter("password"));
        token.setUsername(request.getParameter("username"));
        return token;
    }
    
    @Override
    protected boolean onAccessDenied(ServletRequest request,
            ServletResponse response) throws Exception
    {
        return super.onAccessDenied(request, response);
    }
    
}
