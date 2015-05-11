package com.herongwang.p2p.service.investOrder;

import java.util.List;

import com.herongwang.p2p.entity.investOrder.InvestOrderEntity;
import com.sxj.util.exception.ServiceException;

public interface IInvestOrderService {
	/**
	 * 新增投标订单
	 * aa
	 * @param order
	 */
	public void addOrder(InvestOrderEntity order) throws ServiceException;

	/**
	 * 更新投标订单
	 * 
	 * @param order
	 */
	public void updateOrder(InvestOrderEntity order) throws ServiceException;

	/**
	 * 获取投标订单信息
	 * 
	 * @param id
	 * @return
	 */
	public InvestOrderEntity getInvestOrderEntity(String id) throws ServiceException;

	/**
	 * 获取投标订单列表
	 * 
	 * @param query
	 * @return
	 */
	public List<InvestOrderEntity> queryorderList(InvestOrderEntity query)
			throws ServiceException;

	/**
	 * 删除投标订单
	 * 
	 * @param id
	 */
	public void delOrder(String id) throws ServiceException;
}
