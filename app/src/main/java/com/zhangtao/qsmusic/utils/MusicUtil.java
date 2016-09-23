package com.zhangtao.qsmusic.utils;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;

import com.zhangtao.qsmusic.R;
import com.zhangtao.qsmusic.model.Album;
import com.zhangtao.qsmusic.model.Music;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by tao.zhang on 16-8-31.
 */
public class MusicUtil {
    private static MusicUtil musicUtil;
    private static final Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
    private static final BitmapFactory.Options sBitmapOptions = new BitmapFactory.Options();
    private static Bitmap mCachedBit = null;

    public static MusicUtil getInstance() {
        if (musicUtil == null) {
            musicUtil = new MusicUtil();
        }
        return musicUtil;
    }

    private MusicUtil() {


    }


    public ArrayList<Music> getMusicList(Context context) {
        ArrayList<Music> musics = new ArrayList<>();
        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    String url = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                    if(!url.contains(".ogg")&&!url.contains(".wav")) {
                        Music music = new Music();
                        music.setTitle(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
                        music.setDisplayName(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME)));
                        music.setId(cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID)));
                        music.setAlbum(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)));
                        music.setAlbumId(cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)));
                        music.setArtist(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
                        music.setDuration(cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)));
                        music.setSize(cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE)));
                        music.setUrl(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)));
                        musics.add(music);
                    }
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return musics;
    }


    public ArrayList<Album> getAlbumList(Context context){
        ArrayList<Album> albums = new ArrayList<>();
        String where = null;

        final Uri uri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
        final String _id = MediaStore.Audio.Albums._ID;
        final String album_name = MediaStore.Audio.Albums.ALBUM;
        final String artist = MediaStore.Audio.Albums.ARTIST;
        final String albumart = MediaStore.Audio.Albums.ALBUM_ART;
        final String tracks = MediaStore.Audio.Albums.NUMBER_OF_SONGS;

        final String[] columns = { _id, album_name, artist, albumart, tracks };
        Cursor cursor = context.getContentResolver().query(uri, columns, where, null, null);

        if(cursor!=null && cursor.moveToFirst()){
            do {
                long id = cursor.getLong(cursor.getColumnIndex(_id));
                String name = cursor.getString(cursor.getColumnIndex(album_name));
                String artist2 = cursor.getString(cursor.getColumnIndex(artist));
                String artPath = cursor.getString(cursor.getColumnIndex(albumart));
                Bitmap art = BitmapFactory.decodeFile(artPath);
                int nr =Integer.parseInt(cursor.getString(cursor.getColumnIndex(tracks)));
                Album album = new Album();
                album.setId(id);
                album.setArtist(artist2);
                album.setTitle(name);
                album.setSongsCount(nr);
                albums.add(album);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return albums;
    }

    public static Bitmap getArtwork(Context context, long song_id, long album_id,
                                    boolean allowdefault, int scale) {
        if (album_id < 0) {
            // This is something that is not in the database, so get the album art directly
            // from the file.
            if (song_id >= 0) {
                Bitmap bm = getArtworkFromFile(context, song_id, -1, scale);
                if (bm != null) {
                    return bm;
                }
            }
            if (allowdefault) {
                return getDefaultArtwork(context);
            }
            return null;
        }
        ContentResolver res = context.getContentResolver();
        Uri uri = ContentUris.withAppendedId(sArtworkUri, album_id);
        if (uri != null) {
            InputStream in = null;
            try {
                in = res.openInputStream(uri);
                BitmapFactory.Options opts = new BitmapFactory.Options();

//                opts.inJustDecodeBounds = true;
//                BitmapFactory.decodeStream(in, null, opts);
                opts.inSampleSize = scale;
//                opts.inJustDecodeBounds = false;
                return BitmapFactory.decodeStream(in, null, opts);
            } catch (FileNotFoundException ex) {
                // The album art thumbnail does not actually exist. Maybe the user deleted it, or
                // maybe it never existed to begin with.
                Bitmap bm = getArtworkFromFile(context, song_id, album_id, scale);
                if (bm != null) {
                    if (bm.getConfig() == null) {
                        bm = bm.copy(Bitmap.Config.RGB_565, false);
                        if (bm == null && allowdefault) {
                            return getDefaultArtwork(context);
                        }
                    }
                } else if (allowdefault) {
                    bm = getDefaultArtwork(context);
                }
                return bm;
            } finally {
                try {
                    if (in != null) {
                        in.close();
                    }
                } catch (IOException ex) {
                }
            }
        }

        return null;
    }

    private static Bitmap getArtworkFromFile(Context context, long songid, long albumid, int scale) {
        Bitmap bm = null;
        byte[] art = null;
        String path = null;
        if (albumid < 0 && songid < 0) {
            throw new IllegalArgumentException("Must specify an album or a song id");
        }
        try {
            if (albumid < 0) {
                Uri uri = Uri.parse("content://media/external/audio/media/" + songid + "/albumart");
                ParcelFileDescriptor pfd = context.getContentResolver().openFileDescriptor(uri, "r");
                if (pfd != null) {
                    FileDescriptor fd = pfd.getFileDescriptor();
//                    BitmapFactory.Options opts = new BitmapFactory.Options();
//                    opts.inJustDecodeBounds = true;
//                    BitmapFactory.decodeFileDescriptor(fd,null,opts);
//                    opts.inSampleSize = calculateInSampleSize(opts,120,120);
//                    opts.inJustDecodeBounds = false;
                    bm = BitmapFactory.decodeFileDescriptor(fd);
                }
            } else {
                Uri uri = ContentUris.withAppendedId(sArtworkUri, albumid);
                ParcelFileDescriptor pfd = context.getContentResolver().openFileDescriptor(uri, "r");
                if (pfd != null) {
                    FileDescriptor fd = pfd.getFileDescriptor();
                    BitmapFactory.Options opts = new BitmapFactory.Options();
//                    opts.inJustDecodeBounds = true;
//                    BitmapFactory.decodeFileDescriptor(fd,null,opts);
                    opts.inSampleSize = scale;
//                    opts.inJustDecodeBounds = false;
                    bm = BitmapFactory.decodeFileDescriptor(fd, null, opts);
                }
            }
        } catch (FileNotFoundException ex) {

        }
        if (bm != null) {
            mCachedBit = bm;
        }


        return bm;
    }

    public static String formatTime(long duration) {
        int d = (int) duration / 1000;
        String min = String.valueOf(d / 60);
        String second = String.valueOf(d % 60);
        return (min.length() == 1 ? "0" + min : min) + ":" + (second.length() == 1 ? "0" + second : second);
    }

    private static Bitmap getDefaultArtwork(Context context) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_music_note_grey_500_48dp, opts);
        opts.inSampleSize = calculateInSampleSize(opts, 120, 120);
        opts.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_music_note_grey_500_48dp, opts);
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int width = options.outWidth;
        int height = options.outHeight;
        int inSampleSize = 1;
        if (width > reqWidth || height > reqHeight) {
            int halfWidth = width / 2;
            int halfHeight = height / 2;

            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }


}
