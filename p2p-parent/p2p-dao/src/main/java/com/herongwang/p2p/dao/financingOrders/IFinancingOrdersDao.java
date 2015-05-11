package com.herongwang.p2p.dao.financingOrders;

import java.util.List;

import com.herongwang.p2p.entity.financingOrders.FinancingOrdersEntity;
import com.sxj.mybatis.orm.annotations.Delete;
import com.sxj.mybatis.orm.annotations.Get;
import com.sxj.mybatis.orm.annotations.Insert;
import com.sxj.mybatis.orm.annotations.Update;
import com.sxj.util.persistent.QueryCondition;

/**
 * 借款标Dao
 * @author nishaotang
 *
 */
public interface IFinancingOrdersDao {
	/**
	 * 添加借款标
	 * @param Order
	 */
	@Insert
	public void addOrder(FinancingOrdersEntity order);
	/**
	 * 修改借款标
	 * @param Order
	 */
	@Update
	public void updateOrder(FinancingOrdersEntity order);
	/**
	 * 删除借款标
	 * @param id
	 */
	@Delete
	public void delOrder(String id);
	/**
	 * 查询借款标
	 * @param id
	 */
	@Get
	public FinancingOrdersEntity getOrder(String id);
	/**
	 * 借款标高级查询
	 * @param query
	 * @return
	 */
	public List<FinancingOrdersEntity> query(QueryCondition<FinancingOrdersEntity> query);
	
}
