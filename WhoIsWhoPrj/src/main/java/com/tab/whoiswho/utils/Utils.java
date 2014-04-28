package com.tab.whoiswho.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Utils {

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

    public static void copyStream(InputStream is, OutputStream os) throws IOException {
        final int bufferSize = 1024;

        byte[] bytes = new byte[bufferSize];
        int count = 0;

        while ((count = is.read(bytes, 0, bufferSize)) != -1) {
            os.write(bytes, 0, count);
        }
    }

    public static long getBitmapBytesSize(Bitmap bitmap) {
        if (bitmap == null) {
            return 0;
        }

        return bitmap.getRowBytes() * bitmap.getHeight();
    }

}
