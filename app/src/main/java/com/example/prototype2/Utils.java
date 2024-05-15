package com.example.prototype2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class Utils {
    public static Bitmap getBitmapFromUri(Context context, Uri uri) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            if (inputStream != null) {
                inputStream.close();
            }
            return bitmap;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }
    public static byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }
    public static Bitmap compressBitmap(Bitmap bitmap, int maxSizeKB) {
        // Calculate the compression ratio
        int originalSize = bitmap.getByteCount();
        int maxSizeBytes = maxSizeKB * 1024; // Convert KB to bytes
        float ratio = (float) maxSizeBytes / (float) originalSize;

        // If the bitmap size is already smaller than the target size, return the original bitmap
        if (ratio >= 1.0) {
            return bitmap;
        }

        // Compress the bitmap with the calculated ratio
        return Bitmap.createScaledBitmap(bitmap,
                (int) (bitmap.getWidth() * ratio),
                (int) (bitmap.getHeight() * ratio),
                true);
    }

}

