package com.herongwang.p2p.manage.view;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.view.freemarker.FreeMarkerView;

public class MyFreeMarkerView extends FreeMarkerView
{
    
    private static final String CONTEXT_PATH = "basePath";
    
    private static final String SERVER_PORT = "serverPort";
    
    @Override
    protected void exposeHelpers(Map<String, Object> model,
            HttpServletRequest request) throws Exception
    {
        String path = request.getContextPath() + "/";
        model.put(CONTEXT_PATH, path);
        model.put(SERVER_PORT, request.getServerPort());
        super.exposeHelpers(model, request);
    }
}
