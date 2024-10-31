package core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    public static Database instance = null;
    private Connection connection = null;
    //mysql
    private final String DB_URL = "jdbc:mysql://localhost:3307/manageCustomers?useSSL=false";
    private final String DB_UNAME = "root";
    private final String DB_PASS = "";

    private Database() {
        try {
            this.connection = DriverManager.getConnection(DB_URL, DB_UNAME, DB_PASS);
            System.out.println("Connected to the MySql database successfully.");
        } catch (SQLException e) {
            System.out.println("Connection failure.");
            e.printStackTrace();
        }
    }

    private Connection getConnection() {
        return this.connection;
    }

    public static Connection getInstance(){
        try {
            if (instance == null || instance.connection.isClosed()) {
                instance = new Database();
            }
        }catch (Exception e){
            e.printStackTrace();

        }
        return instance.connection;
    }
}
