package com.herongwang.p2p.service.impl.deal;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.herongwang.p2p.dao.deal.IDealDetailDao;
import com.herongwang.p2p.entity.deal.DealDetailEntity;
import com.herongwang.p2p.service.deal.IDealDetailService;
import com.sxj.util.exception.ServiceException;
import com.sxj.util.persistent.QueryCondition;

@Service
@Transactional
public class DealDetailServiceImpl implements IDealDetailService {
	
	@Autowired
	private IDealDetailDao dealDetailDao;
	
	@Override
	public void addDealDetail(DealDetailEntity deal) throws ServiceException {
		try{
			dealDetailDao.addDeal(deal);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

	@Override
	public void updateDealDetail(DealDetailEntity deal) throws ServiceException {
		try{
			dealDetailDao.updateDeal(deal);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

	@Override
	public DealDetailEntity getDealDetailEntity(String id)throws ServiceException {
		try{
			DealDetailEntity dealDetailEntity = dealDetailDao.getDealFor(id);
			return dealDetailEntity;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<DealDetailEntity> queryApplyFors(DealDetailEntity query) throws ServiceException {
		QueryCondition<DealDetailEntity> condition = new QueryCondition<DealDetailEntity>();
//        condition.addCondition("name", query.getName());
//        condition.addCondition("dealType", query.getDealType());//1：充值；2：投标；3：还款；4：提现。
		 condition.addCondition("memberId", query.getMemberId());//会员id
        condition.setPage(query);
		 List<DealDetailEntity> contractList = dealDetailDao.query(condition);
		return contractList;
	}

	@Override
	public void delDealDetail(String id) throws ServiceException {
		try{
			dealDetailDao.delDeal(id);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	

}
