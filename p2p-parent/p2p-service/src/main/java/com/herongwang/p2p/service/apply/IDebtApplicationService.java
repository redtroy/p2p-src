package com.herongwang.p2p.service.apply;

import java.util.List;

import com.herongwang.p2p.entity.apply.DebtApplicationEntity;
import com.sxj.util.exception.ServiceException;

public interface IDebtApplicationService {
	/**
	 * 新增融资申请
	 * aa
	 * @param apply
	 */
	public void addApply(DebtApplicationEntity apply) throws ServiceException;

	/**
	 * 更新融资申请
	 * 
	 * @param apply
	 */
	public void updateApply(DebtApplicationEntity apply) throws ServiceException;

	/**
	 * 获取融资申请信息
	 * 
	 * @param id
	 * @return
	 */
	public DebtApplicationEntity getApplyForEntity(String id) throws ServiceException;

	/**
	 * 获取融资申请列表
	 * 
	 * @param query
	 * @return
	 */
	public List<DebtApplicationEntity> queryApply(DebtApplicationEntity query)
			throws ServiceException;

	/**
	 * 删除融资申请
	 * 
	 * @param id
	 */
	public void delApply(String id) throws ServiceException;
}
