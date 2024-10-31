package view;

import business.CustomerController;
import core.Helper;
import entitiy.Customer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CustomerUI extends JFrame {
    private JPanel container;
    private JTextField fld_customer_name;
    private JComboBox<Customer.TYPE> cmb_customer_type;
    private JLabel lbl_title;
    private JLabel lbl_name;
    private JLabel lbl_customer_mail;
    private JTextField fld_customer_mail;
    private JTextArea tarea_customer_address;
    private JLabel lbl_customer_address;
    private JButton btn_customer_save;
    private JTextField fld_customer_phone;
    private JLabel lbl_customer_phone;
    private Customer customer;
    private CustomerController customerController;


    public CustomerUI(Customer customer) {
        this.customer = customer;
        this.customerController = new CustomerController();
        this.add(container);
        this.setTitle("Add/Edit Customer");
        this.setSize(300, 500);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        this.cmb_customer_type.setModel(new DefaultComboBoxModel<>(Customer.TYPE.values()));

        if(customer.getId() == 0){
            this.lbl_title.setText("Add Customer");
        } else {
            this.lbl_title.setText("Edit Customer");
            this.fld_customer_name.setText(customer.getName());
            this.fld_customer_mail.setText(customer.getMail());
            this.fld_customer_phone.setText(customer.getPhone());
            this.cmb_customer_type.setSelectedItem(customer.getType());
            this.tarea_customer_address.setText(customer.getAddress());
        }
        this.btn_customer_save.addActionListener(e -> {
            JTextField[] checkList = {this.fld_customer_name, this.fld_customer_phone};
            if(Helper.isFieldListEmpty(checkList)){
                Helper.showMsg("fill");
            } else if(!Helper.isFieldEmpty(this.fld_customer_mail) && Helper.isEmailValid(String.valueOf(this.fld_customer_mail))){
                Helper.showMsg("Please enter a valid email.");
            } else {
                boolean result = false;
                this.customer.setName(this.fld_customer_name.getText());
                this.customer.setPhone(this.fld_customer_phone.getText());
                this.customer.setMail(this.fld_customer_mail.getText());
                this.customer.setType((Customer.TYPE) this.cmb_customer_type.getSelectedItem());
                this.customer.setAddress(this.tarea_customer_address.getText());

                if(this.customer.getId() == 0){
                    result = this.customerController.save(this.customer);
                } else{
                    result = this.customerController.update(this.customer);
                }

                if(result){
                    Helper.showMsg("Success!");
                    this.dispose();
                }else{
                    Helper.showMsg("Try again!");
                }
            }
        });
    }
}
