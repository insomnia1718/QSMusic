package com.zhangtao.qsmusic;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.zhangtao.qsmusic.model.Music;

import java.io.IOException;

public class MusicService extends Service {
    Music currentMusic;
    private IBinder mBinder = new LocalBinder();
    private MediaPlayer player;

    public MusicService() {
        if(player == null){
            player = new MediaPlayer();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return mBinder;
    }

    public class LocalBinder extends Binder {
        public MusicService getService(){
            return MusicService.this;
        }
    }

    public void playMusic(Music music){
        Log.d("MusicService","playMusic");
        try {
            stopMusic();
            player.setDataSource(this, Uri.parse(music.getUrl()));
            player.prepare();
            player.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopMusic(){
        if(player.isPlaying()){
            player.stop();
        }
        player.reset();
    }

    public void pauseMusic(){
        if(player.isPlaying()){
            player.pause();
        }
    }

    public void resumeMusic(){
        player.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        player.release();
    }
}
