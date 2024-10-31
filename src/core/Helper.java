package core;

import javax.swing.*;
import java.util.Optional;

public class Helper {

    public static void setTheme() {
        for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
            if (info.getName().equals("Nimbus")) {
                try {
                    UIManager.setLookAndFeel(info.getClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                         UnsupportedLookAndFeelException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    public static boolean isFieldEmpty(JTextField field) {
        return field.getText().trim().isEmpty();
    }

    public static boolean isFieldListEmpty(JTextField[] fields) {
        for (JTextField field : fields) {
            if (isFieldEmpty(field)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isEmailValid(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        String emailRegex = "^[\\w-.]+@[\\w-]+\\.[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

    public static void showMsg(String message) {
        String msg;
        String title = switch (message) {
            case "fill" -> {
                msg = "The fields can not be empty!";
                yield "Error";
            }
            case "done" -> {
                msg = "Your account has been successfully logged in!";
                yield "Success";
            }
            case "error" -> {
                msg = "Something went wrong!";
                yield "Error";
            }
            default -> {
                msg = message;
                yield "Message";
            }
        };
        JOptionPane.showMessageDialog(null, msg, title, JOptionPane.INFORMATION_MESSAGE);
    }

    public static boolean confirm(String message) {
        String msg;
        if(message.equals("sure")){
            msg = "Are you sure?";
        }else{
            msg = message;
        }
        return JOptionPane.showConfirmDialog(null, msg, "Confirmation", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }
}

