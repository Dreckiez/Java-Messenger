package components;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.security.DigestException;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import java.util.function.Consumer;;

public class ChatList extends JPanel {
    private JList<String> chatList;

    public ChatList(Consumer<String> onChatSelected) {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(200, 0));

        JTextField searchField = new JTextField("Search...");
        add(searchField, BorderLayout.NORTH);

        chatList = new JList<>(new String[] { "Alice", "Bob", "Charlie" });
        // chatList.setSelectedIndex(0);
        chatList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selected = chatList.getSelectedValue();
                if (selected != null)
                    onChatSelected.accept(selected);
            }
        });

        add(new JScrollPane(chatList), BorderLayout.CENTER);
    }

    public void selectFirstChat() {
        if (chatList.getModel().getSize() > 0)
            chatList.setSelectedIndex(0);
    }
}
