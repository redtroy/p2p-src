package com.herongwang.p2p.dao.parameters;

import java.util.List;

import com.herongwang.p2p.entity.parameters.ParametersEntity;
import com.sxj.mybatis.orm.annotations.Delete;
import com.sxj.mybatis.orm.annotations.Get;
import com.sxj.mybatis.orm.annotations.Insert;
import com.sxj.mybatis.orm.annotations.Update;
import com.sxj.util.persistent.QueryCondition;

/**
 * 参数表
 * @author nishaotang
 *
 */
public interface IParametersDao
{
    
    /**
     * 添加参数信息
     * @param Parameters
     */
    @Insert
    public void addParameters(ParametersEntity entity);
    
    /**
     * 修改参数信息
     * @param Parameters
     */
    @Update
    public void updateParameters(ParametersEntity entity);
    
    /**
     * 删除交易明细
     * @param id
     */
    @Delete
    public void delParameters(String id);
    
    /**
     * 查询交易明细
     * @param id
     */
    @Get
    public ParametersEntity getParameters(String id);
    
    /**
     * 参数信息高级查询
     * @param query
     * @return
     */
    public List<ParametersEntity> query(QueryCondition<ParametersEntity> query);
    
}
