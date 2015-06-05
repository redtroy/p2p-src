package com.herongwang.p2p.service.impl.repayplan;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.herongwang.p2p.dao.account.IAccountDao;
import com.herongwang.p2p.dao.debt.IDebtDao;
import com.herongwang.p2p.dao.financing.IFinancingOrdersDao;
import com.herongwang.p2p.dao.investorder.IInvestOrderDao;
import com.herongwang.p2p.dao.profitlist.IProfitListDao;
import com.herongwang.p2p.dao.repayPlan.IRepayPlanDao;
import com.herongwang.p2p.dao.users.IUsersDao;
import com.herongwang.p2p.entity.account.AccountEntity;
import com.herongwang.p2p.entity.debt.DebtEntity;
import com.herongwang.p2p.entity.financing.FinancingOrdersEntity;
import com.herongwang.p2p.entity.funddetail.FundDetailEntity;
import com.herongwang.p2p.entity.investorder.InvestOrderEntity;
import com.herongwang.p2p.entity.profitlist.ProfitListEntity;
import com.herongwang.p2p.entity.repayPlan.RepayPlanEntity;
import com.herongwang.p2p.entity.users.UsersEntity;
import com.herongwang.p2p.loan.util.Common;
import com.herongwang.p2p.loan.util.RsaHelper;
import com.herongwang.p2p.model.loan.LoanInfoBean;
import com.herongwang.p2p.model.post.TransferModel;
import com.herongwang.p2p.service.funddetail.IFundDetailService;
import com.herongwang.p2p.service.impl.post.PostServiceImpl;
import com.herongwang.p2p.service.repayplan.IRepayPlanService;
import com.sxj.util.exception.ServiceException;
import com.sxj.util.logger.SxjLogger;
import com.sxj.util.persistent.QueryCondition;

@Service
@Transactional
public class RepayPlanServiceImpl implements IRepayPlanService
{
    
    @Autowired
    IRepayPlanDao repayPlanDao;
    
    @Autowired
    IAccountDao accountDao;
    
    @Autowired
    IFinancingOrdersDao financingOrdersDao;
    
    @Autowired
    IFundDetailService fundDetailService;
    
    @Autowired
    private IInvestOrderDao investOrderDao;
    
    @Autowired
    private IProfitListDao profitListDao;
    
    @Autowired
    IDebtDao debtDao;
    
    @Autowired
    private IUsersDao userDao;
    
    @Autowired
    private PostServiceImpl postService;
    
    @Override
    public void addRepayPlan(RepayPlanEntity plan) throws ServiceException
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void updateRepayPlan(RepayPlanEntity plan) throws ServiceException
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public RepayPlanEntity getRepayPlan(String id) throws ServiceException
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public List<RepayPlanEntity> queryRepayPlan(FinancingOrdersEntity order)
            throws ServiceException
    {
        try
        {
            List<RepayPlanEntity> planList;
            QueryCondition<RepayPlanEntity> condition = new QueryCondition<RepayPlanEntity>();
            condition.addCondition("orderId", order.getOrderId());//订单id
            condition.addCondition("debtId", order.getDebtId());//订单id
            planList = repayPlanDao.queryRepayPlan(condition);
            return planList;
        }
        catch (ServiceException e)
        {
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new ServiceException(e.getMessage());
        }
        catch (Exception e)
        {
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new ServiceException("查询还款计划错误", e);
        }
    }
    
