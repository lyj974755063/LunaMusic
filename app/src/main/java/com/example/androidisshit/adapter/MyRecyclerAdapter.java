package com.example.androidisshit.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.androidisshit.R;
import com.example.androidisshit.entity.Album;
import com.example.androidisshit.entity.Song;
import com.example.androidisshit.utils.MusicUtils;
import com.example.androidisshit.utils.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MyRecyclerAdapter extends RecyclerView.Adapter {

    private Context context;
    private LayoutInflater inflater;
    private List<Album> albums;

    private RecycleViewOnItemClickListener itemClickListener;
    public interface RecycleViewOnItemClickListener {
        void onItemClick(View view, int position, Map data);
    }
    public void setOnItemClickListener(RecycleViewOnItemClickListener onItemClickListener){
        this.itemClickListener = onItemClickListener;
    }

    public MyRecyclerAdapter(Context context){
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        Song.allSongs = MusicUtils.getAllMusic(context);
        albums = Album.AllAlbums = Album.getAllAlbums(Song.allSongs);
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final ViewHolder viewHolder = new ViewHolder(inflater.inflate(R.layout.mainlist_cover, parent, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int position) {
        final Album album;
        if (!albums.isEmpty()) {
            album = albums.get(position);
            final int alpha = 200;

            // Set cover foreground color and text color
            if (album.getPrimaryColor()==0) {
                Glide.with(context)
                        .load(MusicUtils.getArtUri(album.getAlbumId()))
                        .override(50)
                        .into(new CustomTarget<Drawable>() {
                            @Override
                            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                System.out.println("CalculateColor+1");
                                album.calculateColors(resource, new Album.CalculateListener() {
                                    @Override
                                    public void doSomething() {
                                        ColorDrawable colorDrawable = new ColorDrawable(album.getSecondColor());
                                        colorDrawable.setAlpha(alpha);
                                        ((ViewHolder) viewHolder).getBackgroundImage().setForeground(colorDrawable);
                                        ((ViewHolder) viewHolder).getTextView().setTextColor(album.getPrimaryColor());
                                    }
                                });
                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {

                            }
                        });
            } else {
                ColorDrawable colorDrawable = new ColorDrawable(album.getSecondColor());
                colorDrawable.setAlpha(alpha);
                ((ViewHolder) viewHolder).getBackgroundImage().setForeground(colorDrawable);
                ((ViewHolder) viewHolder).getTextView().setTextColor(album.getPrimaryColor());
            }

            //set album title
            ((ViewHolder)viewHolder).getTextView().setText(album.getAlbumTitle());
            //set album art
            Glide.with(context)
                    .load(MusicUtils.getArtUri(album.getAlbumId()))
                    .fitCenter()
                    //.placeholder(R.mipmap.test_load) //case some size issue
                    .override(512)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(((ViewHolder)viewHolder).getBackgroundImage());

            if (itemClickListener!=null) {
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String,Object> map = new HashMap<String, Object>();
                        map.put("title", album.getAlbumTitle());
                        map.put("artist",album.getAlbumArtist());
                        map.put("id",album.getAlbumId());
                        map.put("album",album);
                        itemClickListener.onItemClick(viewHolder.itemView, position, map);
                    }
                });
            }
            //setAnimation(viewHolder.itemView);
        }
        // # CAUTION:
        // Important to call this method
        ((ViewHolder)viewHolder).getBackgroundImage().reuse();
    }

    private void setAnimation(View viewToAnimate) {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(100);//to make duration random number between [0,501)
        viewToAnimate.startAnimation(anim);
    }

    @Override
    public int getItemCount() {
        return albums.size();
    }

    public static class ViewHolder extends ParallaxViewHolder{

        private final TextView textView;

        public ViewHolder(View v) {
            super(v);
            textView = (TextView) v.findViewById(R.id.cover_title);
        }

        @Override
        public int getParallaxImageId() {
            return R.id.cover_img;
        }

        public TextView getTextView() {
            return textView;
        }
    }

}
