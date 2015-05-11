package com.herongwang.p2p.TypeHandler;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

public class BigDecimalTypeHandler implements TypeHandler<BigDecimal>
{
    
    @Override
    public void setParameter(PreparedStatement ps, int i, BigDecimal parameter,
            JdbcType jdbcType) throws SQLException
    {
        //防止为null时，创建XMLType出现错误
        if (parameter != null && !parameter.equals(""))
        {
            ps.setInt(i, parameter.intValue());
        }
        else
        {
            ps.setInt(i, 0);
            
        }
        
    }
    
    @Override
    public BigDecimal getResult(ResultSet rs, String columnName)
            throws SQLException
    {
        
        return rs.getBigDecimal(columnName);
    }
    
    @Override
    public BigDecimal getResult(ResultSet rs, int columnIndex)
            throws SQLException
    {
        return rs.getBigDecimal(columnIndex);
    }
    
    @Override
    public BigDecimal getResult(CallableStatement cs, int columnIndex)
            throws SQLException
    {
        return cs.getBigDecimal(columnIndex);
    }
    
}
