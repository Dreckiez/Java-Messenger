package app;

import javax.swing.SwingUtilities;
import screens.BaseScreen;
import screens.LoginScreen;
import screens.RegisterScreen;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BaseScreen screen = new BaseScreen();

            LoginScreen login = new LoginScreen(screen);
            RegisterScreen register = new RegisterScreen(screen);

            screen.addPanel(login, "login");
            screen.addPanel(register, "register");
            
            screen.showPanel("login");
        });
    }
}