    @Override
    public void delRepayPlan(String id) throws ServiceException
    {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * 验证余额是否充足
     */
    @Override
    @Transactional
    public String getBalance(String ids, String orderId, String debtId,
            String url) throws ServiceException
    {
        try
        {
            //获取到还款计划
            List<RepayPlanEntity> planlist = repayPlanDao.getRepayPlanList(ids.split(","));
            BigDecimal monthAmount = new BigDecimal(0);
            List<Integer> xhlist = new ArrayList<Integer>();//获取还款序号
            //统计所有还款总价格
            for (RepayPlanEntity repayPlanEntity : planlist)
            {
                monthAmount = monthAmount.add(repayPlanEntity.getMonthAmount());
                xhlist.add(repayPlanEntity.getSequence());
            }
            AccountEntity account = accountDao.getAccountByOrderId(orderId);
            UsersEntity user = userDao.getUserById(account.getCustomerId());
            String[] qddBalance = postService.balanceQuery(user.getMoneymoremoreId(),
                    "1");
            String[] qddBalance1 = qddBalance[1].split("\\|");
            BigDecimal qb = BigDecimal.valueOf(Double.valueOf(qddBalance1[0]))
                    .multiply(new BigDecimal(100));
            BigDecimal blance = account.getBalance();
            int flag = qb.compareTo(monthAmount);
            
            if (flag == -1)
            {
                return "no";
            }
            else
            {
                //余额足够直接还款
                //乾多多平台操作
                String privatekey = Common.privateKeyPKCS8;
                List<Map<String, String>> list = getTransferList(ids,
                        orderId,
                        debtId);
                List<LoanInfoBean> listmlib = new ArrayList<LoanInfoBean>();
                if (list.size() > 0)
                {
                    for (Map<String, String> maplist : list)
                    {
                        LoanInfoBean mlib = new LoanInfoBean();
                        mlib.setLoanOutMoneymoremore(user.getMoneymoremoreId());//付款人
                        mlib.setLoanInMoneymoremore(maplist.get("moneymoremoreId"));//收款人
                        mlib.setOrderNo(maplist.get("orderNo"));//订单号,投资人收益明细的ID
                        mlib.setBatchNo(maplist.get("debtNo"));//标号
                        mlib.setAmount(maplist.get("amount"));
                        mlib.setTransferName("还款");
                        listmlib.add(mlib);
                    }
                }
                String LoanJsonList = Common.JSONEncode(listmlib);
                TransferModel tf = new TransferModel();
                tf.setPlatformMoneymoremore("p1190");
                tf.setTransferAction("2");
                tf.setAction("2");
                tf.setTransferType("2");
                tf.setNeedAudit("1");
                tf.setReturnURL(url + "repayPlan/transferReturn.htm");
                tf.setNotifyURL(url + "repayPlan/transferNotify.htm");
                tf.setRemark1(Common.UrlEncoder(ids, "utf-8"));//还款单的ID
                tf.setRemark2(orderId);//投资订单号
                tf.setRemark3(debtId);//标的ID
                String dataStr = LoanJsonList + tf.getPlatformMoneymoremore()
                        + tf.getTransferAction() + tf.getAction()
                        + tf.getTransferType() + tf.getNeedAudit()
                        + tf.getRandomTimeStamp() + tf.getRemark1()
                        + tf.getRemark2() + tf.getRemark3() + tf.getReturnURL()
                        + tf.getNotifyURL();
                RsaHelper rsa = RsaHelper.getInstance();
                String SignInfo = rsa.signData(dataStr, privatekey);
                LoanJsonList = Common.UrlEncoder(LoanJsonList, "utf-8");
                tf.setLoanJsonList(LoanJsonList);
                tf.setSignInfo(SignInfo);
                String bsflag = postService.transfer(tf);
                //=================================================================平台操作
                if (!"ok".equals(bsflag))
                {
                    return "qdd";
                }
                account.setBalance(account.getBalance().subtract(monthAmount));//减去余额
                accountDao.updateAccount(account);
                repayPlanDao.updateRepayPlanStatus(ids);//还款状态
                Integer num = repayPlanDao.getRepayPlanCount(orderId);
                if (num == 0)
                {
                    DebtEntity db = new DebtEntity();
                    db.setDebtId(debtId);
                    db.setStatus(5);
                    debtDao.updateDebt(db);
                }
                fundDetailService.repayPlanFundDetail(planlist, blance);//还款资金明细
                //投资方收款
                investGetMoney(debtId, xhlist);
                return "ok";
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
            throw new ServiceException("验证余额错误", e);
        }
    }
    
    @Override
    public String saveRepayPlan(String[] ids, String orderId, String debtId)
            throws ServiceException
    {
        try
        {
            //获取到还款计划
            List<RepayPlanEntity> planlist = repayPlanDao.getRepayPlanList(ids);
            List<Integer> xhlist = new ArrayList<Integer>();//获取还款序号
            BigDecimal blance = new BigDecimal(0);
            for (RepayPlanEntity repayPlanEntity : planlist)
            {
                /*  AccountEntity account = accountDao.getAccountByOrderId(orderId);//账户余额
                blance = account.getBalance();
                int flag = account.getBalance()
                        .compareTo(repayPlanEntity.getMonthAmount());//0 相等 1大于  -1 小于
                                if (flag >= 0)
                                {
                                    //扣除款项
                //                    BigDecimal b = account.getBalance()
                //                            .subtract(repayPlanEntity.getMonthAmount());
                //                    account.setBalance(b);//减去余额
                //                    accountDao.updateAccount(account);//更新账户余额
                                    repayPlanEntity.setStatus(1);//
                                    repayPlanDao.updateRepayPlan(repayPlanEntity);//更新状态
                                    Integer num = repayPlanDao.getRepayPlanCount(orderId);
                                    if (num == 0)
                                    {
                                        DebtEntity db = new DebtEntity();
                                        db.setDebtId(debtId);
                                        db.setStatus(5);
                                        debtDao.updateDebt(db);
                                    }
                                }
                                else
                                {
                                    //账户余额不足以还款计划
                                    account.setDebtAmount(account.getDebtAmount()
                                            .add(repayPlanEntity.getMonthAmount()));//负债总额
                                    accountDao.updateAccount(account);//更新账户余额
                                    repayPlanEntity.setStatus(1);//
                                    repayPlanEntity.setPrepaidStatus(1);//0:为垫付1:垫付
                                    repayPlanDao.updateRepayPlan(repayPlanEntity);//更新状态
                                    Integer num = repayPlanDao.getRepayPlanCount(orderId);
                                    if (num == 0)
                                    {
                                        DebtEntity db = new DebtEntity();
                                        db.setDebtId(debtId);
                                        db.setStatus(5);
                                        debtDao.updateDebt(db);
                                    }
                                }*/
                repayPlanEntity.setStatus(1);//
                repayPlanDao.updateRepayPlan(repayPlanEntity);//更新状态
                Integer num = repayPlanDao.getRepayPlanCount(orderId);
                if (num == 0)
                {
                    DebtEntity db = new DebtEntity();
                    db.setDebtId(debtId);
                    db.setStatus(5);
                    debtDao.updateDebt(db);
                }
                xhlist.add(repayPlanEntity.getSequence());
            }
            //  fundDetailService.repayPlanFundDetail(planlist, blance);//还款资金明细
            //投资方收款
            investGetMoney(debtId, xhlist);
            return "ok";
        }
        catch (ServiceException e)
        {
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new ServiceException(e.getMessage());
        }
        catch (Exception e)
        {
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new ServiceException("验证余额错误", e);
        }
    }
    
    //投资方收款并生成明细
    public void investGetMoney(String debtId, List<Integer> xhlist)
    {
        //投资方收款
        List<InvestOrderEntity> orderList = investOrderDao.queryInvestorderList(debtId);//根据标的ID获取投资订单详情
        DebtEntity debt = debtDao.getDebtFor(debtId);
        for (InvestOrderEntity orderEntity : orderList)
        {
            for (Integer se : xhlist)
            {
                if (orderEntity.getStatus() == 0)
                {
                    continue;
                }
                ProfitListEntity entity = profitListDao.getEntityBySeAndOrderId(se.toString(),
                        orderEntity.getOrderId());//通过订单ID和序号获取投资收益
                entity.setStatus(1);
                profitListDao.updateProfitList(entity);//跟新投资方收益明细
                AccountEntity account = accountDao.getAcoountByCustomerId(orderEntity.getCustomerId());//获取账户信息
                account.setBalance(account.getBalance()
                        .add(entity.getMonthCapital()));//账户增加月本金
                account.setDueAmount(account.getDueAmount()
                        .subtract(entity.getMonthCapital()));
                FundDetailEntity fund1 = new FundDetailEntity();
                fund1.setCustomerId(orderEntity.getCustomerId());
                fund1.setAccountId(account.getAccountId());
                fund1.setOrderId(orderEntity.getOrderId());
                fund1.setAmount(entity.getMonthCapital());//金额 
                fund1.setBalance(account.getBalance());//账户可用额
                fund1.setFrozenAmount(account.getFozenAmount());
                fund1.setDueAmount(account.getDueAmount());//代收金额
                fund1.setCreateTime(new Date());
                fund1.setStatus(1);
                fund1.setRemark("被偿还投资" + debt.getTitle() + "第" + se + "期月本金");
                fund1.setType(7);
                fundDetailService.addFundDetail(fund1);
                account.setBalance(account.getBalance()
                        .add(entity.getMonthProfit()));
                account.setDueAmount(account.getDueAmount()
                        .subtract(entity.getMonthProfit()));
                FundDetailEntity fund2 = new FundDetailEntity();
                fund2.setCustomerId(orderEntity.getCustomerId());
                fund2.setAccountId(account.getAccountId());
                fund2.setOrderId(orderEntity.getOrderId());
                fund2.setAmount(entity.getMonthProfit());//金额 
                fund2.setBalance(account.getBalance());//账户可用额
                fund2.setFrozenAmount(account.getFozenAmount());
                fund2.setDueAmount(account.getDueAmount());//代收金额
                fund2.setCreateTime(new Date());
                fund2.setRemark("被偿还投资" + debt.getTitle() + "第" + se + "期月收益");
                fund2.setStatus(1);
                fund2.setType(8);
                fundDetailService.addFundDetail(fund2);
                if (entity.getFee() == null)
                {
                    entity.setFee(new BigDecimal(0));
                }
                account.setBalance(account.getBalance()
                        .subtract(entity.getFee()));
                FundDetailEntity fund3 = new FundDetailEntity();
                fund3.setCustomerId(orderEntity.getCustomerId());
                fund3.setAccountId(account.getAccountId());
                fund3.setOrderId(orderEntity.getOrderId());
                fund3.setAmount(entity.getFee());//金额 
                fund3.setBalance(account.getBalance());//账户可用额
                fund3.setFrozenAmount(account.getFozenAmount());
                fund3.setDueAmount(account.getDueAmount());//代收金额
                fund3.setRemark("投资" + debt.getTitle() + "第" + se + "期平台管理费");
                fund3.setCreateTime(new Date());
                fund3.setStatus(0);
                fund3.setType(11);
                fundDetailService.addFundDetail(fund3);
                accountDao.updateAccount(account);
            }
        }
    }
    
    @Override
    public List<Map<String, String>> getTransferList(String ids,
            String orderId, String debtId) throws ServiceException
    {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        try
        {
            String[] rids = ids.split(",");
            List<RepayPlanEntity> planlist = repayPlanDao.getRepayPlanList(rids);
            List<InvestOrderEntity> orderList = investOrderDao.queryInvestorderList(debtId);//根据标的ID获取投资订单详情
            String debtNo = debtDao.getDebtFor(debtId).getDebtNo();
            for (InvestOrderEntity order : orderList)
            {
                if (order.getStatus() == 0)
                {
                    continue;
                }
                
                String moneymoremoreId = userDao.getUserById(order.getCustomerId())
                        .getMoneymoremoreId();
                for (RepayPlanEntity rp : planlist)
                {
                    Map<String, String> map = new HashMap<String, String>();
                    ProfitListEntity entity = profitListDao.getEntityBySeAndOrderId(rp.getSequence()
                            .toString(),
                            order.getOrderId());//通过订单ID和序号获取投资收益
                    map.put("moneymoremoreId", moneymoremoreId);
                    map.put("amount",
                            (entity.getMonthAmount().divide(new BigDecimal(100))).toString());
                    map.put("orderNo", entity.getProfitId());
                    map.put("debtNo", debtNo);
                    list.add(map);
                    
                }
                
            }
            
        }
        catch (Exception e)
        {
            SxjLogger.error("查询用户乾多多平台标识和返还金额出错", e.getClass());
            throw new ServiceException(e.getMessage());
        }
        return list;
    }
}
