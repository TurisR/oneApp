package com.example.music_app.View.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
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
import com.example.music_app.View.Fragment.MusicListFragment;
import com.example.music_app.mould.Model.bean.Song;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, MusicListFragment.CallBackValue {
    private TabLayout mTabTl;
    private ViewPager mContentVp;
    private ArrayList<Fragment> tabFragments;
    private TextView tv_play_bar_title;
    private boolean isPressSeekBar = false; //判断是否在拖动拖动条
    private int duration;
    private int NowFg=0;
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

    private HomeReceiver homeReceiver;	//自定义的广播接收器
    private ImageView iv_play_bar_cover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();


    }

    private void initData() {
        //song = (Song) getIntent().getSerializableExtra("songInfo");//获得歌曲信息
        homeReceiver = new HomeReceiver();
        // 创建IntentFilter
        IntentFilter filter = new IntentFilter();
        // 指定BroadcastReceiver监听的Action
        filter.addAction(AppConstant.MessageType.MUSIC_CURRENT);
        filter.addAction(AppConstant.MessageType.UPDATE_ACTION);
        filter.addAction(AppConstant.MessageType.MUSIC_STATE);
        filter.addAction(AppConstant.MessageType.MUSIC_DURATION);
        // 注册BroadcastReceiver
        registerReceiver(homeReceiver, filter);

        /*绑定service*/
        mPlayerUtil=new PlayerUtil(this);
        // bindService(MediaServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
        initSeekBar();
        initTab();
        mTabTl.setupWithViewPager(mContentVp);
        mContentPagerManager=new ContentPagerManager(getSupportFragmentManager());
        mContentVp.setAdapter(mContentPagerManager);


    }

    private void initView() {
        mTabTl=findViewById(R.id.top_tab);
        mContentVp=findViewById(R.id.top_viewPage);
        tv_play_bar_title = findViewById(R.id.tv_play_bar_title);
        tv_play_bar_artist = findViewById(R.id.tv_play_bar_artist);
        v_play_bar_play = findViewById(R.id.iv_play_bar_play);
        v_play_bar_play.setOnClickListener(this);
        audioSeekBar = findViewById(R.id.pb_play_bar);
        iv_play_bar_cover=findViewById(R.id.iv_play_bar_cover);
        findViewById(R.id.iv_play_bar_next).setOnClickListener(this);
        findViewById(R.id.v_play_bar_playlist).setOnClickListener(this);
        findViewById(R.id.main_to_play_bottom).setOnClickListener(this);
        iv_play_bar_cover.setOnClickListener(this);
        if(song==null&&AppConstant.getInstance().getPlayingSong()!=null){
            song=AppConstant.getInstance().getPlayingSong();
            tv_play_bar_title.setText(song.getTitle());
            tv_play_bar_artist.setText(song.getSinger());
        }
        tv_play_bar_title.setSelected(true);
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
                //mContentPagerManager.notifyDataSetChanged();
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
                View view = tab.getCustomView();
                if (null == view) {
                    tab.setCustomView(R.layout.tab_item);
                }
                TextView textView = tab.getCustomView().findViewById(android.R.id.text1);
                textView.setTextAppearance(MainActivity.this, R.style.TabLayoutTextSize_two);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_play_bar_next:
                mPlayerUtil.next();
                break;

            case R.id.iv_play_bar_play:
                if(AppConstant.getInstance().getPlayingState()==AppConstant.PlayerMsg.PAUSE_MSG){
                  v_play_bar_play.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_bar_btn_play));
                  mPlayerUtil.resume();
                  tv_play_bar_title.setSelected(true);
                }else{
                   v_play_bar_play.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_bar_btn_pause));
                    mPlayerUtil.pause();
                   tv_play_bar_title.setSelected(false);
                }
                break;

            case R.id.v_play_bar_playlist:
                startActivity(new Intent(MainActivity.this,PlayinglistActivity.class));
                overridePendingTransition(R.anim.push_bottom_in,R.anim.push_bottom_out);
                break;


            case R.id.iv_play_bar_cover:
                startActivity(new Intent(MainActivity.this, PlayViewActivity.class));
                break;
        }

    }

    private void UpdateUI() {//更新UI
        song=AppConstant.getInstance().getPlayingSong();
        if(song!=null){
            tv_play_bar_title.setText(song.getTitle());
            tv_play_bar_artist.setText(song.getSinger());
        }

        NowFg=mContentVp.getCurrentItem();
        if(mContentPagerManager!=null){
           // mContentPagerManager.notifyDataSetChanged();
        }


        if(AppConstant.getInstance().getPlayingState()==AppConstant.PlayerMsg.PAUSE_MSG){
            v_play_bar_play.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_bar_btn_pause));
            tv_play_bar_title.setSelected(false);
        }else{
            v_play_bar_play.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_bar_btn_play));
            tv_play_bar_title.setSelected(true);
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
        Log.e("resume","111111");
        UpdateUI();
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
                case AppConstant.MessageType.MUSIC_CURRENT://音乐状态，进度条监听
                    int currentTime = intent.getIntExtra("currentTime", -1);
                   // Log.e("time","  "+currentTime);
                    if (!isPressSeekBar&&song!=null) {
                        //当进度条未被拖动时，自动更新进度条进度
                        audioSeekBar.setProgress(audioSeekBar.getMax() * currentTime / song.getDuration());    //更新进度条
                    }
                    break;
                case AppConstant.MessageType.MUSIC_DURATION://音乐时长，并入song
                    duration = intent.getIntExtra("duration", -1);
                    Log.e("duration","0"+duration);
                    break;
                case AppConstant.MessageType.MUSIC_STATE:
                    System.out.println("———MUSIC_STATE———");
                    int state = intent.getIntExtra("state", AppConstant.PlayerMsg.PAUSE_MSG);
                    position = intent.getIntExtra("current", -1);
                    AppConstant.getInstance().setPlayingState(state);
                    Log.e("state","00000"+state+" "+position);
                    UpdateUI();
                    break;

                case AppConstant.MessageType.UPDATE_ACTION://更新操作换歌
                    System.out.println("———接受到更新广播———");
                    position = intent.getIntExtra("current", -1);
                    AppConstant.getInstance().setPosotion(position);
                    Log.e("time","  "+ position);
                    UpdateUI();
                    break;
            }

        }

    }


    /**
     *  初始化拖动条
     */
    private void initSeekBar() {
        //设置拖动监听
        audioSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isPressSeekBar = true;      //当拖动进度条时，进度条停止自动更新
                mPlayerUtil.pause();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();   //拖动条停止位置
                int seekBarMax = seekBar.getMax();      //拖动条最大数值
                int songMax = song.getDuration();       //歌曲总时长
                mPlayerUtil.updateMusicPrg(songMax * progress / seekBarMax);      //更新播放进度
                isPressSeekBar = false;     //拖动进度条结束时，开始自动更新进度条
            }
        });
    }

}
