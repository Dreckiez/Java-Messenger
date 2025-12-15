package utils;

import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import services.AvatarService;

import java.awt.Image;

public class ImageLoader {

    public interface ImageLoadCallback {
        void onLoaded(Image img);
    }

    public static void loadImageAsync(String urlString, ImageLoadCallback callback) {
        // 🔥 BƯỚC 1: KIỂM TRA URL TRƯỚC KHI KHỞI TẠO LUỒNG (FIX VẤN ĐỀ CỐT LÕI)
        if (urlString == null || urlString.trim().isEmpty() || "null".equalsIgnoreCase(urlString)) {
            
            // Vì đây là hàm đồng bộ, bạn phải đảm bảo callback chạy trên EDT
            SwingUtilities.invokeLater(() -> {
                // Gọi callback với kết quả null (hoặc icon placeholder mặc định)
                // Trong trường hợp này, callback sẽ trả về null, và InfoPanel sẽ hiển thị placeholder chữ cái
                callback.onLoaded(null); 
            });
            
            return; // Thoát khỏi hàm, không khởi tạo SwingWorker
        }
        
        // BƯỚC 2: KHỞI TẠO SWINGWORKER CHỈ KHI URL HỢP LỆ
        new SwingWorker<Image, Void>() {
            @Override
            protected Image doInBackground() throws Exception {
                // Giữ nguyên logic tải ảnh
                return AvatarService.loadAvatar(urlString);
            }

            @Override
            protected void done() {
                try {
                    // Xử lý thành công
                    callback.onLoaded(get());
                } catch (Exception e) {
                    // Xử lý lỗi (Nếu tải ảnh thất bại vì lỗi mạng, v.v.)
                    System.err.println("Error loading image from URL: " + urlString + " - " + e.getMessage());
                    // Gọi callback với null (InfoPanel sẽ hiển thị placeholder)
                    callback.onLoaded(null); 
                }
            }
        }.execute();
    }
}
