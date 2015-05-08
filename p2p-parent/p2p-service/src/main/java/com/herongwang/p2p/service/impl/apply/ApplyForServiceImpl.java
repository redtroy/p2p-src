package com.herongwang.p2p.service.impl.apply;

import java.util.List;

import com.herongwang.p2p.service.apply.IApplyForService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.herongwang.p2p.dao.apply.IApplyForDao;
import com.herongwang.p2p.entity.apply.ApplyForEntity;
import com.herongwang.p2p.model.apply.ApplyForModel;
import com.sxj.util.exception.ServiceException;
import com.sxj.util.persistent.QueryCondition;

@Service
@Transactional
public class ApplyForServiceImpl implements IApplyForService {
	
	@Autowired
	private IApplyForDao applyForDao;
	
	
	@Override
    @Transactional
	public void addApplyFor(ApplyForEntity apply)throws ServiceException {
		try{
			/**
			 * 
			 */
			applyForDao.addApply(apply);
		}catch(Exception e){
			e.printStackTrace();
		}

	}

	@Override
    @Transactional
	public void updateApplyFor(ApplyForEntity apply) throws ServiceException{
		try{
			applyForDao.updateApply(apply);;
		}catch(Exception e){
			e.printStackTrace();
		}

	}

	@Override
    @Transactional(readOnly = true)
	public ApplyForModel getApplyForEntity(String id)throws ServiceException {
		ApplyForModel applyModel = new ApplyForModel();
		try{
			ApplyForEntity applyFor = applyForDao.getApplyFor(id);
			applyModel.setApplyId(applyFor.getApplyId());
			applyModel.setMemberId(applyFor.getMemberId());
		}catch(Exception e){
			e.printStackTrace();
		}
		return applyModel;
	}

	@Override
    @Transactional(readOnly = true)
	public List<ApplyForEntity> queryApplyFors(ApplyForEntity query) throws ServiceException{
		QueryCondition<ApplyForEntity> condition = new QueryCondition<ApplyForEntity>();
        condition.addCondition("name", query.getName());
        condition.setPage(query);
		 List<ApplyForEntity> contractList = applyForDao.query(condition);
		return contractList;
	}

	@Override
    @Transactional
	public void delApplyFor(String id)throws ServiceException {
		try{
			applyForDao.delApply(id);
		}catch(Exception e){
			e.printStackTrace();
		}

	}

}
