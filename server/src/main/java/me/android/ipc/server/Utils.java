package me.android.ipc.server;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

public class Utils {

    public static Bitmap loadBitmap(Context context) {
        Drawable icon = context.getApplicationInfo().loadIcon(context.getPackageManager());
        if (icon == null) return null;
        int width = icon.getIntrinsicWidth();
        int height = icon.getIntrinsicHeight();
        if (width == 0 || height == 0) return null;
        icon.setBounds(0, 0, width, height);
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        icon.draw(canvas);
        return bitmap;
    }
}
