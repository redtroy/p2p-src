package com.herongwang.p2p.service.impl.funddetail;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.herongwang.p2p.dao.account.IAccountDao;
import com.herongwang.p2p.dao.funddetail.IFundDetailDao;
import com.herongwang.p2p.dao.investorder.IInvestOrderDao;
import com.herongwang.p2p.dao.profitlist.IProfitListDao;
import com.herongwang.p2p.entity.account.AccountEntity;
import com.herongwang.p2p.entity.debt.DebtEntity;
import com.herongwang.p2p.entity.fee.DiscountEntity;
import com.herongwang.p2p.entity.funddetail.FundDetailEntity;
import com.herongwang.p2p.entity.investorder.InvestOrderEntity;
import com.herongwang.p2p.entity.orders.OrdersEntity;
import com.herongwang.p2p.entity.repayPlan.RepayPlanEntity;
import com.herongwang.p2p.service.debt.IDebtService;
import com.herongwang.p2p.service.fee.IDiscountService;
import com.herongwang.p2p.service.funddetail.IFundDetailService;
import com.sxj.util.exception.ServiceException;
import com.sxj.util.logger.SxjLogger;
import com.sxj.util.persistent.QueryCondition;

@Service
@Transactional
public class FundDetailServiceImpl implements IFundDetailService
{
    @Autowired
    IFundDetailDao fundDetailDao;
    
    @Autowired
    IAccountDao accountDao;
    
    @Autowired
    IDiscountService discountService;
    
    @Autowired
    IDebtService debtService;
    
    @Autowired
    IInvestOrderDao investOrderDao;
    
    @Autowired
    IProfitListDao profitListDao;
    
    @Override
    public void addFundDetail(FundDetailEntity deal) throws ServiceException
    {
        fundDetailDao.addFundDetail(deal);
        
    }
    
