package com.example.androidisshit.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.androidisshit.component.ParallaxRecyclerView;
import com.example.androidisshit.R;
import com.example.androidisshit.adapter.MyRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Parallax list
        final ParallaxRecyclerView parallaxRecyclerView = (ParallaxRecyclerView) findViewById(R.id.RecyclerView);
        parallaxRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        parallaxRecyclerView.setHasFixedSize(true);
        parallaxRecyclerView.setAdapter(new MyRecyclerAdapter(this));
        ((MyRecyclerAdapter) parallaxRecyclerView.getAdapter()).setOnItemClickListener(new MyRecyclerAdapter.RecycleViewOnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, Map data) {
                Intent intent = new Intent();
                intent.putExtra("title", (String) data.get("title"));
                intent.putExtra("artist", (String) data.get("artist"));
                intent.putExtra("id", (Long) data.get("id"));
                intent.setClass(MainActivity.this, NowPlayingActivity.class);
                startActivity(intent);
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
