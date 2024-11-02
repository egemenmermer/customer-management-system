package business;

import dao.CartDao;
import entity.Cart;

import java.util.ArrayList;

public class CartController {
    private final CartDao cartDao = new CartDao() ;

    public boolean save(Cart cart) {
        return this.cartDao.saveBasket(cart);
    }
    public ArrayList<Cart> findAll() {
        return this.cartDao.findAllBasket();
    }

    public boolean clear() {
        return this.cartDao.clear();
    }
}
