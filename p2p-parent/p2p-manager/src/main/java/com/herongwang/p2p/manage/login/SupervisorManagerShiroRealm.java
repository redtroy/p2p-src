package com.herongwang.p2p.manage.login;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.permission.PermissionResolver;
import org.apache.shiro.authz.permission.RolePermissionResolver;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.CollectionUtils;

import com.sxj.spring.modules.util.Reflections;
import com.sxj.util.logger.SxjLogger;

public class SupervisorManagerShiroRealm extends AuthorizingRealm
{
    
    // 用于获取用户信息及用户权限信息的业务接口
    
    public static final String HASH_ALGORITHM = "MD5";
    
    public static final int HASH_INTERATIONS = 1;
    
    private static final int SALT_SIZE = 8;
    
    public SupervisorManagerShiroRealm()
    {
        super.setAuthenticationCachingEnabled(false);
    }
    
    // 获取授权信息
    protected AuthorizationInfo doGetAuthorizationInfo(
            PrincipalCollection principals)
    {
        
        return null;
    }
    
    // 获取认证信息
    protected AuthenticationInfo doGetAuthenticationInfo(
            AuthenticationToken authcToken) throws AuthenticationException
    {
        UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
        // 通过表单接收的用户名
        String username = token.getUsername();
        //
        
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
    
    @Override
    protected void clearCachedAuthorizationInfo(PrincipalCollection arg0)
    {
        super.clearCachedAuthorizationInfo(arg0);
    }
    
    private boolean isPermitted(Permission permission, AuthorizationInfo info)
    {
        Collection<Permission> perms = getPermissions(info);
        if (perms != null && !perms.isEmpty())
        {
            for (Permission perm : perms)
            {
                if (perm.implies(permission))
                {
                    return true;
                }
            }
        }
        return false;
    }
    
    public AuthorizationInfo getAuthorizationInfo(PrincipalCollection principals)
    {
        
        if (principals == null)
        {
            return null;
        }
        
        AuthorizationInfo info = null;
        
        SxjLogger.debug("Retrieving AuthorizationInfo for principals ["
                + principals + "]", getClass());
        
        Cache<Object, AuthorizationInfo> cache = getAvailableAuthorizationCache();
        if (cache != null)
        {
            Object key = getAuthorizationCacheKey(principals);
            info = cache.get(key);
        }
        
        if (info == null)
        {
            // Call template method if the info was not found in a cache
            info = doGetAuthorizationInfo(principals);
            // If the info is not null and the cache has been created, then
            // cache the authorization info.
            if (info != null && cache != null)
            {
                Object key = getAuthorizationCacheKey(principals);
                cache.put(key, info);
            }
        }
        
        return info;
    }
    
    private Cache<Object, AuthorizationInfo> getAvailableAuthorizationCache()
    {
        Cache<Object, AuthorizationInfo> cache = getAuthorizationCache();
        if (cache == null && isAuthorizationCachingEnabled())
        {
            cache = getAuthorizationCacheLazy();
        }
        return cache;
    }
    
    private Cache<Object, AuthorizationInfo> getAuthorizationCacheLazy()
    {
        
        if (getAuthorizationCache() == null)
        {
            
            CacheManager cacheManager = getCacheManager();
            
            if (cacheManager != null)
            {
                String cacheName = getAuthorizationCacheName();
                // setAuthorizationCache(cacheManager.getCache(cacheName));
                Reflections.setFieldValue(this,
                        "authorizationCache",
                        cacheManager.getCache(cacheName));
            }
        }
        
        return getAuthorizationCache();
    }
    
    @Override
    public boolean isPermitted(PrincipalCollection principals,
            Permission permission)
    {
        AuthorizationInfo info = getAuthorizationInfo(principals);
        return isPermitted(permission, info);
    }
    
    @Override
    public boolean isPermitted(PrincipalCollection principals, String permission)
    {
        Permission p = getPermissionResolver().resolvePermission(permission);
        return this.isPermitted(principals, p);
    }
    
    private Collection<Permission> resolveRolePermissions(
            Collection<String> roleNames)
    {
        Collection<Permission> perms = Collections.emptySet();
        RolePermissionResolver resolver = getRolePermissionResolver();
        if (resolver != null && !CollectionUtils.isEmpty(roleNames))
        {
            perms = new LinkedHashSet<Permission>(roleNames.size());
            for (String roleName : roleNames)
            {
                Collection<Permission> resolved = resolver.resolvePermissionsInRole(roleName);
                if (!CollectionUtils.isEmpty(resolved))
                {
                    perms.addAll(resolved);
                }
            }
        }
        return perms;
    }
    
    private Collection<Permission> resolvePermissions(
            Collection<String> stringPerms)
    {
        Collection<Permission> perms = Collections.emptySet();
        PermissionResolver resolver = getPermissionResolver();
        if (resolver != null && !CollectionUtils.isEmpty(stringPerms))
        {
            perms = new LinkedHashSet<Permission>(stringPerms.size());
            for (String strPermission : stringPerms)
            {
                Permission permission = getPermissionResolver().resolvePermission(strPermission);
                perms.add(permission);
            }
        }
        return perms;
    }
    
    private Collection<Permission> getPermissions(AuthorizationInfo info)
    {
        Set<Permission> permissions = new HashSet<Permission>();
        
        if (info != null)
        {
            Collection<Permission> perms = info.getObjectPermissions();
            if (!CollectionUtils.isEmpty(perms))
            {
                permissions.addAll(perms);
            }
            perms = resolvePermissions(info.getStringPermissions());
            if (!CollectionUtils.isEmpty(perms))
            {
                permissions.addAll(perms);
            }
            
            perms = resolveRolePermissions(info.getRoles());
            if (!CollectionUtils.isEmpty(perms))
            {
                permissions.addAll(perms);
            }
            
            // =============================此处检查权限是否变动==============================//
            
            // =============================end==========================================//
        }
        
        if (permissions.isEmpty())
        {
            return Collections.emptySet();
        }
        else
        {
            return Collections.unmodifiableSet(permissions);
        }
    }
}
