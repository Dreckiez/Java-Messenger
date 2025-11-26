package utils;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class StatusCellRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if (value != null) {
            String status = value.toString();
            if (status.equals("Working")) {
                setForeground(new Color(34, 197, 94));
                setText("● " + status);
            } else if (status.equals("Locked")) {
                setForeground(new Color(239, 68, 68));
                setText("● " + status);
            } else if (status.equals("Success")) {
                setForeground(new Color(34, 197, 94));
                setText(status);
            } else if (status.equals("Failed")) {
                setForeground(new Color(239, 68, 68));
                setText(status);
            } else if (status.equals("Pending")) {
                setForeground(new Color(251, 146, 60)); // Orange
                setText("⏳ " + status);
            } else if (status.equals("Proccessed")) {
                setForeground(new Color(34, 197, 94)); // Green
                setText("✓ " + status);
            } else if (status.equals("Locked")) {
                setForeground(new Color(239, 68, 68)); // Red
                setText("🔒 " + status);
            }
            setFont(new Font("Arial", Font.BOLD, 12));
        }

        if (isSelected) {
            setBackground(table.getSelectionBackground());
        } else {
            setBackground(table.getBackground());
        }

        return c;
    }
}
