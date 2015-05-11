package com.herongwang.p2p.manage.controller.investOrder;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.herongwang.p2p.entity.investorder.InvestOrderEntity;
import com.herongwang.p2p.manage.controller.BaseController;
import com.herongwang.p2p.service.investOrder.IInvestOrderService;
import com.sxj.util.exception.WebException;
import com.sxj.util.logger.SxjLogger;


@Controller
@RequestMapping("/investOrder")
public class InvestOrderController extends BaseController
{
    
    @Autowired
    private IInvestOrderService investOrderService;
	/**
	 * 借款标列表
	 * @param entity
	 * @return
	 */
    @RequestMapping("/orderList")
	public String rechargeList(InvestOrderEntity entity,String id, ModelMap map) throws WebException{
    	try{
    		if (entity != null)
            {
    			entity.setPagable(true);
            }
    		entity.setOrderId(id);
	    	List<InvestOrderEntity> list =investOrderService.queryorderList(entity);
            map.put("list", list);
            map.put("query", entity);
			return "manage/tender/investor-list";
    	}catch(Exception e){
    		e.printStackTrace();
            SxjLogger.error("查询投标订单信息错误", e, this.getClass());
            throw new WebException("查询投标订单息错误");
    	}
	}
    
}
