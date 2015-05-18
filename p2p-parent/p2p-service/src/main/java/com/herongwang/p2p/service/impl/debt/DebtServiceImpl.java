package com.herongwang.p2p.service.impl.debt;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.herongwang.p2p.dao.account.IAccountDao;
import com.herongwang.p2p.dao.debt.IDebtDao;
import com.herongwang.p2p.dao.financing.IFinancingOrdersDao;
import com.herongwang.p2p.dao.funddetail.IFundDetailDao;
import com.herongwang.p2p.dao.investorder.IInvestOrderDao;
import com.herongwang.p2p.dao.repayPlan.IRepayPlanDao;
import com.herongwang.p2p.entity.account.AccountEntity;
import com.herongwang.p2p.entity.debt.DebtEntity;
import com.herongwang.p2p.entity.fee.DiscountEntity;
import com.herongwang.p2p.entity.financing.FinancingOrdersEntity;
import com.herongwang.p2p.entity.funddetail.FundDetailEntity;
import com.herongwang.p2p.entity.investorder.InvestOrderEntity;
import com.herongwang.p2p.entity.repayPlan.RepayPlanEntity;
import com.herongwang.p2p.model.profit.ProfitModel;
import com.herongwang.p2p.service.debt.IDebtService;
import com.herongwang.p2p.service.fee.IDiscountService;
import com.herongwang.p2p.service.profit.IProfitService;
import com.sxj.util.exception.ServiceException;
import com.sxj.util.logger.SxjLogger;
import com.sxj.util.persistent.QueryCondition;

@Service
@Transactional
public class DebtServiceImpl implements IDebtService
{
    
    @Autowired
    private IDebtDao DebtDao;
    
    @Autowired
    private IProfitService profitService;
    
    @Autowired
    private IFinancingOrdersDao financingOrder;
    
    @Autowired
    private IRepayPlanDao repayDao;
    
    @Autowired
    private IAccountDao accountDao;
    
    @Autowired
    private IFundDetailDao fundDetailDao;
    
    @Autowired
    private IInvestOrderDao investDao;
    
    @Autowired
    IDiscountService discountService;
    
    @Override
    public void addDebt(DebtEntity Debt) throws ServiceException
    {
        DebtDao.addDebt(Debt);
        
    }
    
    @Override
    public void updateDebt(DebtEntity Debt) throws ServiceException
    {
        DebtDao.updateDebt(Debt);
        
    }
    
    @Override
    public DebtEntity getDebtEntity(String id) throws ServiceException
    {
        return DebtDao.getDebtFor(id);
    }
    
    @Override
    public List<DebtEntity> queryDebtList(DebtEntity query)
            throws ServiceException
    {
        try
        {
            QueryCondition<DebtEntity> condition = new QueryCondition<DebtEntity>();
            List<DebtEntity> debtList = new ArrayList<DebtEntity>();
            if (query == null)
            {
                return debtList;
            }
            condition.addCondition("name", query.getName());
            condition.addCondition("customerId", query.getCustomerId());//会员ID
            condition.setPage(query);
            debtList = DebtDao.query(condition);
            query.setPage(condition);
            return debtList;
        }
        catch (Exception e)
        {
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new ServiceException("查询标的列表信息错误", e);
        }
    }
    
    @Override
    public void delDebt(String id) throws ServiceException
    {
        DebtDao.delDebt(id);
        
    }
    
    @Override
    public List<DebtEntity> queryTop5()
    {
        try
        {
            return DebtDao.queryTop5();
        }
        catch (Exception e)
        {
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new ServiceException("查询标的前5条列表信息错误", e);
        }
    }
    
