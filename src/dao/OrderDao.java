package dao;

import core.Database;
import entity.Order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class OrderDao {
    private Connection connection;
    private ProductDao productDao;
    private CustomerDao customerDao;


    public OrderDao() {
        this.connection = Database.getInstance();
        this.productDao = new ProductDao();
        this.customerDao = new CustomerDao();
    }

    public ArrayList<Order> findAll(){
        ArrayList<Order> orders = new ArrayList<>();
        String query = "SELECT * FROM `order`"; // Wrap order in backticks
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                orders.add(this.match(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    public Order match(ResultSet resultSet) throws SQLException {
        Order order = new Order();
        order.setId(resultSet.getInt("id"));
        order.setCustomerId(resultSet.getInt("customer_id"));
        order.setProductId(resultSet.getInt("product_id"));
        order.setPrice(resultSet.getInt("price"));
        order.setDate(LocalDate.parse(resultSet.getString("date")));
        order.setNote(resultSet.getString("note"));
        order.setCustomer(this.customerDao.findById(order.getCustomerId()));
        order.setProduct(this.productDao.findById(order.getProductId()));
        return order;
    }

    public boolean saveOrder(Order order){
        String query = "INSERT INTO `order` (customer_id, product_id, price, `date`, note) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(query);
            preparedStatement.setInt(1, order.getCustomerId());
            preparedStatement.setInt(2, order.getProductId());
            preparedStatement.setDouble(3, order.getPrice());
            preparedStatement.setDate(4, java.sql.Date.valueOf(order.getDate()));
            preparedStatement.setString(5, order.getNote());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    }

