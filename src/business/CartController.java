package business;

import core.Helper;
import dao.CartDao;
import entity.Cart;
import entity.Customer;

import java.util.ArrayList;

public class CartController {
    private final CartDao cartDao = new CartDao() ;

    public boolean save(Cart cart) {
        return this.cartDao.saveCart( cart );
    }
    public ArrayList<Cart> findAll() {
        return this.cartDao.findALlCart();
    }

    public boolean clear() {
        return this.cartDao.clear();
    }
}
