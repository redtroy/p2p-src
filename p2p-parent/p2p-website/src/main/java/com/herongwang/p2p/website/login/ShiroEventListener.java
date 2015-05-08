package com.herongwang.p2p.website.login;

import java.util.List;

import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

import com.sxj.redis.core.MessageListener;
import com.sxj.redis.core.RMap;
import com.sxj.redis.core.RTopic;
import com.sxj.redis.core.collections.RedisCollections;
import com.sxj.redis.core.pubsub.RedisTopics;
import com.sxj.spring.modules.beanfactory.CustomizedPropertyPlaceholderConfigurer;
import com.sxj.spring.modules.security.shiro.ShiroRedisCacheManager;
import com.sxj.util.Constraints;
import com.sxj.util.logger.SxjLogger;

public class ShiroEventListener implements BeanFactoryPostProcessor
{
    
    private static RedisTopics topics;
    
    private static RedisCollections collections;
    
    private static ShiroRedisCacheManager cacheManager;
    
    private static String cacheName;
    
    @Override
    public void postProcessBeanFactory(
            final ConfigurableListableBeanFactory beanFactory)
            throws BeansException
    {
        topics = beanFactory.getBean(RedisTopics.class);
        collections = beanFactory.getBean(RedisCollections.class);
        cacheManager = beanFactory.getBean(SupervisorShiroRedisCacheManager.class);
        cacheName = CustomizedPropertyPlaceholderConfigurer.getContextProperty("website.authorization.cache.name");
        RTopic<Object> topic = topics.getTopic(Constraints.WEBSITE_CHANNEL_NAME);
        topic.addListener(new MessageListener<Object>()
        {
            @Override
            public void onMessage(Object msg)
            {
                if (!(msg instanceof String))
                {
                    return;
                }
                String[] message = ((String) msg).split(",");
                String type = message[0];
                String accountId = message[1];
                RMap<Object, Object> map = collections.getMap(Constraints.SHIRO_MAP_KEY);
                if (map == null)
                {
                    return;
                }
                if (map.get(accountId) == null)
                {
                    return;
                }
                List<PrincipalCollection> principals = (List<PrincipalCollection>) map.get(accountId);
                Cache<Object, Object> cache = cacheManager.getCache(cacheName);
                for (PrincipalCollection principal : principals)
                {
                    if ("del".equals(type))
                    {
                        SxjLogger.info("del授权信息" + accountId, this.getClass());
                        cache.put(principal, new SimpleAuthorizationInfo());
                    }
                    else if ("update".equals(type))
                    {
                        SxjLogger.info("update授权信息" + accountId,
                                this.getClass());
                        SimpleAuthorizationInfo old = (SimpleAuthorizationInfo) cache.get(principal);
                        if (old == null)
                        {
                            continue;
                        }
                        old.setStringPermissions(null);
                        cache.put(principal, old);
                    }
                    else if ("editCheckState".equals(type))
                    {
                        collections.getSet(Constraints.EDIT_CHECK_STATE_SET)
                                .add(accountId);
                    }
                    else if (Constraints.UN_EDIT_CHECK_STATE_SET.equals(type))
                    {
                        collections.getSet(Constraints.UN_EDIT_CHECK_STATE_SET)
                                .remove(accountId);
                    }
                    
                }
                
            }
        });
        
    }
}
