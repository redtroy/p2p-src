package com.herongwang.p2p.service.post;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.ui.ModelMap;

import com.herongwang.p2p.entity.account.AccountEntity;
import com.herongwang.p2p.entity.tl.TLBillEntity;
import com.herongwang.p2p.entity.users.UsersEntity;
import com.herongwang.p2p.model.loan.LoanOrderQueryBean;
import com.herongwang.p2p.model.loan.LoanRechargeOrderQueryBean;
import com.herongwang.p2p.model.loan.LoanWithdrawsOrderQueryBean;
import com.herongwang.p2p.model.order.OrderModel;
import com.herongwang.p2p.model.order.ResultsModel;
import com.herongwang.p2p.model.post.LoanModel;
import com.herongwang.p2p.model.post.LoanReleaseModel;
import com.herongwang.p2p.model.post.RegisterModel;
import com.herongwang.p2p.model.post.TransferModel;
import com.sxj.util.exception.ServiceException;

public interface IPostService
{
    /**
     * 充值
     * @param apply
     */
    public ModelMap Post(BigDecimal amount, UsersEntity user) throws Exception;
    
    /**
     * 生成支付签名
     * @return
     * @throws ServiceException
     */
    public String getSignMsg(OrderModel orderMember) throws Exception;
    
    /**
     * 生成查询签名
     * @return
     * @throws ServiceException
     */
    public String getQuerySignMsg(OrderModel orderMember) throws Exception;
    
    /**
     * 查询订单
     * @param orderMember
     * @return
     * @throws Exception
     */
    public TLBillEntity getBIll(OrderModel orderMember) throws Exception;
    
    /**
     * 发送代付报文
     * @param aipg
     * @param url
     * @param isFront是否加签字
     * @return 返回报文
     * @throws Exception
     */
    public String PostWithdraw(String url, boolean isFront) throws Exception;
    
    /**
     * 查询支付是否成功。
     * @param result
     * @return
     * @throws Exception
     */
    public TLBillEntity QueryTLBill(ResultsModel result) throws Exception;
    
    /**
     * 更新账户资金信息，并插入资金明细
     * @param account 变动金额
     * @param entity 账户信息
     * @param incomeStatus 进出状态1 : 充值  2: 投标 3：还款 4：提现 
     * @throws Exception
     */
    public void updateAccount(BigDecimal account, AccountEntity entity,
            String orderId, int incomeStatus) throws Exception;
    
    /**
     * 开户
     */
    public String register(RegisterModel rg);
    
    /**
     * 转账
     */
    public String transfer(TransferModel tf);
    
    /**
     * 资金释放
     */
    public String loanRelease(LoanReleaseModel lr);
    
    /**
     * 转账对账
     * @param loan
     * @param 流水号、订单号、标号、时间必须填一个，不能同时为空。
     * @return list
     */
    public List<LoanOrderQueryBean> orderQuery(LoanModel loan,
            String submitURLPrefix) throws Exception;
    
    /**
     * 充值对账
     * @param loan
     * @param 流水号、订单号、标号、时间必须填一个，不能同时为空。
     * @return list
     */
    public List<LoanRechargeOrderQueryBean> rechargeOrderQuery(LoanModel loan,
            String submitURLPrefix) throws Exception;
    
    /**
     * 提现对账
     * @param loan
     * @param 流水号、订单号、标号、时间必须填一个，不能同时为空。
     * @return list
     */
    public List<LoanWithdrawsOrderQueryBean> withdrawsOrderQuery(
            LoanModel loan, String submitURLPrefix) throws Exception;
    
    /**
     * 查询余额
     * @param PlatformId  批量查询将所有标识用英文逗号(,)连成一个字符串
     * @param platformType
     * @return String[] 单个查询格式为“网贷平台子账户可用余额|总可用余额(子账户可用余额+公共账户可用余额)|子账户冻结余额”（例:100.00|200.00|10.00）， 批量查询格式为“查询账户的乾多多标识|网贷平台子账户可用余额|总可用余额(子账户可用余额+公共账户可用余额)|子账户冻结余额”，多个账户之间用英文逗号(,)分隔（例:m1|100.00|200.00|10.00,m2|325.00|458.00|22.00）
     * @throws Exception
     */
    public String[] balanceQuery(String PlatformId, String platformType)
            throws Exception;
}
