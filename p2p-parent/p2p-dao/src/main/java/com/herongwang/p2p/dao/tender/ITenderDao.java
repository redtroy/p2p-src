package com.herongwang.p2p.dao.tender;

import java.util.List;

import com.herongwang.p2p.entity.tender.TenderEntity;
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
public interface ITenderDao {
	/**
	 * 添加借款标
	 * @param tender
	 */
	@Insert
	public void addTender(TenderEntity tender);
	/**
	 * 修改借款标
	 * @param tender
	 */
	@Update
	public void updateTender(TenderEntity tender);
	/**
	 * 删除借款标
	 * @param id
	 */
	@Delete
	public void delTender(String id);
	/**
	 * 查询借款标
	 * @param id
	 */
	@Get
	public TenderEntity getTenderFor(String id);
	/**
	 * 借款标高级查询
	 * @param query
	 * @return
	 */
	public List<TenderEntity> query(QueryCondition<TenderEntity> query);
	
}
