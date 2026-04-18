import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class signup {

    public void signUpView() {
        JFrame frame = new JFrame("Create New User");
        frame.setSize(550, 450);
        frame.setLayout(null);
        frame.getContentPane().setBackground(Color.decode("#32C7FB"));
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel heading = new JLabel("Create User");
        heading.setBounds(0, 50, 450, 50);
        heading.setHorizontalAlignment(JLabel.CENTER);
        heading.setFont(new Font("Times New Roman", Font.BOLD, 40));
        frame.add(heading);

        JLabel fName = new JLabel("Name : ");
        fName.setBounds(50, 120, 150, 50);
        frame.add(fName);

        JTextField fNameField = new JTextField();
        fNameField.setBounds(50, 160, 350, 30);
        frame.add(fNameField);

        JLabel uName = new JLabel("Username : ");
        uName.setBounds(50, 190, 150, 50);
        frame.add(uName);

        JTextField uNameField = new JTextField();
        uNameField.setBounds(50, 230, 350, 30);
        frame.add(uNameField);

        JLabel uPass = new JLabel("Password : ");
        uPass.setBounds(50, 260, 150, 50);
        frame.add(uPass);

        JPasswordField uPassField = new JPasswordField();
        uPassField.setBounds(50, 300, 150, 30);
        frame.add(uPassField);

        JLabel uPass2 = new JLabel("Confirm Password : ");
        uPass2.setBounds(250, 260, 150, 50);
        frame.add(uPass2);

        JPasswordField uPassField2 = new JPasswordField();
        uPassField2.setBounds(250, 300, 150, 30);
        frame.add(uPassField2);

        JButton submit = new JButton("SIGNUP");
        submit.setBounds(175, 350, 100, 40);
        frame.add(submit);

        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String fname = fNameField.getText().trim();
                String uname = uNameField.getText().trim();
                String pass1 = new String(uPassField.getPassword()).trim();
                String pass2 = new String(uPassField2.getPassword()).trim();

                if (fname.isEmpty() || uname.isEmpty() || pass1.isEmpty() || pass2.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please Enter All Information.", "Warning Message", JOptionPane.WARNING_MESSAGE);
                } else if (!pass1.equals(pass2)) {
                    JOptionPane.showMessageDialog(frame, "Password Mismatch!", "Warning Message", JOptionPane.WARNING_MESSAGE);
                } else {
                    try {
                        FileOperations fo = new FileOperations();
                        fo.newUser(fname, uname, pass1);

                        // Clear fields
                        fNameField.setText("");
                        uNameField.setText("");
                        uPassField.setText("");
                        uPassField2.setText("");

                        JOptionPane.showMessageDialog(frame, "User Created Successfully!", "Success", JOptionPane.PLAIN_MESSAGE);
                        frame.dispose();   // Close signup window

                    } catch (IOException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(frame, "Error creating user. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        frame.setVisible(true);
    }
}