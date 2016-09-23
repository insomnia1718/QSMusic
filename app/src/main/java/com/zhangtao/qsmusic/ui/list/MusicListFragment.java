package com.zhangtao.qsmusic.ui.list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by tao.zhang on 16-9-7.
 */
public class MusicListFragment extends Fragment {

    private static MusicListFragment fragment;

    public static MusicListFragment getInstance(){
        if(fragment == null){

            fragment = new MusicListFragment();
        }
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
