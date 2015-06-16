package com.herongwang.p2p.manage.controller.repayplan;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.herongwang.p2p.entity.financing.FinancingOrdersEntity;
import com.herongwang.p2p.entity.repayPlan.RepayPlanEntity;
import com.herongwang.p2p.loan.util.Common;
import com.herongwang.p2p.manage.controller.BaseController;
import com.herongwang.p2p.model.loan.LoanTransferReturnBean;
import com.herongwang.p2p.service.financing.IFinancingOrdersService;
import com.herongwang.p2p.service.impl.post.PostServiceImpl;
import com.herongwang.p2p.service.loan.ILoanService;
import com.herongwang.p2p.service.repayplan.IRepayPlanService;
import com.sxj.util.exception.WebException;
import com.sxj.util.logger.SxjLogger;

@Controller
@RequestMapping("/repayPlan")
public class RepayPlanController extends BaseController
{
    
    @Autowired
    private IRepayPlanService repayPlanService;
    
    @Autowired
    IFinancingOrdersService financingOrdersService;
    
    @Autowired
    private PostServiceImpl postService;
    
    @Autowired
    private ILoanService loanService;
    
    /**
     * 还款计划
     * @param entity
     * @return
     */
    @RequestMapping("/queryRepayPlan")
    public String queryRepayPlan(String debtId, ModelMap map)
            throws WebException
    {
        try
        {
            FinancingOrdersEntity order = financingOrdersService.getOrderByDebtId(debtId);
            List<RepayPlanEntity> list = repayPlanService.queryRepayPlan(order);
            map.put("repayPlan", list);
            //map.put("orderId", entity.getOrderId());
            map.put("orderId", order.getOrderId());
            map.put("debtId", debtId);
            return "manage/repayPlan/repayPlan";
        }
        catch (Exception e)
        {
            e.printStackTrace();
            SxjLogger.error("查询还款计划信息错误", e, this.getClass());
            throw new WebException("查询还款计划信息错误");
        }
    }
    
    /**
     * 验证余额
     * @param session
     * @param ids
     * @return
     * @throws WebException
     */
    @RequestMapping("getBalance")
    public @ResponseBody Map<String, Object> getBalance(String ids,
            String orderId, String debtId, HttpServletRequest request)
            throws WebException
    {
        try
        {
            Map<String, Object> map = new HashMap<String, Object>();
            String flag = repayPlanService.getBalance(ids,
                    orderId,
                    debtId,
                    getBasePath(request));
            map.put("flag", flag);
            return map;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new WebException("验证余额错误", e);
        }
        
    }
    
    /**
     * 还款
     * @param session
     * @param ids
     * @return
     * @throws WebException
     */
    @RequestMapping("saveRepayPlan")
    public String saveRepayPlan(String ids, String orderId, String debtId,
            String payManId, RedirectAttributes ra, HttpServletRequest request)
            throws WebException
    {
        try
        {
            Map<String, Object> map = new HashMap<String, Object>();
            /*List<Object> rdslist = Common.JSONDecodeList(ids, String.class);
            String[] rids = new String[rdslist.size()];
            for (int i = 0; i < rdslist.size(); i++)
            {
                rids[i] = (String) rdslist.get(i);
            }
            ids = Common.UrlDecoder(ids, "utf-8");
            String[] rids = ids.split(",");
            FinancingOrdersEntity fo = new FinancingOrdersEntity();
            fo.setDebtId(debtId);
            fo.setOrderId(orderId);
            List<RepayPlanEntity> list = repayPlanService.queryRepayPlan(fo);
            String[] listIds = new String[rids.length];
            int i = 0;
            for (RepayPlanEntity rp : list)
            {
                for (String xh : rids)
                {
                    if (xh.equals(rp.getSequence()))
                    {
                        listIds[i] = rp.getPlanId();
                        i++;
                    }
                }
            }*/
            transferPost(ids, orderId, debtId, payManId, getBasePath(request));
            String flag = repayPlanService.saveRepayPlan(ids.split(","),
                    orderId,
                    debtId);
            map.put("flag", flag);
            ra.addAttribute("orderId", orderId);
            ra.addAttribute("debtId", debtId);
            return "redirect:/repayPlan/queryRepayPlan.htm";
            
        }
        catch (Exception e)
        {
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new WebException("还款错误", e);
        }
        
    }
    
