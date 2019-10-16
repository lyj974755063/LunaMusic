package com.example.androidisshit.utils;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import com.bumptech.glide.Glide;
import com.example.androidisshit.entity.Song;

import java.util.ArrayList;
import java.util.List;

public class MusicUtils {
    final static int EXCLUDE_DURATION = 40;

    public static List<Song> getAllMusic(Context context) {
        List<Song> songList = new ArrayList<>();
        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                , null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                Song song = new Song();
                song.setDuration(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)));
                if (!checkCanInclude(song.getDuration())) {
                    continue;
                }
                song.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)));
                song.setId(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)));
                song.setArtist(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)));
                song.setPath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)));
                song.setSize(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)));
                song.setAlbumId(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)));
                song.setAlbumTitle(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)));
                songList.add(song);
            }
        }
        cursor.close();
        return songList;
    }

    public static Bitmap getAlbumArt(Context context, long album_id) {
        String mUriAlbums = "content://media/external/audio/albums";
        String[] projection = new String[]{"album_art"};
        Cursor cur = context.getContentResolver().query(Uri.parse(mUriAlbums + "/" + Long.toString(album_id)), projection, null, null, null);
        String album_art = null;
        if (cur.getCount() > 0 && cur.getColumnCount() > 0) {
            cur.moveToNext();
            album_art = cur.getString(0);
        }
        cur.close();
        Bitmap bm = null;
        if (album_art != null) {
            bm = BitmapFactory.decodeFile(album_art);
        } else {
            //bm = BitmapFactory.decodeResource(getResources(), R.drawable.default_cover);
            System.out.println("null cover");
        }
        return bm;
    }

    public static Uri getArtUri(long album_id) {
        //String mUriAlbums = "content://media/external/audio/albumart";
        Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
        Uri uri = ContentUris.withAppendedId(sArtworkUri, album_id);
        //return Uri.parse(mUriAlbums + "/" + Long.toString(album_id));
        return uri;
    }

    private static boolean checkCanInclude(int duration) {
        if (duration > 0) {
            duration /= 1000;
            if (duration > EXCLUDE_DURATION) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }

    }

}

