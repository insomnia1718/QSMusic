package com.zhangtao.qsmusic;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhangtao.qsmusic.control.AppController;
import com.zhangtao.qsmusic.control.MusicService;
import com.zhangtao.qsmusic.control.OnFragmentListener;
import com.zhangtao.qsmusic.control.OnMusicStateListener;
import com.zhangtao.qsmusic.model.Music;
import com.zhangtao.qsmusic.ui.MusicActivity;
import com.zhangtao.qsmusic.ui.fav.MusicLikeFragment;
import com.zhangtao.qsmusic.ui.folder.FolderFragment;
import com.zhangtao.qsmusic.ui.list.MusicListFragment;
import com.zhangtao.qsmusic.ui.setting.SettingFragment;
import com.zhangtao.qsmusic.ui.store.MusicStoreFragment;
import com.zhangtao.qsmusic.utils.MusicUtil;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ImageButton ibPlay;
    private ImageButton ibPause;
    private LinearLayout layoutFooter;
    private ImageView ivFooterIcon;
    private TextView tvFooterTitle;
    private TextView tvFooterArtist;
    private ImageButton ibSkipPre;
    private ImageButton ibSkipNext;
    private FrameLayout mainFrame;
    private AppBarLayout appBarLayout;
    private NavigationView navigationView;
    private Music currentMusic;


    private MusicService getMusicService() {
        return AppController.getInstance().getMusicService();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Intent intent = new Intent(MainActivity.this,MusicService.class);
//        bindService(intent,connection, Context.BIND_AUTO_CREATE);
//        musicService = AppController.getInstance().getMusicService();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        appBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);

        mainFrame = (FrameLayout) findViewById(R.id.main_frame);

        ibPlay = (ImageButton) findViewById(R.id.ibPlay);
        ibPause = (ImageButton) findViewById(R.id.ibPause);
        layoutFooter = (LinearLayout) findViewById(R.id.layout_footer);
        ivFooterIcon = (ImageView) findViewById(R.id.ivFooterIcon);
        tvFooterTitle = (TextView) findViewById(R.id.tvFooterTitle);
        tvFooterArtist = (TextView) findViewById(R.id.tvFooterArtist);
        ibSkipPre = (ImageButton) findViewById(R.id.ibSkipPre);
        ibSkipNext = (ImageButton) findViewById(R.id.ibSkipNext);


        ibPlay.setOnClickListener(onFooterOnClickListener);
        ibPause.setOnClickListener(onFooterOnClickListener);
        ibSkipNext.setOnClickListener(onFooterOnClickListener);
        ibSkipPre.setOnClickListener(onFooterOnClickListener);
        layoutFooter.setOnClickListener(onFooterOnClickListener);
        layoutFooter.setVisibility(View.GONE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkPermisson()) {
                showFragments(MusicStoreFragment.getInstance(onFragmentListener));
                setTitle("音乐库");
                navigationView.setCheckedItem(R.id.nav_store);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        }


    }


    private void showFragments(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_frame, fragment);
        transaction.commit();
        appBarLayout.setExpanded(true);
    }

    private View.OnClickListener onFooterOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ibPlay:
                    if (currentMusic != null) {
                        getMusicService().resumeMusic();
                    }
                    ibPlay.setVisibility(View.GONE);
                    ibPause.setVisibility(View.VISIBLE);
                    break;
                case R.id.ibPause:
                    if (currentMusic != null) {
                        getMusicService().pauseMusic();
                    }
                    ibPause.setVisibility(View.GONE);
                    ibPlay.setVisibility(View.VISIBLE);
                    break;
                case R.id.ibSkipNext:
                    getMusicService().skipNext();
                    break;
                case R.id.ibSkipPre:
                    getMusicService().skipPre();
                    break;
                case R.id.layout_footer:
                    Intent intent = new Intent(MainActivity.this, MusicActivity.class);
                    intent.putExtra("MUSIC", currentMusic);
                    intent.putExtra("IS_PLAYING", getMusicService().isPlaying());
                    Log.d("isPlaying", getMusicService().isPlaying() + "");
                    startActivity(intent);
                    break;

            }
        }
    };


    @TargetApi(23)
    private boolean checkPermisson() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            showFragments(MusicStoreFragment.getInstance(onFragmentListener));
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            moveTaskToBack(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_store) {
            showFragments(MusicStoreFragment.getInstance(onFragmentListener));
            setTitle("音乐库");
        } else if (id == R.id.nav_like) {
            showFragments(MusicLikeFragment.getInstance());
            setTitle("喜欢");
        } else if (id == R.id.nav_list) {
            showFragments(MusicListFragment.getInstance());
            setTitle("我的歌单");
        } else if (id == R.id.nav_folder) {
            showFragments(FolderFragment.getInstance());

            setTitle("文件夹");
        } else if (id == R.id.nav_settings) {
            showFragments(SettingFragment.getInstance());
            setTitle("设置");
        } else if (id == R.id.nav_exit) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void setFooterMusic(Music music) {
        layoutFooter.setVisibility(View.VISIBLE);
        Bitmap footIcon = MusicUtil.getArtwork(MainActivity.this, music.getId(), music.getAlbumId(), true, 4);
        ivFooterIcon.setImageBitmap(footIcon);
        tvFooterArtist.setText(music.getArtist());
        tvFooterTitle.setText(music.getTitle());
        ibPlay.setVisibility(View.GONE);
        ibPause.setVisibility(View.VISIBLE);
        Palette.Builder builder = new Palette.Builder(footIcon);
        builder.generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                layoutFooter.setBackgroundColor(palette.getVibrantColor(Color.WHITE));
            }
        });
    }

    private OnMusicStateListener onMusicStateListener = new OnMusicStateListener() {
        @Override
        public void onProgress(int position) {

        }

        @Override
        public void onPlay(Music music) {
            setFooterMusic(music);
            currentMusic = music;
        }
    };

    private boolean added = false;

    private OnFragmentListener onFragmentListener = new OnFragmentListener() {
        @Override
        public void onAction(Intent intent) {
            String action = intent.getAction();
            if (action.equalsIgnoreCase("PLAY_MUSIC")) {
                Music music = (Music) intent.getSerializableExtra("music");
                if (!added) {
                    getMusicService().addOnMusicStateListener(onMusicStateListener);
                    added = true;
                }
                getMusicService().playMusic(music);
                setFooterMusic(music);
            } else if (action.equalsIgnoreCase("PAUSE_MUSIC")) {
                getMusicService().pauseMusic();
            } else if (action.equalsIgnoreCase("STOP_MUSIC")) {
                getMusicService().stopMusic();
            } else if (action.equalsIgnoreCase("SKIP_NEXT")) {

            } else if (action.equalsIgnoreCase("SKIP_PRE")) {

            }
        }
    };


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
