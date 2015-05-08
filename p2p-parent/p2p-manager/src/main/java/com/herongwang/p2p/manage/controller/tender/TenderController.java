package com.herongwang.p2p.manage.controller.tender;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.util.StringUtils;
import com.herongwang.p2p.entity.tender.TenderEntity;
import com.herongwang.p2p.manage.controller.BaseController;
import com.herongwang.p2p.service.tender.ITenderService;
import com.sxj.util.exception.WebException;
import com.sxj.util.logger.SxjLogger;


@Controller
@RequestMapping("/tender")
public class TenderController extends BaseController
{
    
    @Autowired
    private ITenderService tenderService;
	/**
	 * 借款标列表
	 * @param entity
	 * @return
	 */
    @RequestMapping("/tenderList")
	public String rechargeList(TenderEntity entity, ModelMap map) throws WebException{
    	try{
    		if (entity != null)
            {
    			entity.setPagable(true);
            }
	    	List<TenderEntity> list = tenderService.queryTenderList(entity);
            map.put("list", list);
            map.put("query", entity);
			return "manage/tender/tender-list";
    	}catch(Exception e){
    		e.printStackTrace();
            SxjLogger.error("查询借款标信息错误", e, this.getClass());
            throw new WebException("查询借款标信息错误");
    	}
	}
   
    @RequestMapping("/toEdit")
    public String toEdit(String id, ModelMap map) throws WebException{
        if (StringUtils.isEmpty(id)) {
            return "manage/apply/";
        } else {
        	TenderEntity info = tenderService.getTenderEntity(id);
            map.put("info", info);
            return "manage/apply/apply";
        }
        
    }
    @RequestMapping("edit")
	public @ResponseBody Map<String, String> addApply(String id)throws WebException {
    	TenderEntity entity = new TenderEntity();
    	entity.setId(id);
    	try {
			if(null==id||id.isEmpty()){
				tenderService.addTender(entity);
			}else{
				tenderService.updateTender(entity);
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
    		tenderService.delTenderFor(id);
    		Map<String, String> map = new HashMap<String, String>();
    		map.put("isOK", "ok");
    		return map;
    	} catch (Exception e) {
    		e.printStackTrace();
    		throw new WebException(e);
    	}
    }
}
