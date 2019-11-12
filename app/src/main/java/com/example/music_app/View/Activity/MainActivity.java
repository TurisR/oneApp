package com.example.music_app.View.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.music_app.Presenter.AppConstant;
import com.example.music_app.Presenter.MusicService;
import com.example.music_app.R;
import com.example.music_app.View.Fragment.MusicListFragement;
import com.example.music_app.View.Fragment.SettingFragement;
import com.example.music_app.View.Fragment.TabContentFragment;
import com.example.music_app.mould.Model.bean.Song;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, MusicListFragement.CallBackValue {
    private TabLayout mTabTl;
    private ViewPager mContentVp;
    private Song lastSong;
    private List<Fragment> tabFragments;
    private List<Song> mSongList;
    private RelativeLayout playing_list_sector;

    private TextView tv_play_bar_title;
    private TextView tv_play_bar_artist;
    public ImageView v_play_bar_play;
    private ContentPagerAdapter contentAdapter;
    private boolean isChanged=true;
    public static SeekBar audioSeekBar = null;
    private String tabIndicators[] = {
            "本地歌曲", "个人歌单", "搜索", "设置",
            };
    private Song song;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
        initView();
        initEvent();
        initTab();
        initContent();

    }

    private void initData() {
        //song = (Song) getIntent().getSerializableExtra("songInfo");//获得歌曲信息
        Log.e("song", "activity_song");
    }




    private void initEvent() {
        /* 播放进度监听 */
        audioSeekBar.setOnSeekBarChangeListener(new SeekBarChangeEvent());
        /*退出后再次进去程序时，进度条保持持续更新*/
        if(MusicService.mMediaPlayer!=null){
            //设置进度条最大值
            //audioSeekBar.setMax(MusicService.mMediaPlayer.getDuration());
            audioSeekBar.setProgress(MusicService.mMediaPlayer.getCurrentPosition());
        }


    }

    private void initView() {
        mTabTl=findViewById(R.id.top_tab);
        mContentVp=findViewById(R.id.top_viewPage);
        playing_list_sector=findViewById(R.id.playing_list_sector);
        tv_play_bar_title = findViewById(R.id.tv_play_bar_title);
        tv_play_bar_artist = findViewById(R.id.tv_play_bar_artist);
        v_play_bar_play = findViewById(R.id.iv_play_bar_play);
        v_play_bar_play.setOnClickListener(this);
        audioSeekBar = findViewById(R.id.pb_play_bar);
        findViewById(R.id.iv_play_bar_next).setOnClickListener(this);
        findViewById(R.id.v_play_bar_playlist).setOnClickListener(this);

        if(MusicService.mMediaPlayer!=null){
            v_play_bar_play.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_bar_btn_pause));
            isChanged=true;
        }else {
            v_play_bar_play.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_bar_btn_play));
        }
    }

    private void initTab() {
        mTabTl.setTabMode(TabLayout.MODE_SCROLLABLE);
        mTabTl.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mContentVp.setCurrentItem(tab.getPosition());
                View view = tab.getCustomView();
                if (null == view) {
                    tab.setCustomView(R.layout.tab_item);
                }
                TextView textView = tab.getCustomView().findViewById(android.R.id.text1);
                textView.setTextAppearance(MainActivity.this, R.style.TabLayoutTextSize);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View view = tab.getCustomView();
                if (null == view) {
                    tab.setCustomView(R.layout.tab_item);
                }
                TextView textView = tab.getCustomView().findViewById(android.R.id.text1);
                textView.setTextAppearance(MainActivity.this, R.style.TabLayoutTextSize_two);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void initContent() {
        mTabTl.setupWithViewPager(mContentVp);
        tabFragments = new ArrayList<>();
        tabFragments.add(new MusicListFragement());
        for (int i=1;i<3;i++) {
            tabFragments.add(TabContentFragment.newInstance(tabIndicators[i]));
        }
        tabFragments.add(new SettingFragement());
        contentAdapter = new ContentPagerAdapter(getSupportFragmentManager());
        mContentVp.setAdapter(contentAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_play_bar_next:
                playMusic(AppConstant.PlayerMag.NEXT,song);
                AppConstant.getInstance().setPlayingState(AppConstant.PlayerMag.NEXT);
                break;
            case R.id.iv_play_bar_play:
                 playMusic(AppConstant.PlayerMag.PAUSE,song);
                if(AppConstant.getInstance().getPlayingState()==AppConstant.PlayerMag.PAUSE){
                    v_play_bar_play.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_bar_btn_pause));
                }else
                {
                    v_play_bar_play.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_bar_btn_play));
                }
                isChanged = !isChanged;
                 Log.e("song", "activity_song");
                //playMusic(AppConstant.PlayerMag.PLAY_MAG,song);
                break;
        }

    }


    public void playMusic(int action,Song song) {

        Intent intent = new Intent();
        Bundle mBundle=new Bundle();
        intent.putExtra("MSG", action);
        intent.putExtra("position",position);
        if(song!=null){
            intent.putExtra("songInfo",song.getFileUrl());
            //intent.putExtras("PlaySong",(Serializable)song)
            mBundle.putSerializable("PlaySong",song);
            intent.putExtras(mBundle);
        }
        intent.setClass(this, MusicService.class);
        startService(intent);

    }


    @Override
    public void SendMessageValue(Song song,int position) {
        tv_play_bar_title.setText(song.getTitle());
        tv_play_bar_artist.setText(song.getSinger());
        if(this.song!=song){
            if(MusicService.mMediaPlayer!=null){
                v_play_bar_play.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_bar_btn_play));
                isChanged=true;
            }else {
                v_play_bar_play.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_bar_btn_pause));
            }
        }
        this.song=song;
        this.position=position;
    }

    class ContentPagerAdapter extends FragmentPagerAdapter {

        public ContentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return tabFragments.get(position);
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabIndicators[position];
        }

    }




    /* 拖放进度监听 ，别忘了Service里面还有个进度条刷新*/
    class SeekBarChangeEvent implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
            /*假设改变源于用户拖动*/
            if (fromUser) {
                MusicService.mMediaPlayer.seekTo(progress);// 当进度条的值改变时，音乐播放器从新的位置开始播放
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            MusicService.mMediaPlayer.pause(); // 开始拖动进度条时，音乐暂停播放
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            MusicService.mMediaPlayer.start(); // 停止拖动进度条时，音乐开始播放
        }
    }



}
