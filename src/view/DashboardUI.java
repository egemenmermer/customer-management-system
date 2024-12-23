package view;

import business.CartController;
import business.CustomerController;
import business.ProductController;
import business.OrderController;
import core.Helper;
import core.Item;
import entity.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.util.ArrayList;

public class DashboardUI extends JFrame {
    private JPanel container;
    private JLabel lbl_welcome;
    private JButton btn_logout;
    private JTabbedPane tabbedPane1;
    private JPanel pnl_customer;
    private JScrollPane scrl_customer;
    private JTable tbl_customer;
    private JPanel pnl_customer_filter;
    private JTextField fld_f_customer_name;
    private JComboBox cmb_f_customer_type;
    private JButton btn_customer_search;
    private JButton btn_customer_clear_filter;
    private JButton btn_customer_new;
    private JLabel lbl_f_customer_name;
    private JLabel lbl_f_customer_type;
    private JPanel pnl_product;
    private JTable tbl_product;
    private JScrollPane scrl_product;
    private JPanel pnl_filter_product;
    private JTextField fld_f_product_name;
    private JTextField fld_f_product_code;
    private JComboBox<Item> cmb_f_stock_status;
    private JButton btn_product_search;
    private JButton btn_product_edit;
    private JButton btn_clear_product_filter;
    private JLabel lbl_f_product_name;
    private JLabel lbl_f_product_code;
    private JLabel lbl_f_stock_status;
    private JPanel pnl_cart;
    private JPanel pnl_cart_top;
    private JScrollPane scrl_cart;
    private JComboBox<Item> cmb_cart_customer;
    private JLabel lbl_cart_customer;
    private JButton btn_f_clear_cart;
    private JButton btn_cart_add;
    private JLabel lbl_cart_p_quantity;
    private JLabel lbl_cart_price;
    private JTable tbl_cart;
    private JScrollPane scrl_order;
    private JPanel pnl_order;
    private JTable tbl_order;
    private User user;
    private CustomerController customerController;
    private ProductController productController;
    private CartController cartController;
    private OrderController orderController;
    private DefaultTableModel tmdl_customer = new DefaultTableModel();
    private DefaultTableModel tmdl_product = new DefaultTableModel();
    private DefaultTableModel tmdl_cart = new DefaultTableModel();
    private DefaultTableModel tmdl_order = new DefaultTableModel();
    private JPopupMenu popup_customer = new JPopupMenu();
    private JPopupMenu popup_product = new JPopupMenu();

    public DashboardUI(User user) {
        this.user = user;
        this.customerController = new CustomerController();
        this.productController = new ProductController();
        this.cartController = new CartController();
        this.orderController = new OrderController();

        this.tmdl_customer = new DefaultTableModel();
        this.tbl_customer.setModel(tmdl_customer);
        if (user == null) {
            Helper.showMsg("error");
            this.dispose();
            System.exit(0);
        }

        this.add(container);
        this.setSize(1000, 500);
        this.setTitle("Customer Management System");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.lbl_welcome.setText("Welcome: " + this.user.getName());
        this.btn_logout.addActionListener(e -> {
            dispose();
            LoginUI loginUI = new LoginUI();

        });

        //Customer Tab
        loadCustomerTable(null);
        loadCustomerPopUpMenu();
        loadCustomerButtonEvent();
        this.cmb_f_customer_type.setModel(new DefaultComboBoxModel<>(Customer.TYPE.values()));
        this.cmb_f_customer_type.setSelectedItem(null);

        //Product Tab
        loadProductTable(null);
        loadProductPopUpMenu();
        loadProductButtonEvent();
        this.cmb_f_stock_status.addItem(new Item(1, "In Stock!"));
        this.cmb_f_stock_status.addItem(new Item(2, "Out of Stock!"));
        this.cmb_f_stock_status.setSelectedItem(null);

        //Cart Tab
        loadCartTable();
        loadCartButtonEvent();
        loadCartCustomerCombo();

        //Order Tab
        loadOrderTable();

    }


