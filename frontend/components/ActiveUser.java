package components;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import models.UserRecordOnlineModel;
import services.UserAdminService;
import utils.UserSession;

public class ActiveUser extends JPanel {
    private JTable userTable;
    private DefaultTableModel tableModel;
    private UserAdminService userService;

    // Filter Components
    private JTextField fullNameFilter;
    private JComboBox<String> sortByCombo;
    private JSpinner minActivitySpinner, maxActivitySpinner;

    // --- Enhanced Color Palette ---
    private final Color PRIMARY_COLOR = new Color(59, 130, 246);      // Blue-500
    private final Color PRIMARY_HOVER = new Color(37, 99, 235);       // Blue-600
    private final Color SUCCESS_COLOR = new Color(16, 185, 129);      // Green-500
    private final Color BG_COLOR = new Color(249, 250, 251);          // Gray-50
    private final Color CARD_BG = Color.WHITE;
    private final Color BORDER_COLOR = new Color(229, 231, 235);      // Gray-200
    private final Color TEXT_PRIMARY = new Color(17, 24, 39);         // Gray-900
    private final Color TEXT_SECONDARY = new Color(107, 114, 128);    // Gray-500
    private final Color HOVER_BG = new Color(249, 250, 251);          // Gray-50

    // Fonts
    private final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 28);
    private final Font FONT_SUBTITLE = new Font("Segoe UI", Font.PLAIN, 14);
    private final Font FONT_NORMAL = new Font("Segoe UI", Font.PLAIN, 13);
    private final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 13);
    private final Font FONT_LABEL = new Font("Segoe UI", Font.BOLD, 12);

    public ActiveUser() {
        this.userService = new UserAdminService();

        setLayout(new BorderLayout(0, 20));
        setBackground(BG_COLOR);
        setBorder(new EmptyBorder(25, 35, 25, 35));

        add(createHeaderPanel(), BorderLayout.NORTH);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);

        contentPanel.add(createFilterSection());
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(createTableSection());

        add(contentPanel, BorderLayout.CENTER);

        loadData();
    }

    // ================= UI SECTIONS =================

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setOpaque(false);
        
        JLabel title = new JLabel("Active Users Monitoring");
        title.setFont(FONT_TITLE);
        title.setForeground(TEXT_PRIMARY);
        
        JLabel subtitle = new JLabel("Monitor and track user activity in real-time");
        subtitle.setFont(FONT_SUBTITLE);
        subtitle.setForeground(TEXT_SECONDARY);
        subtitle.setBorder(new EmptyBorder(5, 0, 0, 0));
        
        leftPanel.add(title);
        leftPanel.add(subtitle);
        
        panel.add(leftPanel, BorderLayout.WEST);
        return panel;
    }

    private JPanel createFilterSection() {
        ShadowPanel panel = new ShadowPanel(12, CARD_BG);
        panel.setLayout(new GridBagLayout());
        panel.setBorder(new EmptyBorder(24, 24, 24, 24));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 0, 0, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridy = 0;
        
        // COL 1: Search Name
        gbc.gridx = 0; gbc.weightx = 0.25;
        fullNameFilter = createStyledTextField();
        panel.add(createInputGroup("Search by Name", fullNameFilter), gbc);

        // COL 2: Sort
        gbc.gridx = 1; gbc.weightx = 0.18;
        String[] sortOptions = {"Name (A-Z)", "Name (Z-A)", "Newest Joined", "Oldest Joined"};
        sortByCombo = createStyledComboBox(sortOptions);
        panel.add(createInputGroup("Sort Order", sortByCombo), gbc);

        // COL 3: Min Activity
        gbc.gridx = 2; gbc.weightx = 0.12;
        minActivitySpinner = createStyledSpinner();
        panel.add(createInputGroup("Min Activity", minActivitySpinner), gbc);

        // COL 4: Max Activity
        gbc.gridx = 3; gbc.weightx = 0.12;
        maxActivitySpinner = createStyledSpinner();
        panel.add(createInputGroup("Max Activity", maxActivitySpinner), gbc);

        // COL 5: Buttons - Không co giãn, luôn hiển thị
        gbc.gridx = 4; 
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(21, 12, 0, 0);
        
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        btnPanel.setOpaque(false);

        JButton btnReset = new ModernButton("Reset", new Color(243, 244, 246), TEXT_SECONDARY, false);
        btnReset.setPreferredSize(new Dimension(80, 40));
        btnReset.setMinimumSize(new Dimension(80, 40));

        JButton btnFilter = new ModernButton("Filter", PRIMARY_COLOR, Color.WHITE, true);
        btnFilter.setPreferredSize(new Dimension(80, 40));
        btnFilter.setMinimumSize(new Dimension(80, 40));
        
        btnReset.addActionListener(e -> resetFilters());
        btnFilter.addActionListener(e -> applyFilters());
        
        btnPanel.add(btnReset);
        btnPanel.add(btnFilter);

        panel.add(btnPanel, gbc);

        return panel;
    }

    private JPanel createInputGroup(String label, JComponent input) {
        JPanel p = new JPanel(new BorderLayout(0, 6));
        p.setOpaque(false);
        JLabel lbl = new JLabel(label);
        lbl.setFont(FONT_LABEL);
        lbl.setForeground(TEXT_SECONDARY);
        p.add(lbl, BorderLayout.NORTH);
        p.add(input, BorderLayout.CENTER);
        return p;
    }

    private JPanel createTableSection() {
        ShadowPanel panel = new ShadowPanel(12, CARD_BG);
        panel.setLayout(new BorderLayout());

        String[] columns = { "ID", "Username", "Full Name", "Status", "Activity Count", "Last Online", "Total Time" };
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };

        userTable = new JTable(tableModel);
        styleTable(userTable);

        // Enhanced Status Renderer with Badge
        userTable.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSel, boolean hasFocus, int row, int col) {
                JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
                panel.setOpaque(true);
                
                String status = (String) value;
                JLabel badge = new JLabel(status);
                badge.setFont(new Font("Segoe UI", Font.BOLD, 11));
                badge.setOpaque(true);
                badge.setBorder(new EmptyBorder(4, 12, 4, 12));
                
                if ("Online".equals(status)) {
                    badge.setForeground(new Color(5, 150, 105));
                    badge.setBackground(new Color(209, 250, 229));
                    RoundedBorder border = new RoundedBorder(12, new Color(167, 243, 208));
                    badge.setBorder(BorderFactory.createCompoundBorder(border, new EmptyBorder(3, 11, 3, 11)));
                } else {
                    badge.setForeground(new Color(107, 114, 128));
                    badge.setBackground(new Color(243, 244, 246));
                    RoundedBorder border = new RoundedBorder(12, new Color(229, 231, 235));
                    badge.setBorder(BorderFactory.createCompoundBorder(border, new EmptyBorder(3, 11, 3, 11)));
                }
                
                panel.add(badge);
                
                if (isSel) {
                    panel.setBackground(table.getSelectionBackground());
                } else {
                    panel.setBackground(row % 2 == 0 ? CARD_BG : HOVER_BG);
                }
                
                return panel;
            }
        });

        JScrollPane scroll = new JScrollPane(userTable);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(CARD_BG);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    // ================= LOGIC =================
    
    private void loadData() { applyFilters(); }

    private void applyFilters() {
        if (UserSession.getUser() == null) return;
        String token = UserSession.getUser().getToken();
        
        String keyword = fullNameFilter.getText().trim();
        String sortUI = (String) sortByCombo.getSelectedItem();
        
        String sortParam = "-createdAt"; 
        if (sortUI != null) {
            switch (sortUI) {
                case "Name (A-Z)": sortParam = "fullName"; break;
                case "Name (Z-A)": sortParam = "-fullName"; break;
                case "Newest Joined": sortParam = "-createdAt"; break;
                case "Oldest Joined": sortParam = "createdAt"; break;
            }
        }

        int min = (Integer) minActivitySpinner.getValue();
        int max = (Integer) maxActivitySpinner.getValue();
        Integer greaterThan = (min > 0) ? min : null;
        Integer smallerThan = (max > 0) ? max : null;

        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        String finalSort = sortParam;
        
        new SwingWorker<List<UserRecordOnlineModel>, Void>() {
            @Override
            protected List<UserRecordOnlineModel> doInBackground() throws Exception {
                return userService.getActiveUserRecords(token, keyword, finalSort, greaterThan, smallerThan);
            }
            @Override
            protected void done() {
                try { updateTable(get()); } catch (Exception e) { e.printStackTrace(); } 
                finally { setCursor(Cursor.getDefaultCursor()); }
            }
        }.execute();
    }

    private void updateTable(List<UserRecordOnlineModel> list) {
        tableModel.setRowCount(0);
        if (list == null) return;
        for (UserRecordOnlineModel u : list) {
            tableModel.addRow(new Object[]{
                u.getUserId(), u.getUsername(), u.getFullName(),
                u.isOnline() ? "Online" : "Offline",
                u.getActivityCount(), u.getFormattedLastOnline(), u.getFormattedTotalTime()
            });
        }
    }

    private void resetFilters() {
        fullNameFilter.setText("");
        sortByCombo.setSelectedIndex(2);
        minActivitySpinner.setValue(0);
        maxActivitySpinner.setValue(0);
        applyFilters();
    }

    // ================= STYLES =================
    
    private void styleTable(JTable table) {
        table.setFont(FONT_NORMAL);
        table.setRowHeight(48);
        table.setSelectionBackground(new Color(239, 246, 255));
        table.setSelectionForeground(TEXT_PRIMARY);
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setGridColor(new Color(243, 244, 246));
        table.setFocusable(false);
        table.setIntercellSpacing(new Dimension(0, 0));

        // Zebra striping
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSel, boolean hasFocus, int row, int col) {
                super.getTableCellRendererComponent(table, value, isSel, hasFocus, row, col);
                setHorizontalAlignment(CENTER);
                setFont(FONT_NORMAL);
                
                if (isSel) {
                    setBackground(table.getSelectionBackground());
                    setForeground(table.getSelectionForeground());
                } else {
                    setBackground(row % 2 == 0 ? CARD_BG : HOVER_BG);
                    setForeground(TEXT_PRIMARY);
                }
                
                return this;
            }
        });

        // Enhanced Header with Gradient
        JTableHeader header = table.getTableHeader();
        header.setFont(FONT_BOLD);
        header.setForeground(TEXT_SECONDARY);
        header.setPreferredSize(new Dimension(0, 44));
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSel, boolean hasFocus, int row, int col) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSel, hasFocus, row, col);
                label.setHorizontalAlignment(CENTER);
                label.setFont(FONT_BOLD);
                label.setForeground(TEXT_SECONDARY);
                label.setBackground(new Color(249, 250, 251));
                label.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 2, 0, BORDER_COLOR),
                    new EmptyBorder(0, 8, 0, 8)
                ));
                return label;
            }
        });

        // Column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(60);
        table.getColumnModel().getColumn(1).setPreferredWidth(120);
        table.getColumnModel().getColumn(2).setPreferredWidth(180);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);
        table.getColumnModel().getColumn(4).setPreferredWidth(120);
        table.getColumnModel().getColumn(5).setPreferredWidth(150);
        table.getColumnModel().getColumn(6).setPreferredWidth(100);
    }

    private JTextField createStyledTextField() {
        JTextField tf = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty() && !isFocusOwner()) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(156, 163, 175));
                    g2.setFont(FONT_NORMAL);
                    FontMetrics fm = g2.getFontMetrics();
                    int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                    g2.dispose();
                }
            }
        };
        styleInput(tf);
        return tf;
    }

    private JComboBox<String> createStyledComboBox(String[] items) {
        JComboBox<String> cb = new JComboBox<>(items);
        cb.setPreferredSize(new Dimension(cb.getPreferredSize().width, 40));
        cb.setFont(FONT_NORMAL);
        cb.setForeground(TEXT_PRIMARY);
        cb.setBackground(Color.WHITE);
        cb.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(0, 8, 0, 8)
        ));
        return cb;
    }

    private JSpinner createStyledSpinner() {
        JSpinner sp = new JSpinner(new SpinnerNumberModel(0, 0, 10000, 1));
        sp.setPreferredSize(new Dimension(100, 40));
        sp.setFont(FONT_NORMAL);
        sp.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(8, BORDER_COLOR),
            new EmptyBorder(0, 8, 0, 8)
        ));
        JComponent editor = sp.getEditor();
        if (editor instanceof JSpinner.DefaultEditor) {
            JTextField tf = ((JSpinner.DefaultEditor) editor).getTextField();
            tf.setHorizontalAlignment(JTextField.CENTER);
            tf.setFont(FONT_NORMAL);
        }
        return sp;
    }

    private void styleInput(JComponent c) {
        c.setPreferredSize(new Dimension(c.getPreferredSize().width, 40));
        c.setFont(FONT_NORMAL);
        c.setForeground(TEXT_PRIMARY);
        c.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(8, BORDER_COLOR),
            new EmptyBorder(0, 12, 0, 12)
        ));
    }

    // === Custom Components ===
    
    class ModernButton extends JButton {
        private Color baseColor, hoverColor, textColor;
        private boolean isHovered = false;
        
        public ModernButton(String text, Color bg, Color txt, boolean primary) {
            super(text);
            this.baseColor = bg;
            this.hoverColor = primary ? PRIMARY_HOVER : new Color(229, 231, 235);
            this.textColor = txt;
            
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setForeground(textColor);
            setFont(FONT_BOLD);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    isHovered = true;
                    repaint();
                }
                
                @Override
                public void mouseExited(MouseEvent e) {
                    isHovered = false;
                    repaint();
                }
            });
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            Color bgColor = isHovered ? hoverColor : baseColor;
            g2.setColor(bgColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
            
            super.paintComponent(g);
            g2.dispose();
        }
    }

    class ShadowPanel extends JPanel {
        private int radius;
        private Color bgColor;
        
        public ShadowPanel(int radius, Color bgColor) {
            this.radius = radius;
            this.bgColor = bgColor;
            setOpaque(false);
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Subtle shadow
            g2.setColor(new Color(0, 0, 0, 8));
            g2.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 4, radius, radius);
            
            // Background
            g2.setColor(bgColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            
            super.paintComponent(g);
            g2.dispose();
        }
    }

    class RoundedBorder extends AbstractBorder {
        private int radius;
        private Color color;
        
        public RoundedBorder(int radius, Color color) {
            this.radius = radius;
            this.color = color;
        }
        
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.drawRoundRect(x, y, w - 1, h - 1, radius, radius);
            g2.dispose();
        }
        
        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(radius / 2, radius + 2, radius / 2, radius + 2);
        }
    }
}