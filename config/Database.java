package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class Database {
    private final String dbName;
    private final String userName;
    private final String password;
    private final String host;
    private final String port;
    private Connection connection;


    public Database(String dbName, String userName, String password, String host, String port) {
        this.dbName = dbName;
        this.userName = userName;
        this.password = password;
        this.host = host;
        this.port = port;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setup() {
        String mysqlConnUrlTemplate = "jdbc:mysql://%s:%s/%s";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(String.format(mysqlConnUrlTemplate, host, port, dbName), userName, password);
            System.out.println("Database connected!");

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
