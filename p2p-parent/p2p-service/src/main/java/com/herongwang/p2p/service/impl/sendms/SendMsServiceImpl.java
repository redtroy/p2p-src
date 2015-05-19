package com.herongwang.p2p.service.impl.sendms;

import java.net.URLEncoder;

import org.springframework.stereotype.Service;

import com.herongwang.p2p.ms.Client;
import com.herongwang.p2p.service.sendms.ISendMsService;
import com.sxj.util.exception.ServiceException;
import com.sxj.util.logger.SxjLogger;

@Service
public class SendMsServiceImpl implements ISendMsService
{
    
    @Override
    public Boolean sendMs(String phones, String content)
            throws ServiceException
    {
        try
        {
            //输入软件序列号和密码
            String sn = "DXX-WSS-11M-06110";
            String pwd = "502503";
            String mobiles = phones;
            content = URLEncoder.encode(content + "（平台注册验证码，10分钟有效）【和融网】",
                    "utf8");
            Client client = new Client(sn, pwd);
            String result_mt = client.mdSmsSend_u(mobiles, content, "", "", "");
            if (result_mt.startsWith("-") || result_mt.equals(""))//发送短信，如果是以负号开头就是发送失败。
            {
                System.out.print("发送失败！返回值为：" + result_mt
                        + "请查看webservice返回值对照表");
                return false;
            }
            //输出返回标识，为小于19位的正数，String类型的。记录您发送的批次。
            else
            {
                System.out.print("发送成功，返回值为：" + result_mt);
                return true;
            }
        }
        catch (Exception e)
        {
            SxjLogger.error("信息发送错误", e.getClass());
            throw new ServiceException();
        }
    }
    
    public static void main(String args[])
    {
        SendMsServiceImpl send = new SendMsServiceImpl();
        send.sendMs("15950555822", "test");
    }
    
}
