package business;

import core.Helper;
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
}
