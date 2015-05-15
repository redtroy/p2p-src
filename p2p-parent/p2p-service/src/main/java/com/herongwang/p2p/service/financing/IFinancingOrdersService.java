package com.herongwang.p2p.service.financing;

import java.util.List;

import com.herongwang.p2p.entity.financing.FinancingOrdersEntity;
import com.sxj.util.exception.ServiceException;

public interface IFinancingOrdersService {
	/**
	 * 新增融资订单
	 * aa
	 * @param Order
	 */
	public void addOrder(FinancingOrdersEntity order) throws ServiceException;

	/**
	 * 更新融资订单
	 * 
	 * @param Order
	 */
	public void updateOrder(FinancingOrdersEntity order) throws ServiceException;

	/**
	 * 获取融资订单信息
	 * 
	 * @param id
	 * @return
	 */
	public FinancingOrdersEntity getFinancingOrdersEntity(String id) throws ServiceException;

	/**
	 * 获取融资订单列表
	 * 
	 * @param query
	 * @return
	 */
	public List<FinancingOrdersEntity> queryOrderList(FinancingOrdersEntity query)
			throws ServiceException;

	/**
	 * 删除融资订单
	 * 
	 * @param id
	 */
	public void delOrder(String id) throws ServiceException;

    FinancingOrdersEntity getOrderByDebtId(String debtId)
            throws ServiceException;
}