    public String transferPost(String ids, String orderId, String debtId,
            String payManId, String url)
    {
        
        //        List<Map<String, String>> list = repayPlanService.getTransferList(ids,
        //                orderId,
        //                debtId);
        //        if (list.size() > 0)
        //        {
        //            List<LoanInfoBean> listmlib = new ArrayList<LoanInfoBean>();
        //            List<List<LoanInfoBean>> listmb = new ArrayList<List<LoanInfoBean>>();
        //            for (Map<String, String> maplist : list)
        //            {
        //                List<LoanInfoSecondaryBean> listmlisb = new ArrayList<LoanInfoSecondaryBean>();
        //                LoanInfoSecondaryBean mlisb = new LoanInfoSecondaryBean();
        //                mlisb.setLoanInMoneymoremore("p1190");
        //                mlisb.setAmount(maplist.get("fee"));
        //                mlisb.setTransferName("平台手续费");
        //                listmlisb.add(mlisb);
        //                String SecondaryJsonList = Common.JSONEncode(listmlisb);
        //                
        //                LoanInfoBean mlib = new LoanInfoBean();
        //                mlib.setLoanOutMoneymoremore(payManId);//付款人
        //                mlib.setLoanInMoneymoremore(maplist.get("moneymoremoreId"));//收款人
        //                mlib.setOrderNo(maplist.get("orderNo"));//订单号,投资人收益明细的ID
        //                mlib.setBatchNo(maplist.get("debtNo"));//标号
        //                mlib.setAmount(maplist.get("amount"));
        //                mlib.setTransferName("还款");
        //                mlib.setSecondaryJsonList(SecondaryJsonList);
        //                listmlib.add(mlib);
        //                if (listmlib.size() == 199)
        //                {
        //                    listmb.add(listmlib);
        //                    listmlib = new ArrayList<LoanInfoBean>();
        //                }
        //            }
        //            listmb.add(listmlib);
        //            for (List<LoanInfoBean> lstmlib : listmb)
        //            {
        //                String LoanJsonList = Common.JSONEncode(lstmlib);
        //                TransferModel tf = new TransferModel();
        //                tf.setPlatformMoneymoremore("p1190");
        //                tf.setTransferAction("2");
        //                tf.setAction("2");
        //                tf.setTransferType("2");
        //                tf.setNeedAudit("1");
        //                tf.setReturnURL("");
        //                tf.setNotifyURL(url + "repayPlan/transferNotify.htm");
        //                //                tf.setRemark1(Common.UrlEncoder(ids, "utf-8"));//还款单的ID
        //                //                tf.setRemark2(orderId);//投资订单号
        //                //                tf.setRemark3(debtId);//标的ID
        //                String dataStr = LoanJsonList + tf.getPlatformMoneymoremore()
        //                        + tf.getTransferAction() + tf.getAction()
        //                        + tf.getTransferType() + tf.getNeedAudit()
        //                        + tf.getRandomTimeStamp() + tf.getRemark1()
        //                        + tf.getRemark2() + tf.getRemark3() + tf.getReturnURL()
        //                        + tf.getNotifyURL();
        //                RsaHelper rsa = RsaHelper.getInstance();
        //                String SignInfo = rsa.signData(dataStr, privatekey);
        //                LoanJsonList = Common.UrlEncoder(LoanJsonList, "utf-8");
        //                tf.setLoanJsonList(LoanJsonList);
        //                tf.setSignInfo(SignInfo);
        //                String bsflag = postService.transfer(tf);
        //                //=================================================================平台操作
        //                if (!"ok".equals(bsflag))
        //                {
        //                    return "qdd";
        //                }
        //            }
        //            return "ok";
        //        }
        return "qdd";
    }
    
