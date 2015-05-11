package com.herongwang.p2p.service.impl.member;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.herongwang.p2p.dao.users.IUsersDao;
import com.herongwang.p2p.entity.users.UsersEntity;
import com.herongwang.p2p.model.users.UserModel;
import com.herongwang.p2p.service.member.IMemberService;
import com.sxj.util.exception.ServiceException;
import com.sxj.util.logger.SxjLogger;
import com.sxj.util.persistent.QueryCondition;

@Service
public class MemberServiceImpl implements IMemberService
{
    @Autowired
    private IUsersDao memberDao;
    
    @Override
    public List<UserModel> queryMemberInfo(UserModel member)
            throws ServiceException
    {
        try
        {
            QueryCondition<UserModel> condition = new QueryCondition<UserModel>();
            List<UserModel> memberList = new ArrayList<UserModel>();
            if (member == null)
            {
                return memberList;
            }
            condition.addCondition("memberCode", member.getMemberCode());// 姓名
            /* 
            condition.setPage(query);*/
            memberList = memberDao.queryUserList(condition);
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
    public UsersEntity getMmeberByAccount(String account)
            throws ServiceException
    {
        try
        {
            return memberDao.getUserByAccount(account);
        }
        catch (Exception e)
        {
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new ServiceException("查询会员列表信息错误", e);
        }
    }
    
    @Override
    public UsersEntity getMmeberById(String id) throws ServiceException
    {
        try
        {
            return memberDao.getUserById(id);
        }
        catch (Exception e)
        {
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new ServiceException("查询会员信息错误", e);
        }
        
    }
    
    @Override
    public UsersEntity addMember(UsersEntity member) throws ServiceException
    {
        try
        {
            memberDao.addUser(member);
            return member;
        }
        catch (Exception e)
        {
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new ServiceException("新增会员信息错误", e);
        }
        
    }
    
    @Override
    public UserModel getMmeberByMemberId(String id) throws ServiceException
    {
        try
        {
            return memberDao.getUserModelByUserId(id);
        }
        catch (Exception e)
        {
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new ServiceException("查询会员详细信息错误", e);
        }
    }
    
    @Override
    public int getMemberNum()
    {
        try
        {
            return memberDao.getUserNum();
        }
        catch (Exception e)
        {
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new ServiceException("查询会员数量错误", e);
        }
    }
    
}
