import java.io.*;
import java.util.*;

public class FileOperations {

    private static final String DATA_DIR = "data/";
    private static final String USERS_FILE = DATA_DIR + "users.txt";
    private static final String QUIZZES_FILE = DATA_DIR + "quizzes.txt";
    private static final String RESPONSES_FILE = DATA_DIR + "responses.txt";

    public FileOperations() {
        new File(DATA_DIR).mkdirs();
        createFilesIfNotExist();
    }

    private void createFilesIfNotExist() {
        try {
            new File(USERS_FILE).createNewFile();
            new File(QUIZZES_FILE).createNewFile();
            new File(RESPONSES_FILE).createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ====================== USER ======================
    public void newUser(String fname, String uname, String pass) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(USERS_FILE, true))) {
            bw.write(fname + "|" + uname + "|" + pass);
            bw.newLine();
        }
    }

    public int authUser(String uname, String pass) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            int id = 1;
            while ((line = br.readLine()) != null) {
                String[] p = line.split("\\|");
                if (p.length >= 3 && p[1].equals(uname)) {
                    return p[2].equals(pass) ? id : 0;
                }
                id++;
            }
        }
        return -1;
    }

    // ====================== QUIZ ======================
    public String generateUniqueCode() throws IOException {
        String code;
        do {
            code = "";
            String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
            for (int i = 0; i < 5; i++) {
                code += chars.charAt((int) (Math.random() * chars.length()));
            }
        } while (codeExists(code));
        return code;
    }

    private boolean codeExists(String code) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(QUIZZES_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith(code + "|")) return true;
            }
        }
        return false;
    }

    public void newQuestion(String code, String question, String op1, String op2, String op3, String op4, int correct) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(QUIZZES_FILE, true))) {
            bw.write(code + "|" + question + "|" + op1 + "|" + op2 + "|" + op3 + "|" + op4 + "|" + correct);
            bw.newLine();
        }
    }

    public List<String[]> getQuestions(String quizCode) throws IOException {
        List<String[]> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(QUIZZES_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|", 7);
                if (parts.length == 7 && parts[0].equals(quizCode)) {
                    list.add(new String[]{parts[1], parts[2], parts[3], parts[4], parts[5], parts[6]});
                }
            }
        }
        return list;
    }

    // FIXED: Now safely skips ANSWER lines
    public List<String> getUserQuizzes(int userId) throws IOException {
        List<String> codes = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(RESPONSES_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("ANSWER|")) continue;   // Skip answer lines
                String[] p = line.split("\\|");
                if (p.length >= 2 && p[0].equals(String.valueOf(userId))) {
                    codes.add(p[1]);
                }
            }
        }
        return codes;
    }

    public void saveUserQuiz(int userId, String quizCode) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RESPONSES_FILE, true))) {
            bw.write(userId + "|" + quizCode + "|0");
            bw.newLine();
        }
    }

    public void saveAnswer(String quizCode, int qno, int chosen) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RESPONSES_FILE, true))) {
            bw.write("ANSWER|" + quizCode + "|" + qno + "|" + chosen);
            bw.newLine();
        }
    }

    public int getOptionCount(String quizCode, int qno, int op) throws IOException {
        int count = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(RESPONSES_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("ANSWER|" + quizCode + "|" + qno + "|" + op)) {
                    count++;
                }
            }
        }
        return count;
    }

    public void deleteQuiz(String quizCode) throws IOException {

        // delete quizzes
        List<String> quizLines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(QUIZZES_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.startsWith(quizCode + "|")) {
                    quizLines.add(line);
                }
            }
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(QUIZZES_FILE))) {
            for (String l : quizLines) bw.write(l + "\n");
        }

        // 🔥 ALSO delete responses
        List<String> respLines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(RESPONSES_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.contains(quizCode + "|") && !line.startsWith("ANSWER|" + quizCode)) {
                    respLines.add(line);
                }
            }
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RESPONSES_FILE))) {
            for (String l : respLines) bw.write(l + "\n");
        }
    }

    public List<String> getAllQuizCodes() throws IOException {
        List<String> codes = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(QUIZZES_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length > 0) {
                    if (!codes.contains(parts[0])) {
                        codes.add(parts[0]);
                    }
                }
            }
        }
        return codes;
    }


}