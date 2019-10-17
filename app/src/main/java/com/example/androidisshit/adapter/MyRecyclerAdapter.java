package com.example.androidisshit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.androidisshit.R;
import com.example.androidisshit.entity.Album;
import com.example.androidisshit.utils.MusicUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        albums = Album.AllAlbums = Album.getAllAlbums(MusicUtils.getAllMusic(context));
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.mainlist_cover, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int position) {
        final Album album;
        if (!albums.isEmpty()) {
            album = albums.get(position);
            //set album title
            ((ViewHolder)viewHolder).getTextView().setText(album.getAlbumTitle());
            //set album art
            Glide.with(context)
                    .load(MusicUtils.getArtUri(album.getAlbumId()))
                    .fitCenter()//need fix
                    .placeholder(R.mipmap.test_load)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(((ViewHolder)viewHolder).getBackgroundImage());

            if (itemClickListener!=null) {
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String,Object> map = new HashMap<String, Object>();
                        map.put("title", album.getAlbumTitle());
                        map.put("artist",album.getAlbumArtist());
                        map.put("uri",album.getAlbumId());
                        itemClickListener.onItemClick(viewHolder.itemView, position, map);
                    }
                });
            }
        }
        // # CAUTION:
        // Important to call this method
        ((ViewHolder)viewHolder).getBackgroundImage().reuse();
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
