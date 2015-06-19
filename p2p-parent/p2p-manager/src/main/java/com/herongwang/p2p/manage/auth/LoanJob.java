package com.herongwang.p2p.manage.auth;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.herongwang.p2p.dao.financing.IFinancingOrdersDao;
import com.herongwang.p2p.entity.account.AccountEntity;
import com.herongwang.p2p.entity.admin.AdminEntity;
import com.herongwang.p2p.entity.debt.DebtEntity;
import com.herongwang.p2p.entity.financing.FinancingOrdersEntity;
import com.herongwang.p2p.entity.funddetail.FundDetailEntity;
import com.herongwang.p2p.entity.investorder.InvestOrderEntity;
import com.herongwang.p2p.entity.orders.OrdersEntity;
import com.herongwang.p2p.entity.parameters.ParametersEntity;
import com.herongwang.p2p.entity.profitlist.ProfitListEntity;
import com.herongwang.p2p.entity.repayPlan.RepayPlanEntity;
import com.herongwang.p2p.entity.users.UsersEntity;
import com.herongwang.p2p.model.loan.LoanOrderQueryBean;
import com.herongwang.p2p.model.loan.LoanRechargeOrderQueryBean;
import com.herongwang.p2p.model.loan.LoanWithdrawsOrderQueryBean;
import com.herongwang.p2p.model.post.Loan;
import com.herongwang.p2p.model.post.LoanModel;
import com.herongwang.p2p.service.account.IAccountService;
import com.herongwang.p2p.service.admin.IAdminService;
import com.herongwang.p2p.service.debt.IDebtService;
import com.herongwang.p2p.service.funddetail.IFundDetailService;
import com.herongwang.p2p.service.investorder.IInvestOrderService;
import com.herongwang.p2p.service.orders.IOrdersService;
import com.herongwang.p2p.service.parameters.IParametersService;
import com.herongwang.p2p.service.post.IPostService;
import com.herongwang.p2p.service.profit.IProfitService;
import com.herongwang.p2p.service.repayplan.IRepayPlanService;
import com.herongwang.p2p.service.users.IUserService;
import com.sxj.util.common.StringUtils;
import com.sxj.util.logger.SxjLogger;

/**
 * 定时处理本地客户数据和双乾数据，并更新数据
 * @author nishaotang
 *
 */
public class LoanJob
{
    @Autowired
    IAccountService accountService;
    
    @Autowired
    IFundDetailService fundDetailService;
    
    @Autowired
    private IUserService userService;
    
    @Autowired
    IPostService postService;
    
    @Autowired
    IParametersService parametersService;
    
    @Autowired
    IOrdersService ordersService;
    
    @Autowired
    IProfitService profitService;
    
    @Autowired
    private IInvestOrderService investOrderService;
    
    @Autowired
    private IDebtService debtService;
    
    @Autowired
    private IFinancingOrdersDao financingOrder;
    
    @Autowired
    private IRepayPlanService repayPlanService;
    
    @Autowired
    private IAdminService adminService;
    
    protected void execute()
    {
        try
        {
            AdminEntity user = adminService.gitAdminEntity("1");
            if (null != user && user.getStatus().equals("1"))
            {
                updateOrderQuery();
                updateRecharge();
                updateWithdraws();
                updateAccount();
            }
            
        }
        catch (Exception e)
        {
            SxjLogger.error(e.getMessage(), e, this.getClass());
            e.printStackTrace();
            
        }
    }
    
