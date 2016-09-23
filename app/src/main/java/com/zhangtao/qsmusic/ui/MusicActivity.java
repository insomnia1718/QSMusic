package com.zhangtao.qsmusic.ui;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhangtao.qsmusic.control.AppController;
import com.zhangtao.qsmusic.control.MusicService;
import com.zhangtao.qsmusic.control.OnMusicStateListener;
import com.zhangtao.qsmusic.R;
import com.zhangtao.qsmusic.model.Music;
import com.zhangtao.qsmusic.utils.MusicUtil;

public class MusicActivity extends AppCompatActivity implements View.OnClickListener{


    private Toolbar toolbar;
    private View topBgView;
    private View bottomBgView;
    private ImageView iconAlbum;
    private TextView txtArtist;
    private TextView txtTitle;
    private TextView txtPosition;
    private AppCompatSeekBar seekBar;
    private TextView txtDuration;
    private ImageButton ibSkipPre;
    private ImageButton ibPlay;
    private ImageButton ibPause;
    private ImageButton ibSkipNext;
    private Music music;
    private ImageView bg_iv;


    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 0) {
                int position = msg.arg1;
                seekBar.setProgress(position);
                txtPosition.setText(MusicUtil.formatTime((long) position));
            }else if(msg.what == 1){
                setUpMusicView((Music)msg.obj);
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initView();
        music = (Music)getIntent().getSerializableExtra("MUSIC");
        setUpMusicView(music);
        ActionBar bar = getSupportActionBar();
        if(bar!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        getMusicService().addOnMusicStateListener(new OnMusicStateListener() {
            @Override
            public void onProgress(int position) {
                Message msg = handler.obtainMessage();
                msg.arg1 = position;
                msg.what = 0;
                handler.sendMessage(msg);
            }

            @Override
            public void onPlay(Music music) {
                Message msg = handler.obtainMessage();
                msg.obj = music;
                msg.what = 1;
                handler.sendMessage(msg);
            }
        });

        ibPlay.setOnClickListener(this);
        ibPause.setOnClickListener(this);
        ibSkipNext.setOnClickListener(this);
        ibSkipPre.setOnClickListener(this);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpMusicView(Music music){
        Bitmap bitmap = MusicUtil.getArtwork(this,music.getId(),music.getAlbumId(),true,0);
//        bg_iv.setImageBitmap(DensityUtil.fastblur(this,bitmap,32));
//        viewGroup.setBackgroundTintMode(Mo);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)toolbar.getLayoutParams();
        layoutParams.topMargin = getStatusBarHeight();
        toolbar.setLayoutParams(layoutParams);
        iconAlbum.setImageBitmap(bitmap);
        txtTitle.setText(music.getTitle());
        txtArtist.setText(music.getArtist());
        txtDuration.setText(MusicUtil.formatTime(music.getDuration()));
        if(getMusicService().isPlaying()){
            ibPlay.setVisibility(View.GONE);
            ibPause.setVisibility(View.VISIBLE);
        }else {
            ibPause.setVisibility(View.GONE);
            ibPlay.setVisibility(View.VISIBLE);
        }
        Palette.Builder builder = new Palette.Builder(bitmap);
        builder.generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
//                topBgView.setBackgroundColor(palette.getLightMutedColor(Color.WHITE));
//                toolbar.setBackgroundColor(palette.getVibrantColor(Color.WHITE));
//                toolbar.setTitle("");
//                bottomBgView.setBackgroundColor(palette.getDColor(Color.parseColor("#212121")));
            }
        });
        seekBar.setMax((int)music.getDuration());
    }

    private MusicService getMusicService(){
        return AppController.getInstance().getMusicService();
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        topBgView = (View) findViewById(R.id.topBgView);
//        bottomBgView = (View) findViewById(R.id.bottomBgView);
        iconAlbum = (ImageView) findViewById(R.id.iconAlbum);
        txtArtist = (TextView) findViewById(R.id.txtArtist);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtPosition = (TextView) findViewById(R.id.txtPosition);
        seekBar = (AppCompatSeekBar) findViewById(R.id.seekBar);
        txtDuration = (TextView) findViewById(R.id.txtDuration);
        ibSkipPre = (ImageButton) findViewById(R.id.ibSkipPre);
        ibPlay = (ImageButton) findViewById(R.id.ibPlay);
        ibPause = (ImageButton) findViewById(R.id.ibPause);
        ibSkipNext = (ImageButton) findViewById(R.id.ibSkipNext);
//        bg_iv = (ImageView)findViewById(R.id.bg_iv);
    }

    @TargetApi(21)
    private void setStatusBar(){
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ibPlay:
                getMusicService().resumeMusic();
                ibPlay.setVisibility(View.GONE);
                ibPause.setVisibility(View.VISIBLE);
                break;
            case R.id.ibPause:
                getMusicService().pauseMusic();
                ibPause.setVisibility(View.GONE);
                ibPlay.setVisibility(View.VISIBLE);
                break;
            case R.id.ibSkipNext:
                getMusicService().skipNext();
                break;
            case R.id.ibSkipPre:
                getMusicService().skipPre();
                break;
        }
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
