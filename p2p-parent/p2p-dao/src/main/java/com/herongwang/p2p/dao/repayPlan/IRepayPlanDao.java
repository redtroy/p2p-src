package com.herongwang.p2p.dao.repayPlan;

import java.sql.SQLException;
import java.util.List;

import com.herongwang.p2p.entity.repayPlan.RepayPlanEntity;
import com.sxj.mybatis.orm.annotations.Delete;
import com.sxj.mybatis.orm.annotations.Get;
import com.sxj.mybatis.orm.annotations.Insert;
import com.sxj.mybatis.orm.annotations.Update;
import com.sxj.util.persistent.QueryCondition;

/**
 * 还款计划
 * @author anshaoshuai
 *
 */
public interface IRepayPlanDao
{
    /**
     * 添加还款计划
     * @param plan
     */
    @Insert
    public void addRepayPlan(RepayPlanEntity plan);
    
    /**
     * 修改还款计划
     * @param plan
     */
    @Update
    public void updateRepayPlan(RepayPlanEntity plan);
    
    /**
     * 删除还款计划
     * @param id
     */
    @Delete
    public void delRepayPlan(String id);
    
    /**
     * 查询还款计划
     * @param id
     */
    @Get
    public RepayPlanEntity getRepayPlan(String id);
    
    /**
     * 交易明细高级查询
     * @param query
     * @return
     */
    public List<RepayPlanEntity> queryRepayPlan(
            QueryCondition<RepayPlanEntity> query) throws SQLException;
    
    public List<RepayPlanEntity> getRepayPlanList(String... id);
    
    public void updateRepayPlanStatus(String... id);
}
