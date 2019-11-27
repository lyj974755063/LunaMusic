package com.example.androidisshit.activity;

import android.content.*;
import android.os.AsyncTask;
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
    private Button startButton;
    private MusicService.MyBinder binder;
    private ArrayList<Song> playList;
    private ArrayList<String> paths;
    private ViewPager viewPager;

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

        // register broadcast receiver
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("UPDATE_UI");
        intentFilter.addAction("INITIAL_UI");
        registerReceiver(updateUIReceiver, intentFilter);

        // bind service
        Intent serviceIntent = new Intent(this, MusicService.class);
        System.out.println("MYFLAG_bindService!");
        bindService( serviceIntent, serviceConnection, BIND_AUTO_CREATE);

        // set viewPage and fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        viewPager = findViewById(R.id.nowPlay_viewPager);
        NowPlayingAdapter adapter = new NowPlayingAdapter(fragmentManager,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, playList);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(adapter);
        viewPager.setPageTransformer(true,
                new ParallaxPagerTransformer()
                        .addViewToParallax(new ParallaxPagerTransformer.ParallaxTransformInformation(R.id.nowPlay_title,2f,2f))
                        .addViewToParallax(new ParallaxPagerTransformer.ParallaxTransformInformation(R.id.nowPlay_artist,3f,3f)));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int index;
                index = binder.getPreparedIndex();
                if (index < position) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            binder.nextMusic();
                        }
                    }).start();
                } else if (index > position) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            binder.preciousMusic();
                        }
                    }).start();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        // set start button
        startButton = findViewById(R.id.nowPlay_button_start);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binder.handleMusic();
            }
        });
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = ((MusicService.MyBinder) service);
            binder.iniPlayList(paths);
        }
/**/
        @Override
        public void onServiceDisconnected(ComponentName name) {
        }

    };

    private BroadcastReceiver updateUIReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("UPDATE_UI")) {
                int STATE = intent.getIntExtra("UI_STATE", -2);
                System.out.println("MYFLAG_STATE_" + STATE);
                switch (STATE) {
                    case 1:
                        startButton.setText(getString(R.string.nowPlaying_stop));
                        break;
                    case 0:
                        startButton.setText(getString(R.string.nowPlaying_start));
                        break;
                    case 2:

                        break;
                    case -1:
                        break;
                }
            } else { // for now, think it as action INITIAL_UI
                int STATE = intent.getIntExtra("UI_STATE", -1);
                viewPager.setCurrentItem(STATE);
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d( "MYFLAG", "nowPlayingActivity onDestroy()");
        //binder.closeMedia();
        unregisterReceiver(updateUIReceiver);
        unbindService(serviceConnection);
    }
}
