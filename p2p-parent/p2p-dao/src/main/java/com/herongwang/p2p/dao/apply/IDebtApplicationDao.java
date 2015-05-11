package com.herongwang.p2p.dao.apply;

import java.util.List;

import com.herongwang.p2p.entity.apply.DebtApplicationEntity;
import com.sxj.mybatis.orm.annotations.Delete;
import com.sxj.mybatis.orm.annotations.Get;
import com.sxj.mybatis.orm.annotations.Insert;
import com.sxj.mybatis.orm.annotations.Update;
import com.sxj.util.persistent.QueryCondition;

/**
 * 融资申请1
 * @author nishaotang
 *
 */
public interface IDebtApplicationDao {
	/**
	 * 添加融资申请
	 * @param apply
	 */
	@Insert
	public void addApply(DebtApplicationEntity apply);
	/**
	 * 修改融资申请
	 * @param apply
	 */
	@Update
	public void updateApply(DebtApplicationEntity apply);
	/**
	 * 删除融资申请
	 * @param id
	 */
	@Delete
	public void delApply(String id);
	/**
	 * 查询融资申请
	 * @param id
	 */
	@Get
	public DebtApplicationEntity getApply(String id);
	/**
	 * 融资申请高级查询
	 * @param query
	 * @return
	 */
	public List<DebtApplicationEntity> query(QueryCondition<DebtApplicationEntity> query);
	
}
