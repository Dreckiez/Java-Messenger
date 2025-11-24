package utils;

import javax.swing.SwingWorker;

import services.AvatarService;

import java.awt.Image;

public class ImageLoader {

    public interface ImageLoadCallback {
        void onLoaded(Image img);
    }

    public static void loadImageAsync(String urlString, ImageLoadCallback callback) {
        new SwingWorker<Image, Void>() {
            @Override
            protected Image doInBackground() throws Exception {
                return AvatarService.loadAvatar(urlString);
            }

            @Override
            protected void done() {
                try {
                    callback.onLoaded(get());
                } catch (Exception e) {
                    callback.onLoaded(AvatarService.loadAvatar(null)); // fallback
                }
            }
        }.execute();
    }
}
