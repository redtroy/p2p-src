package com.herongwang.p2p.service.impl.tender;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.herongwang.p2p.dao.tender.ITenderDao;
import com.herongwang.p2p.entity.tender.TenderEntity;
import com.herongwang.p2p.service.tender.ITenderService;
import com.sxj.util.exception.ServiceException;
import com.sxj.util.persistent.QueryCondition;

@Service
@Transactional
public class TenderServiceImpl implements ITenderService {
	
	@Autowired
	private ITenderDao tenderDao;

	@Override
	public void addTender(TenderEntity tender) throws ServiceException {
		tenderDao.addTender(tender);
		
	}

	@Override
	public void updateTender(TenderEntity tender) throws ServiceException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public TenderEntity getTenderEntity(String id) throws ServiceException {
		return tenderDao.getTenderFor(id);
	}

	@Override
	public List<TenderEntity> queryTenderList(TenderEntity query)throws ServiceException {
		QueryCondition<TenderEntity> condition = new QueryCondition<TenderEntity>();
        condition.addCondition("name", query.getName());
        condition.setPage(query);
		 List<TenderEntity> contractList = tenderDao.query(condition);
		return contractList;
	}

	@Override
	public void delTenderFor(String id) throws ServiceException {
		// TODO Auto-generated method stub
		
	}
	
	
	

}
