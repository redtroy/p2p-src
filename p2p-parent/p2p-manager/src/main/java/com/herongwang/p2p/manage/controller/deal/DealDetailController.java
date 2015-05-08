package com.herongwang.p2p.manage.controller.deal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.util.StringUtils;
import com.herongwang.p2p.entity.deal.DealDetailEntity;
import com.herongwang.p2p.manage.controller.BaseController;
import com.herongwang.p2p.service.deal.IDealDetailService;
import com.sxj.util.exception.WebException;
import com.sxj.util.logger.SxjLogger;


@Controller
@RequestMapping("/deal")
public class DealDetailController extends BaseController
{
    
    @Autowired
    private IDealDetailService dealDetailService;
	/**
	 * 充值列表
	 * @param entity
	 * @return
	 */
    @RequestMapping("/recharge")
	public String rechargeList(DealDetailEntity entity, ModelMap map) throws WebException{
    	try{
    		if (entity != null)
            {
    			entity.setPagable(true);
            }
    		entity.setDealType(1);//查询充值记录
	    	List<DealDetailEntity> list = dealDetailService.queryApplyFors(entity);
            map.put("list", list);
            map.put("query", entity);
			return "manage/deal/recharge";
    	}catch(Exception e){
    		e.printStackTrace();
            SxjLogger.error("查询充值信息错误", e, this.getClass());
            throw new WebException("查询充值信息错误");
    	}
	}
    /**
     * 提现列表
     * @param entity
     * @return
     */
    @RequestMapping("/deposit")
    public String depositList(DealDetailEntity entity, ModelMap map) throws WebException{
    	try{
    		if (entity != null)
    		{
    			entity.setPagable(true);
    		}
    		entity.setDealType(4);//提现查询
    		List<DealDetailEntity> list = dealDetailService.queryApplyFors(entity);
    		map.put("list", list);
    		map.put("query", entity);
    		return "manage/deal/deposit";
    	}catch(Exception e){
    		e.printStackTrace();
    		SxjLogger.error("查询提现信息错误", e, this.getClass());
    		throw new WebException("查询提现信息错误");
    	}
    }
    @RequestMapping("/toEdit")
    public String toEdit(String id, ModelMap map) throws WebException{
        if (StringUtils.isEmpty(id)) {
            return "manage/apply/";
        } else {
        	DealDetailEntity info = dealDetailService.getDealDetailEntity(id);
            map.put("info", info);
            return "manage/apply/apply";
        }
        
    }
    @RequestMapping("edit")
	public @ResponseBody Map<String, String> addApply(String dealId)throws WebException {
    	DealDetailEntity entity = new DealDetailEntity();
    	entity.setDealId(dealId);
    	try {
			if(null==dealId||dealId.isEmpty()){
				dealDetailService.addDealDetail(entity);
			}else{
				entity.setDealId(dealId);
				dealDetailService.updateDealDetail(entity);
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
    		dealDetailService.delDealDetail(id);
    		Map<String, String> map = new HashMap<String, String>();
    		map.put("isOK", "ok");
    		return map;
    	} catch (Exception e) {
    		e.printStackTrace();
    		throw new WebException(e);
    	}
    }
}
