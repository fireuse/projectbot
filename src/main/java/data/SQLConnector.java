package data;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class SQLConnector {
    private static SQLConnector instance;
    private final HikariDataSource dataSource;
    private SQLConnector(){
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://172.17.0.2"); //TODO: also change in future
        config.setUsername("root");
        config.setPassword("toor");
        config.setLeakDetectionThreshold(1000);
        config.setMaximumPoolSize(20);
        dataSource = new HikariDataSource(config);
    }
    private static SQLConnector getInstance() {
        if(instance == null){
            instance = new SQLConnector();
        }
        return instance;
    }
    public static Connection getConnection() throws SQLException {
        return getInstance().dataSource.getConnection();
    }
}