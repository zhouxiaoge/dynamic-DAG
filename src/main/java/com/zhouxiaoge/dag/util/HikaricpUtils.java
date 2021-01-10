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
        // config.setCatalog("");
        // config.setConnectionTimeout(0L);
        config.setIdleTimeout(0L);
        // config.setLeakDetectionThreshold(0L);
        // config.setMaxLifetime(0L);
        // config.setMaximumPoolSize(0);
        // config.setMinimumIdle(0);
        config.setPassword("1234");
        config.setUsername("root");
        // config.setValidationTimeout(0L);
        // config.setConnectionTestQuery("");
        // config.setConnectionInitSql("");
        // config.setDataSource(new DataSource());
        // config.setDataSourceClassName("");
        // config.setDataSourceJNDI("");
        // config.setDataSourceProperties(new Properties());
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setJdbcUrl("jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&failOverReadOnly=false&serverTimezone=UTC");
        // config.setAutoCommit(true);
        // config.setAllowPoolSuspension(false);
        // config.setInitializationFailTimeout(0L);
        // config.setIsolateInternalQueries(false);
        // config.setMetricsTrackerFactory(new MetricsTrackerFactory());
        // config.setMetricRegistry(new Object());
        config.setHealthCheckRegistry(new Object());
        // config.setHealthCheckProperties(new Properties());
        // config.setReadOnly(false);
        // config.setRegisterMbeans(false);
        // config.setPoolName("");
        // config.setScheduledExecutor(new ScheduledExecutorService());
        // config.setSchema("");
        // config.setExceptionOverrideClassName("");
        // config.setTransactionIsolation("");
        // config.setThreadFactory(new ThreadFactory());
        HikariDataSource hikariDataSource = new HikariDataSource(config);
        Connection connection = hikariDataSource.getConnection();
        Statement statement = connection.createStatement();
        String sql = "SELECT  * FROM DAG_TEST ";
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
