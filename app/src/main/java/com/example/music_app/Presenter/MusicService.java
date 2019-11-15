package com.example.music_app.Presenter;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.example.music_app.View.Activity.MainActivity;
import com.example.music_app.mould.Model.Model;
import com.example.music_app.mould.Model.bean.Song;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MusicService extends Service {


    /* 定于一个多媒体对象 */
    public static MediaPlayer mMediaPlayer = null;
    // 用户操作
    private int msg;             //操作信息
    private static List<Song> mSongList=new ArrayList<>();
    private int position = -1;
    private boolean isPause; 		// 暂停状态
    private int duration;			//播放长度
    private String path; 			// 音乐文件路径
    private boolean isNext=true;
    private Intent sendIntent;
    private int currentTime;
    private int status = 2;			//播放状态，默认为顺序播放

    //服务要发送的一些Action
    public static final String UPDATE_ACTION = "com.example.music_app.UPDATE_ACTION";	//更新动作
    public static final String MUSIC_CURRENT = "com.action.MUSIC_CURRENT";	//当前音乐播放时间更新动作
    public static final String MUSIC_DURATION = "com.action.MUSIC_DURATION";//新音乐长度更新动作
    public static final String MUSIC_STATE = "com.example.music_app.MUSIC_STATE";			//播放器状态 播放|暂停

    /**
     * handler用来接收消息，来发送广播更新播放时间
     */
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {

            switch (msg.what){
                case 1:
                    if(mMediaPlayer != null) {
                        currentTime = mMediaPlayer.getCurrentPosition(); // 获取当前音乐播放的位置
                        Intent intent = new Intent();
                        intent.setAction(MUSIC_CURRENT);
                        intent.putExtra("currentTime", currentTime);
                        sendBroadcast(intent); // 给PlayerActivity发送广播
                        handler.sendEmptyMessageDelayed(1, 1000);//每秒发送更新进度条
                    }
                    break;
                case 2:
                    sendIntent = new Intent(MUSIC_STATE);
                    sendIntent.putExtra("current", position);
                    sendIntent.putExtra("state",AppConstant.PlayerMsg.PLAY_MSG);
                    // 发送广播，将被Activity组件中的BroadcastReurrenceiver接收到
                    sendBroadcast(sendIntent);
                    break;

                case 3:
                    sendIntent = new Intent(MUSIC_STATE);
                    sendIntent.putExtra("current", position);
                    sendIntent.putExtra("state",AppConstant.PlayerMsg.PAUSE_MSG);
                    // 发送广播，将被Activity组件中的BroadcastReurrenceiver接收到
                    sendBroadcast(sendIntent);
                    break;

                case 4:
                    sendIntent = new Intent(UPDATE_ACTION);
                    sendIntent.putExtra("current", position);
                    sendIntent.putExtra("song",mSongList.get(position));
                    // 发送广播，将被Activity组件中的BroadcastReurrenceiver接收到
                    sendBroadcast(sendIntent);
                    break;
            }
        };
    };



    @Override
    public void onCreate() {
        super.onCreate();
        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        //mSongList=Model.getInstance().getDBMananger().getSongDao().getSonglist();
        mSongList=AppConstant.getInstance().getSongList();
        mMediaPlayer = new MediaPlayer();
        /**
         * 设置音乐播放完成时的监听器
         */
       mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                next_mode_play(isNext);
            }
        });



    }

    private void next_mode_play(boolean state) {//state判断是下一曲还是上一曲
        switch (status){
            case 0: mMediaPlayer.start();//单曲播放
                    break;
            case 1: //循环播放
                if(state){
                    position++;
                    if(position > mSongList.size() - 1) {	//变为第一首的位置继续播放
                        position = 0;
                    }
                }else{
                    position--;
                    if(position<0){
                        position=mSongList.size()-1;
                    }
                }
                path = mSongList.get(position).getFileUrl();
                play(0);
                break;
            case 2://循序播放
                if(state){
                    next();
                }else{
                    previous();
                }
                break;
            case 3://随机播放
                position = getRandomIndex(mSongList.size() - 1);
                path = mSongList.get(position).getFileUrl();
                play(0);
                break;

        }
       handler.sendEmptyMessage(4);
    }

    /**
     * 获取随机位置
     * @param end
     * @return
     */
    protected int getRandomIndex(int end) {
        int index = (int) (Math.random() * end);
        return index;
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        msg = intent.getIntExtra("MSG", AppConstant.PlayerMsg.STOP_MSG);			//播放信息
        switch (msg){
            case AppConstant.PlayerMsg.PLAY_MSG:
                position = intent.getIntExtra("listPosition", -1);	//当前播放歌曲的位置
                if(position!=-1){
                    path=mSongList.get(position).getFileUrl();
                }
                play(0);
                break;
            case AppConstant.PlayerMsg.PAUSE_MSG:
                pause();
                break;
            case AppConstant.PlayerMsg.CONTINUE_MSG:
                resume();
                break;
            case AppConstant.PlayerMsg.NEXT_MSG:
                next_mode_play(isNext);
                break;
            case AppConstant.PlayerMsg.PREVIOUS_MSG:
                next_mode_play(!isNext);
                break;
            case AppConstant.PlayerMsg.CHANG_MODE:
                ChangeMode(intent);
                break;
        }
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 播放音乐
     *
     * @param
     */
    private void play(int currentTime) {
        if(path==null){
            return;
        }
        try {
            mMediaPlayer.reset();// 把各项参数恢复到初始状态
            mMediaPlayer.setDataSource(path);
            mMediaPlayer.prepare(); // 进行缓冲
            mMediaPlayer.setOnPreparedListener(new PreparedListener(currentTime));// 注册一个监听器
            handler.sendEmptyMessage(1);
            handler.sendEmptyMessage(2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 暂停音乐
     */
    private void pause() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            handler.sendEmptyMessage(3);
            isPause = true;
        }
    }

    private void resume() {
        if (isPause) {
            mMediaPlayer.start();
            handler.sendEmptyMessage(2);
            isPause = false;
        }
    }

   /**
    *  上一首
    */
    private void previous() {
        if (position > 0) {
            position--;
        } else {
            position = mSongList.size() - 1;
        }
        path=mSongList.get(position).getFileUrl();
        play(0);
//        sendIntent = new Intent(UPDATE_ACTION);
//        sendIntent.putExtra("current", position);
//        sendIntent.putExtra("song",mSongList.get(position));
        // 发送广播，将被Activity组件中的BroadcastReurrenceiver接收到
        sendBroadcast(sendIntent);
    }

    /**
     * 下一首
     */
    private void next() {

        if (position < mSongList.size()-1){
            position++;
        } else {
            position = 0;
        }
        path=mSongList.get(position).getFileUrl();
        play(0);
    }

    /**
     * 停止音乐
     */
    private void stop() {
        if (mMediaPlayer != null) {
           mMediaPlayer.stop();
            try {
                mMediaPlayer.prepare(); // 在调用stop后如果需要再次通过start进行播放,需要之前调用prepare函数
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * 实现一个OnPrepareLister接口,当音乐准备好的时候开始播放
     *
     */
    private final class PreparedListener implements  OnPreparedListener {
        private int currentTime;

        public PreparedListener(int currentTime) {
            this.currentTime = currentTime;
        }

        @Override
        public void onPrepared(MediaPlayer mp) {
            mMediaPlayer.start(); // 开始播放
            if (currentTime > 0) { // 如果音乐不是从头播放
                mMediaPlayer.seekTo(currentTime);
            }
            Intent intent = new Intent();
            intent.setAction(MUSIC_DURATION);
            duration = mMediaPlayer.getDuration();
            intent.putExtra("duration", duration);	//通过Intent来传递歌曲的总长度
            sendBroadcast(intent);
        }
    }


    private void ChangeMode(Intent intent) {
        int control = intent.getIntExtra("Mode", 3);
        switch (control) {
            case 0:
                status = 0; // 将播放状态置为1表示：单曲循环
                break;
            case 1:
                status = 1;	//将播放状态置为2表示：全部循环
                break;
            case 2:
                status = 2;	//将播放状态置为3表示：顺序播放
                break;
            case 3:
                status = 3;	//将播放状态置为4表示：随机播放
                break;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }

        System.out.println("service onDestroy");
    }



}
