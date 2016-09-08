package com.zhangtao.qsmusic;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by tao.zhang on 16-9-7.
 */
public class SettingFragment extends Fragment {

    private static SettingFragment fragment;

    public static SettingFragment getInstance(){
        if(fragment == null){
            fragment = new SettingFragment();
        }
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
