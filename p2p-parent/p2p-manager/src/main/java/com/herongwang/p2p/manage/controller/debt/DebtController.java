package com.herongwang.p2p.manage.controller.debt;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.util.StringUtils;
import com.herongwang.p2p.entity.admin.AdminEntity;
import com.herongwang.p2p.entity.apply.DebtApplicationEntity;
import com.herongwang.p2p.entity.debt.DebtEntity;
import com.herongwang.p2p.entity.financing.FinancingOrdersEntity;
import com.herongwang.p2p.entity.investorder.InvestOrderEntity;
import com.herongwang.p2p.entity.parameters.ParametersEntity;
import com.herongwang.p2p.entity.users.UsersEntity;
import com.herongwang.p2p.loan.util.Common;
import com.herongwang.p2p.loan.util.RsaHelper;
import com.herongwang.p2p.manage.controller.BaseController;
import com.herongwang.p2p.model.loan.LoanInfoBean;
import com.herongwang.p2p.model.loan.transferauditreturnBean;
import com.herongwang.p2p.model.post.Loan;
import com.herongwang.p2p.model.post.LoanTransferAuditModel;
import com.herongwang.p2p.model.post.TransferModel;
import com.herongwang.p2p.service.admin.IAdminService;
import com.herongwang.p2p.service.apply.IDebtApplicationService;
import com.herongwang.p2p.service.debt.IDebtService;
import com.herongwang.p2p.service.financing.IFinancingOrdersService;
import com.herongwang.p2p.service.investorder.IInvestOrderService;
import com.herongwang.p2p.service.loan.ILoanService;
import com.herongwang.p2p.service.parameters.IParametersService;
import com.herongwang.p2p.service.post.IPostService;
import com.herongwang.p2p.service.users.IUserService;
import com.sxj.util.exception.WebException;
import com.sxj.util.logger.SxjLogger;

@Controller
@RequestMapping("/tender")
public class DebtController extends BaseController
{
    
    @Autowired
    private IDebtService debtService;
    
    @Autowired
    private IParametersService parametersService;
    
    @Autowired
    private IUserService userService;
    
    @Autowired
    private IInvestOrderService investOderService;
    
    @Autowired
    private IFinancingOrdersService financingOrdersService;
    
    @Autowired
    private IDebtApplicationService debtApplicationService;
    
    @Autowired
    private ILoanService loanService;
    
    @Autowired
    private IPostService postService;
    
    @Autowired
    private IAdminService adminService;
    
    /**
     * 借款标列表
     * @param entity
     * @return
     */
    @RequestMapping("/tenderList")
    public String rechargeList(DebtEntity entity, ModelMap map, String message)
            throws WebException
    {
        try
        {
            AdminEntity admin = adminService.gitAdminEntity("1");
            if (entity != null)
            {
                entity.setPagable(true);
            }
            List<DebtEntity> list = debtService.queryDebtList(entity);
            for (int i = 0; i < list.size(); i++)
            {
                DebtEntity debt = list.get(i);
                debt.setAmount(debt.getAmount().divide(new BigDecimal(100)));
                debt.setMinInvest(debt.getMinInvest()
                        .divide(new BigDecimal(100)));
                debt.setMaxInvest(debt.getMaxInvest()
                        .divide(new BigDecimal(100)));
            }
            map.put("list", list);
            map.put("query", entity);
            map.put("message", message);
            map.put("type", admin.getStatus());
            return "manage/tender/tender-list";
        }
        catch (Exception e)
        {
            e.printStackTrace();
            SxjLogger.error("查询借款标信息错误", e, this.getClass());
            throw new WebException("查询借款标信息错误");
        }
    }
    
