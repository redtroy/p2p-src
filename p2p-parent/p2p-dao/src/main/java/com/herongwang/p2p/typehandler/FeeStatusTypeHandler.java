package com.herongwang.p2p.typehandler;

import org.apache.ibatis.type.EnumOrdinalTypeHandler;

import com.herongwang.p2p.entity.fee.FeeStatus;

public class FeeStatusTypeHandler extends EnumOrdinalTypeHandler<FeeStatus>
{
    
    public FeeStatusTypeHandler(Class<FeeStatus> type)
    {
        super(type);
    }
    
}
