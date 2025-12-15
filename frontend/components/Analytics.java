package components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Calendar;

import models.AnalyticsModel;
import services.UserAdminService;
import utils.UserSession;

public class Analytics extends JPanel {
    private UserAdminService userService;
    private JComboBox<String> globalYearCombo;
    
    // Components để update dữ liệu
    private CustomChartPanel registrationChart;
    private CustomChartPanel activeUsersChart;
    
    // Labels Stats Registration
    private JLabel lblRegTotal, lblRegAvg, lblRegHigh, lblRegGrowth;
    
    // Labels Stats Active Users
    private JLabel lblActAvg, lblActHigh, lblActPercent, lblActTrend;

    // Colors
    private final Color BG_COLOR = new Color(241, 245, 249);
    private final Color CHART_COLOR_BLUE = new Color(59, 130, 246);
    private final Color CHART_COLOR_GREEN = new Color(34, 197, 94);

    public Analytics() {
        this.userService = new UserAdminService();
        
        setLayout(new BorderLayout(20, 20));
        setBackground(BG_COLOR);
        setBorder(new EmptyBorder(20, 25, 20, 25));

        // === 1. TOP HEADER (Title + Global Year Selector) ===
        add(createHeaderPanel(), BorderLayout.NORTH);

        // === 2. CENTER: Charts Scrollable Area ===
        // Dùng ScrollPane phòng trường hợp màn hình nhỏ
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(BG_COLOR);

        contentPanel.add(createRegistrationSection());
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(createActiveUsersSection());

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getViewport().setBackground(BG_COLOR);
        
        add(scrollPane, BorderLayout.CENTER);

        // Load data mặc định (Năm hiện tại)
        loadData();
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_COLOR);