    @RequestMapping("/toEdit")
    public String toEdit(HttpServletRequest request, String id,
            String applicationId, String customerId, ModelMap map)
            throws WebException
    {
        ParametersEntity query = new ParametersEntity();
        query.setType("repaymentType");
        try
        {
            List<ParametersEntity> repaymentList = parametersService.queryParameters(query);//还款方式
            query.setType("tenderType");
            List<ParametersEntity> tenderList = parametersService.queryParameters(query);//标的状态
            map.put("repaymentList", repaymentList);
            map.put("tenderList", tenderList);
            if (StringUtils.isEmpty(id))
            {
                DebtApplicationEntity entity = debtApplicationService.getApplyForEntity(applicationId);
                if (!StringUtils.isEmpty(entity.getCustomerId()))
                {
                    UsersEntity user = userService.getUserById(entity.getCustomerId());
                    if (null != user && user.getRepaymentStatus() == 1)
                    {
                        map.put("applyId", user.getCustomerId());
                        map.put("name", user.getName());
                    }
                }
                map.put("applicationId", applicationId);
                map.put("amount", entity.getAmount());
                return "manage/tender/new-tender";
            }
            else
            {
                DebtEntity info = debtService.getDebtEntity(id);
                info.setMaxInvest(info.getMaxInvest()
                        .divide(new BigDecimal(100)));
                info.setMinInvest(info.getMinInvest()
                        .divide(new BigDecimal(100)));
                String descriptionProject = info.getDescriptionProject()
                        .replaceAll("<br />", "\r");
                String capitalUses = info.getCapitalUses().replaceAll("<br />",
                        "\r");
                String sourceRepayment = info.getSourceRepayment()
                        .replaceAll("<br />", "\r");
                String riskControl = info.getRiskControl().replaceAll("<br />",
                        "\r");
                info.setDescriptionProject(descriptionProject);
                info.setCapitalUses(capitalUses);
                info.setSourceRepayment(sourceRepayment);
                info.setRiskControl(riskControl);
                map.put("info", info);
                map.put("applyId", info.getCustomerId());
                map.put("amount", info.getAmount());
                map.put("name",
                        userService.getUserByUserId(info.getCustomerId())
                                .getName());
                return "manage/tender/new-tender";
            }
        }
        catch (Exception e)
        {
            SxjLogger.error(e.getMessage(), e, this.getClass());
            e.printStackTrace();
        }
        return "manage/tender/tender-list";
    }
    
