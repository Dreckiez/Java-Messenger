package utils;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;

public class CircleImage {
    public ImageIcon makeCircularImage(Image srcImg, int size) {
        BufferedImage circleBuffer = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = circleBuffer.createGraphics();

        // Enable smooth edges and high-quality scaling
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 👇 THIS IS THE KEY CHANGE
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        // Draw circular clip
        g2.setClip(new Ellipse2D.Float(0, 0, size, size));

        // Draw the original image, letting g2 handle the high-quality scaling
        g2.drawImage(srcImg, 0, 0, size, size, null);

        g2.dispose();
        return new ImageIcon(circleBuffer);
    }
}