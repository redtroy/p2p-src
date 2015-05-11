package com.herongwang.p2p.manage.controller.tender;

import java.math.BigDecimal;
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
import com.herongwang.p2p.entity.debt.DebtEntity;
import com.herongwang.p2p.entity.parameters.ParametersEntity;
import com.herongwang.p2p.manage.controller.BaseController;
import com.herongwang.p2p.service.member.IMemberService;
import com.herongwang.p2p.service.parameters.IParametersService;
import com.herongwang.p2p.service.tender.IDebtService;
import com.sxj.util.exception.WebException;
import com.sxj.util.logger.SxjLogger;


@Controller
@RequestMapping("/tender")
public class DebtController extends BaseController
{
    
    @Autowired
    private IDebtService debtService;
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
	public String rechargeList(DebtEntity entity, ModelMap map) throws WebException{
    	try{
    		if (entity != null)
            {
    			entity.setPagable(true);
            }
	    	List<DebtEntity> list = debtService.queryDebtList(entity);
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
    	List<ParametersEntity> tenderList = parametersService.queryParameters(query);//标的状态
	   	 map.put("repaymentList", repaymentList);
	   	 map.put("tenderList", tenderList);
        if (StringUtils.isEmpty(id)) {
        	 map.put("applyId", applyId);
        	 map.put("name", name);
            return "manage/tender/new-tender";
        } else {
        	DebtEntity info = debtService.getDebtEntity(id);
            map.put("info", info);
       	 map.put("applyId", info.getCustomerId());
       	 map.put("name", "测试"/*memberService.getMmeberByAccount(info.getCustomerId())*/);
            return "manage/tender/new-tender";
        }
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	return "manage/tender/tender-list";
    }
    @RequestMapping("edit")
	public @ResponseBody Map<String, String> addApply(String id,
			String title,String customerId,Integer repayType,
			Integer months,Double annualizedRate,BigDecimal minInvest,
			BigDecimal maxInvest,BigDecimal amount,Integer status)throws WebException {
    	DebtEntity tender = new DebtEntity();
    	tender.setTitle(title);
    	tender.setCustomerId(customerId);
    	tender.setRepayType(repayType);
    	tender.setMonths(months);
    	tender.setAnnualizedRate(annualizedRate);
    	tender.setMinInvest(minInvest);
    	tender.setMaxInvest(maxInvest);
    	tender.setAmount(amount);
    	tender.setOrderId("test001");//等接口。
    	try {
			if(null==id||id.isEmpty()){
				tender.setCreateTime(new Date());
				tender.setStatus(1);
				debtService.addDebt(tender);
			}else{
				DebtEntity info = debtService.getDebtEntity(id);
				tender.setCreateTime(info.getCreateTime());
				tender.setDebtId(id);
				tender.setStatus(status);
				debtService.updateDebt(tender);
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
    		debtService.delDebt(id);
    		Map<String, String> map = new HashMap<String, String>();
    		map.put("isOK", "ok");
    		return map;
    	} catch (Exception e) {
    		e.printStackTrace();
    		throw new WebException(e);
    	}
    }
}