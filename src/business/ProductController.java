package business;

import core.Helper;
import core.Item;
import dao.CustomerDao;
import dao.ProductDao;
import entity.Customer;
import entity.Product;
import java.util.ArrayList;


public class ProductController {
    private final ProductDao productDao = new ProductDao() ;

    public ArrayList<Product> findAll() {
        return this.productDao.findAllProducts();
    }

    /*
    public boolean save(Product product) {
        return this.productDao.saveProduct( product );
    }
     */
    public boolean save(Product product) {
        return this.productDao.saveProduct( product );
    }
    public boolean update(Product product) {
        if(this.findById(product.getId()) == null){
            Helper.showMsg("There is no product with the ID of : " + product.getId());
            return false;
        }else {
            return this.productDao.update(product);
        }
    }
    public Product findById(int id) {
        return this.productDao.findById(id);
    }

    public boolean delete(int id) {
        if(this.findById(id) == null){
            Helper.showMsg("There is no product with the ID of : " + productDao.findById(id).getId());
            return false;
        }else {
            return this.productDao.delete(id);
        }
    }

    public ArrayList<Product> filterProductTable(String name, String code, Item isStock) {
        String query = "SELECT * FROM product";
        ArrayList<String> whereCommands = new ArrayList<>();

        if(name.length() > 0){
            whereCommands.add("name LIKE '%" + name + "%'");
        }
        if(code != null){
            whereCommands.add("code LIKE '%" + code + "%'");
        }
        if(isStock != null){
            if(isStock.getKey() == 1){
                whereCommands.add("stock > 0");
            }else {
                whereCommands.add("stock <= 0");
            }
        }
        if(whereCommands.size() > 0){
            query += " WHERE " + String.join(" AND ", whereCommands);
        }
        return this.productDao.query(query);
    }
}
