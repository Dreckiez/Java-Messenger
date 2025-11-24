package components;

import javax.swing.*;

import org.json.JSONObject;

import models.Request;
import utils.ApiClient;
import utils.ApiUrl;
import utils.TimeHandler;
import utils.UserSession;

import java.awt.*;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RequestItem extends BaseItem {
    private Request request;
    private String sentAt;
    private ActionListener onRequestHandled;

    public RequestItem(Request r) {
        super(r.getName(), r.getAvatarUrl());
        this.request = r;
        this.sentAt = (r.getSendTime() != null) ? r.getSendTime() : getCurrentTimestamp();
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
        JPanel panel = new JPanel(new BorderLayout(0, 3));
        panel.setOpaque(false);

        JLabel name = new JLabel(username);
        name.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JLabel info = new JLabel("sent you a friend request");
        info.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        info.setForeground(new Color(130, 130, 130));

        TimeHandler th = new TimeHandler();
        JLabel timeLabel = new JLabel(th.formatTimeAgo(sentAt));
        timeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        timeLabel.setForeground(new Color(150, 150, 150));

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        textPanel.add(name);
        textPanel.add(info);
        textPanel.add(timeLabel);

        panel.add(textPanel, BorderLayout.CENTER);

        return panel;
    }

    @Override
    protected JPanel createActionPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setOpaque(false);

        // Accept button
        JButton acceptBtn = new JButton("Accept");
        acceptBtn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        acceptBtn.setForeground(Color.WHITE);
        acceptBtn.setBackground(new Color(0, 122, 255));
        acceptBtn.setFocusPainted(false);
        acceptBtn.setBorderPainted(false);
        acceptBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        acceptBtn.setPreferredSize(new Dimension(120, 36));

        acceptBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                acceptBtn.setBackground(new Color(0, 105, 217));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                acceptBtn.setBackground(new Color(0, 122, 255));
            }
        });

        acceptBtn.addActionListener(e -> handleAccept());

        // Deny button
        JButton denyBtn = new JButton("Deny");
        denyBtn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        denyBtn.setForeground(new Color(220, 53, 69));
        denyBtn.setBackground(Color.WHITE);
        denyBtn.setFocusPainted(false);
        denyBtn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 53, 69), 2),
                BorderFactory.createEmptyBorder(8, 0, 8, 0)));
        denyBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        denyBtn.setPreferredSize(new Dimension(120, 36));

        denyBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                denyBtn.setBackground(new Color(255, 245, 246));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                denyBtn.setBackground(Color.WHITE);
            }
        });

        denyBtn.addActionListener(e -> handleDeny());

        panel.add(acceptBtn);
        panel.add(denyBtn);
        return panel;
    }

    private void handleAccept() {
        // Show confirmation dialog
        int response = JOptionPane.showConfirmDialog(
                this,
                "Accept friend request from " + request.getName() + "?",
                "Accept Friend Request",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        JSONObject payload = new JSONObject();

        payload.put("senderId", request.getUserId());
        payload.put("receiverId", UserSession.getUser().getUserId());
        payload.put("sentAt", request.getSendTime());
        payload.put("status", "ACCEPTED");

        if (response == JOptionPane.YES_OPTION) {
            // Call backend API to accept
            SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
                @Override
                protected Boolean doInBackground() throws Exception {
                    try {
                        ApiClient.postJSON(ApiUrl.DECIDE_FRIEND_REQUEST, payload, UserSession.getUser().getToken());
                        return true;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return false;
                    }
                }

                @Override
                protected void done() {
                    try {
                        if (get()) {
                            JOptionPane.showMessageDialog(
                                    RequestItem.this,
                                    "You are now friends with " + request.getName(),
                                    "Friend Request Accepted",
                                    JOptionPane.INFORMATION_MESSAGE);

                            // Notify parent to remove this item
                            if (onRequestHandled != null) {
                                onRequestHandled.actionPerformed(null);
                            }
                        } else {
                            JOptionPane.showMessageDialog(
                                    RequestItem.this,
                                    "Failed to accept friend request",
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            worker.execute();
        }
    }

    private void handleDeny() {
        // Show confirmation dialog
        int response = JOptionPane.showConfirmDialog(
                this,
                "Decline friend request from " + request.getName() + "?",
                "Decline Friend Request",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        JSONObject payload = new JSONObject();

        payload.put("senderId", request.getUserId());
        payload.put("receiverId", UserSession.getUser().getUserId());
        payload.put("sentAt", request.getSendTime());
        payload.put("status", "REJECTED");

        if (response == JOptionPane.YES_OPTION) {
            // Call backend API to deny
            SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
                @Override
                protected Boolean doInBackground() throws Exception {
                    try {
                        ApiClient.postJSON(ApiUrl.DECIDE_FRIEND_REQUEST, payload, UserSession.getUser().getToken());
                        return true;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return false;
                    }
                }

                @Override
                protected void done() {
                    try {
                        if (get()) {
                            JOptionPane.showMessageDialog(
                                    RequestItem.this,
                                    "Friend request from " + request.getName() + " declined",
                                    "Friend Request Declined",
                                    JOptionPane.INFORMATION_MESSAGE);

                            // Notify parent to remove this item
                            if (onRequestHandled != null) {
                                onRequestHandled.actionPerformed(null);
                            }
                        } else {
                            JOptionPane.showMessageDialog(
                                    RequestItem.this,
                                    "Failed to decline friend request",
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            worker.execute();
        }
    }
}