    @RequestMapping("edit")
    public @ResponseBody Map<String, String> addApply(DebtEntity tender,
            String applicationId, String id) throws WebException
    {
        BigDecimal m = tender.getAmount().multiply(new BigDecimal(100));
        BigDecimal m1 = tender.getMinInvest().multiply(new BigDecimal(100));
        BigDecimal m2 = tender.getMaxInvest().multiply(new BigDecimal(100));
        Map<String, String> map = new HashMap<String, String>();
        FinancingOrdersEntity order = new FinancingOrdersEntity();
        tender.setMinInvest(m1);
        tender.setMaxInvest(m2);
        tender.setAmount(m);
        order.setCustomerId(tender.getCustomerId());
        order.setAmount(m);
        order.setCreateTime(new Date());
        order.setLoanAmount(m);
        String descriptionProject = tender.getDescriptionProject()
                .replaceAll("\n\r", "<br />")
                .replaceAll("\r\n", "<br />")
                .replaceAll("\n", "<br />")
                .replaceAll("\r", "<br />");
        String capitalUses = tender.getCapitalUses()
                .replaceAll("\n\r", "<br />")
                .replaceAll("\r\n", "<br />")
                .replaceAll("\n", "<br />")
                .replaceAll("\r", "<br />");
        String sourceRepayment = tender.getSourceRepayment()
                .replaceAll("\n\r", "<br />")
                .replaceAll("\r\n", "<br />")
                .replaceAll("\n", "<br />")
                .replaceAll("\r", "<br />");
        String riskControl = tender.getRiskControl()
                .replaceAll("\n\r", "<br />")
                .replaceAll("\r\n", "<br />")
                .replaceAll("\n", "<br />")
                .replaceAll("\r", "<br />");
        tender.setDescriptionProject(descriptionProject);
        tender.setCapitalUses(capitalUses);
        tender.setSourceRepayment(sourceRepayment);
        tender.setRiskControl(riskControl);
        try
        {
            if (null == id || id.isEmpty())
            {
                tender.setCreateTime(new Date());
                tender.setStatus(0);
                debtService.addDebt(tender);
                order.setStatus(0);
                order.setDebtId(tender.getDebtId());
                financingOrdersService.addOrder(order);
                if (!applicationId.isEmpty())
                {
                    DebtApplicationEntity info = debtApplicationService.getApplyForEntity(applicationId);
                    info.setStatus(1);
                    debtApplicationService.updateApply(info);
                }
                map.put("isOK", "ok");
            }
            else
            {
                DebtEntity info = debtService.getDebtEntity(id);
                if (info.getStatus() == 0)
                {
                    tender.setDebtId(id);
                    debtService.updateDebt(tender);
                    order.setDebtId(tender.getDebtId());
                    FinancingOrdersEntity o = financingOrdersService.getOrderByDebtId(id);
                    order.setOrderId(o.getOrderId());
                    order.setStatus(1);
                    financingOrdersService.updateOrder(order);
                    map.put("isOK", "ok");
                }
                else
                {
                    map.put("isOK", "修改失败，已有人投标。");
                }
            }
            return map;
        }
        catch (Exception e)
        {
            SxjLogger.error(e.getMessage(), e, this.getClass());
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
            FinancingOrdersEntity entity = financingOrdersService.getOrderByDebtId(id);
            financingOrdersService.delOrder(entity.getOrderId());
            debtService.delDebt(id);
            Map<String, String> map = new HashMap<String, String>();
            map.put("isOK", "ok");
            return map;
        }
        catch (Exception e)
        {
            SxjLogger.error(e.getMessage(), e, this.getClass());
            e.printStackTrace();
            throw new WebException(e);
        }
    }
    
    /**
     * 满标审核
     * @return
     */
    @RequestMapping("audit")
    public String audit(String debtId) throws WebException
    {
        try
        {
            System.out.println(debtId);
            String flag = debtService.audit(debtId);
            return "redirect:/tender/tenderList.htm?message=" + flag;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            SxjLogger.error("满标审核失败", e.getClass());
            throw new WebException(e);
        }
    }
    
    /**
     * 审核
     */
    @RequestMapping("loanTransferAudit")
    public String loanTransferAuditModel(String debtId, ModelMap map,
            HttpServletRequest request) throws WebException
    {
        try
        {
            Loan loan = parametersService.getLoan();
            List<InvestOrderEntity> list = investOderService.queryListInvest(debtId);
            if (list.size() > 0)
            {
                String loanNoList = "";
                for (InvestOrderEntity investOrder : list)
                {
                    if (!"".equals(investOrder.getLoanNo())
                            && investOrder.getLoanNo() != null)
                    {
                        loanNoList = loanNoList + investOrder.getLoanNo() + ",";
                    }
                }
                if (!"".equals(loanNoList))
                {
                    loanNoList = loanNoList.substring(0,
                            loanNoList.length() - 1);
                }
                LoanTransferAuditModel ltsa = new LoanTransferAuditModel();
                ltsa.setPlatformMoneymoremore(loan.getMoremoreId());
                ltsa.setAuditType("1");
                ltsa.setLoanNoList(loanNoList);
                ltsa.setRemark3(debtId);//存放DebtId
                String privatekey = loan.getPrivatekey();
                ltsa.setReturnURL(getBasePath(request)
                        + "tender/loanTransferAuditModelReturn.htm");
                ltsa.setNotifyURL(loan.getServiceIp()
                        + "p2p-manager/tender/loanTransferAuditModelNotify.htm");
                String dataStr = ltsa.getLoanNoList()
                        + ltsa.getPlatformMoneymoremore() + ltsa.getAuditType()
                        + ltsa.getRandomTimeStamp() + ltsa.getRemark1()
                        + ltsa.getRemark2() + ltsa.getRemark3()
                        + ltsa.getReturnURL() + ltsa.getNotifyURL();
                RsaHelper rsa = RsaHelper.getInstance();
                String SignInfo = rsa.signData(dataStr, privatekey);
                ltsa.setSignInfo(SignInfo);
                map.put("model", ltsa);
            }
        }
        catch (Exception e)
        {
            SxjLogger.error("审核失败", e.getClass());
            throw new WebException(e);
        }
        return "manage/debt/loantransferaudit";
    }
    
