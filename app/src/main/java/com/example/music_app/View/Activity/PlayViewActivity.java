package com.example.music_app.View.Activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.music_app.Presenter.AppConstant;
import com.example.music_app.Presenter.MusicService;
import com.example.music_app.Presenter.PlayerUtil;
import com.example.music_app.R;
import com.example.music_app.mould.Model.bean.Song;

/**
 *  內容：播放界面Activity
 *  编写：yxy
 *  版本: 1.0
 *  时间：2019/11/15
 */

public class PlayViewActivity extends Activity implements View.OnClickListener {
    private Song song;
    private PlayerUtil mPlayerUtil;

    private TextView songNameText;  //歌曲名称显示
    private TextView singerText;    //歌手名字显示
    private TextView lyricText;     //歌词显示

    private TextView prgTime;       //歌曲进度时间
    private TextView wholeTime;     //歌曲总时长

    private ImageView playBtn;      //播放、暂停按键

    private SeekBar seekBar;        //拖动条
    private boolean isPressSeekBar = false; //判断是否在拖动拖动条

    //一系列动作
    public static final String UPDATE_ACTION= "com.example.music_app.UPDATE_ACTION";		//更新动作
    public static final String MUSIC_CURRENT = "com.action.MUSIC_CURRENT";		//当前音乐改变动作
    public static final String REPEAT_ACTION = "com.action.REPEAT_ACTION";		//音乐重复改变动作
    public static final String SHUFFLE_ACTION = "com.action.SHUFFLE_ACTION";	//音乐随机播放动作
    private ServiceReceiver serviceReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_view);

        mPlayerUtil = new PlayerUtil(this);

        initView();     //初始化界面显示
        initBottom();   //初始界面按键监听
        initSeekBar();  //初始化拖动条
        initReceiver(); //设置接受广播

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //退出界面时，注销广播接受器
        unregisterReceiver(serviceReceiver);
    }

    /**
     * 初始化界面
     */
    private void initView() {
        songNameText = findViewById(R.id.activity_play_song_name);  //顶部歌名显示
        singerText = findViewById(R.id.activity_play_song_singer);  //顶部歌手显示
        lyricText = findViewById(R.id.activity_play_lyric);         //歌词显示
        prgTime = findViewById(R.id.activity_play_progress_time);   //歌曲播放进度时间
        wholeTime = findViewById(R.id.activity_play_song_time);     //歌曲总时间

        if(song == null && AppConstant.getInstance().getPlayingSong() != null){
            //获取当前正在播放的歌曲
            song = AppConstant.getInstance().getPlayingSong();
            //显示歌名、歌手。
            songNameText.setText(song.getTitle());
            singerText.setText("- " + song.getSinger() + " -");
            //显示歌曲总时长
            wholeTime.setText(transformTime(song.getDuration()));
        }
    }

    /**
     * 初始化界面按键监听
     */
    private void initBottom() {
        findViewById(R.id.activity_play_return_main).setOnClickListener(this); //返回主界面按键
        findViewById(R.id.activity_play_list).setOnClickListener(this);        //菜单按键
        findViewById(R.id.activity_play_last).setOnClickListener(this);        //上一曲按键
        findViewById(R.id.activity_play_next).setOnClickListener(this);        //下一曲按键

        //播放、暂停按键
        playBtn = findViewById(R.id.activity_play_start);
        playBtn.setOnClickListener(this);
        if(AppConstant.getInstance().getPlayingState() == AppConstant.PlayerMsg.PAUSE_MSG) {
            //若未播放歌曲，设置该按键为 播放按键
            playBtn.setImageDrawable(getResources().getDrawable(R.drawable.play_btn_play_selector));
        }else {
            playBtn.setImageDrawable(getResources().getDrawable(R.drawable.play_btn_pause_selector));
        }
    }

    /**
     *  按键操作
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_play_return_main :
                //退出播放界面
                finish();
                break;

            case R.id.activity_play_list :
                //播放列表
                break;

            case R.id.activity_play_start :
                if (AppConstant.getInstance().getPlayingState() == AppConstant.PlayerMsg.PAUSE_MSG) {
                    //播放
                    mPlayerUtil.resume();
                    playBtn.setImageDrawable(getResources().getDrawable(R.drawable.play_btn_pause_selector));
                } else {
                    //暂停
                    mPlayerUtil.pause();
                    playBtn.setImageDrawable(getResources().getDrawable(R.drawable.play_btn_play_selector));
                }
                break;

            case R.id.activity_play_last :
                //上一曲
                mPlayerUtil.previous();
                break;

            case R.id.activity_play_next :
                //下一曲
                mPlayerUtil.next();
                break;
        }
    }

    /**
     *  初始化拖动条
     */
    private void initSeekBar() {
        seekBar = findViewById(R.id.activity_play_seekBar);

        //设置拖动监听
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //拖动进度条时，歌曲进度时间 同步显示进度条所指向的时间
                    int seekBarMax = seekBar.getMax();
                    int songMax = song.getDuration();
                    prgTime.setText(transformTime(songMax * progress / seekBarMax));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isPressSeekBar = true;      //当拖动进度条时，进度条停止自动更新
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

    /**
     *  设置接受MusicService发送的广播
     */
    private void initReceiver() {
        serviceReceiver = new ServiceReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(MUSIC_CURRENT);
        filter.addAction(UPDATE_ACTION);
        registerReceiver(serviceReceiver, filter);
    }

    /**
     *  自定义的BroadcastReceiver， 负责监听MusicService传来的消息
     */
    class ServiceReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            switch (action) {
                case MUSIC_CURRENT :
                    //音乐状态，进度条监听
                    int currentTime = intent.getIntExtra("currentTime", -1);   //获取从MusicService传来的歌曲进度时间
                    if (!isPressSeekBar) {
                        //当进度条未被拖动时，自动更新进度条进度
                        seekBar.setProgress(seekBar.getMax() * currentTime / song.getDuration());    //更新进度条
                    }
                    break;

                case UPDATE_ACTION :
                    //当切歌时，更新UI
                    int position = intent.getIntExtra("current", -1);    //获取当前正在播放的歌曲
                    System.out.println("---播放界面收到广播---");
                    if (position != -1) {
                        AppConstant.getInstance().setPosotion(position);
                        UpdateUI();       //更新UI界面的歌曲信息
                    }
                    break;
            }
        }
    }

    /**
     *  界面更新： 当歌曲切换时进行
     */
    private void UpdateUI() {
        song = AppConstant.getInstance().getPlayingSong();
        if (song != null) {
            songNameText.setText(song.getTitle());      //更新歌名
            singerText.setText("- " + song.getSinger() + " -");       //更新歌手
            wholeTime.setText(transformTime(song.getDuration()));   //更新歌曲总时长
        }

        //更新播放按键显示
        if (AppConstant.getInstance().getPlayingState() == AppConstant.PlayerMsg.PAUSE_MSG) {
            playBtn.setImageDrawable(getResources().getDrawable(R.drawable.play_btn_play_selector));
        }else{
            playBtn.setImageDrawable(getResources().getDrawable(R.drawable.play_btn_pause_selector));
        }
    }

    /**
     *  时间格式转化函数
     */
    public String transformTime(int time){
        String t = "";
        time /= 1000;

        //分
        int tt = time / 60;
        if(tt < 10)     t += "0";
        t += tt + ":";

        //秒
        tt = time % 60;
        if (tt < 10)    t += "0";
        t = t + tt;

        return t;
    }

}
