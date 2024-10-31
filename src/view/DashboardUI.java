package view;

import business.CustomerController;
import core.Helper;
import entitiy.Customer;
import entitiy.User;
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
    private JComboBox cmb_f_stock_status;
    private JButton btn_product_search;
    private JButton btn_product_edit;
    private JButton btn_clear_product_filter;
    private JLabel lbl_f_product_name;
    private JLabel lbl_f_product_code;
    private JLabel lbl_f_stock_status;
    private User user;
    private CustomerController customerController;
    private DefaultTableModel tmdl_customer = new DefaultTableModel();
    private JPopupMenu popup_customer = new JPopupMenu();

    public DashboardUI(User user) {
        this.user = user;
        this.customerController = new CustomerController();
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
        loadPopUpMenu();
        loadCustomerButtonEvent();
        this.cmb_f_customer_type.setModel(new DefaultComboBoxModel<>(Customer.TYPE.values()));
        this.cmb_f_customer_type.setSelectedItem(null);

    }

    private void loadCustomerButtonEvent() {
        this.btn_customer_new.addActionListener(e -> {
            CustomerUI customerUI = new CustomerUI(new Customer());
            customerUI.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadCustomerTable(null);
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

    private void loadPopUpMenu() {
        this.tbl_customer.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int selectedRow = tbl_customer.rowAtPoint(e.getPoint());
                tbl_customer.setRowSelectionInterval(selectedRow, selectedRow);
            }
        });
        this.popup_customer.add("Update").addActionListener(e -> {
            int selectedId = Integer.parseInt(tbl_customer.getValueAt(tbl_customer.getSelectedRow(), 0).toString());
            CustomerUI customerUI = new CustomerUI(this.customerController.findById(selectedId));
            customerUI.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadCustomerTable(null);
                }


            });
        });
        this.popup_customer.add("Delete").addActionListener(e -> {
            int selectedId = Integer.parseInt(tbl_customer.getValueAt(tbl_customer.getSelectedRow(), 0).toString());
            if(Helper.confirm("sure")){
                if(this.customerController.delete(selectedId)) {
                    Helper.showMsg("Success!");
                    loadCustomerTable(null);
                }else{
                    Helper.showMsg("error");
                }
            }
        });

        this.tbl_customer.setComponentPopupMenu(this.popup_customer);
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

        tbl_customer.setModel(tmdl_customer);
    }
}

