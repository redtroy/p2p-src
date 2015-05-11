package com.herongwang.p2p.service.tender;

import java.util.List;

import com.herongwang.p2p.entity.tender.DebtEntity;
import com.sxj.util.exception.ServiceException;

public interface ITenderService {
	/**
	 * 新增借款标
	 * aa
	 * @param tender
	 */
	public void addTender(DebtEntity tender) throws ServiceException;

	/**
	 * 更新借款标
	 * 
	 * @param tender
	 */
	public void updateTender(DebtEntity tender) throws ServiceException;

	/**
	 * 获取借款标信息
	 * 
	 * @param id
	 * @return
	 */
	public DebtEntity getTenderEntity(String id) throws ServiceException;

	/**
	 * 获取借款标列表
	 * 
	 * @param query
	 * @return
	 */
	public List<DebtEntity> queryTenderList(DebtEntity query)
			throws ServiceException;

	/**
	 * 删除借款标
	 * 
	 * @param id
	 */
	public void delTenderFor(String id) throws ServiceException;
}