    private void loadOrderTable() {
        if (tmdl_order.getColumnCount() == 0) {
            Object[] columnOrder = {"ID", "Customer Name", "Product Name", "Price", "Order Date", "Note"};
            tmdl_order.setColumnIdentifiers(columnOrder);
        }

        ArrayList<Order> orders = this.orderController.findAll();
        System.out.println("Orders size: " + orders.size()); // Debug line to check if data is fetched

        tmdl_order.setRowCount(0); // Clear existing rows

        for (Order order : orders) {

            Object[] rowObject = {
                    order.getId(),
                    order.getCustomer().getName(),
                    order.getProduct().getName(),
                    order.getPrice(),
                    order.getDate(),
                    order.getNote()
            };
            tmdl_order.addRow(rowObject);
        }

        tbl_order.setModel(tmdl_order);
        tbl_order.getTableHeader().setReorderingAllowed(false);
        tbl_order.getColumnModel().getColumn(0).setPreferredWidth(50);
        tbl_order.repaint(); // Optional, to ensure the UI refreshes
        /*
        Object[] columnOrder = {"ID", "Customer Name", "Product Name", "Price", "Order Date", "Note"};
        ArrayList<Order> orders = this.orderController.findAll();

        DefaultTableModel clearModel = (DefaultTableModel) tbl_order.getModel();
        clearModel.setRowCount(0);

        this.tmdl_order.setColumnIdentifiers(columnOrder);
        for (Order order : orders) {
            Object[] rowObject = {
                    order.getId(),
                    order.getCustomer().getName(),
                    order.getProduct().getName(),
                    order.getPrice(),
                    order.getDate(),
                    order.getNote()
            };
            this.tmdl_order.addRow(rowObject);
        }
        this.tbl_order.setModel(tmdl_order);
        this.tbl_order.getTableHeader().setReorderingAllowed(false);
        this.tbl_order.getColumnModel().getColumn(0).setPreferredWidth(50);
        this.tbl_order.setEnabled(false);

         */
    }

