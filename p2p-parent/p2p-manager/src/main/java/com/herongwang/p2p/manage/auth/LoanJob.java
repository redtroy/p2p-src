package com.herongwang.p2p.manage.auth;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.herongwang.p2p.entity.account.AccountEntity;
import com.herongwang.p2p.entity.funddetail.FundDetailEntity;
import com.herongwang.p2p.entity.investorder.InvestOrderEntity;
import com.herongwang.p2p.entity.orders.OrdersEntity;
import com.herongwang.p2p.entity.parameters.ParametersEntity;
import com.herongwang.p2p.entity.users.UsersEntity;
import com.herongwang.p2p.model.loan.LoanOrderQueryBean;
import com.herongwang.p2p.model.loan.LoanRechargeOrderQueryBean;
import com.herongwang.p2p.model.loan.LoanWithdrawsOrderQueryBean;
import com.herongwang.p2p.model.post.Loan;
import com.herongwang.p2p.model.post.LoanModel;
import com.herongwang.p2p.service.account.IAccountService;
import com.herongwang.p2p.service.funddetail.IFundDetailService;
import com.herongwang.p2p.service.investorder.IInvestOrderService;
import com.herongwang.p2p.service.orders.IOrdersService;
import com.herongwang.p2p.service.parameters.IParametersService;
import com.herongwang.p2p.service.post.IPostService;
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
    private IInvestOrderService investOrderService;
    
    protected void execute()
    {
        try
        {
            updateAccount();
            updateOrderQuery();
            updateRecharge();
            updateWithdraws();
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
                if (StringUtils.isNotEmpty(b1))
                {
                    BigDecimal b2 = new BigDecimal(b1);
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
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
        String date = sf.format(new Date());
        UsersEntity u = new UsersEntity();
        List<UsersEntity> userList = userService.queryUsers(u);
        LoanModel loan = new LoanModel();
        loan.setPlatformMoneymoremore(l.getMoremoreId());
        loan.setBeginTime(p.getValue());
        String endTime = date + "235959";
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
        /* //添加客户明细，平台不存在的充值记录
         for (int i = 0; i < rList.size(); i++)
         {
             LoanRechargeOrderQueryBean recharge = rList.get(i);
             for (int j = 0; j < userList.size(); j++)
             {
                 UsersEntity user = userList.get(j);
                 if (recharge.getRechargeMoneymoremore()
                         .equals(user.getMoneymoremoreId()))
                 {
                     AccountEntity account = accountService.getAccountByCustomerId(user.getCustomerId());
                     //添加资金明细
                     FundDetailEntity deal = new FundDetailEntity();
                     deal.setCustomerId(user.getCustomerId());
                     deal.setAccountId(account.getAccountId());
                     deal.setOrderId(recharge.getOrderNo());
                     deal.setType(1);
                     deal.setCreateTime(new Date());
                     deal.setStatus(1);
                     deal.setAmount(multiply(new BigDecimal(recharge.getAmount())));
                     deal.setBalance(account.getBalance());
                     deal.setDueAmount(account.getDueAmount());
                     deal.setFrozenAmount(account.getFozenAmount());
                     String rechargeTime = recharge.getRechargeTime();
                     deal.setRemark("非本平台充值记录，充值" + recharge.getAmount()
                             + "元成功！订单号：" + recharge.getOrderNo() + ",充值时间："
                             + sf.parse(rechargeTime));
                     if (recharge.getRechargeState().equals("1"))
                     {
                         fundDetailService.addFundDetail(deal);
                     }
                 }
             }
         }*/
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
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
        String date = sf.format(new Date());
        UsersEntity u = new UsersEntity();
        List<UsersEntity> userList = userService.queryUsers(u);
        LoanModel loan = new LoanModel();
        loan.setPlatformMoneymoremore(l.getMoremoreId());
        loan.setBeginTime(p.getValue());
        String endTime = date + "235959";
        loan.setEndTime(endTime);
        List<LoanOrderQueryBean> list = postService.orderQuery(loan, null);
        List<LoanOrderQueryBean> rList = new ArrayList<LoanOrderQueryBean>();
        for (int i = 0; i < list.size(); i++)
        {
            LoanOrderQueryBean recharge = list.get(i);
            for (int j = 0; j < userList.size(); j++)
            {
                UsersEntity user = userList.get(j);
                if (recharge.getLoanInMoneymoremore()
                        .equals(user.getMoneymoremoreId()))
                {
                    rList.add(recharge);
                }
            }
        }
        InvestOrderEntity entity = new InvestOrderEntity();
        //查询充值记录
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
                        rList.remove(i);
                    }
                }
            }
        }
        //更新转账对账时间
        p.setValue(endTime);
        parametersService.updateParameters(p);
        /*//添加客户明细，平台不存在的转账记录
        for (int i = 0; i < rList.size(); i++)
        {
            LoanOrderQueryBean recharge = rList.get(i);
            for (int j = 0; j < userList.size(); j++)
            {
                UsersEntity user = userList.get(j);
                if (recharge.getLoanInMoneymoremore()
                        .equals(user.getMoneymoremoreId())
                        && recharge.getTransferAction().equals("2")//2.还款
                        && recharge.getActState().equals("1"))//1.已通过
                {
                    AccountEntity account = accountService.getAccountByCustomerId(user.getCustomerId());
                    //添加资金明细
                    FundDetailEntity deal = new FundDetailEntity();
                    deal.setCustomerId(user.getCustomerId());
                    deal.setAccountId(account.getAccountId());
                    deal.setOrderId(recharge.getOrderNo());
                    deal.setType(13);
                    deal.setCreateTime(new Date());
                    deal.setStatus(1);
                    deal.setAmount(multiply(new BigDecimal(recharge.getAmount())));
                    deal.setBalance(account.getBalance());
                    deal.setDueAmount(account.getDueAmount());
                    deal.setFrozenAmount(account.getFozenAmount());
                    String rechargeTime = recharge.getActTime();
                    deal.setRemark("非在本平台收款记录，收款" + recharge.getAmount()
                            + "元成功！订单号：" + recharge.getOrderNo() + ",收款时间："
                            + sf.parse(rechargeTime));
                    fundDetailService.addFundDetail(deal);
                }
            }
        }
        //添加客户明细，平台不存在的收款记录
        for (int i = 0; i < rList.size(); i++)
        {
            LoanOrderQueryBean recharge = rList.get(i);
            for (int j = 0; j < userList.size(); j++)
            {
                UsersEntity user = userList.get(j);
                if (recharge.getLoanOutMoneymoremore()
                        .equals(user.getMoneymoremoreId())
                        && recharge.getTransferAction().equals("2")//2.还款
                        && recharge.getActState().equals("1"))//1.已通过
                {
                    AccountEntity account = accountService.getAccountByCustomerId(user.getCustomerId());
                    //添加资金明细
                    FundDetailEntity deal = new FundDetailEntity();
                    deal.setCustomerId(user.getCustomerId());
                    deal.setAccountId(account.getAccountId());
                    deal.setOrderId(recharge.getOrderNo());
                    deal.setType(13);
                    deal.setCreateTime(new Date());
                    deal.setStatus(1);
                    deal.setAmount(multiply(new BigDecimal(recharge.getAmount())));
                    deal.setBalance(account.getBalance());
                    deal.setDueAmount(account.getDueAmount());
                    deal.setFrozenAmount(account.getFozenAmount());
                    String rechargeTime = recharge.getActTime();
                    deal.setRemark("非在本平台转账记录，转账" + recharge.getAmount()
                            + "元成功！订单号：" + recharge.getOrderNo() + ",转账时间："
                            + sf.parse(rechargeTime));
                    fundDetailService.addFundDetail(deal);
                }
            }
        }*/
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
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
        String date = sf.format(new Date());
        UsersEntity u = new UsersEntity();
        List<UsersEntity> userList = userService.queryUsers(u);
        LoanModel loan = new LoanModel();
        loan.setPlatformMoneymoremore(l.getMoremoreId());
        loan.setBeginTime(p.getValue());
        String endTime = date + "235959";
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
        /*//添加客户明细，平台不存在的充值记录
        for (int i = 0; i < rList.size(); i++)
        {
            LoanWithdrawsOrderQueryBean recharge = rList.get(i);
            for (int j = 0; j < userList.size(); j++)
            {
                UsersEntity user = userList.get(j);
                if (recharge.getWithdrawMoneymoremore()
                        .equals(user.getMoneymoremoreId()))
                {
                    AccountEntity account = accountService.getAccountByCustomerId(user.getCustomerId());
                    //添加资金明细
                    FundDetailEntity deal = new FundDetailEntity();
                    deal.setCustomerId(user.getCustomerId());
                    deal.setAccountId(account.getAccountId());
                    deal.setOrderId(recharge.getOrderNo());
                    deal.setType(2);
                    deal.setCreateTime(new Date());
                    deal.setStatus(1);
                    deal.setAmount(multiply(new BigDecimal(recharge.getAmount())));
                    deal.setBalance(account.getBalance());
                    deal.setDueAmount(account.getDueAmount());
                    deal.setFrozenAmount(account.getFozenAmount());
                    String rechargeTime = recharge.getWithdrawsTime();
                    deal.setRemark("非本平台提现记录，提现" + recharge.getAmount()
                            + "元成功！订单号：" + recharge.getOrderNo() + ",提现时间："
                            + sf.parse(rechargeTime));
                    if (recharge.getWithdrawState().equals("1"))
                    {
                        fundDetailService.addFundDetail(deal);
                    }
                }
            }
        }*/
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
