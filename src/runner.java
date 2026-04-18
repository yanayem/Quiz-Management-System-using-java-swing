public class runner {
    public static void main(String args[]) {
        login loginScreen = new login();
        try {
            loginScreen.loginView();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error starting the application.");
        }
    }
}