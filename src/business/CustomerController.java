package business;

import core.Helper;
import dao.CustomerDao;
import entitiy.Customer;

import java.util.ArrayList;

public class CustomerController {

    private final CustomerDao customerDao = new CustomerDao() ;

    public ArrayList<Customer> findAll() {
        return this.customerDao.findAll();
    }

    public boolean save(Customer customer) {
        return this.customerDao.saveCustomer( customer );
    }
    public Customer findById(int id) {
        return this.customerDao.findById(id);
    }
    public boolean update(Customer customer) {
        if(this.findById(customer.getId()) == null){
            Helper.showMsg("There is no customer with the ID of : " + customer.getId());
            return false;
        }else {
            return this.customerDao.update(customer);
        }
    }
    public boolean delete(int id) {
        if(this.findById(id) == null){
            Helper.showMsg("There is no customer with the ID of : " + customerDao.findById(id).getId());
            return false;
        }else {
            return this.customerDao.delete(id);
        }
    }

    public ArrayList<Customer> filterCustomerTable(String name, Customer.TYPE type) {
        String query = "SELECT * FROM customer";
        ArrayList<String> whereCommands = new ArrayList<>();

        if(name.length() > 0){
            whereCommands.add("name LIKE '%" + name + "%'");
        }
        if(type != null){
            whereCommands.add("type = '" + type + "'");
        }
        if(whereCommands.size() > 0){
            query += " WHERE " + String.join(" AND ", whereCommands);
        }
        return this.customerDao.query(query);
    }
}
