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
import android.text.style.UpdateAppearance;
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
    private boolean isPause; 		// 暂停状态
    private int duration;			//播放长度
    private Intent sendIntent;
    private int currentTime;

    private Song NowSong;

    /**
     * handler用来接收消息，来发送广播更新播放时间
     */
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what){
                case 1://进度条更新
                    if(mMediaPlayer != null) {
                        currentTime = mMediaPlayer.getCurrentPosition(); // 获取当前音乐播放的位置
                        Intent intent = new Intent();
                        intent.setAction(AppConstant.MessageType.MUSIC_CURRENT);
                        intent.putExtra("currentTime", currentTime);
                        sendBroadcast(intent); // 给PlayerActivity发送广播
                        handler.sendEmptyMessageDelayed(1, 1000);//每秒发送更新进度条
                    }
                    break;
                case 2://歌曲播放
                    sendIntent = new Intent(AppConstant.MessageType.MUSIC_STATE);
                    sendIntent.putExtra("state",AppConstant.PlayerMsg.PLAY_MSG);
                    // 发送广播，将被Activity组件中的BroadcastReurrenceiver接收到
                    sendBroadcast(sendIntent);
                    break;

                case 3://歌曲暂停
                    sendIntent = new Intent(AppConstant.MessageType.MUSIC_STATE);
                    sendIntent.putExtra("state",AppConstant.PlayerMsg.PAUSE_MSG);
                    // 发送广播，将被Activity组件中的BroadcastReurrenceiver接收到
                    sendBroadcast(sendIntent);
                    break;

                case 4://歌曲切换
                    sendIntent = new Intent(AppConstant.MessageType.UPDATE_ACTION);
                    sendIntent.putExtra("PlayingSong",NowSong);
                    // 发送广播，将被Activity组件中的BroadcastReurrenceiver接收到
                    sendBroadcast(sendIntent);
                    break;

                case 5://下一曲请求歌曲
                    sendIntent = new Intent(AppConstant.MessageType.MUSIC_NEXT);
                    // 发送广播，将被Activity组件中的BroadcastReurrenceiver接收到
                    sendBroadcast(sendIntent);
                    break;
                case 6://上一曲请求歌曲
                    sendIntent = new Intent(AppConstant.MessageType.MUSIC_PREVIOUS);
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

        mMediaPlayer = new MediaPlayer();
        /**设置音乐播放完成时的监听器
         * */


       mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
               // next_mode_play(isNext);
                handler.sendEmptyMessage(5);
            }
        });
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
               // position = intent.getIntExtra("listPosition", -1);	//当前播放歌曲的位置
                NowSong= (Song) intent.getSerializableExtra("Song");
                Log.e("play222",NowSong.getTitle());
                play(0, NowSong);
                break;
            case AppConstant.PlayerMsg.PAUSE_MSG:
                pause();
                break;
            case AppConstant.PlayerMsg.CONTINUE_MSG:
                resume();
                break;
            case AppConstant.PlayerMsg.NEXT_MSG:
                NowSong= (Song) intent.getSerializableExtra("NextSong");
                play(0, NowSong);
                break;
            case AppConstant.PlayerMsg.PREVIOUS_MSG:
                NowSong= (Song) intent.getSerializableExtra("PreviousSong");
                play(0, NowSong);
                break;

            case AppConstant.PlayerMsg.CHANGE_PRG :
                //改变歌曲的播放进度 /by:yxy
                int newMusicPrg = intent.getIntExtra("NewMusicTime", -1);
                if (newMusicPrg != -1)
                    updateMusicPrg(newMusicPrg);
                    //play(0, NowSong);
                break;
        }
        //mSongList=AppConstant.getInstance().getCurrrentSongList();
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 播放音乐
     *
     * @param
     */
    private void play(int currentTime,Song song) {
        if(song==null){
            return;
        }
        try {
            mMediaPlayer.reset();// 把各项参数恢复到初始状态
            mMediaPlayer.setDataSource(song.getFileUrl());
            mMediaPlayer.prepare(); // 进行缓冲
            mMediaPlayer.setOnPreparedListener(new PreparedListener(currentTime));// 注册一个监听器
            handler.sendEmptyMessage(1);
            handler.sendEmptyMessage(2);
            handler.sendEmptyMessage(4);
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
            intent.setAction(AppConstant.MessageType.MUSIC_DURATION);
            duration = mMediaPlayer.getDuration();
            intent.putExtra("duration", duration);	//通过Intent来传递歌曲的总长度
            sendBroadcast(intent);
        }
    }


    /**
     *  改变歌曲当前的播放进度  /by：yxy
     */
    private void updateMusicPrg(int newCurrentTime) {
        if (mMediaPlayer != null) {
            mMediaPlayer.seekTo(newCurrentTime);
            currentTime = newCurrentTime;
        }
        if (isPause) {
            mMediaPlayer.start();
            handler.sendEmptyMessage(2);
            isPause = false;
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
