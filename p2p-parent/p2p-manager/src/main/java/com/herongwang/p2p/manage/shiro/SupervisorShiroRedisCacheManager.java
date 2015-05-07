package com.herongwang.p2p.manage.shiro;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;

import com.sxj.cache.manager.CacheLevel;
import com.sxj.spring.modules.security.shiro.ShiroRedisCache;
import com.sxj.spring.modules.security.shiro.ShiroRedisCacheManager;

public class SupervisorShiroRedisCacheManager extends ShiroRedisCacheManager
{
    
    private CacheLevel level;
    
    @Override
    protected Cache createCache(String cacheName) throws CacheException
    {
        ShiroRedisCache<String, Object> supervisorShiroRedisCache = new ShiroRedisCache<String, Object>(
                getLevel(), cacheName);
        return supervisorShiroRedisCache;
    }
    
    public CacheLevel getLevel()
    {
        return level;
    }
    
    public void setLevel(CacheLevel level)
    {
        this.level = level;
    }
    
}
