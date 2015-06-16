package com.herongwang.p2p.website.auth;

import java.security.cert.X509Certificate;

import com.sxj.spring.modules.shiro.x509.authc.x509.X509AuthenticationToken;

public class P2pWebsiteX509AuthenticationToken extends X509AuthenticationToken
{
    private String username;
    
    private String password;
    
    public P2pWebsiteX509AuthenticationToken(X509Certificate[] clientCertChain,
            String host)
    {
        super(clientCertChain, host);
        // TODO Auto-generated constructor stub
    }
    
    public String getUsername()
    {
        return username;
    }
    
    public void setUsername(String username)
    {
        this.username = username;
    }
    
    public String getPassword()
    {
        return password;
    }
    
    public void setPassword(String password)
    {
        this.password = password;
    }
    
}