    /**
     * 更新客户账户余额
     */
    private void updateAccount() throws Exception
    {
        UsersEntity u = new UsersEntity();
        List<UsersEntity> userList = userService.queryUsers(u);
        for (int i = 0; i < userList.size(); i++)
        {
            UsersEntity user = userList.get(i);
            AccountEntity account = accountService.getAccountByCustomerId(user.getCustomerId());
            if (StringUtils.isNotEmpty(user.getMoneymoremoreId()))
            {
                String PlatformId = user.getMoneymoremoreId();
                String platformType = "1";//1.托管账户 2.自有账户
                
                String[] result = postService.balanceQuery(PlatformId,
                        platformType);
                String[] balance = result[1].split("\\|");
                
                String b1 = balance[0];
                BigDecimal b2 = new BigDecimal(b1);
                BigDecimal b3 = account.getBalance()
                        .subtract(multiply(b2))
                        .abs();
                if (StringUtils.isNotEmpty(b1)
                        && b3.compareTo(new BigDecimal(0)) > 0)
                {
                    //添加资金明细
                    FundDetailEntity deal = new FundDetailEntity();
                    deal.setCustomerId(user.getCustomerId());
                    deal.setAccountId(account.getAccountId());
                    deal.setType(13);
                    deal.setCreateTime(new Date());
                    deal.setStatus(1);
                    deal.setAmount((account.getBalance().subtract(multiply(b2))).abs());
                    deal.setBalance(account.getBalance());
                    deal.setDueAmount(account.getDueAmount());
                    deal.setFrozenAmount(account.getFozenAmount());
                    deal.setRemark("系统自动对账，资金以托管账户金额为准！");
                    fundDetailService.addFundDetail(deal);
                    account.setBalance(multiply(new BigDecimal(balance[0])));
                    account.setFozenAmount(multiply(new BigDecimal(balance[2])));
                    accountService.updateAccount(account);
                }
            }
        }
    }
    
    /**
     * 对比本地和双乾客户充值交易记录
     */
    private void updateRecharge() throws Exception
    {
        Loan l = parametersService.getLoan();
        ParametersEntity p = new ParametersEntity();
        p.setType("rechargeTime");//上次对账结束时间
        List<ParametersEntity> postList = parametersService.queryParameters(p);
        p = postList.get(0);
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
        String date = sf.format(new Date());
        UsersEntity u = new UsersEntity();
        List<UsersEntity> userList = userService.queryUsers(u);
        LoanModel loan = new LoanModel();
        loan.setPlatformMoneymoremore(l.getMoremoreId());
        loan.setBeginTime(p.getValue());
        String endTime = date;
        loan.setEndTime(endTime);
        List<LoanRechargeOrderQueryBean> list = postService.rechargeOrderQuery(loan,
                null);
        List<LoanRechargeOrderQueryBean> rList = new ArrayList<LoanRechargeOrderQueryBean>();
        for (int i = 0; i < list.size(); i++)
        {
            LoanRechargeOrderQueryBean recharge = list.get(i);
            for (int j = 0; j < userList.size(); j++)
            {
                UsersEntity user = userList.get(j);
                if (recharge.getRechargeMoneymoremore()
                        .equals(user.getMoneymoremoreId()))
                {
                    rList.add(recharge);
                }
            }
        }
        OrdersEntity entity = new OrdersEntity();
        entity.setOrderType(1);
        //查询充值记录
        List<OrdersEntity> oList = ordersService.queryOrdersList(entity);
        
        //对比平台未接收到成功信息的充值记录并更新
        for (int i = 0; i < rList.size(); i++)
        {
            LoanRechargeOrderQueryBean recharge = rList.get(i);
            for (int j = 0; j < oList.size(); j++)
            {
                OrdersEntity o = oList.get(j);
                if (o.getOrdersNo().equals(recharge.getOrderNo()))
                {
                    if (o.getStatus() == 0
                            && recharge.getRechargeState().equals("1"))
                    {
                        //更新订单未支付成功！
                        o.setStatus(1);
                        o.setLoanNo(recharge.getLoanNo());
                        String rechargeTime = recharge.getRechargeTime();
                        o.setArriveTime(sf.parse(rechargeTime));
                        o.setFeeWithdraws(multiply(new BigDecimal(
                                recharge.getFee())));
                        ordersService.updateOrders(o);
                        //添加资金明细
                        fundDetailService.addFunds(o);
                        rList.remove(i);
                    }
                }
            }
        }
        //更新充值对账时间
        p.setValue(endTime);
        parametersService.updateParameters(p);
    }
    
