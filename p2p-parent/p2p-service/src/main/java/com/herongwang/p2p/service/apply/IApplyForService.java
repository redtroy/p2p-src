package com.herongwang.p2p.service.apply;

import java.util.List;

import com.herongwang.p2p.entity.apply.ApplyForEntity;
import com.herongwang.p2p.model.apply.ApplyForModel;
import com.sxj.util.exception.ServiceException;

public interface IApplyForService {
	/**
	 * 新增融资申请
	 * aa
	 * @param apply
	 */
	public void addApplyFor(ApplyForEntity apply) throws ServiceException;

	/**
	 * 更新融资申请
	 * 
	 * @param apply
	 */
	public void updateApplyFor(ApplyForEntity apply) throws ServiceException;

	/**
	 * 获取融资申请信息
	 * 
	 * @param id
	 * @return
	 */
	public ApplyForModel getApplyForEntity(String id) throws ServiceException;

	/**
	 * 获取融资申请列表
	 * 
	 * @param query
	 * @return
	 */
	public List<ApplyForEntity> queryApplyFors(ApplyForEntity query)
			throws ServiceException;

	/**
	 * 删除融资申请
	 * 
	 * @param id
	 */
	public void delApplyFor(String id) throws ServiceException;
}
