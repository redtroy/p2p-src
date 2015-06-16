package com.herongwang.p2p.model.invest;

import java.util.Date;

import com.herongwang.p2p.entity.investorder.InvestOrderEntity;

public class InvestModel extends InvestOrderEntity
{
    
    /**
     * 
     */
    private static final long serialVersionUID = 3894299311199968967L;
    
    private String statusText;
    
    private String title;
    
    private Date finishTime;
    
    public String getStatusText()
    {
        return statusText;
    }
    
    public void setStatusText(String statusText)
    {
        this.statusText = statusText;
    }
    
    public String getTitle()
    {
        return title;
    }
    
    public void setTitle(String title)
    {
        this.title = title;
    }
    
    public Date getFinishTime()
    {
        return finishTime;
    }
    
    public void setFinishTime(Date finishTime)
    {
        this.finishTime = finishTime;
    }
    
}
