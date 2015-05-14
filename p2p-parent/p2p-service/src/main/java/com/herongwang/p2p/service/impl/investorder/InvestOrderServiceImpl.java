package com.herongwang.p2p.service.impl.investorder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.herongwang.p2p.dao.debt.IDebtDao;
import com.herongwang.p2p.dao.investorder.IInvestOrderDao;
import com.herongwang.p2p.dao.profitlist.IProfitListDao;
import com.herongwang.p2p.entity.investorder.InvestOrderEntity;
import com.herongwang.p2p.entity.profitlist.ProfitListEntity;
import com.herongwang.p2p.model.profit.ProfitModel;
import com.herongwang.p2p.service.investorder.IInvestOrderService;
import com.herongwang.p2p.service.profit.IProfitService;
import com.sxj.util.common.StringUtils;
import com.sxj.util.exception.ServiceException;
import com.sxj.util.logger.SxjLogger;
import com.sxj.util.persistent.QueryCondition;

@Service
@Transactional
public class InvestOrderServiceImpl implements IInvestOrderService
{
    @Autowired
    IInvestOrderDao investOrderDao;
    
    @Autowired
    IDebtDao debtDao;
    
    @Autowired
    IProfitService profitService;
    
    @Autowired
    IProfitListDao profitListDao;
    
    /**
     * 生成投资订单
     */
    @Override
    @Transactional
    public InvestOrderEntity addOrder(String debtId, String amount)
            throws ServiceException
    {
        try
        {
            InvestOrderEntity io = new InvestOrderEntity();//投资订单
            ProfitModel pm = profitService.calculatingProfit(debtId,
                    new BigDecimal(amount));
            io.setDebtId(debtId);
            io.setAmount(new BigDecimal(amount));
            io.setCreateTime(new Date());
            io.setStatus(0);//状态
            io.setDueProfitAmount(pm.getAmount());//本息总额
            io.setDueTotalAmount(pm.getTotalInterest());//收益总额
            io.setTotalFee(pm.getTotalFee());//平台管理费
            investOrderDao.addInvestOrder(io);
            return io;
        }
        catch (Exception e)
        {
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new ServiceException("生成投资订单错误", e);
        }
        
    }
    
    /**
     * 订单支付完成
     */
    @Override
    public void finishOrder(InvestOrderEntity io) throws ServiceException
    {
        try
        {
            ProfitModel pm = profitService.calculatingProfit(io.getDebtId(),
                    io.getAmount());
            List<ProfitListEntity> profits = new ArrayList<ProfitListEntity>();
            for (ProfitListEntity pro : pm.getMonthProfit())
            {
                pro.setProfitId(StringUtils.getUUID());
                pro.setOrderId(io.getOrderId());
                profits.add(pro);
            }
            profitListDao.addProfitList(profits);
            //调用支付接口
            if (true)
            {
                io.setChannel(0); //渠道
                io.setPayTime(new Date());//支付时间
                io.setArriveTime(new Date());//到账时间
                io.setStatus(1);//支付状态
                investOrderDao.updateInvestOrder(io);
            }
        }
        catch (Exception e)
        {
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new ServiceException("生成收益明细错误", e);
        }
        
    }
    
    @Override
    public void updateOrder(InvestOrderEntity order) throws ServiceException
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public InvestOrderEntity getInvestOrderEntity(String id)
            throws ServiceException
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public List<InvestOrderEntity> queryorderList(InvestOrderEntity query)
            throws ServiceException
    {
        try
        {
            QueryCondition<InvestOrderEntity> condition = new QueryCondition<InvestOrderEntity>();
            List<InvestOrderEntity> investList = new ArrayList<InvestOrderEntity>();
            if (query == null)
            {
                return investList;
            }
            condition.addCondition("debtId", query.getDebtId());
            investList = investOrderDao.query(condition);
            return investList;
        }
        catch (Exception e)
        {
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new ServiceException("查询投资订单列表信息错误", e);
        }
    }
    
    @Override
    public void delOrder(String id) throws ServiceException
    {
        // TODO Auto-generated method stub
        
    }
    
}
