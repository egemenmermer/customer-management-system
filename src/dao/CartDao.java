package dao;

import core.Database;
import entity.Cart;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CartDao {
    private Connection connection;
    private ProductDao productDao;

    public CartDao() {
        this.connection = Database.getInstance();
        this.productDao = new ProductDao();
    }

    public boolean saveBasket(Cart cart){
        String query = "INSERT INTO cart " +
                "("
                + "product_id"
                + ")"
                + " VALUES (?)";

        try {
            PreparedStatement pr = this.connection.prepareStatement(query);
            pr.setInt(1, cart.getProductId());
            return pr.executeUpdate() != -1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }
    public boolean clear() {
        String query = "DELETE FROM cart";
        try (PreparedStatement pr = this.connection.prepareStatement(query)) {
            return pr.executeUpdate() != -1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public ArrayList<Cart> findAllBasket(){
        ArrayList<Cart> carts = new ArrayList<>();
        try {
            ResultSet resultSet = this.connection.createStatement().executeQuery("SELECT * FROM cart");
            while(resultSet.next()){
                carts.add(this.match(resultSet));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return carts;
    }

    public Cart match(ResultSet resultSet) throws SQLException {
        Cart cart = new Cart();
        cart.setId(resultSet.getInt("id"));
        cart.setProductId(resultSet.getInt("product_id"));
        cart.setProduct(productDao.findById(resultSet.getInt("product_id")));
        return cart;
    }
}
