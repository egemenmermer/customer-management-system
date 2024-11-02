package business;

import dao.OrderDao;
import entity.Order;

import java.util.ArrayList;

public class OrderController {

    private final OrderDao orderDao = new OrderDao() ;

    public boolean save(Order order) {
        return this.orderDao.saveOrder(order);
    }
    public ArrayList<Order> findAll() {
        return this.orderDao.findAll();
    }
}
