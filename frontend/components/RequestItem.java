package components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI; 

import org.json.JSONObject;

import models.Request;
import utils.ApiClient;
import utils.ApiUrl;
import utils.TimeHandler;
import utils.UserSession;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RequestItem extends BaseItem {

    private boolean isSelected = false;
    private Request request;
    private String sentAt;
    private ActionListener onRequestHandled;
    
    private JButton acceptBtn;
    private JButton denyBtn;

    // --- MÀU SẮC ĐỒNG BỘ ---
    private static final Color TEXT_PRIMARY = new Color(30, 41, 59);     
    private static final Color TEXT_SECONDARY = new Color(148, 163, 184); 
    private static final Color COLOR_GREEN_ACCEPT = new Color(16, 185, 129); 
    private static final Color COLOR_RED_DENY = new Color(239, 68, 68); 
    private static final Color COLOR_WHITE = Color.WHITE;
    private static final Color BG_HOVER = new Color(241, 245, 249); 
    private static final Color BG_SELECTED = new Color(219, 234, 254); 
    private static final Color BORDER_COLOR = new Color(226, 232, 240); 

    public RequestItem(Request r) {
        super(r.getName(), r.getAvatarUrl());
        this.request = r;
        this.sentAt = (r.getSendTime() != null) ? r.getSendTime() : getCurrentTimestamp();

        setLayout(new BorderLayout(15, 0)); // Thêm gap 15px giữa Avatar và nội dung
        setBackground(COLOR_WHITE);

        // 🔥🔥🔥 BƯỚC 1: THÊM AVATAR VÀO WEST 🔥🔥🔥
        // Giả định Avatar là component đầu tiên được BaseItem thêm vào (getComponent(0))
        if (getComponentCount() > 0) {
            Component avatarComponent = getComponent(0);
            
            // Xóa component khỏi vị trí mặc định
            super.remove(avatarComponent); 
            
            // Thêm lại vào vị trí WEST trong layout mới
            add(avatarComponent, BorderLayout.WEST); 
        }
        
        // --- CENTER PANEL (Name & Info) ---
        JPanel centerWrapper = new JPanel(new BorderLayout());
        centerWrapper.setOpaque(false);
        centerWrapper.add(createCenterPanel(), BorderLayout.CENTER); 
        add(centerWrapper, BorderLayout.CENTER);

        // --- ACTION PANEL (SOUTH - Expand/Collapse) ---
        JPanel rawActionPanel = createActionPanel();
        if (rawActionPanel != null) {
            JPanel actionWrapper = new JPanel(new BorderLayout());
            actionWrapper.setOpaque(false);
            actionWrapper.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0)); 

            JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);
            separator.setForeground(BORDER_COLOR);
            actionWrapper.add(separator, BorderLayout.NORTH);

            actionWrapper.add(rawActionPanel, BorderLayout.CENTER);
            add(actionWrapper, BorderLayout.SOUTH);

            actionWrapper.setVisible(false);
            this.actionPanel = actionWrapper;
        }

        // --- EVENTS ---
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                if (!(e.getSource() instanceof JButton)) { 
                    setSelected(!isSelected); 
                }
            }
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                if (!isSelected) {
                    setBackground(BG_HOVER);
                }
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                if (!isSelected) {
                    setBackground(COLOR_WHITE);
                }
            }
        });

        setAlignmentX(Component.LEFT_ALIGNMENT);
        setMinimumSize(new Dimension(0, 70));
        setPreferredSize(new Dimension(0, 95));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, getPreferredSize().height));
    }

    private String getCurrentTimestamp() {
        LocalDateTime now = LocalDateTime.now();
        return now.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    public Request getRequest() {
        return request;
    }

    public void setOnRequestHandled(ActionListener callback) {
        this.onRequestHandled = callback;
    }

    @Override
    protected JPanel createCenterPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 1, 0, 4)); 
        panel.setOpaque(false);

        JLabel name = new JLabel(username);
        name.setFont(new Font("Segoe UI", Font.BOLD, 15)); 
        name.setForeground(TEXT_PRIMARY);

        JLabel info = new JLabel("sent you a friend request");
        info.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        info.setForeground(TEXT_SECONDARY);

        TimeHandler th = new TimeHandler();
        JLabel timeLabel = new JLabel("• " + th.formatTimeAgo(sentAt));
        timeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        timeLabel.setForeground(TEXT_SECONDARY);
        
        JPanel statusTimePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        statusTimePanel.setOpaque(false);
        statusTimePanel.add(info);
        statusTimePanel.add(timeLabel);

        panel.add(name);
        panel.add(statusTimePanel);
        
        return panel;
    }

    @Override
    protected JPanel createActionPanel() {
        // FlowLayout.CENTER để căn giữa các nút theo chiều ngang
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10)); 
        panel.setOpaque(false);
        
        // Accept button (Pill Shape - Xanh lá)
        acceptBtn = createPillButton("Accept", COLOR_GREEN_ACCEPT, new Color(22, 163, 74));
        acceptBtn.addActionListener(e -> handleAccept());
        acceptBtn.setPreferredSize(new Dimension(80, 30)); 

        // Deny button (Pill Shape - Đỏ)
        denyBtn = createPillButton("Deny", COLOR_RED_DENY, new Color(185, 28, 28));
        denyBtn.addActionListener(e -> handleDeny());
        denyBtn.setPreferredSize(new Dimension(80, 30)); 
        
        panel.add(acceptBtn);
        panel.add(denyBtn);
        
        // Đặt kích thước ưu tiên/tối thiểu cho Action Panel
        panel.setPreferredSize(new Dimension(Integer.MAX_VALUE, 40));
        panel.setMinimumSize(new Dimension(0, 10));
        
        return panel;
    }

    // Hàm tạo nút Pill Shape (Đồng bộ)
    private JButton createPillButton(String text, Color baseColor, Color hoverColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setForeground(Color.WHITE); 
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.setBorder(new EmptyBorder(0, 10, 0, 10)); 

        button.setUI(new CustomButtonUI(baseColor, hoverColor));
        return button;
    }

    // Custom Button UI (Đồng bộ)
    static class CustomButtonUI extends BasicButtonUI {
        private final Color baseColor;
        private final Color hoverColor;

        public CustomButtonUI(Color base, Color hover) {
            this.baseColor = base;
            this.hoverColor = hover;
        }

        @Override
        public void paint(Graphics g, JComponent c) {
            JButton button = (JButton) c;
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            Color paintColor = baseColor;
            if (button.getModel().isRollover() && button.isEnabled()) {
                paintColor = hoverColor;
            }
            
            g2.setColor(paintColor);
            g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), c.getHeight(), c.getHeight());
            
            super.paint(g2, c);
            g2.dispose();
        }
        
        @Override
        protected void paintFocus(Graphics g, AbstractButton b, Rectangle viewRect, Rectangle textRect, Rectangle iconRect) {
        }
    }

    private void handleAccept() {
        acceptBtn.setEnabled(false);
        denyBtn.setEnabled(false);

        int response = JOptionPane.showConfirmDialog(
                this,
                "Accept friend request from " + request.getName() + "?",
                "Accept Friend Request",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (response == JOptionPane.YES_OPTION) {
            sendDecision("ACCEPTED", "You are now friends with " + request.getName(), "Friend Request Accepted");
        } else {
             acceptBtn.setEnabled(true);
             denyBtn.setEnabled(true);
        }
    }

    private void handleDeny() {
        acceptBtn.setEnabled(false);
        denyBtn.setEnabled(false);
        
        int response = JOptionPane.showConfirmDialog(
                this,
                "Decline friend request from " + request.getName() + "?",
                "Decline Friend Request",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (response == JOptionPane.YES_OPTION) {
            sendDecision("REJECTED", "Friend request from " + request.getName() + " declined", "Friend Request Declined");
        } else {
            acceptBtn.setEnabled(true);
            denyBtn.setEnabled(true);
        }
    }
    
    private void sendDecision(String status, String successMessage, String successTitle) {
        
        JSONObject payload = new JSONObject();
        payload.put("senderId", request.getUserId());
        payload.put("receiverId", UserSession.getUser().getId());
        payload.put("sentAt", request.getSendTime());
        payload.put("status", status);

        SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                try {
                    ApiClient.postJSON(ApiUrl.DECIDE_FRIEND_REQUEST, payload, UserSession.getUser().getToken());
                    return true;
                } catch (Exception e) {
                    return false;
                }
            }

            @Override
            protected void done() {
                try {
                    if (get()) {
                        JOptionPane.showMessageDialog(
                                RequestItem.this,
                                successMessage,
                                successTitle,
                                JOptionPane.INFORMATION_MESSAGE);

                        if (onRequestHandled != null) {
                            onRequestHandled.actionPerformed(null);
                        }
                    } else {
                        JOptionPane.showMessageDialog(
                                RequestItem.this,
                                "Failed to process friend request",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                        
                        acceptBtn.setEnabled(true);
                        denyBtn.setEnabled(true);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    acceptBtn.setEnabled(true);
                    denyBtn.setEnabled(true);
                }
            }
        };
        worker.execute();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (isSelected) {
            g.setColor(new Color(59, 130, 246));
            g.fillRect(0, 0, 3, getHeight());
        }
    }
    
    @Override
    public void setSelected(boolean selected) {
        this.isSelected = selected;
        
        int actionPanelHeight = (actionPanel != null) ? actionPanel.getPreferredSize().height : 0; 
        int baseHeight = 95; 

        if (selected) {
            setBackground(BG_SELECTED);
            if (actionPanel != null) {
                actionPanel.setVisible(true);
                setMaximumSize(new Dimension(Integer.MAX_VALUE, baseHeight + actionPanelHeight));
            }
        } else {
            setBackground(COLOR_WHITE);
            if (actionPanel != null) {
                actionPanel.setVisible(false);
                setMaximumSize(new Dimension(Integer.MAX_VALUE, baseHeight));
            }
        }
        
        if (getParent() != null) {
            getParent().revalidate();
        }
        repaint();
    }
}