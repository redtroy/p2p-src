package com.herongwang.p2p.manage.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import com.herongwang.p2p.entity.admin.AdminEntity;
import com.sxj.util.exception.SystemException;
import com.sxj.util.logger.SxjLogger;

public class BaseController
{
    
    public static final String LOGIN = "manage/index";
    
    // public static final String INDEX = "manage/index";
    
    public static final String SPRING_SECURITY_CONTEXT = "SPRING_SECURITY_CONTEXT";
    
    @InitBinder
    public void initBinder(WebDataBinder binder)
    {
        
    }
    
    protected String getBasePath(HttpServletRequest request)
    {
        return request.getScheme() + "://" + request.getServerName() + ":"
                + request.getServerPort() + request.getContextPath() + "/";
    }
    
    // static {
    // CometEngine engine = CometContext.getInstance().getEngine();
    // // 启动 Comet Server Thread
    // MessageThread cometServer = MessageThread.newInstance(engine);
    // RedisTopics redis = RedisTopics.create();
    // RTopic<String> topic1 = redis.getTopic("topic1");
    //
    // // cometServer.run();
    // // cometServer.setDaemon(true);
    // // cometServer.setDelay(3);
    // // cometServer.setPeriod(2);
    // // cometServer.schedule();
    // // MessageConnectListener lis = new MessageConnectListener();
    // engine.addConnectListener(new MessageConnectListener());
    // // MessageDropListener drop = new MessageDropListener();
    // engine.addDropListener(new MessageDropListener());
    //
    // }''
    
    //    protected void registChannel(final String channelName)
    //    {
    //        if (!CometContext.getInstance().getAppModules().contains(channelName))
    //        {
    //            CometContext.getInstance().registChannel(channelName);// 注册应用的channel
    //        }
    //    }
    
    protected String getValidError(BindingResult result) throws SystemException
    {
        String message = "";
        if (result.hasErrors())
        {
            List<ObjectError> errors = result.getAllErrors();
            for (ObjectError error : errors)
            {
                if (error == null)
                {
                    continue;
                }
                message = message + error.getDefaultMessage();
            }
            SxjLogger.error("Nested path [" + result.getNestedPath()
                    + "] has errors [" + message + "]", this.getClass());
            throw new SystemException(message);
        }
        return message;
    }
    
    public AdminEntity getUsersEntity()
    {
        Subject user = SecurityUtils.getSubject();
        return (AdminEntity) user.getPrincipal();
    }
}
