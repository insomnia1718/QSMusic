package com.zhangtao.qsmusic.ui.folder;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by tao.zhang on 16-9-7.
 */
public class FolderFragment extends Fragment {

    private static FolderFragment fragment;

    public static FolderFragment getInstance(){
        if(fragment == null){
            fragment = new FolderFragment();
        }
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
