package com.herongwang.p2p.manage.controller.deal;

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
import com.allinpay.xmltrans.service.TranxServiceImpl;
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
            entity.setOrderType(4);
            //查询充值记录
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
    
    @RequestMapping("edit")
    public @ResponseBody Map<String, String> addApply(String dealId)
            throws WebException
    {
        FundDetailEntity entity = new FundDetailEntity();
        try
        {
            if (null == dealId || dealId.isEmpty())
            {
                fundDetailService.addFundDetail(entity);
            }
            else
            {
                entity.setDetailId(dealId);
                fundDetailService.updateFundDetail(entity);
            }
            Map<String, String> map = new HashMap<String, String>();
            map.put("isOK", "ok");
            return map;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new WebException(e);
        }
    }
    
    @RequestMapping("delete")
    public @ResponseBody Map<String, String> delApply(String id)
            throws WebException
    {
        try
        {
            fundDetailService.delFundDetail(id);
            Map<String, String> map = new HashMap<String, String>();
            map.put("isOK", "ok");
            return map;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new WebException(e);
        }
    }
    
    @SuppressWarnings("finally")
    @RequestMapping("withdraw")
    public @ResponseBody Map<String, String> Withdraw(String orderId)
            throws WebException
    {
        
        Map<String, String> map = new HashMap<String, String>();
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
            TranxServiceImpl tranxService = new TranxServiceImpl();
            tranxService.nistTest(testTranURL,
                    trx_code,
                    busicode,
                    trans_detail,
                    true);
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
