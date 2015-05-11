package com.herongwang.p2p.service.fundDetail;

import java.sql.SQLException;
import java.util.List;

import com.herongwang.p2p.entity.fundDetail.FundDetailEntity;
import com.sxj.util.exception.ServiceException;

public interface IFundDetailService
{
    /**
     * 新增交易明细
     * @param apply
     */
    public void addFundDetail(FundDetailEntity deal) throws ServiceException;
    
    /**
     * 更新交易明细
     * 
     * @param apply
     */
    public void updateFundDetail(FundDetailEntity deal) throws ServiceException;
    
    /**
     * 获取交易明细信息
     * 
     * @param id
     * @return
     */
    public FundDetailEntity getFundDetail(String id) throws ServiceException;
    
    /**
     * 获取交易明细列表
     * 
     * @param query
     * @return
     * @throws SQLException 
     */
    public List<FundDetailEntity> queryFundDetail(FundDetailEntity query)
            throws ServiceException;
    
    /**
     * 删除交易明细
     * 
     * @param id
     */
    public void delFundDetail(String id) throws ServiceException;
}
