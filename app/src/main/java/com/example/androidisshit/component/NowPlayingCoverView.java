package com.example.androidisshit.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.example.androidisshit.R;
import com.example.androidisshit.utils.NowPlayingUtils;
import com.example.androidisshit.utils.Utils;

import java.net.URI;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;

/**
 * TODO: document your custom view class.
 */
public class NowPlayingCoverView extends View {

    private Context context;
    private Uri coverUri;
    private Bitmap[] bitmapCovers;
    private int bIsDrawSuccess;

    // Prepare high quality bitmapCover[0] for cover and low bitmapCover[1] for blur
    int coverSize = 630;
    int zipMultiple = 15;
    int lowCoverSize = coverSize/zipMultiple;
    int lowCoverTransfer = coverSize/20;
    int blurRadius = 4;

    public NowPlayingCoverView(Context context) {
        super(context);
        init(null, 0);
        this.context = context;
    }

    public NowPlayingCoverView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
        this.context = context;
    }

    public NowPlayingCoverView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
        this.context = context;
    }

    private void init(AttributeSet attrs, int defStyle) {
        coverUri = null;
        bitmapCovers = new Bitmap[2];
        bIsDrawSuccess = 2;
    }

    private void invalidateTextPaintAndMeasurements() {

    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (bitmapCovers[1]!=null && bitmapCovers[0]!=null){
            bIsDrawSuccess = 1;

            // Prepare view info
            float halfViewHeight = getMeasuredHeight()/2;
            float halfViewWidth = getMeasuredWidth()/2;

            float halfMapHeight = bitmapCovers[0].getHeight()/2;
            float halfMapWidth = bitmapCovers[0].getWidth()/2;

            // Prepare tools
            Paint paint = new Paint();
            Matrix matrix = new Matrix();
            ColorMatrix colorMatrix = new ColorMatrix();
            colorMatrix.setScale(0.8f,0.8f,0.8f,0.7f);

            // Create new canvas for blur cover bitmap
            Bitmap blurBitCover = Bitmap.createBitmap((int) halfViewWidth*2, (int) halfViewHeight*2, Bitmap.Config.ARGB_8888);
            Canvas blurCanvas = new Canvas(blurBitCover);

            // anti-aliasing
            paint.setAntiAlias(true);
            canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG));

            // Set matrix for center
            matrix.setTranslate(halfViewWidth-halfMapWidth, halfViewHeight-halfMapHeight+lowCoverTransfer);

            // debug
            //canvas.drawBitmap(bitmapCovers[0],new Matrix(),paint);
            //canvas.drawBitmap(bitmapCovers[1],matrix,paint);

            // Do blur
            //paint.setAlpha(178);
            paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
            blurCanvas.drawBitmap(bitmapCovers[1],matrix,paint);
            blurBitCover = NowPlayingUtils.renderScriptBlur(context, blurBitCover, blurRadius);


            // Draw blurred cover to original canvas
            //matrix.reset();
            matrix.setScale(zipMultiple, zipMultiple, halfViewWidth-halfMapWidth,halfViewHeight-halfMapHeight+lowCoverTransfer);
            paint.reset();
            canvas.drawBitmap(blurBitCover, matrix, paint);

            // Draw top cover
            matrix.setTranslate(halfViewWidth-halfMapWidth, halfViewHeight-halfMapHeight);
            canvas.drawBitmap(bitmapCovers[0], matrix, paint);
        } else {
            bIsDrawSuccess = 0;
        }
        super.onDraw(canvas);
    }


    public Uri getCoverUri() {
        return coverUri;
    }

    public void setCoverUri(Uri coverUri) {
        this.coverUri = coverUri;
        Glide.with(context)
                .load(coverUri)
                .override(coverSize)
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        bitmapCovers[0] = Utils.D2B(resource);
                        if (bIsDrawSuccess == 0) {
                            invalidate();
                        }
                    }
                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) { }
                });

        Glide.with(context)
                .load(coverUri)
                .override(lowCoverSize)
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        bitmapCovers[1] = Utils.D2B(resource);
                        if (bIsDrawSuccess == 0) {
                            invalidate();
                        }
                    }
                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) { }
                });
    }

}

