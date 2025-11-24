package components;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;

import org.json.JSONArray;
import org.json.JSONObject;

import models.Contact;
import utils.ApiClient;
import utils.ApiUrl;
import utils.UserSession;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class SearchFriend extends JPanel {

    private JTextField searchField;
    private JPanel contactsPanel;

    private final List<ContactItem> displayed;

    private ContactItem selected;

    private SwingWorker<List<Contact>, Void> worker;

    public SearchFriend(NavPanel parent) {

        displayed = new ArrayList<>();

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        add(createHeader(), BorderLayout.NORTH);
        add(createScrollArea(), BorderLayout.CENTER);

        clearList(); // Start with empty list
    }

    private JPanel createHeader() {
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));
        header.setBackground(Color.WHITE);

        JLabel title = new JLabel("Search Friends");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        header.add(title);
        header.add(Box.createVerticalStrut(10));

        searchField = new JTextField("Search for friends...");
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setForeground(new Color(150, 150, 150));
        searchField.setBackground(new Color(240, 242, 245));
        searchField.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        // Placeholder logic
        searchField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (searchField.getText().equals("Search for friends...")) {
                    searchField.setText("");
                    searchField.setForeground(new Color(50, 50, 50));
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText("Search for friends...");
                    searchField.setForeground(new Color(150, 150, 150));
                }
            }
        });

        // Trigger backend search
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String text = searchField.getText().trim();
                if (text.isEmpty()) {
                    clearList();
                } else {
                    searchBackend(text);
                }
            }
        });

        header.add(searchField);
        return header;
    }

    private JScrollPane createScrollArea() {
        contactsPanel = new JPanel();
        contactsPanel.setLayout(new BoxLayout(contactsPanel, BoxLayout.Y_AXIS));
        contactsPanel.setBackground(Color.WHITE);

        JScrollPane scroll = new JScrollPane(contactsPanel);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(Color.WHITE);

        scroll.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(180, 180, 180);
                this.trackColor = new Color(240, 240, 240);
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return zero();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return zero();
            }

            private JButton zero() {
                JButton b = new JButton();
                b.setPreferredSize(new Dimension(0, 0));
                return b;
            }
        });

        return scroll;
    }

    public void clearList() {
        contactsPanel.removeAll();
        displayed.clear();
        contactsPanel.revalidate();
        contactsPanel.repaint();
    }

    private void displayResults(List<Contact> contacts) {
        clearList();

        for (Contact c : contacts) {
            ContactItem item = new ContactItem(c);
            setupItemClick(item);
            displayed.add(item);
            contactsPanel.add(item);
        }

        contactsPanel.revalidate();
        contactsPanel.repaint();
    }

    private void setupItemClick(ContactItem item) {
        item.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (selected != null)
                    selected.deselect();
                selected = item;
                item.select();
            }
        });
    }

    private void searchBackend(String keyword) {

        // Cancel previous search if still running
        if (worker != null && !worker.isDone()) {
            worker.cancel(true);
        }

        worker = new SwingWorker<List<Contact>, Void>() {
            @Override
            protected List<Contact> doInBackground() throws Exception {
                List<Contact> list = new ArrayList<>();

                String url = ApiUrl.SEARCH + "?keyword=" + keyword;

                // Call backend → JSON array text
                JSONObject json = ApiClient.getJSON(url, UserSession.getUser().getToken());

                JSONArray arr = json.getJSONArray("array");

                for (int i = 0; i < arr.length(); i++) {
                    JSONObject o = arr.getJSONObject(i);

                    int id = o.getInt("userId");
                    String name = o.optString("fullName", "");
                    String avatar = o.optString("avatarUrl", "");
                    boolean isFriend = false; // backend doesn't send this

                    list.add(new Contact(id, name, avatar, isFriend));
                }
                return list;
            }

            @Override
            protected void done() {
                try {
                    if (!isCancelled()) {
                        List<Contact> results = get();
                        displayResults(results);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    clearList();
                }
            }
        };

        worker.execute();
    }

    // When user opens the Search tab again
    public void resetSearch() {
        searchField.setText("Search for friends...");
        searchField.setForeground(new Color(150, 150, 150));
        clearList();
    }
}
