package components;

import javax.swing.*;
import java.awt.*;

public class Analytics extends JPanel {
    private JComboBox<String> registrationYearCombo, activeUsersYearCombo;
    private JPanel registrationChartPanel, activeUsersChartPanel;

    public Analytics() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // === TOP: Title ===
        JLabel titleLabel = new JLabel("Analytics Dashboard");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        // === CENTER: Charts Grid ===
        JPanel chartsPanel = new JPanel(new GridLayout(2, 1, 0, 20));
        chartsPanel.setBackground(Color.WHITE);

        // Registration Chart Section
        JPanel registrationSection = createRegistrationSection();
        chartsPanel.add(registrationSection);

        // Active Users Chart Section
        JPanel activeUsersSection = createActiveUsersSection();
        chartsPanel.add(activeUsersSection);

        add(chartsPanel, BorderLayout.CENTER);
    }

    private JPanel createRegistrationSection() {
        JPanel section = new JPanel(new BorderLayout(10, 10));
        section.setBackground(Color.WHITE);
        section.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240), 2),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)));

        // Header with title and year selector
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);

        JLabel sectionTitle = new JLabel("New Registration");
        sectionTitle.setFont(new Font("Arial", Font.BOLD, 18));
        headerPanel.add(sectionTitle, BorderLayout.WEST);

        JPanel yearPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        yearPanel.setBackground(Color.WHITE);
        yearPanel.add(new JLabel("Năm:"));
        registrationYearCombo = new JComboBox<>(new String[] { "2024", "2023", "2022", "2021", "2020" });
        registrationYearCombo.addActionListener(e -> updateRegistrationChart());
        yearPanel.add(registrationYearCombo);
        headerPanel.add(yearPanel, BorderLayout.EAST);

        section.add(headerPanel, BorderLayout.NORTH);

        // Chart Panel
        registrationChartPanel = createRegistrationChart("2024");
        section.add(registrationChartPanel, BorderLayout.CENTER);

        // Statistics Panel
        JPanel statsPanel = createRegistrationStats();
        section.add(statsPanel, BorderLayout.SOUTH);

        return section;
    }

    private JPanel createActiveUsersSection() {
        JPanel section = new JPanel(new BorderLayout(10, 10));
        section.setBackground(Color.WHITE);
        section.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240), 2),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)));

        // Header with title and year selector
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);

        JLabel sectionTitle = new JLabel("Online Users");
        sectionTitle.setFont(new Font("Arial", Font.BOLD, 18));
        headerPanel.add(sectionTitle, BorderLayout.WEST);

        JPanel yearPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        yearPanel.setBackground(Color.WHITE);
        yearPanel.add(new JLabel("Năm:"));
        activeUsersYearCombo = new JComboBox<>(new String[] { "2024", "2023", "2022", "2021", "2020" });
        activeUsersYearCombo.addActionListener(e -> updateActiveUsersChart());
        yearPanel.add(activeUsersYearCombo);
        headerPanel.add(yearPanel, BorderLayout.EAST);

        section.add(headerPanel, BorderLayout.NORTH);

        // Chart Panel
        activeUsersChartPanel = createActiveUsersChart("2024");
        section.add(activeUsersChartPanel, BorderLayout.CENTER);

        // Statistics Panel
        JPanel statsPanel = createActiveUsersStats();
        section.add(statsPanel, BorderLayout.SOUTH);

        return section;
    }

    private JPanel createRegistrationChart(String year) {
        JPanel chartPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Sample data for the selected year
                int[] data = getRegistrationDataForYear(year);
                String[] months = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov",
                        "Dec" };

                int width = getWidth();
                int height = getHeight();
                int padding = 50;
                int chartWidth = width - 2 * padding;
                int chartHeight = height - 2 * padding;

                // Find max value for scaling
                int maxValue = 0;
                for (int value : data) {
                    if (value > maxValue)
                        maxValue = value;
                }

                // Draw axes
                g2d.setColor(new Color(100, 116, 139));
                g2d.setStroke(new BasicStroke(2));
                g2d.drawLine(padding, height - padding, width - padding, height - padding); // X-axis
                g2d.drawLine(padding, padding, padding, height - padding); // Y-axis

                // Draw bars
                int barWidth = chartWidth / (data.length * 2);
                int spacing = barWidth / 2;

                for (int i = 0; i < data.length; i++) {
                    int barHeight = (int) ((double) data[i] / maxValue * chartHeight);
                    int x = padding + i * (barWidth + spacing) + spacing;
                    int y = height - padding - barHeight;

                    // Gradient color for bars
                    GradientPaint gradient = new GradientPaint(
                            x, y, new Color(59, 130, 246),
                            x, y + barHeight, new Color(147, 197, 253));
                    g2d.setPaint(gradient);
                    g2d.fillRoundRect(x, y, barWidth, barHeight, 8, 8);

                    // Draw value on top of bar
                    g2d.setColor(new Color(51, 65, 85));
                    g2d.setFont(new Font("Arial", Font.BOLD, 11));
                    String valueStr = String.valueOf(data[i]);
                    FontMetrics fm = g2d.getFontMetrics();
                    int textWidth = fm.stringWidth(valueStr);
                    g2d.drawString(valueStr, x + (barWidth - textWidth) / 2, y - 5);

                    // Draw month label
                    g2d.setFont(new Font("Arial", Font.PLAIN, 10));
                    int labelWidth = fm.stringWidth(months[i]);
                    g2d.drawString(months[i], x + (barWidth - labelWidth) / 2, height - padding + 20);
                }

                // Draw Y-axis labels
                g2d.setFont(new Font("Arial", Font.PLAIN, 10));
                g2d.setColor(new Color(100, 116, 139));
                for (int i = 0; i <= 5; i++) {
                    int value = (maxValue / 5) * i;
                    int y = height - padding - (chartHeight / 5) * i;
                    g2d.drawString(String.valueOf(value), 10, y + 5);
                    g2d.setColor(new Color(226, 232, 240));
                    g2d.drawLine(padding, y, width - padding, y);
                    g2d.setColor(new Color(100, 116, 139));
                }
            }
        };

        chartPanel.setBackground(Color.WHITE);
        chartPanel.setPreferredSize(new Dimension(0, 250));
        return chartPanel;
    }

    private JPanel createActiveUsersChart(String year) {
        JPanel chartPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Sample data for the selected year
                int[] data = getActiveUsersDataForYear(year);
                String[] months = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov",
                        "Dec" };

                int width = getWidth();
                int height = getHeight();
                int padding = 50;
                int chartWidth = width - 2 * padding;
                int chartHeight = height - 2 * padding;

                // Find max value for scaling
                int maxValue = 0;
                for (int value : data) {
                    if (value > maxValue)
                        maxValue = value;
                }

                // Draw axes
                g2d.setColor(new Color(100, 116, 139));
                g2d.setStroke(new BasicStroke(2));
                g2d.drawLine(padding, height - padding, width - padding, height - padding); // X-axis
                g2d.drawLine(padding, padding, padding, height - padding); // Y-axis

                // Draw grid lines
                g2d.setColor(new Color(241, 245, 249));
                for (int i = 0; i <= 5; i++) {
                    int y = height - padding - (chartHeight / 5) * i;
                    g2d.drawLine(padding, y, width - padding, y);
                }

                // Draw line chart
                g2d.setColor(new Color(34, 197, 94));
                g2d.setStroke(new BasicStroke(3));

                int pointSpacing = chartWidth / (data.length - 1);

                for (int i = 0; i < data.length - 1; i++) {
                    int x1 = padding + i * pointSpacing;
                    int y1 = height - padding - (int) ((double) data[i] / maxValue * chartHeight);
                    int x2 = padding + (i + 1) * pointSpacing;
                    int y2 = height - padding - (int) ((double) data[i + 1] / maxValue * chartHeight);

                    g2d.drawLine(x1, y1, x2, y2);
                }

                // Draw points and values
                for (int i = 0; i < data.length; i++) {
                    int x = padding + i * pointSpacing;
                    int y = height - padding - (int) ((double) data[i] / maxValue * chartHeight);

                    // Point
                    g2d.setColor(Color.WHITE);
                    g2d.fillOval(x - 5, y - 5, 10, 10);
                    g2d.setColor(new Color(34, 197, 94));
                    g2d.fillOval(x - 4, y - 4, 8, 8);

                    // Value
                    g2d.setColor(new Color(51, 65, 85));
                    g2d.setFont(new Font("Arial", Font.BOLD, 11));
                    String valueStr = String.valueOf(data[i]);
                    FontMetrics fm = g2d.getFontMetrics();
                    int textWidth = fm.stringWidth(valueStr);
                    g2d.drawString(valueStr, x - textWidth / 2, y - 10);

                    // Month label
                    g2d.setFont(new Font("Arial", Font.PLAIN, 10));
                    g2d.setColor(new Color(100, 116, 139));
                    int labelWidth = fm.stringWidth(months[i]);
                    g2d.drawString(months[i], x - labelWidth / 2, height - padding + 20);
                }

                // Draw Y-axis labels
                g2d.setFont(new Font("Arial", Font.PLAIN, 10));
                g2d.setColor(new Color(100, 116, 139));
                for (int i = 0; i <= 5; i++) {
                    int value = (maxValue / 5) * i;
                    int y = height - padding - (chartHeight / 5) * i;
                    g2d.drawString(String.valueOf(value), 10, y + 5);
                }
            }
        };

        chartPanel.setBackground(Color.WHITE);
        chartPanel.setPreferredSize(new Dimension(0, 250));
        return chartPanel;
    }

    private JPanel createRegistrationStats() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 10));
        panel.setBackground(new Color(248, 250, 252));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        panel.add(createStatCard("Total Registration", "1,247", new Color(59, 130, 246)));
        panel.add(createStatCard("Avg/month", "104", new Color(168, 85, 247)));
        panel.add(createStatCard("Highest Month", "145 (Aug)", new Color(34, 197, 94)));
        panel.add(createStatCard("Growth", "+23.5%", new Color(251, 146, 60)));

        return panel;
    }

    private JPanel createActiveUsersStats() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 10));
        panel.setBackground(new Color(248, 250, 252));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        panel.add(createStatCard("Avg activities/month", "856", new Color(34, 197, 94)));
        panel.add(createStatCard("Highest Month", "923 (Nov)", new Color(59, 130, 246)));
        panel.add(createStatCard("Activity Percentage", "68.7%", new Color(168, 85, 247)));
        panel.add(createStatCard("Trend", "Rising", new Color(34, 197, 94)));

        return panel;
    }

    private JPanel createStatCard(String label, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240)),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)));
        card.setPreferredSize(new Dimension(200, 80));

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Arial", Font.BOLD, 24));
        lblValue.setForeground(color);
        card.add(lblValue, BorderLayout.CENTER);

        JLabel lblLabel = new JLabel(label);
        lblLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        lblLabel.setForeground(new Color(100, 116, 139));
        card.add(lblLabel, BorderLayout.SOUTH);

        return card;
    }

    private void updateRegistrationChart() {
        String selectedYear = (String) registrationYearCombo.getSelectedItem();
        JPanel parent = (JPanel) registrationChartPanel.getParent();
        parent.remove(registrationChartPanel);
        registrationChartPanel = createRegistrationChart(selectedYear);
        parent.add(registrationChartPanel, BorderLayout.CENTER);
        parent.revalidate();
        parent.repaint();
    }

    private void updateActiveUsersChart() {
        String selectedYear = (String) activeUsersYearCombo.getSelectedItem();
        JPanel parent = (JPanel) activeUsersChartPanel.getParent();
        parent.remove(activeUsersChartPanel);
        activeUsersChartPanel = createActiveUsersChart(selectedYear);
        parent.add(activeUsersChartPanel, BorderLayout.CENTER);
        parent.revalidate();
        parent.repaint();
    }

    private int[] getRegistrationDataForYear(String year) {
        // Sample data for demonstration
        switch (year) {
            case "2024":
                return new int[] { 89, 95, 102, 118, 125, 132, 128, 145, 138, 142, 135, 98 };
            case "2023":
                return new int[] { 67, 72, 78, 85, 92, 98, 105, 112, 108, 115, 120, 110 };
            case "2022":
                return new int[] { 45, 52, 58, 63, 68, 75, 82, 88, 92, 95, 98, 87 };
            default:
                return new int[] { 30, 35, 40, 45, 50, 55, 60, 65, 70, 75, 80, 85 };
        }
    }

    private int[] getActiveUsersDataForYear(String year) {
        // Sample data for demonstration
        switch (year) {
            case "2024":
                return new int[] { 723, 745, 768, 802, 835, 856, 872, 889, 895, 910, 923, 856 };
            case "2023":
                return new int[] { 598, 612, 635, 658, 678, 695, 715, 732, 748, 765, 780, 720 };
            case "2022":
                return new int[] { 445, 468, 489, 512, 535, 558, 578, 595, 612, 628, 645, 610 };
            default:
                return new int[] { 320, 345, 368, 390, 412, 435, 458, 480, 502, 525, 548, 520 };
        }
    }
}
