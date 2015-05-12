package com.herongwang.p2p.dao.tl;

import java.util.List;

import com.herongwang.p2p.entity.tl.TLBillEntity;
import com.sxj.mybatis.orm.annotations.Delete;
import com.sxj.mybatis.orm.annotations.Get;
import com.sxj.mybatis.orm.annotations.Insert;
import com.sxj.mybatis.orm.annotations.Update;
import com.sxj.util.persistent.QueryCondition;

/**
 * 通联账单Dao
 * @author nishaotang
 *
 */
public interface ITLBillDao
{
    /**
     * 添加通联账单
     * @param Orders
     */
    @Insert
    public void addBill(TLBillEntity bill);
    
    /**
     * 修改通联账单
     * @param Orders
     */
    @Update
    public void updateBill(TLBillEntity bill);
    
    /**
     * 删除通联账单
     * @param id
     */
    @Delete
    public void delBill(String id);
    
    /**
     * 查询通联账单
     * @param id
     */
    @Get
    public TLBillEntity getOrders(String id);
    
    /**
     * 通联账单高级查询
     * @param query
     * @return
     */
    public List<TLBillEntity> query(QueryCondition<TLBillEntity> query);
    
    /**
     * 根据订单号获取通联账单信息
     * @param ordersNo
     * @return
     */
    public TLBillEntity getTLBIllByNo(String ordersNo);
}
