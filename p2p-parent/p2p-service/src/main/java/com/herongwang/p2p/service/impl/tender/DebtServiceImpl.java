package com.herongwang.p2p.service.impl.tender;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.herongwang.p2p.dao.debt.IDebtDao;
import com.herongwang.p2p.entity.debt.DebtEntity;
import com.herongwang.p2p.service.tender.IDebtService;
import com.sxj.util.exception.ServiceException;
import com.sxj.util.persistent.QueryCondition;

@Service
@Transactional
public class DebtServiceImpl implements IDebtService {
	
	@Autowired
	private IDebtDao DebtDao;

	@Override
	public void addDebt(DebtEntity Debt) throws ServiceException {
		DebtDao.addDebt(Debt);
		
	}

	@Override
	public void updateDebt(DebtEntity Debt) throws ServiceException {
		DebtDao.updateDebt(Debt);
		
	}

	@Override
	public DebtEntity getDebtEntity(String id) throws ServiceException {
		return DebtDao.getDebtFor(id);
	}

	@Override
	public List<DebtEntity> queryDebtList(DebtEntity query)throws ServiceException {
		QueryCondition<DebtEntity> condition = new QueryCondition<DebtEntity>();
        condition.addCondition("name", query.getName());
        condition.setPage(query);
		 List<DebtEntity> contractList = DebtDao.query(condition);
		return contractList;
	}

	@Override
	public void delDebt(String id) throws ServiceException {
		DebtDao.delDebt(id);
		
	}
	
	
	

}
