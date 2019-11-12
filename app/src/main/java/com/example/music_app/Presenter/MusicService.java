package com.example.music_app.Presenter;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.example.music_app.View.Activity.MainActivity;
import com.example.music_app.mould.Model.Model;
import com.example.music_app.mould.Model.bean.Song;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MusicService extends Service {


    private static  String PATH = null ;
    /* 定于一个多媒体对象 */
    public static MediaPlayer mMediaPlayer = null;
    // 是否单曲循环
    private static boolean isLoop = false;
    // 用户操作
    private int MSG;

    private List<Song> mSongList=new ArrayList<>();
    private int position;

    private Song song;

    @Override
    public IBinder onBind(Intent intent) {
        return null;// 这里的绑定没的用，上篇我贴出了如何将activity与service绑定的代码
    }

    @Override
    public void onCreate() {

        super.onCreate();
        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        mMediaPlayer = new MediaPlayer();
        /* 监听播放是否完成 */
      //  mMediaPlayer.setOnCompletionListener((MediaPlayer.OnCompletionListener) this);
        System.out.println("Service---onCreate----");
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
    /*启动service时执行的方法*/
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        /*得到从startService传来的动作，后是默认参数，这里是我自定义的常量*/
        MSG = intent.getIntExtra("MSG", AppConstant.PlayerMag.PLAY_MAG);
        mSongList=Model.getInstance().getDBMananger().getSongDao().getSonglist();
        position=intent.getIntExtra("position",-1);
        PATH=intent.getStringExtra("songInfo");
       /* Bundle bundle=intent.getExtras();
        song = (Song) bundle.getSerializable("PlaySong");//获得歌曲信息
        if (song!=null){
            PATH=intent.getStringExtra("songInfo");
            Log.e("position"+mSongList.indexOf(song),"songTitle"+song.getTitle());

        }*/
        System.out.println("Service---onCreate----p2"+position);
        switch (MSG){
            case AppConstant.PlayerMag.NEXT:
                if(position!=-1&&position>=0&&position<mSongList.size()){
                    playMusic(mSongList.get(position+1).getFileUrl());
                }
                AppConstant.getInstance().setPlayingState(AppConstant.PlayerMag.NEXT);
                break;
            case AppConstant.PlayerMag.PLAY_MAG:
                if(PATH!=null){
                    playMusic(PATH);
                }
                AppConstant.getInstance().setPlayingState(AppConstant.PlayerMag.PLAY_MAG);
                break;
            case AppConstant.PlayerMag.PAUSE:
                if (mMediaPlayer.isPlaying()) {// 正在播放
                    mMediaPlayer.pause();// 暂停
                    AppConstant.getInstance().setPlayingState(AppConstant.PlayerMag.PAUSE);
                } else {// 没有播放
                    mMediaPlayer.start();
                    AppConstant.getInstance().setPlayingState(AppConstant.PlayerMag.PLAY_MAG);
                }

                break;
        }


       /* if(PATH!=null){
           if (MSG == AppConstant.PlayerMag.PLAY_MAG) {
                playMusic(PATH);
                System.out.println("Service---onCreate----playing1");
           }
            if (MSG == AppConstant.PlayerMag.PAUSE) {
                if (mMediaPlayer.isPlaying()) {// 正在播放
                    mMediaPlayer.pause();// 暂停
                } else {// 没有播放
                    mMediaPlayer.start();
                }
                System.out.println("Service---onCreate----playing22");
            }

        }*/
        return super.onStartCommand(intent, flags, startId);
    }

    @SuppressWarnings("static-access")
    public void playMusic(String path) {
        try {
            /* 重置多媒体 */
            mMediaPlayer.reset();
            /* 读取mp3文件 */
            mMediaPlayer.setDataSource(path);
            /* 准备播放 */
            mMediaPlayer.prepare();
            /* 开始播放 */
            mMediaPlayer.start();
            /* 是否单曲循环 */
            mMediaPlayer.setLooping(isLoop);
            // 设置进度条最大值
           MainActivity.audioSeekBar.setMax(MusicService.mMediaPlayer.getDuration());
           new Thread(new Runnable() {
               @Override
               public void run() {
                   SeekBar();
               }
           }).start();
            System.out.println("Service---onCreate----playing222"+MusicService.mMediaPlayer.getDuration());
        } catch (IOException e) {
        }

    }

    public void SeekBar() {
        int CurrentPosition = 0;// 设置默认进度条当前位置
        int total = mMediaPlayer.getDuration();//
        while (mMediaPlayer != null && CurrentPosition < total) {
            try {
                Thread.sleep(1000);
                if (mMediaPlayer != null) {
                    CurrentPosition = mMediaPlayer.getCurrentPosition();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            MainActivity.audioSeekBar.setProgress(CurrentPosition);
        }

    }

    public void onCompletion(MediaPlayer mp,int position) {
        //播放完当前歌曲，自动播放下一首

        if (position >= mSongList.size()) {
            Toast.makeText(MusicService.this, "已到最后一首歌曲", Toast.LENGTH_SHORT)
                    .show();
            position--;
            MainActivity.audioSeekBar.setMax(0);
        } else {
            playMusic(mSongList.get(position).getFileUrl());
        }
    }

}