        JLabel titleLabel = new JLabel("Analytics Dashboard");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 26));
        titleLabel.setForeground(new Color(15, 23, 42));
        panel.add(titleLabel, BorderLayout.WEST);

        // Global Year Selector
        JPanel yearPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        yearPanel.setBackground(BG_COLOR);
        yearPanel.add(new JLabel("Select Year:"));
        
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        String[] years = new String[5];
        for (int i = 0; i < 5; i++) years[i] = String.valueOf(currentYear - i);
        
        globalYearCombo = new JComboBox<>(years);
        globalYearCombo.setPreferredSize(new Dimension(100, 30));
        globalYearCombo.addActionListener(e -> loadData()); // Gọi loadData khi đổi năm
        
        yearPanel.add(globalYearCombo);
        panel.add(yearPanel, BorderLayout.EAST);

        return panel;
    }

    // === SECTION 1: REGISTRATION ===
    private JPanel createRegistrationSection() {
        JPanel section = createSectionContainer();

        // Header
        JPanel header = createSectionHeader("New Registration");
        section.add(header, BorderLayout.NORTH);

        // Chart
        registrationChart = new CustomChartPanel(CHART_COLOR_BLUE, true); // true = Bar chart
        section.add(registrationChart, BorderLayout.CENTER);

        // Stats
        JPanel stats = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 10));
        stats.setBackground(new Color(248, 250, 252));
        stats.setBorder(new EmptyBorder(10, 0, 0, 0));

        lblRegTotal = createStatValueLabel("...");
        lblRegAvg = createStatValueLabel("...");
        lblRegHigh = createStatValueLabel("...");
        lblRegGrowth = createStatValueLabel("...");

        stats.add(createStatCard("Total Registration", lblRegTotal, CHART_COLOR_BLUE));
        stats.add(createStatCard("Avg/month", lblRegAvg, new Color(168, 85, 247)));
        stats.add(createStatCard("Highest Month", lblRegHigh, CHART_COLOR_GREEN));
        stats.add(createStatCard("Growth", lblRegGrowth, new Color(251, 146, 60)));

        section.add(stats, BorderLayout.SOUTH);
        return section;
    }

    // === SECTION 2: ACTIVE USERS ===
    private JPanel createActiveUsersSection() {
        JPanel section = createSectionContainer();

        // Header
        JPanel header = createSectionHeader("Online Users Activity");
        section.add(header, BorderLayout.NORTH);

        // Chart
        activeUsersChart = new CustomChartPanel(CHART_COLOR_GREEN, false); // false = Line chart
        section.add(activeUsersChart, BorderLayout.CENTER);

        // Stats
        JPanel stats = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 10));
        stats.setBackground(new Color(248, 250, 252));
        stats.setBorder(new EmptyBorder(10, 0, 0, 0));

        lblActAvg = createStatValueLabel("...");
        lblActHigh = createStatValueLabel("...");
        lblActPercent = createStatValueLabel("...");
        lblActTrend = createStatValueLabel("...");

        stats.add(createStatCard("Avg Activities", lblActAvg, CHART_COLOR_GREEN));
        stats.add(createStatCard("Highest Month", lblActHigh, CHART_COLOR_BLUE));
        stats.add(createStatCard("Activity %", lblActPercent, new Color(168, 85, 247)));
        stats.add(createStatCard("Trend", lblActTrend, new Color(251, 146, 60)));

        section.add(stats, BorderLayout.SOUTH);
        return section;
    }

    // === LOGIC LOAD DATA ===
    private void loadData() {
        if (UserSession.getUser() == null) return;
        
        String yearStr = (String) globalYearCombo.getSelectedItem();
        int year = Integer.parseInt(yearStr);
        String token = UserSession.getUser().getToken();

        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        new SwingWorker<AnalyticsModel, Void>() {
            @Override
            protected AnalyticsModel doInBackground() throws Exception {
                return userService.getAnalytics(token, year);
            }

            @Override
            protected void done() {
                try {
                    AnalyticsModel data = get();
                    if (data != null) {
                        updateUI(data);
                    } else {
                        // Reset về 0 nếu không có data hoặc lỗi
                        registrationChart.setData(new int[12]);
                        activeUsersChart.setData(new int[12]);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    setCursor(Cursor.getDefaultCursor());
                }
            }
        }.execute();
    }

    private void updateUI(AnalyticsModel data) {
        // 1. Update Registration
        if (data.getRegistration() != null) {
            registrationChart.setData(data.getRegistration().dataByMonth);
            
            AnalyticsModel.RegStats rStats = data.getRegistration().stats;
            lblRegTotal.setText(String.valueOf(rStats.totalRegistration));
            lblRegAvg.setText(String.valueOf(rStats.avgMonthly));
            lblRegHigh.setText(rStats.highestMonth);
            lblRegGrowth.setText(rStats.growthPercentage);
        }

        // 2. Update Active Users
        if (data.getActiveUsers() != null) {
            activeUsersChart.setData(data.getActiveUsers().dataByMonth);
            
            AnalyticsModel.ActiveStats aStats = data.getActiveUsers().stats;
            lblActAvg.setText(String.valueOf(aStats.avgActivitiesMonthly));
            lblActHigh.setText(aStats.highestMonth);
            lblActPercent.setText(aStats.activityPercentage);
            lblActTrend.setText(aStats.trend);
        }
    }

    // === UI HELPERS ===

    private JPanel createSectionContainer() {
        JPanel section = new JPanel(new BorderLayout(10, 10));
        section.setBackground(Color.WHITE);
        section.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
                new EmptyBorder(15, 15, 15, 15)));
        section.setMaximumSize(new Dimension(Integer.MAX_VALUE, 350)); // Chiều cao cố định cho đẹp
        return section;
    }

    private JPanel createSectionHeader(String title) {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        header.add(lblTitle, BorderLayout.WEST);
        return header;
    }

    private JLabel createStatValueLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.BOLD, 22)); // Font to cho số liệu
        return label;
    }

    private JPanel createStatCard(String title, JLabel valueLabel, Color color) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240)),
                new EmptyBorder(10, 15, 10, 15)));
        card.setPreferredSize(new Dimension(180, 80));

        valueLabel.setForeground(color);
        card.add(valueLabel, BorderLayout.CENTER);

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblTitle.setForeground(new Color(100, 116, 139));
        card.add(lblTitle, BorderLayout.SOUTH);

        return card;
    }

    // === CUSTOM CHART PANEL (Tái sử dụng logic vẽ) ===
    class CustomChartPanel extends JPanel {
        private int[] data = new int[12]; // Mặc định 0
        private Color color;
        private boolean isBarChart; // true = Bar, false = Line

        public CustomChartPanel(Color color, boolean isBarChart) {
            this.color = color;
            this.isBarChart = isBarChart;
            setBackground(Color.WHITE);
            setPreferredSize(new Dimension(0, 200));
        }

        public void setData(int[] newData) {
            this.data = newData != null ? newData : new int[12];
            repaint(); // 🔥 Vẽ lại khi có dữ liệu mới
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();
            int padding = 40;
            int chartWidth = width - 2 * padding;
            int chartHeight = height - 2 * padding;

            // Tìm Max Value
            int maxValue = 1; // Tránh chia cho 0
            for (int v : data) maxValue = Math.max(maxValue, v);
            // Làm tròn max value lên (ví dụ 17 -> 20) để biểu đồ đẹp hơn
            maxValue = (int) (Math.ceil(maxValue / 5.0) * 5); 
            if (maxValue == 0) maxValue = 5;

            // Vẽ trục
            g2d.setColor(new Color(203, 213, 225));
            g2d.drawLine(padding, height - padding, width - padding, height - padding); // X
            g2d.drawLine(padding, padding, padding, height - padding); // Y

            // Vẽ lưới ngang & Label trục Y
            g2d.setFont(new Font("SansSerif", Font.PLAIN, 10));
            for (int i = 0; i <= 5; i++) {
                int y = height - padding - (chartHeight / 5) * i;
                int val = (maxValue / 5) * i;
                g2d.setColor(new Color(226, 232, 240));
                g2d.drawLine(padding, y, width - padding, y); // Grid line
                
                g2d.setColor(new Color(100, 116, 139));
                g2d.drawString(String.valueOf(val), 10, y + 5); // Y Label
            }

            // Vẽ biểu đồ
            String[] months = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
            int step = chartWidth / 12;

            if (isBarChart) {
                // BAR CHART
                int barWidth = step / 2;
                for (int i = 0; i < 12; i++) {
                    int barHeight = (int) ((double) data[i] / maxValue * chartHeight);
                    int x = padding + i * step + step / 4;
                    int y = height - padding - barHeight;

                    g2d.setColor(color);
                    g2d.fillRoundRect(x, y, barWidth, barHeight, 5, 5);
                    
                    // Month Label
                    g2d.setColor(new Color(100, 116, 139));
                    g2d.drawString(months[i], x, height - padding + 15);
                    
                    // Value Label (Nếu > 0)
                    if (data[i] > 0) {
                        g2d.setColor(Color.BLACK);
                        g2d.drawString(String.valueOf(data[i]), x + barWidth/2 - 5, y - 5);
                    }
                }
            } else {
                // LINE CHART
                g2d.setColor(color);
                g2d.setStroke(new BasicStroke(2));
                
                // Points calculation
                int[][] points = new int[12][2];
                for(int i=0; i<12; i++) {
                    points[i][0] = padding + i * step + step / 2;
                    points[i][1] = height - padding - (int) ((double) data[i] / maxValue * chartHeight);
                }

                // Draw lines
                for (int i = 0; i < 11; i++) {
                    g2d.drawLine(points[i][0], points[i][1], points[i+1][0], points[i+1][1]);
                }
                
                // Draw dots & Labels
                for (int i = 0; i < 12; i++) {
                    g2d.setColor(Color.WHITE);
                    g2d.fillOval(points[i][0] - 4, points[i][1] - 4, 8, 8);
                    g2d.setColor(color);
                    g2d.drawOval(points[i][0] - 4, points[i][1] - 4, 8, 8);
                    
                    g2d.setColor(new Color(100, 116, 139));
                    g2d.drawString(months[i], points[i][0] - 10, height - padding + 15);
                    
                    if (data[i] > 0) {
                        g2d.setColor(Color.BLACK);
                        g2d.drawString(String.valueOf(data[i]), points[i][0] - 5, points[i][1] - 8);
                    }
                }
            }
        }
    }
}