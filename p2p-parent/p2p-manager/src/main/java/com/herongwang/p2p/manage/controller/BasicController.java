package com.herongwang.p2p.manage.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SimpleTimeZone;
import java.util.TreeMap;

import javax.servlet.ServletOutputStream;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import third.rewrite.fastdfs.NameValuePair;
import third.rewrite.fastdfs.service.IStorageClientService;

import com.herongwang.p2p.entity.admin.AdminEntity;
import com.herongwang.p2p.manage.login.SupervisorShiroRedisCache;
import com.herongwang.p2p.manage.login.SupervisorShiroRedisCacheManager;
import com.herongwang.p2p.service.admin.IAdminService;
import com.sxj.cache.manager.CacheLevel;
import com.sxj.cache.manager.HierarchicalCacheManager;
import com.sxj.file.fastdfs.IFileUpLoad;
import com.sxj.spring.modules.mapper.JsonMapper;
import com.sxj.util.common.FileUtil;
import com.sxj.util.common.StringUtils;
import com.sxj.util.exception.WebException;
import com.sxj.util.logger.SxjLogger;

@Controller
public class BasicController extends BaseController
{
    @Autowired
    private IAdminService adminService;
    
    @Autowired
    private IStorageClientService storageClientService;
    
    @Autowired
    private SupervisorShiroRedisCacheManager redesCacheManager;
    
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
    
    /**
     * 检查用户乾多多账户的余额是否够还款
     * @return
     */
    @RequestMapping("checkQddBlance")
    public @ResponseBody String checkQddBlance()
    {
        return "";
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
    @RequestMapping("downloadFile")
    public void downloadFile(HttpServletRequest request,
            HttpServletResponse response, String filePath) throws IOException
    {
        try
        {
            ServletOutputStream output = response.getOutputStream();
            String fileName = "扫描件" + stringDate();
            String group = filePath.substring(0, filePath.indexOf("/"));
            //            response.addHeader("Content-Disposition", "attachment;filename="
            //                    + fileName + ".pdf");
            response.setContentType("image/*");
            String path = filePath.substring(filePath.indexOf("/") + 1,
                    filePath.length());
            storageClientService.downloadFile(group, path, output);
            output.flush();
            output.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            SxjLogger.debug("下载文件出错", e.getClass());
        }
    }
    
    @RequestMapping(value = "{group}/{st}/{f1}/{f2}/{id}", method = RequestMethod.GET)
    public void getImage(@PathVariable String group, @PathVariable String st,
            @PathVariable String f1, @PathVariable String f2,
            @PathVariable String id, HttpServletRequest request,
            HttpServletResponse response) throws WebException
    {
        try
        {
            ServletOutputStream output = response.getOutputStream();
            response.setDateHeader("expries",
                    System.currentTimeMillis() + 1000 * 3600);
            StringBuffer modifyId = new StringBuffer();
            modifyId.append(group);
            modifyId.append("/");
            modifyId.append(st);
            modifyId.append("/");
            modifyId.append(f1);
            modifyId.append("/");
            modifyId.append(f2);
            modifyId.append("/");
            modifyId.append(id);
            modifyId.append("-LastModified");
            SimpleDateFormat dataformat = new SimpleDateFormat(
                    "EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
            dataformat.setTimeZone(new SimpleTimeZone(0, "GMT"));
            
            Object lastModified = HierarchicalCacheManager.get(CacheLevel.REDIS,
                    IFileUpLoad.CACHE_NAME,
                    modifyId.toString());
            if (lastModified == null)
            {
                Date nowdate = new Date();
                lastModified = dataformat.format(nowdate);
                HierarchicalCacheManager.set(CacheLevel.REDIS,
                        IFileUpLoad.CACHE_NAME,
                        modifyId.toString(),
                        lastModified.toString());
                response.addHeader("Last-Modified", lastModified.toString());
            }
            else
            {
                String ifmodify = request.getHeader("If-Modified-Since");
                response.addHeader("Last-Modified", lastModified.toString());
                if (StringUtils.isNotEmpty(ifmodify))
                {
                    Date ifmodifydate = dataformat.parse(ifmodify);
                    Date lastmodifydate = dataformat.parse(lastModified.toString());
                    if (ifmodifydate.getTime() == lastmodifydate.getTime())
                    {
                        // response.setStatus(304);
                        // return;
                    }
                }
            }
            String url = request.getRequestURI();
            String type = url.substring(url.lastIndexOf(".") + 1, url.length());
            // type=type.toLowerCase();
            StringBuffer idbuff = new StringBuffer();
            // idbuff.append(group);
            // idbuff.append("/");
            idbuff.append(st);
            idbuff.append("/");
            idbuff.append(f1);
            idbuff.append("/");
            idbuff.append(f2);
            idbuff.append("/");
            idbuff.append(id);
            id = idbuff.append(".").append(type).toString();
            // id = id.replace("-", "/") + "." + type;
            if (StringUtils.isEmpty(id))
            {
                response.setStatus(404);
                return;
            }
            // id = id.substring("/upload/".length(), id.length());
            int index = id.lastIndexOf(".");
            int index2 = id.indexOf(type);
            if (index2 == index + 1)
            {
                storageClientService.downloadFile(group, id, output);
            }
            else
            {
                String size = id.substring(index2 + type.length(), index);
                id = id.substring(0, index2 + type.length());
                String[] sizes = size.split("[x]");
                int width = Integer.parseInt(sizes[0]);
                int height = Integer.parseInt(sizes[1]);
                if (width > 500 || height > 500)
                {
                    width = 500;
                    height = 500;
                }
                storageClientService.downloadSmallImage(group,
                        id,
                        width,
                        height,
                        output);
            }
            
            type = "image/" + "*";
            //response.setContentType(type);
            // output.write(image);
            output.flush();
            output.close();
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
            SxjLogger.error("获取图片错误", e, this.getClass());
            
        }
        
    }
    
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
    
    /**
     * 更新开关状态
     * @param request
     * @return
     */
    @RequestMapping("switchLoan")
    public @ResponseBody AdminEntity switchLoan(HttpServletRequest request,
            String type)
    {
        AdminEntity admin = adminService.gitAdminEntity("1");
        if (!type.equals("9"))
        {
            admin.setUserId("1");
            Integer status = 0;
            if (admin.getStatus() == 0)
            {
                status = 1;
            }
            admin.setStatus(status);
            adminService.updateAdmin(admin);
        }
        return admin;
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
