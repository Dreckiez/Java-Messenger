package utils;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
public class ImageEditor {

    public BufferedImage scaleImage(Image srcImg, int targetSize) {
        int origWidth = srcImg.getWidth(null);
        int origHeight = srcImg.getHeight(null);

        // Calculate new dimensions maintaining aspect ratio
        int newWidth;
        int newHeight;

        if (origWidth < origHeight) {
            newWidth = targetSize;
            newHeight = (targetSize * origHeight) / origWidth;
        } else {
            newHeight = targetSize;
            newWidth = (targetSize * origWidth) / origHeight;
        }

        // Create scaled image
        BufferedImage scaledImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = scaledImage.createGraphics();

        // Set rendering hints for high quality
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);

        g2.drawImage(srcImg, 0, 0, newWidth, newHeight, null);
        g2.dispose();

        return scaledImage;
    }

    public ImageIcon makeCircularImage(Image srcImg, int size) {
        BufferedImage scaledImg = scaleImage(srcImg, size);

        BufferedImage circleBuffer = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = circleBuffer.createGraphics();

        // Set all the rendering hints for high quality
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);

        int x = (size - scaledImg.getWidth()) / 2;
        int y = (size - scaledImg.getHeight()) / 2;

        g2.fillOval(0, 0, size, size);

        g2.setComposite(AlphaComposite.SrcIn);

        g2.drawImage(scaledImg, x, y, null);

        g2.dispose();
        return new ImageIcon(circleBuffer);
    }
    // 🔥 PHƯƠNG THỨC MỚI CẦN THÊM (1)
    public BufferedImage createInitialAvatar(String name, Color bgColor, int size) {
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Vẽ nền tròn
        g2d.setColor(bgColor);
        g2d.fillOval(0, 0, size, size);

        // Chuẩn bị vẽ chữ
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Segoe UI", Font.BOLD, size / 2));
        
        // Lấy chữ cái đầu tiên
        String initial = (name != null && !name.isEmpty()) ? name.substring(0, 1).toUpperCase() : "?";
        
        // Căn giữa chữ
        FontMetrics fm = g2d.getFontMetrics();
        int x = (size - fm.stringWidth(initial)) / 2;
        int y = ((size - fm.getHeight()) / 2) + fm.getAscent();
        g2d.drawString(initial, x, y);
        g2d.dispose();

        return img;
    }
}