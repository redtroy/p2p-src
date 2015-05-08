package com.herongwang.p2p.service.impl.member;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.herongwang.p2p.dao.member.IMemberDao;
import com.herongwang.p2p.entity.member.MemberEntity;
import com.herongwang.p2p.model.member.MemberModel;
import com.herongwang.p2p.service.member.IMemberService;
import com.sxj.util.exception.ServiceException;
import com.sxj.util.logger.SxjLogger;
import com.sxj.util.persistent.QueryCondition;

@Service
public class MemberServiceImpl implements IMemberService
{
    @Autowired
    private IMemberDao memberDao;
    
    @Override
    public List<MemberModel> queryMemberInfo(MemberModel member)
            throws ServiceException
    {
        try
        {
            QueryCondition<MemberModel> condition = new QueryCondition<MemberModel>();
            List<MemberModel> memberList = new ArrayList<MemberModel>();
            if (member == null)
            {
                return memberList;
            }
            condition.addCondition("memberCode", member.getMemberCode());// 姓名
            /* condition.addCondition("parentId", query.getMemberNo());// 父会员号
            condition.addCondition("accountNo", query.getAccountNo());// 子会员
            condition.addCondition("accountName", query.getAccountName());// 子会员名称
            condition.addCondition("state", query.getState());// 子账户状态
            condition.addCondition("delstate", query.getDelstate());// 删除标记
            condition.addCondition("startDate", query.getStartDate());// 开始时间
            condition.addCondition("endDate", query.getEndDate());// 结束时间
            condition.addCondition("functionId", query.getFunctionId());// 权限ＩＤ
            condition.setPage(query);*/
            memberList = memberDao.queryMembers(condition);
            // query.setPage(condition);
            return memberList;
        }
        catch (Exception e)
        {
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new ServiceException("查询会员列表信息错误", e);
        }
    }
    
    @Override
    public MemberEntity getMmeberByAccount(String account)
            throws ServiceException
    {
        try
        {
            return memberDao.getMmeberByAccount(account);
        }
        catch (Exception e)
        {
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new ServiceException("查询会员列表信息错误", e);
        }
    }
    
    @Override
    public MemberEntity getMmeberById(String id) throws ServiceException
    {
        try
        {
            return memberDao.getMemberById(id);
        }
        catch (Exception e)
        {
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new ServiceException("查询会员信息错误", e);
        }
        
    }
    
    @Override
    public MemberEntity addMember(MemberEntity member) throws ServiceException
    {
        try
        {
            memberDao.addMember(member);
            return member;
        }
        catch (Exception e)
        {
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new ServiceException("新增会员信息错误", e);
        }
        
    }
    
    @Override
    public MemberModel getMmeberByMemberId(String id) throws ServiceException
    {
        try
        {
            return memberDao.getMemberModelByMemberId(id);
        }
        catch (Exception e)
        {
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new ServiceException("查询会员详细信息错误", e);
        }
    }
    
}
