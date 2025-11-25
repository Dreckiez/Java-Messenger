package components;

import services.AvatarService;
import utils.ImageEditor;

import javax.swing.*;
import java.awt.*;

public abstract class BaseItem extends JPanel {

    protected String username;
    protected String avatarUrl;
    protected JPanel actionPanel;

    private boolean selected = false;
    private JLabel avatarLabel;

    public BaseItem(String username, String avatarUrl) {
        this.username = username;
        this.avatarUrl = avatarUrl;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        avatarLabel = createAvatarLabel();
        add(avatarLabel, BorderLayout.WEST);
    }

    private JLabel createAvatarLabel() {
        JLabel label = new JLabel();
        label.setPreferredSize(new Dimension(50, 50));

        Image img = AvatarService.loadAvatar(avatarUrl);
        ImageEditor editor = new ImageEditor();

        label.setIcon(editor.makeCircularImage(img, 50));
        return label;
    }

    protected abstract JPanel createCenterPanel();

    protected abstract JPanel createActionPanel();

    public void setSelected(boolean sel) {
        selected = sel;
        setBackground(sel ? new Color(230, 240, 255) : Color.WHITE);

        if (actionPanel != null)
            actionPanel.setVisible(sel);

        setMaximumSize(new Dimension(Integer.MAX_VALUE, getPreferredSize().height));

        if (sel)
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 4, 0, 0, new Color(59, 130, 246)),
                    BorderFactory.createEmptyBorder(10, 11, 10, 15)));
        else
            setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        // Notify parent container to recalculate layout
        Container parent = getParent();
        if (parent != null) {
            parent.revalidate();
            parent.repaint();
        }

        revalidate();
        repaint();
    }

    public void select() {
        setSelected(true);
    }

    public void deselect() {
        setSelected(false);
    }
}
