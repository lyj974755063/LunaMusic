package com.example.androidisshit.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;

import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import com.example.androidisshit.adapter.NowPlayingAdapter;
import com.example.androidisshit.component.ParallaxPagerTransformer;
import com.example.androidisshit.entity.Song;
import com.example.androidisshit.service.MusicService;
import androidx.appcompat.app.AppCompatActivity;
import com.example.androidisshit.R;

import java.util.ArrayList;

import static androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

public class NowPlayingActivity extends AppCompatActivity {
    Button startButton;
    MusicService.MyBinder binder;
    ArrayList<Song> playList;
    ArrayList<String> paths;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now_playing);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        Intent intent=getIntent();
        playList = (ArrayList<Song>) intent.getSerializableExtra("playList");
        paths = new ArrayList<>();
        for (Song t : playList) {
            paths.add(t.getPath());
            System.out.println("MYFLAG_PATH_"+t.getPath());
        }

        // bind service
        Intent serviceIntent = new Intent(this, MusicService.class);
        System.out.println("MYFLAG_bindService!");
        bindService( serviceIntent, serviceConnection, BIND_AUTO_CREATE);

        // set viewPage and fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        ViewPager viewPager = findViewById(R.id.nowPlay_viewPager);
        NowPlayingAdapter adapter = new NowPlayingAdapter(fragmentManager,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, playList);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(adapter);
        viewPager.setPageTransformer(true,
                new ParallaxPagerTransformer()
                        .addViewToParallax(new ParallaxPagerTransformer.ParallaxTransformInformation(R.id.nowPlay_title,2f,2f))
                        .addViewToParallax(new ParallaxPagerTransformer.ParallaxTransformInformation(R.id.nowPlay_artist,3f,3f)));

        // set start button
        startButton = findViewById(R.id.nowPlay_button_start);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binder.playMusic();
            }
        });
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = ((MusicService.MyBinder) service);
            binder.iniPlayList(paths);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }

    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d( "MYFLAG", "nowPlayingActivity onDestroy()");
        //binder.closeMedia();
        unbindService(serviceConnection);
    }
}
