package com.herongwang.p2p.dao.member;

import java.util.List;

import com.herongwang.p2p.model.member.MemberModel;
import com.sxj.util.persistent.QueryCondition;

public interface IMemberDao
{
    /**
     * 查询会员列表
     */
    public List<MemberModel> queryMembers(QueryCondition<MemberModel> query);
}
