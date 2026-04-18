import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;

public class login {
    int id;

    public void loginView() {

        JFrame frame = new JFrame("Quiz Management System - Login");
        frame.setSize(700, 550);
        frame.setLayout(null);
        frame.getContentPane().setBackground(Color.decode("#429ef5"));
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel heading = new JLabel("Quiz Management System");
        heading.setBounds(0, 50, 700, 50);
        heading.setHorizontalAlignment(JLabel.CENTER);
        heading.setFont(new Font("Times New Roman", Font.BOLD, 40));
        frame.add(heading);

        JLabel unameLabel = new JLabel("Username : ");
        unameLabel.setBounds(175, 130, 150, 50);
        frame.add(unameLabel);

        JTextField name = new JTextField();
        name.setBounds(175, 170, 350, 30);
        frame.add(name);

        JLabel upassLabel = new JLabel("Password : ");
        upassLabel.setBounds(175, 200, 150, 50);
        frame.add(upassLabel);

        JPasswordField pass = new JPasswordField();
        pass.setBounds(175, 240, 350, 30);
        frame.add(pass);

        // ================= LOGIN =================
        JButton loginBtn = new JButton("LOGIN");
        loginBtn.setBounds(225, 300, 100, 40);
        frame.add(loginBtn);

        loginBtn.addActionListener(e -> {

            String username = name.getText().trim();
            String password = new String(pass.getPassword()).trim();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please Enter All Information.");
                return;
            }

            try {
                FileOperations fo = new FileOperations();
                id = fo.authUser(username, password);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "File error!");
                return;
            }

            if (id == -1) {
                JOptionPane.showMessageDialog(frame, "User Not Found");
            } else if (id == 0) {
                JOptionPane.showMessageDialog(frame, "Wrong Password");
            } else {
                mainpage mainPage = new mainpage();
                mainPage.mainPageView(id);
                frame.dispose();
            }
        });

        // ================= SIGNUP =================
        JButton signUpBtn = new JButton("SIGNUP");
        signUpBtn.setBounds(375, 300, 100, 40);
        frame.add(signUpBtn);

        signUpBtn.addActionListener(e -> {
            signup s = new signup();
            s.signUpView();
        });

        // ================= GUEST BUTTON =================
        JButton attend = new JButton("Complete Quiz (Guest)");
        attend.setBounds(225, 360, 250, 40);
        frame.add(attend);

        attend.addActionListener(e -> {

            JFrame popup = new JFrame("Enter Quiz Code");
            popup.setSize(400, 350);
            popup.setLayout(null);
            popup.setLocationRelativeTo(frame);

            JLabel heading2 = new JLabel("Please Enter the Unique Quiz Code (5 CAPITAL letters):");
            heading2.setBounds(30, 0, 350, 30);
            heading2.setFont(new Font("Arial", Font.BOLD, 12));
            popup.add(heading2);

            JTextField codeField = new JTextField();
            codeField.setBounds(50, 30, 300, 30);
            popup.add(codeField);

            DefaultListModel<String> listModel = new DefaultListModel<>();
            JList<String> suggestionList = new JList<>(listModel);

            JScrollPane scroll = new JScrollPane(suggestionList);
            scroll.setBounds(50, 70, 300, 150);
            popup.add(scroll);

            JButton enterBtn = new JButton("START QUIZ");
            enterBtn.setBounds(120, 230, 150, 40);
            popup.add(enterBtn);

            FileOperations fo = new FileOperations();
            List<String> allCodes;

            try {
                allCodes = fo.getAllQuizCodes();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "File error loading codes");
                return;
            }

            codeField.addKeyListener(new KeyAdapter() {
                public void keyReleased(KeyEvent e) {
                    String input = codeField.getText().trim().toUpperCase();

                    listModel.clear();

                    for (String c : allCodes) {
                        if (c.startsWith(input)) {
                            listModel.addElement(c);
                        }
                    }
                }
            });

            suggestionList.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    codeField.setText(suggestionList.getSelectedValue());
                }
            });

            enterBtn.addActionListener(ev -> {
                String code = codeField.getText().trim().toUpperCase();

                if (code.length() != 5) {
                    JOptionPane.showMessageDialog(popup, "Code must be 5 letters");
                    return;
                }

                try {
                    if (!fo.getQuestions(code).isEmpty()) {
                        guest g = new guest();
                        g.guestView(code);
                        popup.dispose();
                    } else {
                        JOptionPane.showMessageDialog(popup, "Invalid Quiz Code");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });

            popup.setVisible(true);
        });

        frame.setVisible(true);
    }
}