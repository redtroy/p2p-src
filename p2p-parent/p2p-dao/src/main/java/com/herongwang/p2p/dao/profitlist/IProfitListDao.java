package com.herongwang.p2p.dao.profitlist;

import java.sql.SQLException;
import java.util.List;

import com.herongwang.p2p.entity.profitlist.ProfitListEntity;
import com.sxj.mybatis.orm.annotations.BatchInsert;

public interface IProfitListDao
{
   /**
    * 添加收益明细
    * @param list
    * @throws SQLException
    */
    @BatchInsert
    public void addProfitList(List<ProfitListEntity> list)throws SQLException;
}
