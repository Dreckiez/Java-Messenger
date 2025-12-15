package utils;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.*;
import java.awt.*;

public class StatusCellRenderer extends DefaultTableCellRenderer {
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        
        // Gọi super để lấy các thuộc tính mặc định (background, foreground chuẩn)
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        // 🔥 THÊM DÒNG NÀY ĐỂ CĂN GIỮA
        setHorizontalAlignment(JLabel.CENTER);

        if (value != null) {
            String status = value.toString();
            
            // Logic màu sắc
            if (status.equals("Working") || status.equals("Proccessed") || status.equals("Active")) {
                setForeground(new Color(34, 197, 94)); // Xanh lá
                setText("● " + status);
            } else if (status.equals("Locked")) {
                setForeground(new Color(239, 68, 68)); // Đỏ
                setText("● " + status);
            } else if (status.equals("Success")) {
                setForeground(new Color(34, 197, 94)); // Xanh lá
                setText(status);
            } else if (status.equals("Failed") || status.equals("Expired")) {
                setForeground(new Color(239, 68, 68)); // Đỏ
                setText(status);
            } else if (status.equals("Pending")) {
                setForeground(new Color(251, 146, 60)); // Cam
                setText("● " + status);
            }

            if (status.equals("PROCESSED")) {
                setForeground(new Color(34, 197, 94));
                setText(status.substring(0, 1).toUpperCase() + status.substring(1).toLowerCase()); // Format lại chữ hoa chữ thường
            } 
            // Cam/Vàng (Pending)
            else if (status.equals("PENDING")) {
                setForeground(new Color(245, 158, 11)); // Màu Cam/Vàng (Orange-500)
                setText(status.substring(0, 1).toUpperCase() + status.substring(1).toLowerCase());
            } 
            // Đỏ (Locked / Failed)
            else if (status.equals("LOCKED") || status.equals("FAILED") || status.equals("EXPIRED")) {
                setForeground(new Color(239, 68, 68)); // Màu Đỏ
                setText(status.substring(0, 1).toUpperCase() + status.substring(1).toLowerCase());
            } 
        
            setFont(new Font("SansSerif", Font.BOLD, 12)); // Dùng SansSerif cho đồng bộ với App
        }

        // Xử lý màu nền khi chọn dòng
        if (isSelected) {
            setBackground(new Color(239, 246, 255)); // Màu xanh nhạt khi select (đồng bộ với các bảng khác)
            // Nếu muốn chữ vẫn giữ màu khi select thì không setForeground lại màu đen/trắng ở đây
        } else {
            setBackground(Color.WHITE);
        }

        return c;
    }
}