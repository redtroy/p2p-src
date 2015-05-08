package com.herongwang.p2p.service.deal;

import java.util.List;

import com.herongwang.p2p.entity.deal.DealDetailEntity;
import com.sxj.util.exception.ServiceException;

public interface IDealDetailService {
	/**
	 * 新增交易明细
	 * @param apply
	 */
	public void addDealDetail(DealDetailEntity deal) throws ServiceException;

	/**
	 * 更新交易明细
	 * 
	 * @param apply
	 */
	public void updateDealDetail(DealDetailEntity deal) throws ServiceException;

	/**
	 * 获取交易明细信息
	 * 
	 * @param id
	 * @return
	 */
	public DealDetailEntity getDealDetailEntity(String id) throws ServiceException;

	/**
	 * 获取交易明细列表
	 * 
	 * @param query
	 * @return
	 */
	public List<DealDetailEntity> queryApplyFors(DealDetailEntity query)
			throws ServiceException;

	/**
	 * 删除交易明细
	 * 
	 * @param id
	 */
	public void delDealDetail(String id) throws ServiceException;
}
