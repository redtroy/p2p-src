package com.herongwang.p2p.typehandler;

import org.apache.ibatis.type.EnumOrdinalTypeHandler;

import com.herongwang.p2p.entity.fee.DiscountTarget;

public class DiscountTargetTypeHandler extends
        EnumOrdinalTypeHandler<DiscountTarget>
{
    public DiscountTargetTypeHandler(Class<DiscountTarget> type)
    {
        super(type);
    }
}
