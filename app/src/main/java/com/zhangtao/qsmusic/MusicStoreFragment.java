package com.zhangtao.qsmusic;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by tao.zhang on 16-9-7.
 */
public class MusicStoreFragment extends Fragment {


    private ViewPager viewPager;
    private TabLayout tabLayout;
    private AllMusicFragment allMusicFragment;
    private static MusicStoreFragment fragment;
    private OnFragmentListener listener;



    public static MusicStoreFragment getInstance(OnFragmentListener listener){
        if(fragment == null){
            fragment = new MusicStoreFragment();
            Log.d("getInstance","Create new MusicStoreFragment");
        }
        fragment.setOnFragmentListener(listener);
        return fragment;
    }

    private void setOnFragmentListener(OnFragmentListener listener){
        this.listener = listener;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("oncreateView","MusicStore");
        View v = inflater.inflate(R.layout.fragment_music_store,container,false);
        viewPager = (ViewPager) v.findViewById(R.id.viewPager);
        tabLayout = (TabLayout) v.findViewById(R.id.tabLayout);

        allMusicFragment  = AllMusicFragment.getInstance(getActivity(),listener);
        setupTabLayout();
        return v;
    }




    private void setupTabLayout(){
        viewPager.setAdapter(new HomeViewPagerAdapter(getChildFragmentManager()));

        tabLayout.setupWithViewPager(viewPager);
    }

    class HomeViewPagerAdapter extends FragmentPagerAdapter {

        public HomeViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {

                return AllMusicFragment.getInstance(getActivity(),listener);
            }
            return new RecentFragment();
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return position == 0 ? "全部音乐" : "最近播放";
        }
    }
}
