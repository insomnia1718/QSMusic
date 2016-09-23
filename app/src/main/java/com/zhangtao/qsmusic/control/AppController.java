package com.zhangtao.qsmusic.control;

import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by tao.zhang on 16-9-8.
 */
public class AppController extends Application {


    private MusicService musicService;
    private boolean binded = false;
    private static AppController appController;

    private ServiceConnection connection  = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d("onServiceConnected","AppController");
            MusicService.LocalBinder binder = (MusicService.LocalBinder)service;
            musicService = binder.getService();
            binded = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    public static synchronized AppController getInstance(){
        return appController;
    }

    public MusicService getMusicService(){
        return this.musicService;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appController = this;
        appController.init();
    }

    private void init(){
        Intent intent = new Intent(AppController.this,MusicService.class);
        bindService(intent,connection,BIND_AUTO_CREATE);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        if(binded) {
            unbindService(connection);
        }
    }
}
