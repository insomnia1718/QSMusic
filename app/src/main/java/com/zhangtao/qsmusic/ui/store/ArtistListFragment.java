package com.zhangtao.qsmusic.ui.store;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhangtao.qsmusic.R;

/**
 * Created by tao.zhang on 16-9-19.
 */
public class ArtistListFragment extends Fragment {
    private static ArtistListFragment f;

    public static ArtistListFragment getInstance(Context context){
        if(f == null){
            f = new ArtistListFragment();
        }
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v  = inflater.inflate(R.layout.fragment_list_artist,container,false);
        return v;
    }
}
