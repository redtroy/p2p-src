package com.herongwang.p2p.service.impl.investorder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.herongwang.p2p.dao.account.IAccountDao;
import com.herongwang.p2p.dao.debt.IDebtDao;
import com.herongwang.p2p.dao.investorder.IInvestOrderDao;
import com.herongwang.p2p.dao.profitlist.IProfitListDao;
import com.herongwang.p2p.entity.account.AccountEntity;
import com.herongwang.p2p.entity.debt.DebtEntity;
import com.herongwang.p2p.entity.investorder.InvestOrderEntity;
import com.herongwang.p2p.entity.profitlist.ProfitListEntity;
import com.herongwang.p2p.model.invest.InvestModel;
import com.herongwang.p2p.model.profit.ProfitModel;
import com.herongwang.p2p.service.debt.IDebtService;
import com.herongwang.p2p.service.funddetail.IFundDetailService;
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
    
    @Autowired
    IAccountDao accountDao;
    
    @Autowired
    IDebtService debtService;
    
    @Autowired
    IFundDetailService fundDetailService;
    
    /**
     * 生成投资订单
     */
    @Override
    @Transactional
    public InvestOrderEntity addOrder(String debtId, String amount,
            String customerId) throws ServiceException
    {
        try
        {
            
            InvestOrderEntity io = new InvestOrderEntity();//投资订单
            ProfitModel pm = profitService.calculatingProfit(debtId,
                    new BigDecimal(amount),customerId);
            io.setDebtId(debtId);
            io.setCustomerId(customerId);
            io.setAmount(new BigDecimal(amount));
            io.setCreateTime(new Date());
            io.setStatus(0);//状态
            io.setDueProfitAmount(pm.getTotalInterest());//本息总额
            io.setDueTotalAmount(pm.getAmount());//收益总额
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
    @Transactional
    public void finishOrder(InvestOrderEntity io) throws ServiceException
    {
        try
        {
            
            InvestOrderEntity newIo = investOrderDao.getInvestOrder(io.getOrderId());
            DebtEntity debt = debtDao.getDebtFor(io.getDebtId());
            ProfitModel pm = profitService.calculatingProfit(newIo.getDebtId(),
                    newIo.getAmount(),newIo.getCustomerId());
            List<ProfitListEntity> profits = new ArrayList<ProfitListEntity>();
            if (debt.getRepayType() == 3)
            {
                ProfitListEntity pl = new ProfitListEntity();
                pl.setProfitId(StringUtils.getUUID());
                pl.setOrderId(io.getOrderId());
                pl.setStatus(0);
                pl.setCreateTime(new Date());
                pl.setFee(newIo.getTotalFee());
                pl.setMonthAmount(newIo.getDueTotalAmount());
                pl.setMonthCapital(newIo.getAmount());
                pl.setMonthProfit(newIo.getDueProfitAmount());
                profits.add(pl);
            }
            else
            {
                for (ProfitListEntity pro : pm.getMonthProfit())
                {
                    pro.setProfitId(StringUtils.getUUID());
                    pro.setOrderId(io.getOrderId());
                    pro.setStatus(0);
                    profits.add(pro);
                }
            }
            List<ProfitListEntity> list;
            QueryCondition<ProfitListEntity> condition = new QueryCondition<ProfitListEntity>();
            condition.addCondition("orderId", io.getOrderId());//会员id
            list = profitListDao.query(condition);
            if (CollectionUtils.isEmpty(list))
            {
                profitListDao.addProfitList(profits);
                //调用支付接口
                if (io.getStatus() == 1)
                {
                    investOrderDao.updateInvestOrder(io);
                    //更新冻结金额
                    //                    AccountEntity account = accountDao.getAcoountByCustomerId(newIo.getCustomerId());
                    //                    account.setFozenAmount(account.getFozenAmount()
                    //                            .add(newIo.getAmount()));
                    //                    accountDao.updateAccount(account);
                    //是否满标
                    int flag = debt.getAmount().compareTo(debt.getFinance()
                            .add(newIo.getAmount()));
                    if (flag == 0)
                    {
                        debt.setStatus(3);
                        debt.setFinishTime(new Date());
                    }
                    else if (flag == 1)
                    {
                        debt.setStatus(1);
                    }
                    else
                    {
                        throw new ServiceException("标的已满,投资失败");
                    }
                    debt.setFinance(debt.getFinance().add(newIo.getAmount()));
                    debtDao.updateDebt(debt);
                    fundDetailService.investFundDetail(newIo);//创建资金明细
                }
            }
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
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
        return investOrderDao.getInvestOrder(id);
    }
    
    @Override
    public List<InvestOrderEntity> queryorderList(InvestOrderEntity query)
            throws ServiceException
    {
        try
        {
            QueryCondition<InvestOrderEntity> condition = new QueryCondition<InvestOrderEntity>();
            List<InvestOrderEntity> investList = new ArrayList<InvestOrderEntity>();
            condition.addCondition("debtId", query.getDebtId());
            condition.setPage(query);
            investList = investOrderDao.query(condition);
            query.setPage(condition);
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
    
    @Override
    public List<InvestModel> queryInvestModel(String custId)
    {
        try
        {
            return investOrderDao.queryDebt(custId);
        }
        catch (Exception e)
        {
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new ServiceException("查询投资订单列表,标的信息错误", e);
        }
    }
    
    /**'
     * 查询收益总额
     */
    @Override
    public int queryDueProfitAmount() throws ServiceException
    {
        try
        {
            Integer amount = investOrderDao.queryDueProfitAmount();
            if (amount != null)
            {
                return amount;
            }
            return 0;
        }
        catch (Exception e)
        {
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new ServiceException("查询收益总额错误", e);
        }
        
    }
    
    @Override
    public int queryAllAmount() throws ServiceException
    {
        try
        {
            Integer amount = investOrderDao.queryAllAmount();
            if (amount != null)
            {
                return amount;
            }
            return 0;
        }
        catch (Exception e)
        {
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new ServiceException("查询投资总额错误", e);
        }
    }
    
}
