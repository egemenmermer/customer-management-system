package dao;

import core.Database;
import entity.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserDao {
    private Connection connection;

    public UserDao() {
        this.connection = Database.getInstance();
    }

    public User findByLogin(String mail, String password){
        String sql = "SELECT * FROM `user` WHERE mail = ? AND password = ?";
        User user = null;
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
            preparedStatement.setString(1, mail);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                user = this.match(resultSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return user;
    }
    public ArrayList<User> findAll(){
        ArrayList<User> users = new ArrayList<>();
        try {
            ResultSet resultSet = this.connection.createStatement().executeQuery("SELECT * FROM USER");
            while(resultSet.next()){
                users.add(this.match(resultSet));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return users;
    }

    public User match(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getInt("id"));
        user.setEmail(resultSet.getString("mail"));
        user.setPassword(resultSet.getString("password"));
        user.setName(resultSet.getString("name"));
        return user;
    }
}
