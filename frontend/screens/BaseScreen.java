package screens;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.CardLayout;

public class BaseScreen extends JFrame {

    private CardLayout layout;
    private JPanel panel;

    public BaseScreen() {
        setTitle("Chat Sech with Loli");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setSize(1280, 720);
        setResizable(true);

        layout = new CardLayout();
        panel = new JPanel(layout);

        add(panel);

        ImageIcon image = new ImageIcon(getClass().getClassLoader().getResource("assets/wolf-howling.jpg"));
        setIconImage(image.getImage());

        setVisible(true);
    }

    public void addPanel(JPanel p, String name) {
        panel.add(p, name);
    }

    public void showPanel(String name) {
        layout.show(panel, name);
    }
}
