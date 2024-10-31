package dao;

import core.Database;
import entity.Customer;
import entity.Product;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProductDao {
    private Connection connection;

    public ProductDao() {
        this.connection = Database.getInstance();
    }

    public ArrayList<Product> findAllProducts(){
        ArrayList<Product> products = new ArrayList<>();
        try {
            ResultSet resultSet = this.connection.createStatement().executeQuery("SELECT * FROM product");
            while(resultSet.next()){
                products.add(this.match(resultSet));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return products;
    }
    public Product findById(int id){
        Product product = null;
        String query = "SELECT * FROM product WHERE id = ?";
        try {
            PreparedStatement pr = this.connection.prepareStatement(query);
            pr.setInt(1, id);
            ResultSet resultSet = pr.executeQuery();
            if(resultSet.next()){
                product = this.match(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return product;
    }

    public boolean delete(int id) {
        String query = "DELETE FROM product WHERE id = ?";

        try {
            PreparedStatement pr = this.connection.prepareStatement(query);
            pr.setInt(1, id);
            return pr.executeUpdate() != -1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }
    public boolean saveProduct(Product product){
        String query = "INSERT INTO product " +
                "("
                + "name,"
                + "code,"
                + "price,"
                + "stock"
                + ")"
                + " VALUES (?,?,?,?)";

        try {
            PreparedStatement pr = this.connection.prepareStatement(query);
            pr.setString(1, product.getName());
            pr.setString(2, product.getCode());
            pr.setDouble(3, product.getPrice());
            pr.setInt(4, product.getStock());
            return pr.executeUpdate() != -1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean update(Product product) {
        String query = "UPDATE product SET "
                + "name = ? ,"
                + "code = ? ,"
                + "price = ? ,"
                + "stock = ? "
                + "WHERE id = ?";
        try {
            PreparedStatement pr = this.connection.prepareStatement(query);
            pr.setString(1, product.getName());
            pr.setString(2, product.getCode());
            pr.setDouble(3, product.getPrice());
            pr.setInt(4, product.getStock());
            pr.setInt(5, product.getId());
            return pr.executeUpdate() != -1;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public Product match(ResultSet resultSet) throws SQLException {
        Product product = new Product();
        product.setId(resultSet.getInt("id"));
        product.setName(resultSet.getString("name"));
        product.setCode(resultSet.getString("code"));
        product.setPrice(resultSet.getInt("price"));
        product.setStock(resultSet.getInt("stock"));
        return product;
    }
}

