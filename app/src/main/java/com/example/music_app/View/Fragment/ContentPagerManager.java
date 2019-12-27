package com.example.music_app.View.Fragment;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;


import java.util.ArrayList;
import java.util.List;


public class ContentPagerManager extends FragmentPagerAdapter {

    private List<Fragment> fragmentlist= new ArrayList<>();
    private FragmentManager fm;
    private String tabIndicators[] = {
            "本地歌曲", "个人歌单", "搜索", "设置",
    };

    public ContentPagerManager(FragmentManager fm) {
        super(fm);
        this.fm=fm;
        if (fragmentlist!=null){
            fragmentlist.clear();
            fragmentlist.add(new MusicListFragment());
            fragmentlist.add(new PersonalListFragment());
            fragmentlist.add(new SearchFragment());
            fragmentlist.add(new SettingFragment());
        }

    }

    @Override
    public Fragment getItem(int i) {
        return fragmentlist.get(i);
    }

    @Override
    public int getCount() {
        return fragmentlist.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabIndicators[position];
    }

    public void setFragments(List<Fragment> fragments) {
        if (this.fragmentlist != null) {
            FragmentTransaction ft = fm.beginTransaction();
            for (Fragment f : this.fragmentlist) {
                ft.remove(f);
            }
            ft.commit();
            ft = null;
            fm.executePendingTransactions();
        }
        this.fragmentlist = fragments;
        notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }


}
