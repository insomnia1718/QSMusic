package com.zhangtao.qsmusic;



import android.app.Instrumentation;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhangtao.qsmusic.model.Music;
import com.zhangtao.qsmusic.utils.MusicUtil;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by tao.zhang on 16-8-31.
 */
public class AllMusicFragment extends Fragment {

    RecyclerView rv;
    private static AllMusicFragment f;
    private ArrayList<Music> musics;
    private HashMap<Long,Bitmap> icons;
    private long playingMusicId = 10000000;
    MusicService musicService;


    public static AllMusicFragment getInstance(){
        if(f == null){
            f = new AllMusicFragment();

        }
        return f;
    }

    public void setMusicService(MusicService service){
        this.musicService = service;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("allMusic","allMusic");
        View v = inflater.inflate(R.layout.fragment_all_music,container,false);
        rv  = (RecyclerView)v.findViewById(R.id.rvMusicList);
        musics = MusicUtil.getInstance().getMusicList(getActivity());
        icons = new HashMap<>();
        double size = 0.0;
        for (Music m:musics){
            Bitmap bitmap = MusicUtil.getArtwork(getActivity(),m.getId(),m.getAlbumId(),true);
            size+=bitmap.getByteCount();
            icons.put(m.getId(),bitmap);
        }
        Log.d("size",size/1024/1024+"");

        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        MusicListAdapter adapter = new MusicListAdapter();
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position,Music music) {
                switch (v.getId()){
                    case R.id.ibAdd:

                        break;
                    case R.id.ibAddList:

                        break;
                    case R.id.ibDelete:

                        break;
                    case R.id.ibLike:
                        v.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_favorite_red_300_24dp));
                        break;
                    case R.id.layoutItem:
                        Log.d("onclick","layout");
                        musicService.playMusic(music);
//                        v.setBackgroundColor(getResources().getColor(R.color.grey300)) ;
                        break;
                }
            }
        });
        rv.setAdapter(adapter);

        return v;
    }


    class MusicListAdapter extends RecyclerView.Adapter{

        private OnItemClickListener listener;
        Handler handler;

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MusicViewHolder(getActivity().getLayoutInflater().inflate(R.layout.item_music,parent,false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            final MusicViewHolder h = (MusicViewHolder)holder;
            final Music m = musics.get(position);
            h.tvTitle.setText(m.getTitle());
            h.tvArtistAndAlbum.setText(m.getArtist()+" - "+m.getAlbum());
            h.tvDuration.setText(MusicUtil.getInstance().formatDuration(m.getDuration()));
            h.ivIcon.setImageBitmap(icons.get(m.getId()));




            View.OnClickListener onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(v,h.getLayoutPosition(),m);
                }
            };
            h.layoutItem.setOnClickListener(onClickListener);
            h.ibAdd.setOnClickListener(onClickListener);
            h.ibAddList.setOnClickListener(onClickListener);
            h.ibDelete.setOnClickListener(onClickListener);
            h.ibLike.setOnClickListener(onClickListener);
        }

        @Override
        public int getItemCount() {
            return musics.size();
        }

        class MusicViewHolder extends RecyclerView.ViewHolder{
            ImageView ivIcon;
            TextView tvTitle;
            TextView tvArtistAndAlbum;
            TextView tvDuration;
            ImageButton ibLike;
            ImageButton ibAddList;
            ImageButton ibAdd;
            ImageButton ibDelete;
            View layoutItem;

            public MusicViewHolder(View v){
                super(v);
                this.layoutItem = v.findViewById(R.id.layoutItem);
                ivIcon = (ImageView) v.findViewById(R.id.ivIcon);
                tvTitle = (TextView) v.findViewById(R.id.tvTitle);
                tvArtistAndAlbum = (TextView) v.findViewById(R.id.tvArtistAndAlbum);
                tvDuration = (TextView) v.findViewById(R.id.tvDuration);
                ibLike = (ImageButton) v.findViewById(R.id.ibLike);
                ibAddList = (ImageButton) v.findViewById(R.id.ibAddList);
                ibAdd = (ImageButton) v.findViewById(R.id.ibAdd);
                ibDelete = (ImageButton) v.findViewById(R.id.ibDelete);
            }
        }

        public void setOnItemClickListener(OnItemClickListener listener){
            this.listener = listener;
        }

    }

    interface OnItemClickListener{
        void onItemClick(View v,int position,Music music);
    }


}
