package view;

import business.CartController;
import business.OrderController;
import business.ProductController;
import core.Helper;
import entity.Cart;
import entity.Order;
import entity.Customer;
import entity.Product;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class OrderUI extends JFrame {
    private JPanel container;
    private JLabel lbl_title;
    private JLabel lbl_customer_name;
    private JTextField fld_order_date;
    private JTextArea tarea_order_note;
    private JButton btn_create_order;
    private JLabel lbl_order_note;
    private JLabel lbl_order_date;
    private Customer customer;
    private CartController cartController;
    private ProductController productController;
    private OrderController orderController;

    public OrderUI(Customer customer) {
        this.customer = customer;
        this.cartController = new CartController();
        this.orderController = new OrderController();
        this.productController = new ProductController();

        this.add(container);
        this.setTitle("Create Order");
        this.setSize(300, 350);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.lbl_customer_name.setText("Customer Name: " + this.customer.getName());

        if(customer.getId() == 0){
            Helper.showMsg("Please select a valid customer!");
            dispose();
        }

        ArrayList<Cart> carts = this.cartController.findAll();
        if(carts.size() == 0){
            Helper.showMsg("Please add products to the cart!");
            dispose();
        }

        this.btn_create_order.addActionListener(e -> {
            if (Helper.isFieldEmpty(fld_order_date)) {
                Helper.showMsg("Please enter a valid order date!");
            } else {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                for (Cart cart : carts) {
                    if (cart.getProduct().getStock() <= 0) continue;
                    Order order = new Order();
                    order.setCustomerId(customer.getId());
                    order.setProductId(cart.getProductId());
                    order.setPrice(cart.getProduct().getPrice());
                    order.setDate(LocalDate.parse(fld_order_date.getText(), formatter));
                    order.setNote(tarea_order_note.getText());
                    this.orderController.save(order);

                    // Update stock
                    Product product = cart.getProduct();
                    product.setStock(product.getStock() - 1);  // Deduct stock
                    this.productController.update(product);  // Persist stock update
                }

                // Clear the cart after all orders are created
                this.cartController.clear();
                Helper.showMsg("Order successfully created!");
                dispose();  // Close the OrderUI
            }
        });
        /*
        this.btn_create_order.addActionListener(e -> {
            if(Helper.isFieldEmpty(fld_order_date)){
                Helper.showMsg("Please enter a valid order date!");
            }else{
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                for (Cart cart : carts) {

                    if(cart.getProduct().getStock() <= 0) continue;
                    Order order = new Order();
                    order.setCustomerId(customer.getId());
                    order.setProductId(cart.getProductId());
                    order.setPrice(cart.getProduct().getPrice());
                    order.setDate(LocalDate.parse(fld_order_date.getText(), formatter));
                    order.setNote(tarea_order_note.getText());
                    this.orderController.save(order);

                    Product unStockProduct = cart.getProduct();
                    unStockProduct.setStock(unStockProduct.getStock() - 1);
                    this.productController.update(unStockProduct);

                }
                this.cartController.clear();
                Helper.showMsg("Order successfully created!");
                dispose();
            }

        });

         */
    }

    private void createUIComponents() throws ParseException {
        this.fld_order_date = new JFormattedTextField(new MaskFormatter("##/##/####"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        this.fld_order_date.setText(formatter.format(LocalDate.now()));
    }
}