    /**
     * 对比本地和双乾客户转账交易记录
     */
    private void updateOrderQuery() throws Exception
    {
        Loan l = parametersService.getLoan();
        ParametersEntity p = new ParametersEntity();
        p.setType("loanOrderTime");//上次对账结束时间
        List<ParametersEntity> postList = parametersService.queryParameters(p);
        p = postList.get(0);
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
        String date = sf.format(new Date());
        UsersEntity u = new UsersEntity();
        List<UsersEntity> userList = userService.queryUsers(u);
        LoanModel loan = new LoanModel();
        loan.setPlatformMoneymoremore(l.getMoremoreId());
        loan.setBeginTime(p.getValue());
        String endTime = date;
        loan.setEndTime(endTime);
        List<LoanOrderQueryBean> list = postService.orderQuery(loan,
                l.getPrivatekey());
        List<LoanOrderQueryBean> rList = new ArrayList<LoanOrderQueryBean>();//投标
        List<LoanOrderQueryBean> outList = new ArrayList<LoanOrderQueryBean>();//还款人记录
        List<LoanOrderQueryBean> noList = new ArrayList<LoanOrderQueryBean>();//还款冻结未接收到成功的记录
        for (int i = 0; i < list.size(); i++)
        {
            LoanOrderQueryBean recharge = list.get(i);
            for (int j = 0; j < userList.size(); j++)
            {
                UsersEntity user = userList.get(j);
                if (recharge.getLoanOutMoneymoremore()
                        .equals(user.getMoneymoremoreId()))
                {
                    if (recharge.getTransferAction().equals("1"))
                    {//投标
                        rList.add(recharge);
                    }
                    else if (recharge.getTransferAction().equals("2"))//还款付款人记录
                    {
                        if (recharge.getActState().equals("3"))
                        {
                            outList.add(recharge);
                        }
                        else if (recharge.getActState().equals("1"))
                        {
                            noList.add(recharge);
                        }
                    }
                }
            }
        }
        InvestOrderEntity entity = new InvestOrderEntity();
        //查询投资记录
        List<InvestOrderEntity> oList = investOrderService.queryorderList(entity);
        
        //对比平台未接收到成功信息的投资记录并更新
        for (int i = 0; i < rList.size(); i++)
        {
            LoanOrderQueryBean recharge = rList.get(i);
            for (int j = 0; j < oList.size(); j++)
            {
                InvestOrderEntity o = oList.get(j);
                if (o.getOrderNo().equals(recharge.getOrderNo()))
                {
                    if (o.getStatus() == 0
                            && recharge.getActState().equals("1"))
                    {
                        //更新订单未支付成功！
                        accountService.updateAccountBalance(o.getCustomerId(),
                                multiply(new BigDecimal(recharge.getAmount())),
                                o.getOrderId(),
                                recharge.getLoanNo());
                    }
                }
            }
        }
        
        //获取转账成功但平台为接收到信息的记录
        for (LoanOrderQueryBean loanQuery : outList)
        {
            String profitId = loanQuery.getOrderNo();
            ProfitListEntity pl = profitService.getProfitListEntity(profitId);
            if (null != pl && pl.getStatus() == 0)
            {
                DebtEntity query = new DebtEntity();
                query.setDebtNo(loanQuery.getBatchNo());
                List<DebtEntity> dedtList = debtService.queryDebtList(query);
                if (null != dedtList && dedtList.size() > 0)
                {
                    DebtEntity debt = dedtList.get(0);
                    FinancingOrdersEntity f = financingOrder.getOrderByDebtId(debt.getDebtId());
                    List<RepayPlanEntity> rpList = repayPlanService.queryRepayPlan(f);
                    for (int i = 0; i < rpList.size(); i++)
                    {
                        RepayPlanEntity rpe = rpList.get(i);
                        if (pl.getSequence().equals(rpe.getSequence()))
                        {
                            repayPlanService.repay(rpe.getPlanId(),
                                    f.getOrderId(),
                                    f.getDebtId());
                        }
                    }
                }
            }
        }
        //更新还款冻结未接收到流水号信息的记录
        for (LoanOrderQueryBean loanQuery : noList)
        {
            String profitId = loanQuery.getOrderNo();
            ProfitListEntity pl = profitService.getProfitListEntity(profitId);
            pl.setLoanNo(loanQuery.getLoanNo());
            profitService.update(pl);
            
            repayPlanService.refundAudit(null, loanQuery.getBatchNo());
        }
        //更新转账对账时间
        p.setValue(endTime);
        parametersService.updateParameters(p);
    }
    
