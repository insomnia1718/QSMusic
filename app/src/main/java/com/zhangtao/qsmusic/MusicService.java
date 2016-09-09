package com.zhangtao.qsmusic;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.TimedText;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.zhangtao.qsmusic.model.Music;
import com.zhangtao.qsmusic.model.MusicList;
import com.zhangtao.qsmusic.utils.MusicUtil;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class MusicService extends Service {
    private Music currentMusic;
    private MusicList currentMusicList;
    private IBinder mBinder = new LocalBinder();
    private MediaPlayer player;
    private Timer timer;
    private Set<OnMusicStateListener> listeners = new HashSet<>();

    public void addOnMusicStateListener(OnMusicStateListener onMusicStateListener) {
        listeners.add(onMusicStateListener);
    }


    public MusicService() {
        if(player == null){
            player = new MediaPlayer();
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    playMusic(currentMusicList.next());
                }
            });
            timer = new Timer();

        }

    }

    public void skipNext(){
        playMusic(currentMusicList.next());
    }

    public void skipPre(){
        playMusic(currentMusicList.pre());
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        if(currentMusicList == null){
            currentMusicList = new MusicList("ALL_MUSIC", MusicList.MusicListType.DEFAULT);
            currentMusicList.setMusics(MusicUtil.getInstance().getMusicList(this));

        }
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
            currentMusic = music;
            currentMusicList.setCurrentMusic(music);
            timer.cancel();
            scheduleTimer();
            for (OnMusicStateListener listener:listeners){
                listener.onPlay(music);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isPlaying(){
        return player.isPlaying();
    }

    private void scheduleTimer(){
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                for (OnMusicStateListener listener:listeners){
                    listener.onProgress(player.getCurrentPosition());
                }
            }
        },1000,1000);
    }

    public void stopMusic(){
        if(player.isPlaying()){
            player.stop();
            timer.cancel();
        }
        player.reset();
    }

    public void pauseMusic(){
        if(player.isPlaying()){
            player.pause();
            timer.cancel();
        }
    }

    public void resumeMusic(){
        player.start();
        scheduleTimer();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        player.release();
    }
}
