package com.example.androidisshit.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import com.example.androidisshit.component.NowPlayingCoverView;
import com.example.androidisshit.component.VerticalTextView;
import com.example.androidisshit.entity.Album;
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
        Album album = (Album) intent.getSerializableExtra("album");

        NowPlayingCoverView nowPlayingCoverView = findViewById(R.id.nowPlay_cover);
        nowPlayingCoverView.setCoverUri(MusicUtils.getArtUri(id));

        VerticalTextView verticalTextView = findViewById(R.id.nowPlay_title);
        verticalTextView.setText(title.toUpperCase());
        verticalTextView.setTextColor(album.getSecondColor());

        TextView artistTextView = findViewById(R.id.nowPlay_artist);
        artistTextView.setTextColor(album.getSecondColor());
        artistTextView.setText(album.getAlbumArtist()+"の世界");

        Button button = findViewById(R.id.nowPlay_button_start);
        button.setTextColor(album.getPrimaryColor());

    }
}
