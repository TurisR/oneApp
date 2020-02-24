package com.example.music_app.View.Activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.music_app.Presenter.AppConstant;
import com.example.music_app.Presenter.DataManageUtil;
import com.example.music_app.Presenter.DataManager;
import com.example.music_app.Presenter.PlayerUtil;
import com.example.music_app.R;
import com.example.music_app.View.Fragment.ContentPagerManager;
import com.example.music_app.View.widget.CustomDialog;
import com.example.music_app.mould.Model.bean.Song;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TabLayout mTabTl;
    private ViewPager mContentVp;
    private TextView tv_play_bar_title;
    private boolean isPressSeekBar = false; //判断是否在拖动拖动条
    private int duration;
    private TextView tv_play_bar_artist;
    public ImageView v_play_bar_play;
    public ContentPagerManager mContentPagerManager;
    public static SeekBar audioSeekBar = null;
    private Song song;
    private PlayerUtil mPlayerUtil;
    private DataManager dataManager;
    private DataManageUtil manageUtil;

    private HomeReceiver homeReceiver;	//自定义的广播接收器
    private ImageView iv_play_bar_cover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();

      // requestAllPower();
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
        filter.addAction(AppConstant.MessageType.MUSIC_NEXT);
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

       //Toast.makeText(this,dataManager.getListName("listName"),Toast.LENGTH_SHORT).show();
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

        dataManager = new DataManager(this);
        dataManager.initData();
        manageUtil=new DataManageUtil(this);

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
                Song song=AppConstant.getInstance().getSwitchSong().getNextSong(true);
                Log.e("next",song.getTitle()+" "+AppConstant.getInstance().getMode());
                mPlayerUtil.next();
                break;
            case R.id.iv_play_bar_play:
                if(AppConstant.getInstance().getPlayingState()==AppConstant.PlayerMsg.PAUSE_MSG){
                  mPlayerUtil.resume();
                }else{
                  mPlayerUtil.pause();
                }
                break;
            case R.id.v_play_bar_playlist:
                Intent intent =new Intent(MainActivity.this, ShowListActivity.class);
                intent.putExtra("MUSIC_TYPE",AppConstant.DataType.CURRENT_MUSIC); // 传字符串, 更多传值方法
                startActivity(intent);
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

        if(AppConstant.getInstance().getPlayingState()==AppConstant.PlayerMsg.PAUSE_MSG){
            v_play_bar_play.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_bar_btn_pause));
            tv_play_bar_title.setSelected(false);
        }else{
            v_play_bar_play.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_bar_btn_play));
            tv_play_bar_title.setSelected(true);
        }
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("start","2");
    }

    @Override
    protected void onResume() {
        super.onResume();
         requestAllPower();
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
                    if (!isPressSeekBar&&song!=null) {
                        //当进度条未被拖动时，自动更新进度条进度
                        audioSeekBar.setProgress(audioSeekBar.getMax() * currentTime / song.getDuration());    //更新进度条
                    }
                    break;
                case AppConstant.MessageType.MUSIC_DURATION://音乐时长，并入song
                    duration = intent.getIntExtra("duration", -1);
                    Log.e("MUSIC_DURATION","0"+duration);
                    break;
                case AppConstant.MessageType.MUSIC_STATE:
                    int state = intent.getIntExtra("state", AppConstant.PlayerMsg.PAUSE_MSG);
                    AppConstant.getInstance().setPlayingState(state);
                    Log.e("MUSIC_STATE",""+state);
                    UpdateUI();
                    break;

                case AppConstant.MessageType.UPDATE_ACTION://更新操作换歌
                    Log.e("UPDATE_ACTION","接受到更新广播");
                    song=(Song) intent.getSerializableExtra("PlayingSong");
                    AppConstant.getInstance().setPlayingSong(song);
                    AppConstant.getInstance().addListSong(AppConstant.DataType.RECENT_MUSIC,song);
                    UpdateUI();
                    break;

                case AppConstant.MessageType.MUSIC_NEXT:
                    mPlayerUtil.next();
                    Log.e("MUSIC_NEXT","next");
                    break;
            }

        }

    }

    public void requestAllPower() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                // 检查权限状态
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    //  用户彻底拒绝授予权限，一般会提示用户进入设置权限界面
                    getAllPower();

                } else {
                    //  用户未彻底拒绝授予权限
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    //ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                }
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PERMISSION_GRANTED) {
                    // 申请成功
                    Toast.makeText(MainActivity.this, "权限请求成功", Toast.LENGTH_LONG).show();
                } else {
                    // 申请失败
                    Toast.makeText(MainActivity.this, "权限请求失败，可在系统设置授权", Toast.LENGTH_LONG).show();
                }
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
                 if (duration==progress){
                     mPlayerUtil.next();
                 }
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


    @Override
    protected void onStop() {
        super.onStop();
        dataManager.upDate();
        Gson gson = new Gson();
        //转换成json数据，再保存
        String strJson1 = gson.toJson(AppConstant.getInstance().getListNumber(AppConstant.DataType.RECENT_MUSIC));
       // Toast.makeText(this,"stop"+strJson1,Toast.LENGTH_LONG).show();
        //manageUtil.saveData(AppConstant.DataType.RECENT_MUSIC,AppConstant.getInstance().getListNumber(AppConstant.DataType.RECENT_MUSIC));


    }

    private void getAllPower() {
        CustomDialog customDialog=new CustomDialog(this,R.style.CustomDialog);
        customDialog.setType(0).setTitle("权 限 请 求").setContent("为保证程序正常运行，是否给予“存储读写”权限？").setCancel(new CustomDialog.InOnCancelListener() {
            @Override
            public void onCancel(CustomDialog customDialog) {
                customDialog.dismiss();
                Toast.makeText(MainActivity.this, "权限请求失败，可在系统设置授权", Toast.LENGTH_LONG).show();

            }
        }).setConfirm(new CustomDialog.InOnConfirmListener() {
            @Override
            public void onConfirm(CustomDialog customDialog) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                Toast.makeText(MainActivity.this, "权限请求成功", Toast.LENGTH_LONG).show();
                customDialog.dismiss();
            }
        }).show();
    }
}