    /**
     * 对比本地和双乾客户提现交易记录
     */
    private void updateWithdraws() throws Exception
    {
        Loan l = parametersService.getLoan();
        ParametersEntity p = new ParametersEntity();
        p.setType("withdrawsTime");//上次对账结束时间
        List<ParametersEntity> postList = parametersService.queryParameters(p);
        p = postList.get(0);
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
        String date = sf.format(new Date());
        UsersEntity u = new UsersEntity();
        List<UsersEntity> userList = userService.queryUsers(u);
        LoanModel loan = new LoanModel();
        loan.setPlatformMoneymoremore(l.getMoremoreId());
        loan.setBeginTime(p.getValue());
        String endTime = date;
        loan.setEndTime(endTime);
        List<LoanWithdrawsOrderQueryBean> list = postService.withdrawsOrderQuery(loan,
                null);
        List<LoanWithdrawsOrderQueryBean> rList = new ArrayList<LoanWithdrawsOrderQueryBean>();
        for (int i = 0; i < list.size(); i++)
        {
            LoanWithdrawsOrderQueryBean recharge = list.get(i);
            for (int j = 0; j < userList.size(); j++)
            {
                UsersEntity user = userList.get(j);
                if (recharge.getWithdrawMoneymoremore()
                        .equals(user.getMoneymoremoreId()))
                {
                    rList.add(recharge);
                }
            }
        }
        OrdersEntity entity = new OrdersEntity();
        entity.setOrderType(2);
        //查询提现记录
        List<OrdersEntity> oList = ordersService.queryOrdersList(entity);
        
        //对比平台未接收到成功信息的提现记录并更新
        for (int i = 0; i < rList.size(); i++)
        {
            LoanWithdrawsOrderQueryBean recharge = rList.get(i);
            for (int j = 0; j < oList.size(); j++)
            {
                OrdersEntity o = oList.get(j);
                if (o.getOrdersNo().equals(recharge.getOrderNo()))
                {
                    if (o.getStatus() == 0
                            && recharge.getWithdrawState().equals("1"))
                    {
                        //更新订单未支付成功！
                        o.setStatus(1);
                        o.setLoanNo(recharge.getLoanNo());
                        String rechargeTime = recharge.getWithdrawsTime();
                        o.setArriveTime(sf.parse(rechargeTime));
                        o.setFeeWithdraws(multiply(new BigDecimal(
                                recharge.getFee())));
                        ordersService.updateOrders(o);
                        //添加资金明细
                        fundDetailService.addFunds(o);
                        rList.remove(i);
                    }
                }
            }
        }
        //更新提现对账时间
        p.setValue(endTime);
        parametersService.updateParameters(p);
    }
    
    /**
     * 乘100
     * @param m
     * @return
     */
    private BigDecimal multiply(BigDecimal m)
    {
        if (m == null)
        {
            return new BigDecimal(0);
        }
        BigDecimal b2 = new BigDecimal(100);
        return m.multiply(b2);
    }
}
