package com.herongwang.p2p.dao.investorder;

import java.util.List;

import com.herongwang.p2p.entity.investorder.InvestOrderEntity;
import com.herongwang.p2p.model.invest.InvestModel;
import com.sxj.mybatis.orm.annotations.Delete;
import com.sxj.mybatis.orm.annotations.Get;
import com.sxj.mybatis.orm.annotations.Insert;
import com.sxj.mybatis.orm.annotations.Update;
import com.sxj.util.persistent.QueryCondition;

/**
 * 投标订单
 * @author nishaotang
 *
 */
public interface IInvestOrderDao
{
    /**
     * 添加投标订单
     * @param InvestOrder
     */
    @Insert
    public void addInvestOrder(InvestOrderEntity investOrder);
    
    /**
     * 修改投标订单
     * @param InvestOrder
     */
    @Update
    public void updateInvestOrder(InvestOrderEntity investOrder);
    
    /**
     * 删除投标订单
     * @param id
     */
    @Delete
    public void delInvestOrder(String id);
    
    /**
     * 查询投标订单
     * @param id
     */
    @Get
    public InvestOrderEntity getInvestOrder(String id);
    
    /**
     * 投标订单高级查询
     * @param query
     * @return
     */
    public List<InvestOrderEntity> query(QueryCondition<InvestOrderEntity> query);
    
    /**
     * 查询订单，标的详情
     */
    public List<InvestModel> queryDebt(String customerId);
    
    /**
     * 根据DebtId查询投资订单
     */
    public List<InvestOrderEntity> queryInvestorderList(String debtId);
    
    /**
     * 查询所有收益人 总收益
     */
    public Integer queryDueProfitAmount();
    
    /**
     * 查询投资总额
     */
    public Integer queryAllAmount();
    
}
