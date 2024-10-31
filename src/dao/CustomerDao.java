package dao;
import core.Database;
import entitiy.Customer;
import entitiy.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerDao {
    private Connection connection;

    public CustomerDao() {
        this.connection = Database.getInstance();
    }

    public ArrayList<Customer> findAll(){
        ArrayList<Customer> customers = new ArrayList<>();
        try {
            ResultSet resultSet = this.connection.createStatement().executeQuery("SELECT * FROM customer");
            while(resultSet.next()){
                customers.add(this.match(resultSet));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return customers;
    }

    public boolean saveCustomer(Customer customer){
        String query = "INSERT INTO customer " +
                "("
                + "name,"
                + "type,"
                + "phone,"
                + "mail,"
                + "address"
                + ")"
                + " VALUES (?,?,?,?,?)";

        try {
            PreparedStatement pr = this.connection.prepareStatement(query);
            pr.setString(1, customer.getName());
            pr.setString(2, customer.getType().toString());
            pr.setString(3, customer.getPhone());
            pr.setString(4, customer.getMail());
            pr.setString(5, customer.getAddress());
            return pr.executeUpdate() != -1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }


    public Customer match(ResultSet resultSet) throws SQLException {
        Customer customer = new Customer();
        customer.setId(resultSet.getInt("id"));
        customer.setMail(resultSet.getString("mail"));
        customer.setName(resultSet.getString("name"));
        customer.setPhone(resultSet.getString("phone"));
        customer.setAddress(resultSet.getString("address"));
        customer.setType(Customer.TYPE.valueOf(resultSet.getString("type")));
        return customer;
    }

    public Customer findById(int id){
        Customer customer = null;
        String query = "SELECT * FROM CUSTOMER WHERE id = ?";
        try {
            PreparedStatement pr = this.connection.prepareStatement(query);
            pr.setInt(1, id);
            ResultSet resultSet = pr.executeQuery();
            if(resultSet.next()){
                customer = this.match(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customer;
    }
    public boolean update(Customer customer) {
        String query = "UPDATE customer SET "
                + "name = ? ,"
                + "type = ? ,"
                + "phone = ? ,"
                + "mail = ? ,"
                + "address = ? "
                + "WHERE id = ?";
        try {
            PreparedStatement pr = this.connection.prepareStatement(query);
            pr.setString(1, customer.getName());
            pr.setString(2, customer.getType().toString());
            pr.setString(3, customer.getPhone());
            pr.setString(4, customer.getMail());
            pr.setString(5, customer.getAddress());
            pr.setInt(6, customer.getId());
            return pr.executeUpdate() != -1;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }
    public boolean delete(int id) {
        String query = "DELETE FROM CUSTOMER WHERE id = ?";

        try {
            PreparedStatement pr = this.connection.prepareStatement(query);
            pr.setInt(1, id);
            return pr.executeUpdate() != -1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public ArrayList<Customer> query(String query){
        ArrayList<Customer> customers = new ArrayList<>();
        try {
            ResultSet rs = this.connection.createStatement().executeQuery(query);
            while(rs.next()){
                customers.add(this.match(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }
}
