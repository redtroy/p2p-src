package com.herongwang.p2p.dao;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.herongwang.p2p.dao.apply.IApplyForDao;
import com.herongwang.p2p.entity.apply.ApplyForEntity;
import com.herongwang.p2p.model.apply.ApplyForModel;
import com.sxj.util.persistent.QueryCondition;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/applicationContext.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public class ApplyForDaoTest
{
    @Autowired
    IApplyForDao dao;
    
    /**
     * 添加融资申请
     * @param apply
     */
    @Test
    public void testAddApply()
    {
        ApplyForEntity entity = new ApplyForEntity();
        dao.addApply(entity);
    }
    
    /**
     * 修改融资申请
     * @param apply
     */
    public void testUpdateApply(ApplyForEntity apply)
    {
        
    }
    
    /**
     * 删除融资申请
     * @param id
     */
    public void testDelApply(String id)
    {
        
    }
    
    /**
     * 查询融资申请
     * @param id
     */
    public ApplyForEntity testGetApplyFor(String id)
    {
        return null;
    }
    
    /**
     * 融资申请高级查询
     * @param query
     * @return
     */
    public List<ApplyForModel> testQuery(QueryCondition<ApplyForModel> query)
    {
        return null;
    }
    
}