    @Override
    public String audit(String debtId) throws ServiceException
    {
        try
        {
            DecimalFormat df = new DecimalFormat(".00");
            //更新标的信息
            DebtEntity debt = DebtDao.getDebtFor(debtId); //获取标的信息
            debt.setStatus(4);
            DebtDao.updateDebt(debt);
            //---
            BigDecimal amout = debt.getAmount();
            ProfitModel prift = profitService.calculatingProfit(debtId, amout);//获取利息，总额
            //查询更新融资订单
            FinancingOrdersEntity financeOrder = financingOrder.getOrderByDebtId(debtId);//查询订单
            financeOrder.setLoanAmount(debt.getAmount());
            financeOrder.setActualAmount(debt.getAmount());
            financeOrder.setTotalFee(prift.getAmount());
            financeOrder.setTotalAmount(prift.getInvestment());
            financeOrder.setProfitAmount(prift.getTotalInterest());
            financingOrder.updateOrder(financeOrder);
            //生成还款计划表
            List<RepayPlanEntity> reList = new ArrayList<RepayPlanEntity>();
            BigDecimal monthCapital = new BigDecimal(df.format(debt.getAmount()
                    .divide(new BigDecimal(debt.getMonths()),
                            2,
                            BigDecimal.ROUND_HALF_UP)));
            BigDecimal monthProfit = new BigDecimal(
                    df.format(prift.getTotalInterest().divide(new BigDecimal(
                            debt.getMonths()),
                            2,
                            BigDecimal.ROUND_HALF_UP)));
            for (int i = 0; i < debt.getMonths(); i++)
            {
                RepayPlanEntity repayPlan = new RepayPlanEntity(); //
                repayPlan.setOrderId(financeOrder.getOrderId());
                repayPlan.setDebtId(debtId);
                repayPlan.setSequence(i + 1);
                repayPlan.setMonthCapital(monthCapital); //月本金
                repayPlan.setMonthProfit(monthProfit); //月利息
                repayPlan.setMonthAmount(repayPlan.getMonthCapital()
                        .add(repayPlan.getMonthProfit())); //月总额
                repayPlan.setLeftAmount(prift.getAmount()
                        .subtract(repayPlan.getMonthAmount()
                                .multiply(new BigDecimal(i)))); //剩余本息总额
                repayPlan.setStatus(0);
                repayPlan.setCreateTime(new Date());
                repayPlan.setUpdateTime(new Date());
                repayPlan.setPrepaidStatus(0);
                reList.add(repayPlan);
            }
            repayDao.addRepayPlanList(reList);
            //根据会员ID 查询账户
            AccountEntity account = accountDao.getAcoountByCustomerId(debt.getCustomerId());
            //生成融资方资金明细
            //获取到融资方费率
            FundDetailEntity fd = new FundDetailEntity();
            
            fd.setCustomerId(debt.getCustomerId());//用户ID
            fd.setAccountId(account.getAccountId());
            fd.setOrderId(financeOrder.getOrderId());
            fd.setAmount(debt.getAmount());
            fd.setBalance(account.getBalance());
            fd.setFrozenAmount(account.getFozenAmount());
            fd.setDueAmount(account.getDueAmount());
            fd.setCreateTime(new Date());
            fd.setStatus(1);//收入
            fd.setType(8);//投标
            fd.setRemark("融资"+debt.getTitle()+",完成");
            fundDetailDao.addFundDetail(fd);
            List<DiscountEntity> list = discountService.getDiscountByCustomerId(debt.getCustomerId());
            //重新设置对象
            if (!CollectionUtils.isEmpty(list))
            {
                for (DiscountEntity discountEntity : list)
                {
                    if (discountEntity.getType() == 0
                            && discountEntity.getFee() != 0)
                    {
                        fd.setDetailId(null);
                        BigDecimal fee = new BigDecimal(discountEntity.getFee()).divide(new BigDecimal(
                                100),
                                2,
                                BigDecimal.ROUND_HALF_UP);
                        fd.setAmount(debt.getAmount().multiply(fee));
                        fd.setType(9);
                        fd.setRemark("融资"+debt.getTitle()+"完成");
                        fundDetailDao.addFundDetail(fd);//插入手续费
                    }
                }
            }
            
            //生成投资方 资金明细
            List<InvestOrderEntity> investList = investDao.queryInvestorderList(debtId);
            for (int i = 0; i < investList.size(); i++)
            {
                //查询用户账户
                FundDetailEntity fundDetail = new FundDetailEntity();
                AccountEntity account1 = accountDao.getAcoountByCustomerId(investList.get(i)
                        .getCustomerId());
                fundDetail.setCustomerId(investList.get(i).getCustomerId());//用户ID
                fundDetail.setAccountId(account1.getAccountId());
                fundDetail.setOrderId(investList.get(i).getOrderId());
                fundDetail.setAmount(investList.get(i).getAmount());
                fundDetail.setBalance(account1.getBalance());
                fundDetail.setFrozenAmount(account1.getFozenAmount());
                fundDetail.setDueAmount(account1.getDueAmount());
                fundDetail.setCreateTime(new Date());
                fundDetail.setStatus(0);//收入
                fundDetail.setType(6);//解冻
                fundDetail.setRemark("投资"+debt.getTitle()+"完成,资金解冻");
                fundDetailDao.addFundDetail(fundDetail);
                List<DiscountEntity> disList = discountService.getDiscountByCustomerId(investList.get(i).getCustomerId());
                if (!CollectionUtils.isEmpty(disList))
                {
                    for (DiscountEntity discountEntity : disList)
                    {
                        if (discountEntity.getType() == 0
                                && discountEntity.getFee() != 0)
                        {
                            fundDetail.setDetailId(null);
                            BigDecimal fee = new BigDecimal(discountEntity.getFee()).divide(new BigDecimal(
                                    100),
                                    2,
                                    BigDecimal.ROUND_HALF_UP);
                            fundDetail.setAmount(debt.getAmount().multiply(fee));
                            fundDetail.setType(7);
                            fundDetail.setRemark("投资"+debt.getTitle()+"完成,手续费解冻");
                            fundDetailDao.addFundDetail(fundDetail);//插入手续费
                        }
                    }
                }
            }
            return "ok";
        }
        catch (Exception e)
        {
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new ServiceException("满标审核错误", e);
        }
    }
    
}
