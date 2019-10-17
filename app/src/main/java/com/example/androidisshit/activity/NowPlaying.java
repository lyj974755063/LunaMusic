package com.example.androidisshit.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.androidisshit.component.VerticalTextView;
import com.example.androidisshit.utils.MusicUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import com.example.androidisshit.R;

public class NowPlaying extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now_playing);
        Intent intent=getIntent();
        String title = intent.getStringExtra("title");
        String artist = intent.getStringExtra("artist");
        Long uri = intent.getLongExtra("uri",0);
        VerticalTextView verticalTextView = findViewById(R.id.nowPlay_title);
        verticalTextView.setText(title);

        ImageView imageView = findViewById(R.id.nowPlay_albumImage);
        Glide.with(this).load(MusicUtils.getArtUri(uri))
                .fitCenter()//need fix
                .placeholder(R.mipmap.test_load)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView);

    }

}
