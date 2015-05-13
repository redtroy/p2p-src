package com.herongwang.p2p.model.post;

import java.util.ArrayList;
import java.util.List;

public class Body
{
    private Trans_Sum TRANS_SUM;
    
    private List<Trans_Detail> details = new ArrayList<Trans_Detail>();
    
    public Trans_Sum getTRANS_SUM()
    {
        return TRANS_SUM;
    }
    
    public void setTRANS_SUM(Trans_Sum trans_sum)
    {
        TRANS_SUM = trans_sum;
    }
    
    public List<Trans_Detail> getDetails()
    {
        return details;
    }
    
    public void setDetails(List<Trans_Detail> details)
    {
        this.details = details;
    }
    
    public void addDetail(Trans_Detail detail)
    {
        details.add(detail);
    }
}