    /**
     * 审核POST 
     */
    @RequestMapping("loanTransferAuditPost")
    public @ResponseBody String loanTransferAuditModelPost(String debtId,
            ModelMap map, HttpServletRequest request) throws WebException
    {
        try
        {
            Loan l = parametersService.getLoan();
            List<InvestOrderEntity> list = investOderService.queryListInvest(debtId);
            String privatekey = l.getPrivatekey();
            RsaHelper rsa = RsaHelper.getInstance();
            if (list.size() > 0)
            {
                //String loanNoList = "";
                //  String[] loanNoList = new String[(int) Math.ceil(list.size() / 200)];
                List<String> loanNoList = new ArrayList<String>();
                int k = 0;
                String loan = "";
                for (InvestOrderEntity investOrder : list)
                {
                    if (!"".equals(investOrder.getLoanNo())
                            && investOrder.getLoanNo() != null)
                    {
                        loan = loan + investOrder.getLoanNo() + ",";
                        if (k == 199)
                        {
                            k = -1;
                            loanNoList.add(loan);
                            loan = "";
                        }
                        k++;
                    }
                }
                loanNoList.add(loan);
                for (String loanNo : loanNoList)
                {
                    if (!"".equals(loanNo))
                    {
                        loanNo = loanNo.substring(0, loanNo.length() - 1);
                    }
                    
                    LoanTransferAuditModel ltsa = new LoanTransferAuditModel();
                    ltsa.setPlatformMoneymoremore(l.getMoremoreId());
                    ltsa.setAuditType("1");
                    ltsa.setLoanNoList(loanNo);
                    ltsa.setRemark3(debtId);//存放DebtId
                    ltsa.setReturnURL(getBasePath(request)
                            + "tender/loanTransferAuditModelReturn.htm");
                    ltsa.setNotifyURL(l.getServiceIp()
                            + "p2p-website/loan/transferNotify.htm");
                    String dataStr = ltsa.getLoanNoList()
                            + ltsa.getPlatformMoneymoremore()
                            + ltsa.getAuditType() + ltsa.getRandomTimeStamp()
                            + ltsa.getRemark1() + ltsa.getRemark2()
                            + ltsa.getRemark3() + ltsa.getReturnURL()
                            + ltsa.getNotifyURL();
                    String SignInfo = rsa.signData(dataStr, privatekey);
                    ltsa.setSignInfo(SignInfo);
                    String flag = postService.audit(ltsa);
                    if (!"ok".equals(flag))
                    {
                        return "false";
                    }
                }
                List<LoanInfoBean> listmlib = new ArrayList<LoanInfoBean>();
                FinancingOrdersEntity financeOrder = financingOrdersService.getOrderByDebtId(debtId);//查询订单
                LoanInfoBean mlib = new LoanInfoBean();
                mlib.setLoanOutMoneymoremore(userService.getUserById(financeOrder.getCustomerId())
                        .getMoneymoremoreId());//付款人
                mlib.setLoanInMoneymoremore(l.getMoremoreId());//收款人
                mlib.setOrderNo(financeOrder.getOrderId());//订单号,投资人收益明细的ID
                mlib.setBatchNo(debtId);//标号
                mlib.setAmount((financeOrder.getAmount()
                        .multiply(new BigDecimal(0.03)).divide(new BigDecimal(
                        100))).toString());
                mlib.setTransferName("平台管理费");
                mlib.setSecondaryJsonList("");
                listmlib.add(mlib);
                String LoanJsonList = Common.JSONEncode(listmlib);
                TransferModel tf = new TransferModel();
                tf.setPlatformMoneymoremore(l.getMoremoreId());
                tf.setTransferAction("2");
                tf.setAction("2");
                tf.setTransferType("2");
                tf.setNeedAudit("1");
                tf.setReturnURL("");
                tf.setNotifyURL(l.getServiceIp()
                        + "p2p-website/loan/transferNotify.htm");
                //                tf.setRemark1(Common.UrlEncoder(ids, "utf-8"));//还款单的ID
                //                tf.setRemark2(orderId);//投资订单号
                //                tf.setRemark3(debtId);//标的ID
                String dataStr = LoanJsonList + tf.getPlatformMoneymoremore()
                        + tf.getTransferAction() + tf.getAction()
                        + tf.getTransferType() + tf.getNeedAudit()
                        + tf.getRandomTimeStamp() + tf.getRemark1()
                        + tf.getRemark2() + tf.getRemark3() + tf.getReturnURL()
                        + tf.getNotifyURL();
                rsa = RsaHelper.getInstance();
                String SignInfo = rsa.signData(dataStr, privatekey);
                LoanJsonList = Common.UrlEncoder(LoanJsonList, "utf-8");
                tf.setLoanJsonList(LoanJsonList);
                tf.setSignInfo(SignInfo);
                String bsflag = postService.transfer(tf);
                if ("ok".equals(bsflag))
                {
                    return debtService.audit(debtId);
                }
                
            }
        }
        catch (Exception e)
        {
            SxjLogger.error("审核失败", e.getClass());
            return "false";
        }
        return "false";
    }
    
