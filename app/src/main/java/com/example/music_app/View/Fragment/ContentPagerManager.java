package com.example.music_app.View.Fragment;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


import java.util.ArrayList;


public class ContentPagerManager extends FragmentPagerAdapter {

    private  ArrayList<Fragment> fragmentlist= new ArrayList<>();
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

    @Override
    public int getItemPosition(Object object) {//没有找到child要求重新加载。
        return POSITION_NONE;
    }


}
