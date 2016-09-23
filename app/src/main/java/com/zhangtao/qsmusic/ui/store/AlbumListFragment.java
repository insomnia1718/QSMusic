package com.zhangtao.qsmusic.ui.store;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhangtao.qsmusic.R;
import com.zhangtao.qsmusic.model.Album;
import com.zhangtao.qsmusic.model.Music;
import com.zhangtao.qsmusic.utils.MusicUtil;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by tao.zhang on 16-9-19.
 */
public class AlbumListFragment extends Fragment {

    private static AlbumListFragment f;
    private ArrayList<Album> albums = new ArrayList<>();
    private RecyclerView rvAlbumList;
    Context context;
    HashMap<Long,Bitmap> icons;

    public static AlbumListFragment getInstance(Context context){
        if(f == null){
            f = new AlbumListFragment(context);
        }
        return f;
    }

    public AlbumListFragment(Context context){
        albums = MusicUtil.getInstance().getAlbumList(context);
        this.context = context;
        icons = new HashMap<>();
        double size = 0.0;
        for (Album a:albums){
            Bitmap bitmap = MusicUtil.getArtwork(context,-1,a.getId(),true,2);
            size+=bitmap.getByteCount();
            icons.put(a.getId(),bitmap);
        }
        Log.d("size",size/1024/1024+"");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list_album,container,false);
        rvAlbumList = (RecyclerView)v.findViewById(R.id.rvAlbumList);
        rvAlbumList.setLayoutManager(new GridLayoutManager(context,2));
        rvAlbumList.setAdapter(new AlbumListAdapter());
        return v;
    }

    class AlbumListAdapter extends RecyclerView.Adapter{

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new AlbumViewHolder(getActivity().getLayoutInflater().inflate(R.layout.item_album_list,parent,false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            AlbumViewHolder albumViewHolder = (AlbumViewHolder)holder;
            albumViewHolder.ivAlbumIcon.setImageBitmap(icons.get(albums.get(position).getId()));
            albumViewHolder.tvAlbumArtist.setText(albums.get(position).getArtist());
            albumViewHolder.tvAlbumTitle.setText(albums.get(position).getTitle());
        }

        @Override
        public int getItemCount() {
            return albums.size();
        }

        class AlbumViewHolder extends RecyclerView.ViewHolder{
            ImageView ivAlbumIcon;
            TextView tvAlbumTitle;
            TextView tvAlbumArtist;

            public AlbumViewHolder(View itemView) {
                super(itemView);
                ivAlbumIcon = (ImageView)itemView.findViewById(R.id.ivAlbumIcon);
                tvAlbumTitle = (TextView)itemView.findViewById(R.id.tvAlbumTitle);
                tvAlbumArtist = (TextView)itemView.findViewById(R.id.tvAlbumArtist);
            }
        }
    }
}