    @Override
    public void updateFundDetail(FundDetailEntity deal) throws ServiceException
    {
        fundDetailDao.updateFundDetail(deal);
        
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
            condition.addCondition("type", query.getType());//会员id
            condition.addCondition("orderId", query.getOrderId());//会员id
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
    
    /**
     * 投资资金明细(投资成功,冻结金额)
     */
    @Override
    @Transactional
    public void investFundDetail(InvestOrderEntity io)
    {
        try
        {
            FundDetailEntity fd = new FundDetailEntity();
            AccountEntity account = accountDao.getAcoountByCustomerId(io.getCustomerId());
            DebtEntity debt = debtService.getDebtEntity(io.getDebtId());
            fd.setCustomerId(io.getCustomerId());//用户ID
            fd.setAccountId(account.getAccountId());
            fd.setOrderId(io.getOrderId());
            fd.setAmount(io.getAmount());
            fd.setBalance(account.getBalance());
            fd.setFrozenAmount(account.getFozenAmount());
            fd.setDueAmount(account.getDueAmount());
            fd.setCreateTime(new Date());
            fd.setStatus(0);//支出
            fd.setType(3);//投标
            fd.setRemark("投资" + debt.getTitle() + ",资金冻结");
            fundDetailDao.addFundDetail(fd);//插入总金额明细
            
            //            //获取到当前会员折扣
            //            List<DiscountEntity> list = discountService.getDiscountByCustomerId(io.getCustomerId());
            //            //重新设置对象
            //            if (!CollectionUtils.isEmpty(list))
            //            {
            //                for (DiscountEntity discountEntity : list)
            //                {
            //                    if (discountEntity.getType() == 0
            //                            && discountEntity.getFee() != 0)
            //                    {
            //                        fd.setDetailId(null);
            //                        BigDecimal fee = new BigDecimal(discountEntity.getFee()).divide(new BigDecimal(
            //                                100),
            //                                2,
            //                                BigDecimal.ROUND_HALF_UP);
            //                        fd.setAmount(io.getAmount().multiply(fee));
            //                        fd.setType(5);
            //                        fd.setRemark("投资" + debt.getCapitalUses() + ",手续费冻结");
            //                        fundDetailDao.addFundDetail(fd);//插入手续费
            //                    }
            //                }
            //            }
            
        }
        catch (ServiceException e)
        {
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new ServiceException(e.getMessage());
        }
        catch (Exception e)
        {
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new ServiceException("生成资金明细错误", e);
        }
    }
    
    /**
     * 充值提现生成资金明细
     */
    @Override
    @Transactional
    public void orderFundDetail(OrdersEntity order) throws ServiceException
    {
        try
        {
            FundDetailEntity fd = new FundDetailEntity();
            AccountEntity account = accountDao.getAcoountByCustomerId(order.getCustomerId());
            fd.setCustomerId(order.getCustomerId());//用户ID
            fd.setAccountId(account.getAccountId());
            fd.setOrderId(order.getOrderId());
            fd.setAmount(order.getAmount());
            fd.setBalance(account.getBalance());
            fd.setFrozenAmount(account.getFozenAmount());
            fd.setDueAmount(account.getDueAmount());
            fd.setCreateTime(new Date());
            
            if (order.getOrderType() == 1)
            {
                fd.setType(1);//投标
                fd.setStatus(1);//
                fd.setRemark("充值"
                        + order.getAmount().divide(new BigDecimal(100),
                                2,
                                BigDecimal.ROUND_HALF_UP) + "元");
            }
            else if (order.getOrderType() == 2)
            {
                fd.setType(2);//投标
                fd.setStatus(1);//
                fd.setRemark("提现"
                        + order.getAmount().divide(new BigDecimal(100),
                                2,
                                BigDecimal.ROUND_HALF_UP) + "元");
            }
            
            fundDetailDao.addFundDetail(fd);//插入总金额明细
            if (order.getOrderType() == 2)
            {
                List<DiscountEntity> list = discountService.getDiscountByCustomerId(order.getCustomerId());
                //重新设置对象
                if (!CollectionUtils.isEmpty(list))
                {
                    for (DiscountEntity discountEntity : list)
                    {
                        if (discountEntity.getType() == 2
                                && discountEntity.getFee() != 0)
                        {
                            fd.setDetailId(null);
                            BigDecimal fee = new BigDecimal(
                                    discountEntity.getFee()).divide(new BigDecimal(
                                    100),
                                    6,
                                    BigDecimal.ROUND_HALF_UP);
                            fd.setAmount(order.getAmount().multiply(fee));
                            fd.setType(12);
                            fd.setRemark("提现手续费"
                                    + order.getAmount()
                                            .multiply(fee)
                                            .divide(new BigDecimal(100),
                                                    2,
                                                    BigDecimal.ROUND_HALF_UP)
                                    + "元");
                            fundDetailDao.addFundDetail(fd);//插入手续费
                        }
                    }
                }
            }
            
        }
        catch (ServiceException e)
        {
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new ServiceException(e.getMessage());
        }
        catch (Exception e)
        {
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new ServiceException("生成资金明细错误", e);
        }
    }
    
    @Override
    @Transactional
    public void repayPlanFundDetail(List<RepayPlanEntity> planlist,
            BigDecimal blance) throws ServiceException
    {
        try
        {
            List<FundDetailEntity> list = new ArrayList<FundDetailEntity>();
            for (RepayPlanEntity repayPlan : planlist)
            {
                //生成还款资金明细
                FundDetailEntity fd = new FundDetailEntity();
                //获取融资用户信息
                if (blance.intValue() >= repayPlan.getMonthCapital().intValue())
                {
                    blance = blance.subtract(repayPlan.getMonthCapital());
                }
                DebtEntity debt = debtService.getDebtEntity(repayPlan.getDebtId());
                AccountEntity account = accountDao.getAcoountByCustomerId(debt.getCustomerId());
                fd.setCustomerId(debt.getCustomerId());//用户ID
                fd.setAccountId(account.getAccountId());
                fd.setOrderId(repayPlan.getOrderId());
                fd.setAmount(repayPlan.getMonthCapital());//月本金
                fd.setBalance(blance);
                fd.setFrozenAmount(account.getFozenAmount());
                fd.setDueAmount(account.getDueAmount());
                fd.setCreateTime(new Date());
                fd.setStatus(0);//
                fd.setType(7);//偿还本金.
                if (repayPlan.getPrepaidStatus() == 1)
                {
                    fd.setRemark("平台垫付偿还" + debt.getTitle() + "融资第"
                            + repayPlan.getSequence() + "期本金");
                }
                else
                {
                    fd.setRemark("偿还" + debt.getTitle() + "融资第"
                            + repayPlan.getSequence() + "期本金");
                }
                list.add(fd);//融资本金
                FundDetailEntity fd2 = new FundDetailEntity();
                if (blance.intValue() >= repayPlan.getMonthProfit().intValue())
                {
                    blance = blance.subtract(repayPlan.getMonthProfit());
                }
                //融资利息
                fd2.setCustomerId(debt.getCustomerId());//用户ID
                fd2.setAccountId(account.getAccountId());
                fd2.setOrderId(repayPlan.getOrderId());
                fd2.setAmount(repayPlan.getMonthProfit());//月利息
                fd2.setBalance(blance);
                fd2.setFrozenAmount(account.getFozenAmount());
                fd2.setDueAmount(account.getDueAmount());
                fd2.setCreateTime(new Date());
                fd2.setStatus(0);//
                fd2.setType(8);//偿还利息
                fd2.setRemark("偿还" + debt.getTitle() + "融资第"
                        + repayPlan.getSequence() + "期利息");
                list.add(fd2);//融资利息
                //获取到所有的投资人
                //                List<InvestOrderEntity> investOrser = investOrderDao.queryInvestorderList(debt.getDebtId());
                //                for (InvestOrderEntity investOrderEntity : investOrser)
                //                {
                //                    //投资人账户
                //                    AccountEntity InvestAccount = accountDao.getAcoountByCustomerId(investOrderEntity.getCustomerId());
                //                    //收益明细
                //                    QueryCondition<ProfitListEntity> condition = new QueryCondition<ProfitListEntity>();
                //                    condition.addCondition("orderId",
                //                            investOrderEntity.getOrderId());//会员id
                //                    List<ProfitListEntity> profit = profitListDao.query(condition);
                //                    
                //                    for (ProfitListEntity profitListEntity : profit)
                //                    {
                //                        if (profitListEntity.getSequence() == repayPlan.getSequence())
                //                        {
                //                            FundDetailEntity investDetail = new FundDetailEntity();
                //                            investDetail.setCustomerId(investOrderEntity.getCustomerId());//用户ID
                //                            investDetail.setAccountId(InvestAccount.getAccountId());
                //                            investDetail.setOrderId(investOrderEntity.getOrderId());
                //                            investDetail.setAmount(profitListEntity.getMonthCapital());//月本金
                //                            investDetail.setBalance(InvestAccount.getBalance());
                //                            investDetail.setFrozenAmount(InvestAccount.getFozenAmount());
                //                            investDetail.setDueAmount(InvestAccount.getDueAmount());
                //                            investDetail.setCreateTime(new Date());
                //                            investDetail.setStatus(1);//
                //                            investDetail.setType(9);//投资本金
                //                            investDetail.setRemark("被偿还投资" + debt.getTitle() + "第"
                //                                    + profitListEntity.getSequence() + "期本金");
                //                            list.add(investDetail);//投资本金
                //                            FundDetailEntity investDetail2 = new FundDetailEntity();
                //                            investDetail2.setCustomerId(investOrderEntity.getCustomerId());//用户ID
                //                            investDetail2.setAccountId(InvestAccount.getAccountId());
                //                            investDetail2.setOrderId(investOrderEntity.getOrderId());
                //                            investDetail2.setAmount(profitListEntity.getMonthProfit());//月利息
                //                            investDetail2.setBalance(InvestAccount.getBalance());
                //                            investDetail2.setFrozenAmount(InvestAccount.getFozenAmount());
                //                            investDetail2.setDueAmount(InvestAccount.getDueAmount());
                //                            investDetail2.setCreateTime(new Date());
                //                            investDetail2.setStatus(1);//
                //                            investDetail2.setType(10);//投资利息
                //                            investDetail2.setRemark("被偿还投资" + debt.getTitle()
                //                                    + "第" + profitListEntity.getSequence()
                //                                    + "期利息");
                //                            FundDetailEntity investDetail3 = new FundDetailEntity();
                //                            investDetail3.setCustomerId(investOrderEntity.getCustomerId());//用户ID
                //                            investDetail3.setAccountId(InvestAccount.getAccountId());
                //                            investDetail3.setOrderId(investOrderEntity.getOrderId());
                //                            investDetail3.setAmount(profitListEntity.getFee());//月利息手续费
                //                            investDetail3.setBalance(InvestAccount.getBalance());
                //                            investDetail3.setFrozenAmount(InvestAccount.getFozenAmount());
                //                            investDetail3.setDueAmount(InvestAccount.getDueAmount());
                //                            investDetail3.setCreateTime(new Date());
                //                            investDetail3.setStatus(0);//
                //                            investDetail3.setType(11);//收益管理费
                //                            investDetail3.setRemark("投资" + debt.getTitle()
                //                                    + "第" + profitListEntity.getSequence()
                //                                    + "期利息手续费");
                //                            list.add(investDetail3);//投资利息 
                //                        }
                //                    }
                //                }
            }
            
            fundDetailDao.addFundDetailList(list);//插入总金额明细
            //获取到当前会员折扣
            //            List<DiscountEntity> list = discountService.getDiscountByCustomerId(io.getCustomerId());
            //            //重新设置对象
            //            if (!CollectionUtils.isEmpty(list))
            //            {
            //                for (DiscountEntity discountEntity : list)
            //                {
            //                    if (discountEntity.getType() == 0
            //                            && discountEntity.getFee() != 0)
            //                    {
            //                        fd.setDetailId(null);
            //                        BigDecimal fee = new BigDecimal(discountEntity.getFee()).divide(new BigDecimal(
            //                                100),
            //                                2,
            //                                BigDecimal.ROUND_HALF_UP);
            //                        fd.setAmount(io.getAmount().multiply(fee));
            //                        fd.setType(5);
            //                        fd.setRemark("投资" + debt.getCapitalUses() + ",手续费冻结");
            //                        fundDetailDao.addFundDetail(fd);//插入手续费
            //                    }
            //                }
            //            }
        }
        catch (ServiceException e)
        {
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new ServiceException(e.getMessage());
        }
        catch (Exception e)
        {
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new ServiceException("生成资金明细错误", e);
        }
    }
}
