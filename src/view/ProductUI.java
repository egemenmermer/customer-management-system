package view;

import business.ProductController;
import core.Helper;
import entity.Product;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProductUI extends JFrame {
    private JPanel container;
    private JTextField fld_product_name;
    private JTextField fld_product_code;
    private JTextField fld_product_price;
    private JButton btn_product_save;
    private JLabel lbl_p_title;
    private JLabel lbl_product_name;
    private JLabel lbl_product_code;
    private JLabel lbl_product_price;
    private JLabel lbl_product_stock;
    private JTextField fld_product_stock;
    private ProductController productController;
    private Product product;


    public ProductUI(Product product) {
        this.product = product;
        this.productController = new ProductController();

        this.add(container);
        this.setTitle("Add/Edit Product");
        this.setSize(300, 350);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        if(this.product.getId() == 0){
            this.lbl_p_title.setText("Add Products");
        }else {
            this.lbl_p_title.setText("Edit Product");
            this.fld_product_name.setText(product.getName());
            this.fld_product_code.setText(product.getCode());
            this.fld_product_price.setText(String.valueOf(product.getPrice()));
            this.fld_product_stock.setText(String.valueOf(product.getStock()));
        }
        btn_product_save.addActionListener(e -> {
            JTextField[] checklist = {
                    this.fld_product_name,
                    this.fld_product_code,
                    this.fld_product_price,
                    this.fld_product_stock
            };

            if (Helper.isFieldListEmpty(checklist)) {
                Helper.showMsg("fill");
            } else {
                this.product.setName(this.fld_product_name.getText());
                this.product.setCode(this.fld_product_code.getText());
                this.product.setPrice(Integer.parseInt(this.fld_product_price.getText()));
                this.product.setStock(Integer.parseInt(this.fld_product_stock.getText()));


                boolean result;
                if (this.product.getId() == 0){
                    result = this.productController.save(this.product);
                } else {
                    result = this.productController.update(this.product);
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
