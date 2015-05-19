package com.herongwang.p2p.manage.controller.deal;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.aipg.payreq.Trans_Detail;
import com.alibaba.druid.util.StringUtils;
import com.herongwang.p2p.entity.account.AccountEntity;
import com.herongwang.p2p.entity.funddetail.FundDetailEntity;
import com.herongwang.p2p.entity.orders.OrdersEntity;
import com.herongwang.p2p.entity.users.UsersEntity;
import com.herongwang.p2p.manage.controller.BaseController;
import com.herongwang.p2p.service.account.IAccountService;
import com.herongwang.p2p.service.funddetail.IFundDetailService;
import com.herongwang.p2p.service.orders.IOrdersService;
import com.herongwang.p2p.service.users.IUserService;
import com.sxj.util.exception.WebException;
import com.sxj.util.logger.SxjLogger;

@Controller
@RequestMapping("/deal")
public class DealDetailController extends BaseController
{
    
    @Autowired
    IFundDetailService fundDetailService;
    
    @Autowired
    IOrdersService ordersService;
    
    @Autowired
    IAccountService accountService;
    
    @Autowired
    IUserService userService;
    
    /**
     * 充值列表
     * @param entity
     * @return
     */
    @RequestMapping("/recharge")
    public String rechargeList(OrdersEntity entity, ModelMap map)
            throws WebException
    {
        try
        {
            if (entity != null)
            {
                entity.setPagable(true);
            }
            entity.setOrderType(1);
            //查询充值记录
            List<OrdersEntity> list = ordersService.queryOrdersList(entity);
            map.put("list", list);
            map.put("query", entity);
            return "manage/deal/recharge";
        }
        catch (Exception e)
        {
            e.printStackTrace();
            SxjLogger.error("查询充值信息错误", e, this.getClass());
            throw new WebException("查询充值信息错误");
        }
    }
    
    /**
     * 提现列表
     * @param entity
     * @return
     */
    @RequestMapping("/deposit")
    public String depositList(OrdersEntity entity, ModelMap map)
            throws WebException
    {
        try
        {
            if (entity != null)
            {
                entity.setPagable(true);
            }
            entity.setOrderType(2);
            //查询提现记录
            List<OrdersEntity> list = ordersService.queryOrdersList(entity);
            map.put("list", list);
            map.put("query", entity);
            return "manage/deal/deposit";
        }
        catch (Exception e)
        {
            e.printStackTrace();
            SxjLogger.error("查询提现信息错误", e, this.getClass());
            throw new WebException("查询提现信息错误");
        }
    }
    
    @RequestMapping("/toEdit")
    public String toEdit(String id, ModelMap map) throws WebException
    {
        if (StringUtils.isEmpty(id))
        {
            return "manage/apply/";
        }
        else
        {
            FundDetailEntity info = fundDetailService.getFundDetail(id);
            map.put("info", info);
            return "manage/apply/apply";
        }
        
    }
    
    @SuppressWarnings("finally")
    @RequestMapping("withdraw")
    public @ResponseBody Map<String, String> Withdraw(String orderId)
            throws WebException
    {
        
        Map<String, String> map = new HashMap<String, String>();
        if (StringUtils.isEmpty(orderId))
        {
            map.put("isOK", "提现失败，请联系管理员。");
            return map;
        }
        else
        {
            OrdersEntity order = ordersService.getOrdersEntity(orderId);
            UsersEntity user = userService.getUserById(order.getCustomerId());
            String testTranURL = "https://113.108.182.3/aipg/ProcessServlet";
            String trx_code, busicode;//100001批量代收 100002批量代付 100011单笔实时代收 100014单笔实时代付
            trx_code = "100002";
            if ("100011".equals(trx_code))//收款的时候，填写收款的业务代码
                busicode = "10600";
            else
                busicode = "00600";
            
            Trans_Detail trans_detail = new Trans_Detail();
            trans_detail.setSN("0001");
            trans_detail.setACCOUNT_NAME(user.getCardHolder());
            trans_detail.setACCOUNT_PROP("0");
            trans_detail.setACCOUNT_NO(user.getCardNo());
            trans_detail.setBANK_CODE("103");
            trans_detail.setAMOUNT(order.getAmount().toString());
            trans_detail.setCURRENCY("CNY");
            try
            {
                /*TranxServiceImpl tranxService = new TranxServiceImpl();
                tranxService.nistTest(testTranURL,
                        trx_code,
                        busicode,
                        trans_detail,
                        true);*/
                order.setStatus(1);
                order.setWithdrawTime(new Date());
                order.setChannel("1");
                AccountEntity account = accountService.getAccountByCustomerId(user.getCustomerId());
                account.setFozenAmount(account.getFozenAmount()
                        .subtract(order.getAmount()));
                accountService.updateAccount(account);//更新冻结金额
                BigDecimal fee = new BigDecimal(0.25).divide(new BigDecimal(100),
                        6,
                        BigDecimal.ROUND_HALF_UP);
                order.setAmount(order.getAmount().subtract(order.getAmount()
                        .multiply(fee)));
                ordersService.updateOrders(order);
                fundDetailService.orderFundDetail(order);//生成资金明细
                map.put("isOK", "ok");
            }
            catch (Exception e)
            {
                map.put("isOK", "提现失败，请联系管理员。");
                e.printStackTrace();
                throw new WebException(e);
            }
            finally
            {
                return map;
            }
        }
    }
}
