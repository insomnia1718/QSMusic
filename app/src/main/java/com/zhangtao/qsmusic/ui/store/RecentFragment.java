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
 * Created by tao.zhang on 16-8-31.
 */
public class RecentFragment extends Fragment {
    private static RecentFragment f;

    public static RecentFragment getInstance(Context context){
        if(f == null){
            f = new RecentFragment();
        }
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recent,container,false);
        return v;
    }
}