    /**
     * 审核页面返回信息
     */
    @RequestMapping("loanTransferAuditModelReturn")
    public String loanTransferAuditModelReturn(transferauditreturnBean tfb,
            ModelMap map) throws WebException
    {
        try
        {
            loanService.addOrder(Common.JSONEncode(tfb),
                    "transferauditreturnBean",
                    "满标审核页面返回Model");
            DebtEntity d = debtService.getDebtEntity(tfb.getRemark3());
            if ("88".equals(tfb.getResultCode()) && d.getStatus() == 3)
            {
                return "redirect:/tender/audit.htm?debtId=" + tfb.getRemark3();
            }
            map.put("model", tfb);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            SxjLogger.error(e.getMessage(), e, this.getClass());
        }
        return "manage/debt/transferauditreturn";
    }
    
    /**
     * 审核后台通知信息
     */
    @RequestMapping("loanTransferAuditModelNotify")
    public @ResponseBody String loanTransferAuditModelNotify(
            transferauditreturnBean tfb) throws WebException
    {
        try
        {
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
            SxjLogger.error(e.getMessage(), e, this.getClass());
        }
        return "SUCCESS";
    }
    
    /**
     * 会员联想
     * 
     * @param request
     * @param response
     * @param keyword
     * @return
     * @throws IOException
     */
    @RequestMapping("userList")
    public @ResponseBody Map<String, String> userList(
            HttpServletRequest request, HttpServletResponse response,
            String keyword) throws IOException
    {
        UsersEntity user = new UsersEntity();
        if (keyword != "" && keyword != null)
        {
            user.setName(keyword);
            user.setRepaymentStatus(1);
        }
        List<UsersEntity> list = userService.queryUsers(user);
        List<String> strlist = new ArrayList<String>();
        String sb = "";
        for (UsersEntity userEntity : list)
        {
            sb = "{\"title\":\"" + userEntity.getName() + "\",\"result\":\""
                    + userEntity.getCustomerId() + "\"}";
            strlist.add(sb);
        }
        String json = "{\"data\":" + strlist.toString() + "}";
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.print(json);
        out.flush();
        out.close();
        return null;
    }
}