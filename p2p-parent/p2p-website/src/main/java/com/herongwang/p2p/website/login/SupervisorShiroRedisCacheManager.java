package com.herongwang.p2p.website.login;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;

import com.sxj.cache.manager.CacheLevel;
import com.sxj.redis.core.collections.RedisCollections;
import com.sxj.spring.modules.security.shiro.ShiroRedisCacheManager;

public class SupervisorShiroRedisCacheManager extends ShiroRedisCacheManager
{
    private RedisCollections collections;
    
    private CacheLevel level;
    
    @Override
    protected Cache createCache(String cacheName) throws CacheException
    {
        // TODO Auto-generated method stub
        SupervisorShiroRedisCache<String, Object> supervisorShiroRedisCache = new SupervisorShiroRedisCache<String, Object>(
                getLevel(), cacheName);
        supervisorShiroRedisCache.setCollections(collections);
        return supervisorShiroRedisCache;
    }
    
    public void setCollections(RedisCollections collections)
    {
        this.collections = collections;
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
