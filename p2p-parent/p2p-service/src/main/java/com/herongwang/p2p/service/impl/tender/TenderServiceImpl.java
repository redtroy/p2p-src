package com.herongwang.p2p.service.impl.tender;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.herongwang.p2p.dao.tender.IDebtDao;
import com.herongwang.p2p.entity.tender.DebtEntity;
import com.herongwang.p2p.service.tender.ITenderService;
import com.sxj.util.exception.ServiceException;
import com.sxj.util.persistent.QueryCondition;

@Service
@Transactional
public class TenderServiceImpl implements ITenderService {
	
	@Autowired
	private IDebtDao tenderDao;

	@Override
	public void addTender(DebtEntity tender) throws ServiceException {
		tenderDao.addTender(tender);
		
	}

	@Override
	public void updateTender(DebtEntity tender) throws ServiceException {
		tenderDao.updateTender(tender);
		
	}

	@Override
	public DebtEntity getTenderEntity(String id) throws ServiceException {
		return tenderDao.getTenderFor(id);
	}

	@Override
	public List<DebtEntity> queryTenderList(DebtEntity query)throws ServiceException {
		QueryCondition<DebtEntity> condition = new QueryCondition<DebtEntity>();
        condition.addCondition("name", query.getName());
        condition.setPage(query);
		 List<DebtEntity> contractList = tenderDao.query(condition);
		return contractList;
	}

	@Override
	public void delTenderFor(String id) throws ServiceException {
		tenderDao.delTender(id);
		
	}
	
	
	

}
