package com.herongwang.p2p.dao.member;

import java.util.List;

import com.herongwang.p2p.entity.member.MemberEntity;
import com.sxj.util.persistent.QueryCondition;

public interface IMemberDao
{
    /**
     * 查询会员列表
     */
    public List<MemberEntity> queryMembers(QueryCondition<MemberEntity> query);
}
