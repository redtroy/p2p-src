package com.herongwang.p2p.website.auth;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.realm.AuthenticatingRealm;

import com.herongwang.p2p.entity.users.UsersEntity;
import com.sxj.spring.modules.shiro.x509.authc.x509.X509AuthenticationInfo;
import com.sxj.spring.modules.shiro.x509.authc.x509.X509AuthenticationToken;

public class P2pWebsiteX509Realm extends AuthenticatingRealm
{
    
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(
            AuthenticationToken arg0) throws AuthenticationException
    {
        X509AuthenticationToken token = (X509AuthenticationToken) arg0;
        return new X509AuthenticationInfo(new UsersEntity(),
                token.getSubjectDN(), getName());
    }
    
}
