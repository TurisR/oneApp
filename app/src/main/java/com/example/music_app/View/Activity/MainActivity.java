package com.example.music_app.View.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.music_app.Presenter.AppConstant;
import com.example.music_app.Presenter.PlayerUtil;
import com.example.music_app.R;
import com.example.music_app.View.Fragment.ContentPagerManager;
import com.example.music_app.View.Fragment.MusicListFragement;
import com.example.music_app.View.Fragment.SettingFragement;
import com.example.music_app.View.Fragment.TabContentFragment;
import com.example.music_app.mould.Model.bean.Song;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, MusicListFragement.CallBackValue {
    private TabLayout mTabTl;
    private ViewPager mContentVp;
    private List<Fragment> tabFragments;
    private TextView tv_play_bar_title;
    private int duration;
    private Boolean first=true;

    private TextView tv_play_bar_artist;
    public ImageView v_play_bar_play;
    private ContentPagerManager mContentPagerManager;
    public static SeekBar audioSeekBar = null;
    private String tabIndicators[] = {
            "本地歌曲", "个人歌单", "搜索", "设置",
            };
    private Song song;
    private int position;
    private PlayerUtil mPlayerUtil;
    //一系列动作
    public static final String UPDATE_ACTION= "com.example.music_app.UPDATE_ACTION";		//更新动作
    public static final String MUSIC_STATE = "com.example.music_app.MUSIC_STATE";			//播放器状态 播放|暂停
    public static final String MUSIC_CURRENT = "com.action.MUSIC_CURRENT";		//当前音乐改变动作
    public static final String MUSIC_DURATION = "com.action.MUSIC_DURATION";	//音乐时长改变动作
    public static final String REPEAT_ACTION = "com.action.REPEAT_ACTION";		//音乐重复改变动作
    public static final String SHUFFLE_ACTION = "com.action.SHUFFLE_ACTION";	//音乐随机播放动作
    private HomeReceiver homeReceiver;	//自定义的广播接收器

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

        homeReceiver = new HomeReceiver();
        // 创建IntentFilter
        IntentFilter filter = new IntentFilter();
        // 指定BroadcastReceiver监听的Action
        filter.addAction(UPDATE_ACTION);
        filter.addAction(MUSIC_CURRENT);
        filter.addAction(MUSIC_DURATION);
        filter.addAction(REPEAT_ACTION);
        filter.addAction(SHUFFLE_ACTION);
        // 注册BroadcastReceiver
        registerReceiver(homeReceiver, filter);


        /*绑定service*/
        mPlayerUtil=new PlayerUtil(this);
      // bindService(MediaServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
    }


    private void initView() {
        mTabTl=findViewById(R.id.top_tab);
        mContentVp=findViewById(R.id.top_viewPage);
        tv_play_bar_title = findViewById(R.id.tv_play_bar_title);
        tv_play_bar_artist = findViewById(R.id.tv_play_bar_artist);
        v_play_bar_play = findViewById(R.id.iv_play_bar_play);
        v_play_bar_play.setOnClickListener(this);
        audioSeekBar = findViewById(R.id.pb_play_bar);
        findViewById(R.id.iv_play_bar_next).setOnClickListener(this);
        findViewById(R.id.v_play_bar_playlist).setOnClickListener(this);
        if(song==null&&AppConstant.getInstance().getPlayingSong()!=null){
            song=AppConstant.getInstance().getPlayingSong();
            tv_play_bar_title.setText(song.getTitle());
            tv_play_bar_artist.setText(song.getSinger());
        }

       if(AppConstant.getInstance().getPlayingState()==AppConstant.PlayerMsg.PAUSE_MSG){
            v_play_bar_play.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_bar_btn_pause));
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
        mContentPagerManager=new ContentPagerManager(getSupportFragmentManager(),tabFragments);
       // contentAdapter = new ContentPagerAdapter(getSupportFragmentManager());
        mContentVp.setAdapter(mContentPagerManager);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_play_bar_next:
                mPlayerUtil.next();
                //Intent intent = new Intent(this,MusicService.class);
                //UpdataUI();
                break;
            case R.id.iv_play_bar_play:
                if(AppConstant.getInstance().getPlayingState()==AppConstant.PlayerMsg.PAUSE_MSG){
                    v_play_bar_play.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_bar_btn_play));
                    mPlayerUtil.resume();
                }else{
                    v_play_bar_play.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_bar_btn_pause));
                    mPlayerUtil.pause();
                }

                 //Log.e("song", "activity_song");
                break;
                case R.id.v_play_bar_playlist:
                    startActivity(new Intent(MainActivity.this,PlayinglistActivity.class));
                    break;

        }

    }

    private void UpdateUI() {//更新UI
        song=AppConstant.getInstance().getPlayingSong();
        if(song!=null){
            tv_play_bar_title.setText(song.getTitle());
            tv_play_bar_artist.setText(song.getSinger());
        }
        mContentVp.setAdapter(mContentPagerManager);
        if(AppConstant.getInstance().getPlayingState()==AppConstant.PlayerMsg.PAUSE_MSG){
            v_play_bar_play.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_bar_btn_pause));
        }else{
            v_play_bar_play.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_bar_btn_play));
        }
    }


    public void playMusic(int pos,Song song) {
        mPlayerUtil.play(pos);
        v_play_bar_play.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_bar_btn_play));
        Log.e("sss 11",pos+"");
    }


    //与fragment交换数据
    @Override
    public void SendMessageValue(Song song,int position) {
        tv_play_bar_title.setText(song.getTitle());
        tv_play_bar_artist.setText(song.getSinger());
        this.song=song;
        this.position=position;
    }




    @Override
    protected void onResume() {
        super.onResume();
        Log.e("resume","11111111");



        UpdateUI();

        //
        /*song=AppConstant.getInstance().getPlayingSong();
        tv_play_bar_title.setText(song.getTitle());
        tv_play_bar_artist.setText(song.getSinger());
        if(AppConstant.getInstance().getPlayingState()==AppConstant.PlayerMsg.PAUSE_MSG){
            v_play_bar_play.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_bar_btn_pause));
        }else{
            v_play_bar_play.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_bar_btn_play));
        }*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(homeReceiver);
    }

    //自定义的BroadcastReceiver，负责监听从Service传回来的广播
    public class HomeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();


            switch (action) {
                case MUSIC_CURRENT://音乐状态，进度条监听
                    int currentTime = intent.getIntExtra("currentTime", -1);
                    //Log.e("send 1","1111 "+currentTime);
                    audioSeekBar.setMax(duration);
                    audioSeekBar.setProgress(currentTime);
                    break;
                case MUSIC_DURATION://音乐时长，并入song
                    duration = intent.getIntExtra("duration", -1);
                    //Log.e("send 1", "2 " + duration + " " + song.getDuration());
                    break;
                case MUSIC_STATE:
                    int state = intent.getIntExtra("state", AppConstant.PlayerMsg.PAUSE_MSG);
                    AppConstant.getInstance().setPlayingState(state);
                    UpdateUI();
                    break;

                case UPDATE_ACTION://更新操作
                    position = intent.getIntExtra("current", -1);
                    //Log.e("send 1", "3 " + position + " " + song.getTitle() + " " + size);
                    AppConstant.getInstance().setPosotion(position);
                    UpdateUI();
                    break;
            }

        }

    }


}
