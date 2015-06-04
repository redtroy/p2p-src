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
import com.herongwang.p2p.entity.apply.DebtApplicationEntity;
import com.herongwang.p2p.entity.debt.DebtEntity;
import com.herongwang.p2p.entity.financing.FinancingOrdersEntity;
import com.herongwang.p2p.entity.investorder.InvestOrderEntity;
import com.herongwang.p2p.entity.parameters.ParametersEntity;
import com.herongwang.p2p.entity.users.UsersEntity;
import com.herongwang.p2p.loan.util.Common;
import com.herongwang.p2p.loan.util.RsaHelper;
import com.herongwang.p2p.manage.controller.BaseController;
import com.herongwang.p2p.model.loan.transferauditreturnBean;
import com.herongwang.p2p.model.post.LoanTransferAuditModel;
import com.herongwang.p2p.service.apply.IDebtApplicationService;
import com.herongwang.p2p.service.debt.IDebtService;
import com.herongwang.p2p.service.financing.IFinancingOrdersService;
import com.herongwang.p2p.service.investorder.IInvestOrderService;
import com.herongwang.p2p.service.parameters.IParametersService;
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
                    if (user.getRepaymentStatus() == 1)
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
            SxjLogger.error("满标审核失败", e.getClass());
            throw new WebException(e);
        }
    }
    
    /**
     * 审核
     */
    @RequestMapping("loanTransferAudit")
    public String loanTransferAuditModel(String debtId, ModelMap map)
            throws WebException
    {
        try
        {
            List<InvestOrderEntity> list = investOderService.queryListInvest(debtId);
            if (list.size() > 0)
            {
                String loanNoList = "";
                for (InvestOrderEntity investOrder : list)
                {
                    loanNoList = loanNoList + investOrder.getLoanNo() + ",";
                }
                loanNoList = loanNoList.substring(0, loanNoList.length() - 1);
                LoanTransferAuditModel ltsa = new LoanTransferAuditModel();
                ltsa.setPlatformMoneymoremore("p1190");
                ltsa.setAuditType("1");
                ltsa.setLoanNoList(loanNoList);
                ltsa.setRemark3(debtId);//存放DebtId
                String privatekey = Common.privateKeyPKCS8;
                ltsa.setReturnURL("http://127.0.0.1:8080/p2p-website/loan/loanTransferAuditModelReturn.htm");
                ltsa.setNotifyURL("http://127.0.0.1:8080/p2p-website/loan/loanTransferAuditModelNotify.htm");
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
     * 审核页面返回信息
     */
    @RequestMapping("loanTransferAuditModelReturn")
    public String loanTransferAuditModelReturn(transferauditreturnBean tfb,
            ModelMap map) throws WebException
    {
        try
        {
            if ("88".equals(tfb.getResultCode()))
            {
                return "redirect:/tender/audit.htm?debtId=" + tfb.getRemark3();
            }
            map.put("model", tfb);
        }
        catch (Exception e)
        {
            // TODO: handle exception
        }
        return "manage/debt/transferauditreturn.htm";
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
            // TODO: handle exception
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
