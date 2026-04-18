import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.List;
import javax.swing.*;

public class guest {
    FileOperations fo;
    List<String[]> questions;
    int[] userAnswers;
    int current = 0;

    public void guestView(String quizCode) {
        fo = new FileOperations();
        try {
            questions = fo.getQuestions(quizCode);
            if (questions.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Invalid Quiz Code!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            userAnswers = new int[questions.size()];

            JFrame frame = new JFrame("Attending Quiz");
            frame.setSize(800, 600);
            frame.setLayout(null);
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            JLabel title = new JLabel("ATTENDING THE QUIZ");
            title.setBounds(0, 40, 800, 50);
            title.setHorizontalAlignment(JLabel.CENTER);
            title.setFont(new Font("Times New Roman", Font.BOLD, 40));
            frame.add(title);

            JLabel quesLabel = new JLabel();
            quesLabel.setBounds(80, 140, 650, 50);
            quesLabel.setFont(new Font("Times New Roman", Font.BOLD, 18));
            frame.add(quesLabel);

            JRadioButton op1 = new JRadioButton(); op1.setBounds(100, 220, 600, 30);
            JRadioButton op2 = new JRadioButton(); op2.setBounds(100, 260, 600, 30);
            JRadioButton op3 = new JRadioButton(); op3.setBounds(100, 300, 600, 30);
            JRadioButton op4 = new JRadioButton(); op4.setBounds(100, 340, 600, 30);

            ButtonGroup group = new ButtonGroup();
            group.add(op1); group.add(op2); group.add(op3); group.add(op4);

            Font optFont = new Font("Times New Roman", Font.PLAIN, 16);
            op1.setFont(optFont); op2.setFont(optFont); op3.setFont(optFont); op4.setFont(optFont);

            frame.add(op1); frame.add(op2); frame.add(op3); frame.add(op4);

            JButton nextBtn = new JButton("NEXT");
            nextBtn.setBounds(100, 460, 600, 60);
            frame.add(nextBtn);

            loadQuestion(quesLabel, op1, op2, op3, op4);

            nextBtn.addActionListener(e -> {
                int choice = op1.isSelected() ? 1 : op2.isSelected() ? 2 : op3.isSelected() ? 3 : op4.isSelected() ? 4 : 0;
                if (choice == 0) {
                    JOptionPane.showMessageDialog(frame, "Please select an option!", "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                userAnswers[current] = choice;
                current++;

                if (current < questions.size()) {
                    loadQuestion(quesLabel, op1, op2, op3, op4);
                    group.clearSelection();
                } else {
                    showResult(frame, quizCode);
                    frame.dispose();
                }
            });

            frame.setVisible(true);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }



    private void loadQuestion(JLabel quesLabel, JRadioButton op1, JRadioButton op2, JRadioButton op3, JRadioButton op4) {
        String[] q = questions.get(current);
        quesLabel.setText((current + 1) + ". " + q[0]);
        op1.setText("1. " + q[1]);
        op2.setText("2. " + q[2]);
        op3.setText("3. " + q[3]);
        op4.setText("4. " + q[4]);
    }

    private void showResult(JFrame parent, String quizCode) {
        int score = 0;
        StringBuilder resultText = new StringBuilder("<html><h2>Quiz Result</h2><br>");

        for (int j = 0; j < questions.size(); j++) {
            String[] q = questions.get(j);
            int correct = Integer.parseInt(q[5]);
            int chosen = userAnswers[j];
            boolean isCorrect = chosen == correct;
            if (isCorrect) score++;

            resultText.append((j + 1) + ". ")
                      .append(isCorrect ? "✅ Correct" : "❌ Wrong")
                      .append(" (You chose " + chosen + ", Correct was " + correct + ")<br>");
        }

        resultText.append("<br><b>Your Score: ").append(score).append(" / ").append(questions.size()).append("</b></html>");

        JOptionPane.showMessageDialog(parent, resultText.toString(), "Quiz Completed", JOptionPane.INFORMATION_MESSAGE);
    }
}