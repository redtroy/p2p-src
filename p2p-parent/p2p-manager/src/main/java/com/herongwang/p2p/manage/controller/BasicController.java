package com.herongwang.p2p.manage.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import third.rewrite.fastdfs.NameValuePair;
import third.rewrite.fastdfs.service.IStorageClientService;

import com.herongwang.p2p.entity.admin.AdminEntity;
import com.herongwang.p2p.manage.login.SupervisorShiroRedisCache;
import com.herongwang.p2p.service.admin.IAdminService;
import com.sxj.spring.modules.mapper.JsonMapper;
import com.sxj.util.common.FileUtil;
import com.sxj.util.common.StringUtils;
import com.sxj.util.logger.SxjLogger;

@Controller
public class BasicController extends BaseController
{
    @Autowired
    private IAdminService adminService;
    
    @Autowired
    private IStorageClientService storageClientService;
    
    private static final HttpServletRequest DefaultMultipartHttpServletRequest = null;
    
    @RequestMapping("to_login")
    public String to_login()
    {
        return LOGIN;
    }
    
    @RequestMapping("login")
    public String login(String account, String password, HttpSession session,
            HttpServletRequest request, ModelMap map)
    {
        if (account == null || account == "")
        {
            return LOGIN;
        }
        AdminEntity admin = adminService.getAdminEntityByName(account);
        if (admin == null)
        {
            map.put("message", "用户名不存在");
            return LOGIN;
        }
        Subject currentUser = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(account,
                password);
        try
        {
            currentUser.login(token);
            PrincipalCollection principals = SecurityUtils.getSubject()
                    .getPrincipals();
            String userNo = admin.getUserNo();
            SupervisorShiroRedisCache.addToMap(userNo, principals);
        }
        catch (AuthenticationException e)
        {
            map.put("account", account);
            map.put("message", "用户名或密码错误");
            return LOGIN;
            
        }
        if (currentUser.isAuthenticated())
        {
            session.setAttribute("adminInfo", admin);
            return "redirect:" + getBasePath(request) + "user/manage.htm";
        }
        else
        {
            map.put("account", account);
            map.put("message", "登陆失败");
            return LOGIN;
        }
    }
    
    @RequestMapping("filesort")
    public @ResponseBody List<String> fileSort(String fileId)
            throws IOException
    {
        List<String> sortFile = new ArrayList<String>();
        try
        {
            if (StringUtils.isEmpty(fileId))
            {
                return sortFile;
            }
            String[] fileids = fileId.split(",");
            Map<String, String> nameMap = new TreeMap<String, String>();
            Map<String, NameValuePair[]> values = storageClientService.getMetadata(fileids);
            for (String key : values.keySet())
            {
                if (key == null)
                {
                    continue;
                }
                NameValuePair[] value = values.get(key);
                nameMap.put(key, value[0].getValue());
            }
            List<Map.Entry<String, String>> mappingList = null;
            // 通过ArrayList构造函数把map.entrySet()转换成list
            mappingList = new ArrayList<Map.Entry<String, String>>(
                    nameMap.entrySet());
            // 通过比较器实现比较排序
            Collections.sort(mappingList,
                    new Comparator<Map.Entry<String, String>>()
                    {
                        public int compare(Map.Entry<String, String> mapping1,
                                Map.Entry<String, String> mapping2)
                        {
                            return mapping1.getValue()
                                    .compareTo(mapping2.getValue());
                        }
                    });
            for (Map.Entry<String, String> mapping : mappingList)
            {
                sortFile.add(mapping.getKey());
            }
            // Map<String, Object> map = new HashMap<String, Object>();
            // map.put("", sortFile);
        }
        catch (Exception e)
        {
            SxjLogger.error(e.getMessage(), e, this.getClass());
        }
        return sortFile;
        
    }
    
    /**
     * 上传文件
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("upload")
    public void uploadFile(HttpServletRequest request,
            HttpServletResponse response) throws IOException
    {
        Map<String, Object> map = new HashMap<String, Object>();
        if (!(request instanceof DefaultMultipartHttpServletRequest))
        {
            return;
        }
        DefaultMultipartHttpServletRequest re = (DefaultMultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMaps = re.getFileMap();
        Collection<MultipartFile> files = fileMaps.values();
        List<String> fileIds = new ArrayList<String>();
        for (MultipartFile myfile : files)
        {
            if (myfile.isEmpty())
            {
                System.err.println("文件未上传");
            }
            else
            {
                String originalName = myfile.getOriginalFilename();
                String extName = FileUtil.getFileExtName(originalName);
                String filePath = storageClientService.uploadFile(null,
                        new ByteArrayInputStream(myfile.getBytes()),
                        myfile.getBytes().length,
                        extName.toUpperCase());
                SxjLogger.info("siteUploadFilePath=" + filePath,
                        this.getClass());
                fileIds.add(filePath);
                
                // 上传元数据
                NameValuePair[] metaList = new NameValuePair[1];
                metaList[0] = new NameValuePair("originalName", originalName);
                storageClientService.overwriteMetadata(filePath, metaList);
            }
        }
        map.put("fileIds", fileIds);
        String res = JsonMapper.nonDefaultMapper().toJson(map);
        response.setContentType("text/plain;UTF-8");
        PrintWriter out = response.getWriter();
        out.print(res);
        out.flush();
        out.close();
    }
    
    /**
     * 下载文件
     * @param request
     * @param response
     * @param filePath
     * @throws IOException
     */
    //    @RequestMapping("downloadFile")
    //    public void downloadFile(HttpServletRequest request,
    //            HttpServletResponse response, String filePath) throws IOException
    //    {
    //        try
    //        {
    //            ServletOutputStream output = response.getOutputStream();
    //            String fileName = "扫描件" + stringDate();
    //            String group = filePath.substring(0, filePath.indexOf("/"));
    //            response.addHeader("Content-Disposition", "attachment;filename="
    //                    + fileName + ".pdf");
    //            response.setContentType("application/pdf");
    //            String path = filePath.substring(filePath.indexOf("/") + 1,
    //                    filePath.length());
    //            storageClientService.downloadFile(group, path, output);
    //            output.flush();
    //            output.close();
    //        }
    //        catch (IOException e)
    //        {
    //            e.printStackTrace();
    //            SxjLogger.debug("下载文件出错", e.getClass());
    //        }
    //    }
    
    @RequestMapping("logout")
    public String logout(HttpServletRequest request)
    {
        Subject currentUser = SecurityUtils.getSubject();
        currentUser.logout();
        return "redirect:" + getBasePath(request) + "index.htm";
        
    }
    
    @RequestMapping("checkLogin")
    public @ResponseBody String checkLogin()
    {
        if (getUsersEntity() == null)
        {
            return "erro";
        }
        return "ok";
    }
    
    @RequestMapping("leftMenu")
    public String leftMenu(HttpServletRequest request)
    {
        return "manage/leftMenu";
    }
    
    @RequestMapping("userName")
    public @ResponseBody String userName(HttpServletRequest request)
    {
        
        return getUsersEntity().getUserName();
    }
    
    private String stringDate()
    {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);//获取年份         
        int month = cal.get(Calendar.MONTH);//获取月份         
        int day = cal.get(Calendar.DATE);//获取日         
        int hour = cal.get(Calendar.HOUR);//小时          
        int minute = cal.get(Calendar.MINUTE);//分                     
        int second = cal.get(Calendar.SECOND);//秒     
        String date = year + "" + month + "" + day + "" + hour + "" + minute
                + "" + second;
        
        return date;
    }
}
