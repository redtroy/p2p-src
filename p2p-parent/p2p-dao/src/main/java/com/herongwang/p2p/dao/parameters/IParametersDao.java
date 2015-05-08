package com.herongwang.p2p.dao.parameters;

import java.util.List;

import com.herongwang.p2p.entity.parameters.ParametersEntity;
import com.sxj.mybatis.orm.annotations.Delete;
import com.sxj.mybatis.orm.annotations.Get;
import com.sxj.mybatis.orm.annotations.Insert;
import com.sxj.mybatis.orm.annotations.Update;
import com.sxj.util.persistent.QueryCondition;

/**
 * 交易明细1
 * @author nishaotang
 *
 */
public interface IParametersDao {

	/**
	 * 添加交易明细
	 * @param Deal
	 */
	@Insert
	public void addDeal(ParametersEntity deal);
	/**
	 * 修改交易明细
	 * @param Deal
	 */
	@Update
	public void updateDeal(ParametersEntity deal);
	/**
	 * 删除交易明细
	 * @param id
	 */
	@Delete
	public void delDeal(String id);
	/**
	 * 查询交易明细
	 * @param id
	 */
	@Get
	public ParametersEntity getDealFor(String id);
	/**
	 * 交易明细高级查询
	 * @param query
	 * @return
	 */
	public List<ParametersEntity> query(QueryCondition<ParametersEntity> query);
	

}
