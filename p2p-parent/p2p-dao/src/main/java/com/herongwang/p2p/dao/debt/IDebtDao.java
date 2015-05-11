package com.herongwang.p2p.dao.debt;

import java.util.List;

import com.herongwang.p2p.entity.debt.DebtEntity;
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
public interface IDebtDao
{
    /**
     * 添加借款标
     * @param Debt
     */
    @Insert
    public void addDebt(DebtEntity Debt);
    
    /**
     * 修改借款标
     * @param Debt
     */
    @Update
    public void updateDebt(DebtEntity Debt);
    
    /**
     * 删除借款标
     * @param id
     */
    @Delete
    public void delDebt(String id);
    
    /**
     * 查询借款标
     * @param id
     */
    @Get
    public DebtEntity getDebtFor(String id);
    
    /**
     * 借款标高级查询
     * @param query
     * @return
     */
    public List<DebtEntity> query(QueryCondition<DebtEntity> query);
    
    /**
     * 查询标的前5条
     */
    public List<DebtEntity> queryTop5();
}
