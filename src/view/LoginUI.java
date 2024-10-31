package view;

import business.UserController;
import core.Helper;
import entitiy.User;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginUI extends JFrame {
    private JPanel container;
    private JPanel pnl_top;
    private JLabel lbl_title;
    private JPanel pnl_bottom;
    private JTextField fld_email;
    private JButton btn_login;
    private JLabel lbl_mail;
    private JLabel lbl_password;
    private JPasswordField fld_password;
    private UserController userController;

    public LoginUI() {
        this.add(container);
        this.setTitle("Customer Management System");
        this.setSize(400,400);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.userController = new UserController();

        this.btn_login.addActionListener(e -> {
            JTextField[] checkList = {this.fld_email, this.fld_password};
            if (!Helper.isEmailValid(this.fld_email.getText())) {
                Helper.showMsg("error");
            } else if (Helper.isFieldListEmpty(checkList)) {
                Helper.showMsg("fill");
            } else {
                String password = new String(this.fld_password.getPassword());
                System.out.println("Giriş yapılıyor: " + fld_email.getText() + " - " + password);

                User user = this.userController.findByLogin(this.fld_email.getText(), password);
                if (user == null) {
                    Helper.showMsg("User not found!");
                } else {
                    Helper.showMsg("done");
                    this.dispose();
                    DashboardUI dashboardUI = new DashboardUI(user);
                    dashboardUI.setVisible(true);
                }
            }
        });
    }
}
