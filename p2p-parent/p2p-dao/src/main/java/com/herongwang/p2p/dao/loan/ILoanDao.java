package com.herongwang.p2p.dao.loan;

import com.herongwang.p2p.entity.loan.LoanEntity;
import com.sxj.mybatis.orm.annotations.Insert;

/**
 * 双乾报文Dao
 * @author nishaotang
 *
 */
public interface ILoanDao
{
    /**
     * 添加双乾报文信息
     * @param InvestOrder
     */
    @Insert
    public void addInvestOrder(LoanEntity loanInfo);
}
