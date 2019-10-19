package com.example.androidisshit.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.androidisshit.component.NowPlayingCoverView;
import com.example.androidisshit.component.VerticalTextView;
import com.example.androidisshit.utils.MusicUtils;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import com.example.androidisshit.R;
import com.example.androidisshit.utils.NowPlayingUtils;

import java.io.FileNotFoundException;

public class NowPlayingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now_playing);
        Intent intent=getIntent();
        String title = intent.getStringExtra("title");
        String artist = intent.getStringExtra("artist");
        Long id = intent.getLongExtra("id",0);

        NowPlayingCoverView nowPlayingCoverView = findViewById(R.id.nowPlay_cover);
        nowPlayingCoverView.setCoverUri(MusicUtils.getArtUri(id));

        VerticalTextView verticalTextView = findViewById(R.id.nowPlay_title);
        verticalTextView.setText(title);

    }
}
