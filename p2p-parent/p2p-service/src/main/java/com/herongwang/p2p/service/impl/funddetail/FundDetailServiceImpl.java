package com.herongwang.p2p.service.impl.funddetail;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.herongwang.p2p.dao.account.IAccountDao;
import com.herongwang.p2p.dao.funddetail.IFundDetailDao;
import com.herongwang.p2p.entity.account.AccountEntity;
import com.herongwang.p2p.entity.debt.DebtEntity;
import com.herongwang.p2p.entity.fee.DiscountEntity;
import com.herongwang.p2p.entity.funddetail.FundDetailEntity;
import com.herongwang.p2p.entity.investorder.InvestOrderEntity;
import com.herongwang.p2p.entity.orders.OrdersEntity;
import com.herongwang.p2p.service.debt.IDebtService;
import com.herongwang.p2p.service.fee.IDiscountService;
import com.herongwang.p2p.service.funddetail.IFundDetailService;
import com.herongwang.p2p.service.orders.IOrdersService;
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
            fd.setType(4);//投标
            fd.setRemark("投资" + debt.getCapitalUses() + ",资金冻结");
            fundDetailDao.addFundDetail(fd);//插入总金额明细
            
            //获取到当前会员折扣
            List<DiscountEntity> list = discountService.getDiscountByCustomerId(io.getCustomerId());
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
                        fd.setAmount(io.getAmount().multiply(fee));
                        fd.setType(5);
                        fd.setRemark("投资" + debt.getCapitalUses() + ",手续费冻结");
                        fundDetailDao.addFundDetail(fd);//插入手续费
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
            fd.setStatus(1);//
            if (order.getStatus() == 1)
            {
                fd.setType(1);//投标
                fd.setRemark("充值" + order.getAmount() + "成功");
            }
            else if (order.getStatus() == 2)
            {
                fd.setType(1);//投标
                fd.setRemark("提现" + order.getAmount() + "成功");
            }
            
            fundDetailDao.addFundDetail(fd);//插入总金额明细
            
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
