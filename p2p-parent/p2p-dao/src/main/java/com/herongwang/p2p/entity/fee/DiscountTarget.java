package com.herongwang.p2p.entity.fee;

public enum DiscountTarget
{
    USER("用户",0), USERLEVEL("用户级别",0);
    
    private Integer id;
    
    private String name;
    
    private DiscountTarget(String name, Integer id)
    {
        this.name = name;
        this.id = id;
    }
    
    public Integer getId()
    {
        return id;
    }
    
    public void setId(Integer id)
    {
        this.id = id;
    }
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
}
