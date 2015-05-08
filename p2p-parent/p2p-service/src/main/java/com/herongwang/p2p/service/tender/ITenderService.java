package com.herongwang.p2p.service.tender;

import java.util.List;

import com.herongwang.p2p.entity.tender.TenderEntity;
import com.sxj.util.exception.ServiceException;

public interface ITenderService {
	/**
	 * 新增借款标
	 * aa
	 * @param tender
	 */
	public void addTender(TenderEntity tender) throws ServiceException;

	/**
	 * 更新借款标
	 * 
	 * @param tender
	 */
	public void updateTender(TenderEntity tender) throws ServiceException;

	/**
	 * 获取借款标信息
	 * 
	 * @param id
	 * @return
	 */
	public TenderEntity getTenderEntity(String id) throws ServiceException;

	/**
	 * 获取借款标列表
	 * 
	 * @param query
	 * @return
	 */
	public List<TenderEntity> queryTenderList(TenderEntity query)
			throws ServiceException;

	/**
	 * 删除借款标
	 * 
	 * @param id
	 */
	public void delTenderFor(String id) throws ServiceException;
}
