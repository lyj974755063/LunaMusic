package com.example.lunamusic.utils;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class Utils {
    // Drawable to Bitmap
    public static Bitmap D2B(Drawable D) {
        return ((BitmapDrawable)D).getBitmap();
    }
}
