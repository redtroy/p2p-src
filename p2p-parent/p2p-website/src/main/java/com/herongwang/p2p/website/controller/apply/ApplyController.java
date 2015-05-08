package com.herongwang.p2p.website.controller.apply;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/apply")
public class ApplyController
{
    
    /**
     * 跳转申请融资
     * @param session
     * @param map
     * @return
     */
    @RequestMapping("toApply")
    public String accountList(HttpSession session,
            ModelMap map) {
        System.err.println(213123);
        return "site/apply/apply";
    }
}
