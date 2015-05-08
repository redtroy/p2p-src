package com.herongwang.p2p.manage.controller.tender;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.util.StringUtils;
import com.herongwang.p2p.entity.parameters.ParametersEntity;
import com.herongwang.p2p.entity.tender.TenderEntity;
import com.herongwang.p2p.manage.controller.BaseController;
import com.herongwang.p2p.service.member.IMemberService;
import com.herongwang.p2p.service.parameters.IParametersService;
import com.herongwang.p2p.service.tender.ITenderService;
import com.sxj.util.exception.WebException;
import com.sxj.util.logger.SxjLogger;


@Controller
@RequestMapping("/tender")
public class TenderController extends BaseController
{
    
    @Autowired
    private ITenderService tenderService;
    @Autowired
    private IParametersService parametersService;
    @Autowired
    private IMemberService memberService;
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
    public String toEdit(String id,String applyId,String name, ModelMap map) throws WebException{
    	ParametersEntity query = new ParametersEntity();
    	query.setType("repaymentType");
    	System.out.println("jgjhgjh");
    	try{
    	List<ParametersEntity> repaymentList = parametersService.queryParameters(query);//还款方式
    	query.setType("tenderType");
    	List<ParametersEntity> tenderList = parametersService.queryParameters(query);//还款方式
	   	 map.put("repaymentList", repaymentList);
	   	 map.put("tenderList", tenderList);
        if (StringUtils.isEmpty(id)) {
        	 map.put("applyId", applyId);
        	 map.put("name", name);
            return "manage/tender/new-tender";
        } else {
        	TenderEntity info = tenderService.getTenderEntity(id);
            map.put("info", info);
       	 map.put("applyId", info.getBorrower());
       	memberService.getMmeberByAccount(info.getBorrower());
       	 map.put("name", memberService.getMmeberByAccount(info.getBorrower()));
            return "manage/tender/new-tender";
        }
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	return "manage/tender/tender-list";
    }
    @RequestMapping("edit")
	public @ResponseBody Map<String, String> addApply(String id,
			String title,String borrower,Integer repaymentType,
			Integer borrpwTime,Double interest,Double minAmount,
			Double maxAmount,Double borrowingAmount,Integer status)throws WebException {
    	TenderEntity tender = new TenderEntity();
    	tender.setTitle(title);
    	tender.setBorrower(borrower);
    	tender.setRepaymentType(repaymentType);
    	tender.setBorrpwTime(borrpwTime);
    	tender.setInterest(interest);
    	tender.setMinAmount(minAmount);
    	tender.setMaxAmount(maxAmount);
    	tender.setBorrowingAmount(borrowingAmount);
    	tender.setMarkId("M001");
    	try {
			if(null==id||id.isEmpty()){
				tender.setCreateTime(new Date());
				tender.setStatus(1);
				tenderService.addTender(tender);
			}else{
				TenderEntity info = tenderService.getTenderEntity(id);
				tender.setCreateTime(info.getCreateTime());
				tender.setId(id);
				tender.setStatus(status);
				tenderService.updateTender(tender);
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
