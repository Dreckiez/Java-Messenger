package utils;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;

public class CircleImage {
    public ImageIcon makeCircularImage(Image srcImg, int size) {
        BufferedImage circleBuffer = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = circleBuffer.createGraphics();

        // Set all the rendering hints for high quality
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);

        int origWidth = srcImg.getWidth(null);
        int origHeight = srcImg.getHeight(null);

        int newWidth;
        int newHeight;

        if (origWidth < origHeight) {
            newWidth = size;
            newHeight = (size * origHeight) / origWidth;
        } else {
            newHeight = size;
            newWidth = (size * origWidth) / origHeight;
        }

        int x = (size - newWidth) / 2;
        int y = (size - newHeight) / 2;

        g2.fillOval(0, 0, size, size);

        g2.setComposite(AlphaComposite.SrcIn);

        g2.drawImage(srcImg, x, y, newWidth, newHeight, null);

        g2.dispose();
        return new ImageIcon(circleBuffer);
    }
}