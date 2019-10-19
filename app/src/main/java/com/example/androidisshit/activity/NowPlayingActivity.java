package com.example.androidisshit.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import com.example.androidisshit.component.NowPlayingCoverView;
import com.example.androidisshit.component.VerticalTextView;
import com.example.androidisshit.utils.MusicUtils;
import androidx.appcompat.app.AppCompatActivity;
import com.example.androidisshit.R;

public class NowPlayingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now_playing);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        Intent intent=getIntent();
        String title = intent.getStringExtra("title");
        String artist = intent.getStringExtra("artist");
        Long id = intent.getLongExtra("id",0);

        NowPlayingCoverView nowPlayingCoverView = findViewById(R.id.nowPlay_cover);
        nowPlayingCoverView.setCoverUri(MusicUtils.getArtUri(id));

        VerticalTextView verticalTextView = findViewById(R.id.nowPlay_title);
        verticalTextView.setText(title.toUpperCase());

    }
}
