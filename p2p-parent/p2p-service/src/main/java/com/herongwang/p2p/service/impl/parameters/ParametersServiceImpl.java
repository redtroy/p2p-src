package com.herongwang.p2p.service.impl.parameters;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.herongwang.p2p.dao.parameters.IParametersDao;
import com.herongwang.p2p.entity.parameters.ParametersEntity;
import com.herongwang.p2p.model.post.Loan;
import com.herongwang.p2p.service.parameters.IParametersService;
import com.sxj.util.exception.ServiceException;
import com.sxj.util.persistent.QueryCondition;

@Service
@Transactional
public class ParametersServiceImpl implements IParametersService
{
    
    @Autowired
    private IParametersDao parametersDao;
    
    @Override
    public void addParameters(ParametersEntity p) throws ServiceException
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void updateParameters(ParametersEntity p) throws ServiceException
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public ParametersEntity getParametersEntity(String id)
            throws ServiceException
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public List<ParametersEntity> queryParameters(ParametersEntity query)
            throws ServiceException
    {
        QueryCondition<ParametersEntity> condition = new QueryCondition<ParametersEntity>();
        condition.addCondition("type", query.getType());
        condition.setPage(query);
        List<ParametersEntity> contractList = parametersDao.query(condition);
        return contractList;
    }
    
    @Override
    public void delParameters(String id) throws ServiceException
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public Loan getLoan() throws ServiceException
    {
        Loan loan = new Loan();
        QueryCondition<ParametersEntity> condition = new QueryCondition<ParametersEntity>();
        condition.addCondition("type", "loanType");
        List<ParametersEntity> contractList = parametersDao.query(condition);
        for (int i = 0; i < contractList.size(); i++)
        {
            ParametersEntity p = contractList.get(i);
            if (p.getValue().equals("privatekey"))
            {
                loan.setPrivatekey(p.getText());
            }
            if (p.getValue().equals("publickey"))
            {
                loan.setPublickey(p.getText());
            }
            if (p.getValue().equals("moremoreId"))
            {
                loan.setMoremoreId(p.getText());
            }
            if (p.getValue().equals("submitURL"))
            {
                loan.setSubmitURL(p.getText());
            }
        }
        return loan;
    }
    
}
