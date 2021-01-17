package com.zhouxiaoge.dag.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.*;

/**
 * @author 周小哥
 * @date 2021年1月9日22点52分
 */
public class HikaricpUtils {
    public static void main(String[] args) throws SQLException {

        final HikariConfig config = new HikariConfig();
        config.setIdleTimeout(0L);
        config.setPassword("1234");
        config.setUsername("root");
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setJdbcUrl("jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&failOverReadOnly=false&serverTimezone=UTC");
        HikariDataSource hikariDataSource = new HikariDataSource(config);
        Connection connection = hikariDataSource.getConnection();
        Statement statement = connection.createStatement();
        String sql = "SELECT  * FROM dag_test ";
        ResultSet resultSet = statement.executeQuery(sql);
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        for (int i = 1; i < columnCount + 1; i++) {
            System.out.println("-------------------------");
            String columnLabel = metaData.getColumnLabel(i);
            String columnName = metaData.getColumnName(i);
            String columnTypeName = metaData.getColumnTypeName(i);
            int precision = metaData.getPrecision(i);
            String catalogName = metaData.getCatalogName(i);
            String columnClassName = metaData.getColumnClassName(i);
            int columnDisplaySize = metaData.getColumnDisplaySize(i);
            System.out.println(columnLabel);
            System.out.println(columnName);
            System.out.println(columnTypeName);
            System.out.println(precision);
            System.out.println(catalogName);
            System.out.println(columnClassName);
            System.out.println(columnDisplaySize);
        }
    }
}
