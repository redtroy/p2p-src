package com.herongwang.p2p.entity.fee;

public enum FeeStatus
{
    AVAILABLE("可用", 0), UNAVAILABLE("不可用", 1);
    // 成员变量
    private Integer id;
    
    private String name;
    
    private FeeStatus(String name, Integer id)
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
