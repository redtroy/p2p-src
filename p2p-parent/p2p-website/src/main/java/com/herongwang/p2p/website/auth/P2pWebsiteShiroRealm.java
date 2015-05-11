package com.herongwang.p2p.website.auth;

import javax.annotation.PostConstruct;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import com.herongwang.p2p.entity.users.UsersEntity;
import com.herongwang.p2p.service.users.IUserService;

public class P2pWebsiteShiroRealm extends AuthorizingRealm
{
    @Autowired
    private IUserService memberService;
    
    public static final String HASH_ALGORITHM = "MD5";
    
    public static final int HASH_INTERATIONS = 1;
    
    public P2pWebsiteShiroRealm()
    {
        super.setAuthenticationCachingEnabled(false);
    }
    
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection arg0)
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    // 获取认证信息
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(
            AuthenticationToken arg0) throws AuthenticationException
    {
        UsernamePasswordToken token = (UsernamePasswordToken) arg0;
        // 通过表单接收的用户名
        String username = token.getUsername();
        //
        if (username != null && !"".equals(username))
        {
            UsersEntity member = memberService.getUserByAccount(username);
            
            if (member != null)
            {
                return new SimpleAuthenticationInfo(member,
                        member.getPassword(), getName());
            }
        }
        return null;
    }
    
    /**
     * 设定Password校验的Hash算法与迭代次数.
     */
    @PostConstruct
    public void initCredentialsMatcher()
    {
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(
                HASH_ALGORITHM);
        matcher.setHashIterations(HASH_INTERATIONS);
        
        setCredentialsMatcher(matcher);
    }
    
}
