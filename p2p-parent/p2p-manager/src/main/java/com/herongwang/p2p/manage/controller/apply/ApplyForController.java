package com.herongwang.p2p.manage.controller.apply;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.util.StringUtils;
import com.herongwang.p2p.entity.apply.ApplyForEntity;
import com.herongwang.p2p.manage.controller.BaseController;
import com.herongwang.p2p.model.apply.ApplyForModel;
import com.herongwang.p2p.service.apply.IApplyForService;
import com.sxj.util.exception.WebException;
import com.sxj.util.logger.SxjLogger;


@Controller
@RequestMapping("/apply")
public class ApplyForController extends BaseController
{
    
    @Autowired
    private IApplyForService applyService;
	/**
	 * 融资申请页面
	 * @param entity
	 * @return
	 */
    @RequestMapping("/applyList")
	public String developerList(ApplyForEntity entity, ModelMap map) throws WebException{
    	try{
    		if (entity != null)
            {
    			entity.setPagable(true);
            }
	    	List<ApplyForEntity> list = applyService.queryApplyFors(entity);
            map.put("list", list);
            map.put("query", entity);
			return "manage/apply/apply-list";
    	}catch(Exception e){
            SxjLogger.error("查询融资申请错误", e, this.getClass());
            throw new WebException("查询融资申请错误");
    	}
	}
    @RequestMapping("/toEdit")
    public String toEdit(String id, ModelMap map) throws WebException{
        if (StringUtils.isEmpty(id)) {
            return "manage/apply/";
        } else {
        	ApplyForModel info = applyService.getApplyForEntity(id);
            map.put("info", info);
            return "manage/apply/apply";
        }
        
    }
    @RequestMapping("edit")
	public @ResponseBody Map<String, String> addApply(String applyId,String name,String city,String url,String address,String telPhone)
			throws WebException {
    	ApplyForEntity entity = new ApplyForEntity();
    	entity.setApplyId(applyId);
    	try {
			if(null==applyId||applyId.isEmpty()){
				applyService.addApplyFor(entity);
			}else{
				entity.setApplyId(applyId);
				applyService.updateApplyFor(entity);
			}
			Map<String, String> map = new HashMap<String, String>();
			map.put("isOK", "ok");
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			throw new WebException(e);
		}
	}
    @RequestMapping("delete")
    public @ResponseBody Map<String, String> delApply(String id)
    		throws WebException {
    	try {
    		applyService.delApplyFor(id);
    		Map<String, String> map = new HashMap<String, String>();
    		map.put("isOK", "ok");
    		return map;
    	} catch (Exception e) {
    		e.printStackTrace();
    		throw new WebException(e);
    	}
    }
}
