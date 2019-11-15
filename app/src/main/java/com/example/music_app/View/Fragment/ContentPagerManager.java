package com.example.music_app.View.Fragment;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class ContentPagerManager extends FragmentPagerAdapter {

    private  List<Fragment> fragmentlist;
    private String tabIndicators[] = {
            "本地歌曲", "个人歌单", "搜索", "设置",
    };

    public ContentPagerManager(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        this.fragmentlist=fragmentList;
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
}