    private void loadCartCustomerCombo(){
        ArrayList<Customer> customers = this.customerController.findAll();
        this.cmb_cart_customer.removeAllItems();

        for (Customer customer : customers) {
            this.cmb_cart_customer.addItem(new Item(customer.getId(), customer.getName()));
        }
        this.cmb_cart_customer.setSelectedItem(null);
    }
    private void loadCartButtonEvent(){
        this.btn_f_clear_cart.addActionListener(e -> {
            if(this.cartController.clear()){
                Helper.showMsg("Cart emptied!");
                loadCartTable();  // Ensures cart table reloads
            } else {
                Helper.showMsg("Error clearing cart.");
            }
        });

        this.btn_cart_add.addActionListener(e -> {
            Item selectedCustomer = (Item) this.cmb_cart_customer.getSelectedItem();
            if(selectedCustomer == null){
                Helper.showMsg("Please select a customer!");
            } else {
                Customer customer = this.customerController.findById(selectedCustomer.getKey());
                ArrayList<Cart> carts = this.cartController.findAll();
                if(customer.getId() == 0){
                    Helper.showMsg("There is no such customer!");
                } else if(carts.size() == 0){
                    Helper.showMsg("Please add product to the cart!");
                } else {
                    OrderUI orderUI = new OrderUI(customer);
                    orderUI.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosed(WindowEvent e) {
                            loadCartTable();  // Ensure cart is reloaded after closing OrderUI
                            loadProductTable(null);
                        }
                    });
                }
            }
        });
        /*
        this.btn_f_clear_cart.addActionListener(e -> {
            if(this.cartController.clear()){
                Helper.showMsg("Cart emptied!");
                loadCartTable();
            }else{
                Helper.showMsg("error");
            }
        });

        this.btn_cart_add.addActionListener(e -> {
            Item selectedCustomer = (Item) this.cmb_cart_customer.getSelectedItem();
            if(selectedCustomer == null){
                Helper.showMsg("Please select a customer!");
            } else{
                Customer customer = this.customerController.findById(selectedCustomer.getKey());
                ArrayList<Cart> carts = this.cartController.findAll();
                if(customer.getId() == 0){
                    Helper.showMsg("There is no such customer!");
                }else if(carts.size() == 0){
                    Helper.showMsg("Please add product to the cart!");
                }else{
                    OrderUI orderUI = new OrderUI(customer);
                    orderUI.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosing(WindowEvent e) {
                            loadCartTable();
                            loadProductTable(null);
                        }

                    });
                }
            }
        });

         */
    }
    private void loadCustomerButtonEvent() {
        this.btn_customer_new.addActionListener(e -> {
            CustomerUI customerUI = new CustomerUI(new Customer());
            customerUI.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadCustomerTable(null);
                    loadCartCustomerCombo();
                }
            });
        });

        this.btn_customer_search.addActionListener(e -> {
            ArrayList<Customer> filteredCustomers = this.customerController.filterCustomerTable(
                    this.fld_f_customer_name.getText(),
                    (Customer.TYPE) cmb_f_customer_type.getSelectedItem()
            );
            loadCustomerTable(filteredCustomers);
        });

        btn_customer_clear_filter.addActionListener(e -> {
            loadCustomerTable(null);
            this.fld_f_customer_name.setText(null);
            this.cmb_f_customer_type.setSelectedItem(null);
        });
    }

    private void loadProductButtonEvent() {
        this.btn_product_edit.addActionListener(e -> {
            ProductUI productUI = new ProductUI(new Product());
            productUI.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadProductTable(null);
                }
            });
        });

        this.btn_product_search.addActionListener(e -> {
            ArrayList<Product> filteredProducts = this.productController.filterProductTable(
                    this.fld_f_product_name.getText(),
                    this.fld_f_product_code.getText(),
                    (Item) this.cmb_f_stock_status.getSelectedItem()
            );
            loadProductTable(filteredProducts);
        });

        this.btn_clear_product_filter.addActionListener(e -> {
            this.fld_f_product_name.setText(null);
            this.fld_f_product_code.setText(null);
            this.cmb_f_stock_status.setSelectedItem(null);
            loadProductTable(null);
        });
    }
    private void loadProductPopUpMenu() {
        this.tbl_product.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                showPopup(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                showPopup(e);
            }

            private void showPopup(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    int row = tbl_product.rowAtPoint(e.getPoint());
                    tbl_product.setRowSelectionInterval(row, row);
                    popup_product.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });

        this.popup_product.add("Update").addActionListener(e -> {
            int selectedId = Integer.parseInt(tbl_product.getValueAt(tbl_product.getSelectedRow(), 0).toString());
            ProductUI productUI = new ProductUI(this.productController.findById(selectedId));
            productUI.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadProductTable(null);
                    loadCartTable();
                }
            });
        });

        this.popup_product.add("Delete").addActionListener(e -> {
            int selectedId = Integer.parseInt(tbl_product.getValueAt(tbl_product.getSelectedRow(), 0).toString());
            if (Helper.confirm("Are you sure?")) {
                if (this.productController.delete(selectedId)) {
                    Helper.showMsg("Product deleted successfully!");
                    loadProductTable(null);
                    loadCartTable();
                } else {
                    Helper.showMsg("Error deleting product.");
                }
            }
        });

        this.popup_product.add("Add to Cart").addActionListener(e -> {
            int selectedId = Integer.parseInt(tbl_product.getValueAt(tbl_product.getSelectedRow(), 0).toString());
            Product cartProduct = this.productController.findById(selectedId);

            if(cartProduct.getStock() <= 0){
                Helper.showMsg("Product not in stock!");
            }else {
                Cart cart = new Cart(cartProduct.getId());
                boolean result = false;
                if(this.cartController.save(cart)){
                    Helper.showMsg("Product added successfully!");
                    loadCartTable();
                }else{
                    Helper.showMsg("Error adding to cart.");
                }
            }
        });
    }
        private void loadCustomerPopUpMenu() {
            this.tbl_customer.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    showPopup(e);
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    showPopup(e);
                }

                private void showPopup(MouseEvent e) {
                    if (e.isPopupTrigger()) {
                        int row = tbl_customer.rowAtPoint(e.getPoint());
                        tbl_customer.setRowSelectionInterval(row, row);
                        popup_customer.show(e.getComponent(), e.getX(), e.getY());
                    }
                }
            });

            this.popup_customer.add("Update").addActionListener(e -> {
                int selectedId = Integer.parseInt(tbl_customer.getValueAt(tbl_customer.getSelectedRow(), 0).toString());
                CustomerUI customerUI = new CustomerUI(this.customerController.findById(selectedId));
                customerUI.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        loadCustomerTable(null);
                        loadCartCustomerCombo();
                    }
                });
            });

            this.popup_customer.add("Delete").addActionListener(e -> {
                int selectedId = Integer.parseInt(tbl_customer.getValueAt(tbl_customer.getSelectedRow(), 0).toString());
                if (Helper.confirm("Are you sure?")) {
                    if (this.customerController.delete(selectedId)) {
                        Helper.showMsg("Customer deleted successfully!");
                        loadCustomerTable(null);
                        loadCartCustomerCombo();
                    } else {
                        Helper.showMsg("Error deleting customer.");
                    }
                }
            });
        }
    private void loadProductTable(ArrayList<Product> products) {
        if(tmdl_product.getColumnCount() == 0){
            Object[] columnProduct = {"ID", "Product Name", "Product Code", "Price", "Stock"};
            tmdl_product.setColumnIdentifiers(columnProduct);
        }

        if(products == null){
            products = this.productController.findAll();
        }

        DefaultTableModel model = (DefaultTableModel) this.tbl_product.getModel();
        this.tmdl_product.setRowCount(0);

        for (Product product : products){
            Object[] rowObject = {
                    product.getId(),
                    product.getName(),
                    product.getCode(),
                    product.getPrice(),
                    product.getStock()
            };
            this.tmdl_product.addRow(rowObject);
        }
        this.tbl_product.setModel(tmdl_product);
        this.tbl_product.getTableHeader().setReorderingAllowed(false);
        this.tbl_product.getColumnModel().getColumn(0).setPreferredWidth(50);
        this.tbl_product.setEnabled(false);
    }
    private void loadCustomerTable(ArrayList<Customer> customers) {
        if (tmdl_customer.getColumnCount() == 0) {
            Object[] columnCustomer = {"ID", "Customer Name", "Account Type", "Phone", "E-mail", "Address"};
            tmdl_customer.setColumnIdentifiers(columnCustomer);
        }

        // Clear table
        tmdl_customer.setRowCount(0);

        if (customers == null) {
            customers = this.customerController.findAll();
        }

        for (Customer customer : customers) {
            Object[] rowObject = {
                    customer.getId(),
                    customer.getName(),
                    customer.getType(),
                    customer.getPhone(),
                    customer.getMail(),
                    customer.getAddress()
            };
            tmdl_customer.addRow(rowObject);
        }
        this.tbl_customer.setModel(tmdl_customer);
        this.tbl_customer.getTableHeader().setReorderingAllowed(false);
        this.tbl_customer.getColumnModel().getColumn(0).setPreferredWidth(50);
        this.tbl_customer.setEnabled(false);
    }

    private void loadCartTable() {
        if (tmdl_cart.getColumnCount() == 0) {
            Object[] columnProduct = {"ID", "Product Name", "Product Code", "Price", "Stock"};
            tmdl_cart.setColumnIdentifiers(columnProduct);
        }

        tmdl_cart.setRowCount(0);

        ArrayList<Cart> carts = this.cartController.findAll();
        int totalPrice = 0;
        for (Cart cart : carts) {
            Object[] rowObject = {
                    cart.getId(),
                    cart.getProduct().getName(),
                    cart.getProduct().getCode(),
                    cart.getProduct().getPrice(),
                    cart.getProduct().getStock()
            };
            tmdl_cart.addRow(rowObject);
            totalPrice += cart.getProduct().getPrice();
        }
        this.lbl_cart_price.setText(totalPrice + " $");
        this.lbl_cart_p_quantity.setText(String.valueOf(carts.size()));

        this.tbl_cart.setModel(tmdl_cart);
        this.tbl_cart.getTableHeader().setReorderingAllowed(false);
        this.tbl_cart.getColumnModel().getColumn(0).setPreferredWidth(50);
        this.tbl_cart.setEnabled(false);
    }
}

