package handler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;

import java.sql.*;

@MappedJdbcTypes(value = JdbcType.TIMESTAMP, includeNullJdbcType = true)
public class MybatisTypeHandler extends BaseTypeHandler<Timestamp> {
    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, Timestamp timestamp, JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i, String.valueOf(timestamp));
    }

    @Override
    public Timestamp getNullableResult(ResultSet resultSet, String columnName) throws SQLException {
        return resultSet.getTimestamp(columnName);
    }

    @Override
    public Timestamp getNullableResult(ResultSet resultSet, int columnIndex) throws SQLException {
        return resultSet.getTimestamp(columnIndex);
    }

    @Override
    public Timestamp getNullableResult(CallableStatement callableStatement, int columnIndex) throws SQLException {
        return callableStatement.getTimestamp(columnIndex);
    }
}
