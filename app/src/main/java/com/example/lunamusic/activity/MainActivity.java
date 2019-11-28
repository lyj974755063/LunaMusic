package com.example.lunamusic.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.*;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.lunamusic.component.ParallaxRecyclerView;
import com.example.lunamusic.R;
import com.example.lunamusic.adapter.MyRecyclerAdapter;
import com.example.lunamusic.entity.Song;
import com.example.lunamusic.service.MusicService;
import com.example.lunamusic.utils.NetUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ParallaxRecyclerView parallaxRecyclerView;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        startService(new Intent(this, MusicService.class));

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 1);
        } else {
            try {
                initializeActivity();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void initializeActivity() throws IOException, InterruptedException {
        // All those thing are doing under the prediction that user has given right permission

        // Check is net mod
        if (PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("net", false)) {
            NetUtils.bIsNetMod = true;
            System.out.println("MYLOG_"+"netmod_true");
        }

        //Parallax list
        parallaxRecyclerView = (ParallaxRecyclerView) findViewById(R.id.RecyclerView);
        parallaxRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        parallaxRecyclerView.setHasFixedSize(true);
        parallaxRecyclerView.setAdapter(new MyRecyclerAdapter(this));

        parallaxRecyclerView.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {

                    @Override
                    public boolean onPreDraw() {
                        parallaxRecyclerView.getViewTreeObserver().removeOnPreDrawListener(this);

                        for (int i = 0; i < parallaxRecyclerView.getChildCount(); i++) {
                            View v = parallaxRecyclerView.getChildAt(i);
                            v.setAlpha(0.0f);
                            v.animate().alpha(1.0f)
                                    .setInterpolator(new AccelerateDecelerateInterpolator())
                                    .setDuration(400)
                                    .setStartDelay(i * 50)
                                    .start();
                        }

                        return true;
                    }
                });

        ((MyRecyclerAdapter) parallaxRecyclerView.getAdapter()).setOnItemClickListener(new MyRecyclerAdapter.RecycleViewOnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, ArrayList data) {
                Intent intent = new Intent();
                ArrayList<Song> songs = data;
                intent.putExtra("playList", songs);
                intent.setClass(MainActivity.this, NowPlayingActivity.class);
                startActivity(intent);
            }
        });

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this,SettingActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[]permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    try {
                        initializeActivity();
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                    return;
                } else {
                    Toast.makeText(this, "Can't get permission", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
                break;
        }
    }
}
