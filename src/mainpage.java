import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class mainpage {

    FileOperations fo;
    JButton submit;
    String[] queStr = new String[50];
    String[] op1Str = new String[50];
    String[] op2Str = new String[50];
    String[] op3Str = new String[50];
    String[] op4Str = new String[50];
    int[] correctStr = new int[50];
    int i = 0;
    int id;
    String cd;
    int h = 0;
    static DefaultTableModel model;

    String[] questionsArray = new String[25];
    String[] option1Array = new String[25];
    String[] option2Array = new String[25];
    String[] option3Array = new String[25];
    String[] option4Array = new String[25];
    int[] correctArray = new int[25];

    public void mainPageView(int userId) {
        this.id = userId;
        fo = new FileOperations();

        JFrame frame = new JFrame("Quiz Management System");
        frame.setSize(800, 600);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // ====================== ADD QUIZ PANEL ======================
        JPanel addPanel = new JPanel();
        addPanel.setBounds(250, 0, 550, 600);
        addPanel.setLayout(null);

        JLabel title = new JLabel("CREATE A QUIZ");
        title.setBounds(0, 10, 550, 50);
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setFont(new Font("Times New Roman", Font.BOLD, 40));
        addPanel.add(title);

        JLabel qLabel = new JLabel("Question :");
        qLabel.setBounds(50, 80, 100, 25);
        addPanel.add(qLabel);

        JTextField qField = new JTextField();
        qField.setBounds(50, 105, 450, 30);
        addPanel.add(qField);

        JLabel op1Label = new JLabel("Option 1 :");
        op1Label.setBounds(50, 145, 100, 25);
        addPanel.add(op1Label);
        JTextField op1Field = new JTextField();
        op1Field.setBounds(50, 170, 200, 30);
        addPanel.add(op1Field);

        JLabel op2Label = new JLabel("Option 2 :");
        op2Label.setBounds(50, 205, 100, 25);
        addPanel.add(op2Label);
        JTextField op2Field = new JTextField();
        op2Field.setBounds(50, 230, 200, 30);
        addPanel.add(op2Field);

        JLabel op3Label = new JLabel("Option 3 :");
        op3Label.setBounds(50, 265, 100, 25);
        addPanel.add(op3Label);
        JTextField op3Field = new JTextField();
        op3Field.setBounds(50, 290, 200, 30);
        addPanel.add(op3Field);

        JLabel op4Label = new JLabel("Option 4 :");
        op4Label.setBounds(50, 325, 100, 25);
        addPanel.add(op4Label);
        JTextField op4Field = new JTextField();
        op4Field.setBounds(50, 350, 200, 30);
        addPanel.add(op4Field);

        JLabel correctLabel = new JLabel("Correct Option :");
        correctLabel.setBounds(280, 325, 120, 25);
        addPanel.add(correctLabel);

        JComboBox<Integer> correctBox = new JComboBox<>(new Integer[]{1, 2, 3, 4});
        correctBox.setBounds(400, 350, 80, 30);
        addPanel.add(correctBox);

        JButton addQuestionBtn = new JButton("ADD QUESTION");
        addQuestionBtn.setBounds(50, 410, 450, 40);
        addPanel.add(addQuestionBtn);

        submit = new JButton("SUBMIT QUIZ");
        submit.setBounds(50, 470, 200, 50);
        submit.setEnabled(false);
        addPanel.add(submit);

        JButton cancelBtn = new JButton("CANCEL");
        cancelBtn.setBounds(300, 470, 200, 50);
        addPanel.add(cancelBtn);

        addQuestionBtn.addActionListener(e -> {
            String question = qField.getText().trim();
            String op1 = op1Field.getText().trim();
            String op2 = op2Field.getText().trim();
            String op3 = op3Field.getText().trim();
            String op4 = op4Field.getText().trim();
            int correct = (Integer) correctBox.getSelectedItem();

            if (question.isEmpty() || op1.isEmpty() || op2.isEmpty() || op3.isEmpty() || op4.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please fill all fields!", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            queStr[i] = question;
            op1Str[i] = op1;
            op2Str[i] = op2;
            op3Str[i] = op3;
            op4Str[i] = op4;
            correctStr[i] = correct;
            i++;

            // Clear fields for next question
            qField.setText("");
            op1Field.setText("");
            op2Field.setText("");
            op3Field.setText("");
            op4Field.setText("");

            submit.setEnabled(true);
            JOptionPane.showMessageDialog(frame, "Question " + i + " added successfully!", "Added", JOptionPane.PLAIN_MESSAGE);
        });

        // Submit Quiz Action
        submit.addActionListener(e -> {
            if (i == 0) {
                JOptionPane.showMessageDialog(frame, "No questions added yet!", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                String code = fo.generateUniqueCode();
                fo.saveUserQuiz(id, code);

                for (int j = 0; j < i; j++) {
                    fo.newQuestion(code, queStr[j], op1Str[j], op2Str[j], op3Str[j], op4Str[j], correctStr[j]);
                }

                JOptionPane.showMessageDialog(frame,
                        "Quiz Created Successfully!\n\nQuiz Code: " + code + "\nShare this code with guests.",
                        "Congratulations", JOptionPane.PLAIN_MESSAGE);

                i = 0;
                submit.setEnabled(false);
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Error saving quiz!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelBtn.addActionListener(e -> {
            i = 0;
            submit.setEnabled(false);
            qField.setText("");
            op1Field.setText("");
            op2Field.setText("");
            op3Field.setText("");
            op4Field.setText("");
        });

        frame.add(addPanel);

        // ====================== VIEW QUIZ PANEL ======================
        JPanel viewPanel = new JPanel();
        viewPanel.setBounds(250, 0, 550, 600);
        viewPanel.setLayout(null);

        JLabel searchLabel = new JLabel("Search : ");
        searchLabel.setBounds(100, 20, 100, 30);
        viewPanel.add(searchLabel);

        JTextField searchField = new JTextField();
        searchField.setBounds(170, 20, 280, 30);
        viewPanel.add(searchField);
        searchField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                tableupdate(searchField.getText());
            }
        });

        JTable table = new JTable() {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        model = (DefaultTableModel) table.getModel();
        model.addColumn("Your Quizzes");
        table.setBackground(Color.decode("#f9d6c4"));
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(100, 70, 350, 200);
        viewPanel.add(scroll);

        JLabel quesView = new JLabel();
        quesView.setBounds(50, 310, 450, 40);
        quesView.setFont(new Font("Times New Roman", Font.BOLD, 16));
        viewPanel.add(quesView);

        JLabel op1v = new JLabel(), op2v = new JLabel(), op3v = new JLabel(), op4v = new JLabel();
        JLabel count1 = new JLabel(), count2 = new JLabel(), count3 = new JLabel(), count4 = new JLabel();

        op1v.setBounds(70, 360, 380, 25); count1.setBounds(480, 360, 50, 25);
        op2v.setBounds(70, 390, 380, 25); count2.setBounds(480, 390, 50, 25);
        op3v.setBounds(70, 420, 380, 25); count3.setBounds(480, 420, 50, 25);
        op4v.setBounds(70, 450, 380, 25); count4.setBounds(480, 450, 50, 25);

        viewPanel.add(op1v); viewPanel.add(count1);
        viewPanel.add(op2v); viewPanel.add(count2);
        viewPanel.add(op3v); viewPanel.add(count3);
        viewPanel.add(op4v); viewPanel.add(count4);

        JButton prevBtn = new JButton("PREVIOUS");
        prevBtn.setBounds(100, 280, 100, 40);
        prevBtn.setEnabled(false);
        viewPanel.add(prevBtn);

        JButton deleteBtn = new JButton("DELETE");
        deleteBtn.setBounds(210, 280, 130, 40);
        deleteBtn.setEnabled(false);
        viewPanel.add(deleteBtn);

        JButton nextBtn = new JButton("NEXT");
        nextBtn.setBounds(350, 280, 100, 40);
        nextBtn.setEnabled(false);
        viewPanel.add(nextBtn);

        // Table Click
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row == -1) return;
                cd = (String) model.getValueAt(row, 0);
                h = 0;
                deleteBtn.setEnabled(true);
                prevBtn.setEnabled(true);
                nextBtn.setEnabled(true);
                loadQuestionData(quesView, op1v, op2v, op3v, op4v, count1, count2, count3, count4);
            }
        });

        prevBtn.addActionListener(e -> {
            if (h > 0) {
                h--;
                loadQuestionData(quesView, op1v, op2v, op3v, op4v, count1, count2, count3, count4);
            }
        });

        nextBtn.addActionListener(e -> {
            try {
                List<String[]> qList = fo.getQuestions(cd);

                if (h < qList.size() - 1) {
                    h++;
                    loadQuestionData(quesView, op1v, op2v, op3v, op4v, count1, count2, count3, count4);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        deleteBtn.addActionListener(e -> {

            if (cd == null) {
                JOptionPane.showMessageDialog(frame, "Please select a quiz first!");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(
                    frame,
                    "Delete this quiz and all responses?",
                    "Confirm",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    fo.deleteQuiz(cd);

                    tableupdate("");   // refresh table
                    h = 0;             // reset index

                    JOptionPane.showMessageDialog(frame, "Quiz Deleted Successfully");

                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        frame.add(viewPanel);

        // ====================== SIDE MENU ======================
        JPanel sidePanel = new JPanel();
        sidePanel.setBounds(0, 0, 250, 600);
        sidePanel.setBackground(Color.decode("#429ef5"));
        sidePanel.setLayout(null);
        frame.add(sidePanel);

        JButton addQuizBtn = new JButton("ADD QUIZ");
        addQuizBtn.setBounds(50, 120, 150, 50);
        sidePanel.add(addQuizBtn);
        addQuizBtn.addActionListener(e -> {
            viewPanel.setVisible(false);
            addPanel.setVisible(true);
        });

        JButton viewQuizBtn = new JButton("VIEW QUIZ");
        viewQuizBtn.setBounds(50, 220, 150, 50);
        sidePanel.add(viewQuizBtn);
        viewQuizBtn.addActionListener(e -> {
            tableupdate("");
            viewPanel.setVisible(true);
            addPanel.setVisible(false);
        });

        JButton logoutBtn = new JButton("LOGOUT");
        logoutBtn.setBounds(50, 420, 150, 50);
        sidePanel.add(logoutBtn);
        logoutBtn.addActionListener(e -> {
            login loginScreen = new login();
            try {
                loginScreen.loginView();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            frame.dispose();
        });

        // Initial visibility
        viewPanel.setVisible(false);
        addPanel.setVisible(true);

        frame.setVisible(true);
    }

    private void loadQuestionData(JLabel quesView, JLabel op1v, JLabel op2v, JLabel op3v, JLabel op4v,
                                  JLabel c1, JLabel c2, JLabel c3, JLabel c4) {
        try {
            List<String[]> qList = fo.getQuestions(cd);
            int idx = 0;
            for (String[] q : qList) {
                questionsArray[idx] = q[0];
                option1Array[idx] = q[1];
                option2Array[idx] = q[2];
                option3Array[idx] = q[3];
                option4Array[idx] = q[4];
                correctArray[idx] = Integer.parseInt(q[5]);
                idx++;
            }

            if (questionsArray[h] != null) {
                quesView.setText((h + 1) + ". " + questionsArray[h]);
                op1v.setText("1. " + option1Array[h]);
                op2v.setText("2. " + option2Array[h]);
                op3v.setText("3. " + option3Array[h]);
                op4v.setText("4. " + option4Array[h]);

                // Show vote counts
                c1.setText(String.valueOf(fo.getOptionCount(cd, h + 1, 1)));  // Note: getOptionCount needs to be added in FileOperations if not present
                c2.setText(String.valueOf(fo.getOptionCount(cd, h + 1, 2)));
                c3.setText(String.valueOf(fo.getOptionCount(cd, h + 1, 3)));
                c4.setText(String.valueOf(fo.getOptionCount(cd, h + 1, 4)));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void tableupdate(String search) {
        try {
            model.setRowCount(0);
            List<String> codes = fo.getUserQuizzes(id);
            for (String code : codes) {
                if (code.toUpperCase().contains(search.toUpperCase())) {
                    model.addRow(new Object[]{code});
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}