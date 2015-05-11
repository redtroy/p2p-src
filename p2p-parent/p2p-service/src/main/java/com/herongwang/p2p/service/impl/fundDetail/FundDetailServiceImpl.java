package com.herongwang.p2p.service.impl.fundDetail;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.type.JdbcType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.herongwang.p2p.dao.fundDetail.IFundDetailDao;
import com.herongwang.p2p.entity.fundDetail.FundDetailEntity;
import com.herongwang.p2p.service.fundDetail.IFundDetailService;
import com.sxj.util.exception.ServiceException;
import com.sxj.util.logger.SxjLogger;
import com.sxj.util.persistent.QueryCondition;

@Service
@Transactional
public class FundDetailServiceImpl implements IFundDetailService
{
    @Autowired
    IFundDetailDao fundDetailDao;
    
    @Override
    public void addFundDetail(FundDetailEntity deal) throws ServiceException
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void updateFundDetail(FundDetailEntity deal) throws ServiceException
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public FundDetailEntity getFundDetail(String id) throws ServiceException
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public List<FundDetailEntity> queryFundDetail(FundDetailEntity query)
            throws ServiceException
    {
        try
        {
            
            List<FundDetailEntity> contractList;
            QueryCondition<FundDetailEntity> condition = new QueryCondition<FundDetailEntity>();
            condition.addCondition("customerId", query.getCustomerId());//会员id
            condition.setPage(query);
            contractList = fundDetailDao.queryFundDetail(condition);
            query.setPage(condition);
            return contractList;
        }
        catch (ServiceException e)
        {
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new ServiceException(e.getMessage());
        }
        catch (Exception e)
        {
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new ServiceException("查询资金明细错误", e);
        }
    }
    
    @Override
    public void delFundDetail(String id) throws ServiceException
    {
        // TODO Auto-generated method stub
        
    }
    
}