    /**
     * 转账
     */
    @RequestMapping("transfer")
    public String transfer(ModelMap map, String ids, String orderId, String xh,
            String debtId, String payManId, HttpServletRequest request)
            throws WebException
    {
        try
        {
            //            List<Map<String, String>> list = repayPlanService.getTransferList(ids,
            //                    orderId,
            //                    debtId);
            //            List<LoanInfoBean> listmlib = new ArrayList<LoanInfoBean>();
            //            if (list.size() > 0)
            //            {
            //                for (Map<String, String> maplist : list)
            //                {
            //                    List<LoanInfoSecondaryBean> listmlisb = new ArrayList<LoanInfoSecondaryBean>();
            //                    LoanInfoSecondaryBean mlisb = new LoanInfoSecondaryBean();
            //                    mlisb.setLoanInMoneymoremore("p1190");
            //                    mlisb.setAmount(maplist.get("fee"));
            //                    mlisb.setTransferName("平台手续费");
            //                    listmlisb.add(mlisb);
            //                    String SecondaryJsonList = Common.JSONEncode(listmlisb);
            //                    
            //                    LoanInfoBean mlib = new LoanInfoBean();
            //                    mlib.setLoanOutMoneymoremore(payManId);//付款人
            //                    mlib.setLoanInMoneymoremore(maplist.get("moneymoremoreId"));//收款人
            //                    mlib.setOrderNo(maplist.get("orderNo"));//订单号,投资人收益明细的ID
            //                    mlib.setBatchNo(maplist.get("debtNo"));//标号
            //                    mlib.setAmount(maplist.get("amount"));
            //                    mlib.setTransferName("还款");
            //                    mlib.setSecondaryJsonList(SecondaryJsonList);
            //                    listmlib.add(mlib);
            //                }
            //            }
            //            String LoanJsonList = Common.JSONEncode(listmlib);
            //            TransferModel tf = new TransferModel();
            //            tf.setPlatformMoneymoremore("p1190");
            //            tf.setTransferAction("2");
            //            tf.setAction("1");
            //            tf.setTransferType("2");
            //            tf.setNeedAudit("1");
            //            tf.setReturnURL(getBasePath(request)
            //                    + "repayPlan/transferReturn.htm");
            //            tf.setNotifyURL(getBasePath(request)
            //                    + "repayPlan/transferNotify.htm");
            //            tf.setRemark1(Common.UrlEncoder(xh, "utf-8"));//还款单的ID
            //            tf.setRemark2(orderId);//投资订单号
            //            tf.setRemark3(debtId);//标的ID
            //            String dataStr = LoanJsonList + tf.getPlatformMoneymoremore()
            //                    + tf.getTransferAction() + tf.getAction()
            //                    + tf.getTransferType() + tf.getNeedAudit()
            //                    + tf.getRandomTimeStamp() + tf.getRemark1()
            //                    + tf.getRemark2() + tf.getRemark3() + tf.getReturnURL()
            //                    + tf.getNotifyURL();
            //            RsaHelper rsa = RsaHelper.getInstance();
            //            String SignInfo = rsa.signData(dataStr, privatekey);
            //            LoanJsonList = Common.UrlEncoder(LoanJsonList, "utf-8");
            //            tf.setLoanJsonList(LoanJsonList);
            //            tf.setSignInfo(SignInfo);
            //            map.put("model", tf);
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new WebException("投资转账失败", e);
        }
        return "manage/repayPlan/loantransfer";
    }
    
    /**
     * 转账返回页面
     */
    @RequestMapping("transferReturn")
    public String transferReturn(LoanTransferReturnBean lr, ModelMap map,
            RedirectAttributes ra) throws WebException
    {
        try
        {
            loanService.addOrder(Common.JSONEncode(lr),
                    "LoanTransferReturnBean",
                    "转账页面返回Model");
            if ("88".equals(lr.getResultCode()))
            {
                ra.addAttribute("ids", lr.getRemark1());
                ra.addAttribute("orderId", lr.getRemark2());
                ra.addAttribute("debtId", lr.getRemark3());
                //                return saveRepayPlan(lr.getRemark1(),
                //                        lr.getRemark2(),
                //                        lr.getRemark3(),
                //                        ra);
                return "redirect:/repayPlan/saveRepayPlan.htm";
            }
            map.put("model", lr);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new WebException("还款失败", e);
        }
        return "manage/repayPlan/transferreturn";
    }
    
    /**
     * 转账后台通知
     */
    @RequestMapping("transferNotify")
    public @ResponseBody String transferNotify(LoanTransferReturnBean lr)
            throws WebException
    {
        try
        {
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new WebException("后台通知还款失败", e);
        }
        return "";
    }
}
