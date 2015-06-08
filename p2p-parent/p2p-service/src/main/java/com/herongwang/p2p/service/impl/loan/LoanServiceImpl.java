package com.herongwang.p2p.service.impl.loan;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.herongwang.p2p.dao.loan.ILoanDao;
import com.herongwang.p2p.entity.loan.LoanEntity;
import com.herongwang.p2p.service.loan.ILoanService;
import com.sxj.util.exception.ServiceException;

@Service
@Transactional
public class LoanServiceImpl implements ILoanService
{
    @Autowired
    ILoanDao loanDao;
    
    @Override
    public void addOrder(String message, String className, String remark)
            throws ServiceException
    {
        LoanEntity loan = new LoanEntity();
        loan.setMessage(message);
        loan.setClassName(className);
        loan.setCreateTime(new Date());
        loan.setRemark(remark);
        loanDao.addInvestOrder(loan);
    }
    
}
