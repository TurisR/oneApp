package com.example.music_app.Presenter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.music_app.mould.Model.bean.Song;

import java.io.Serializable;
/**
 * @description:PlayerUtil类，负责MusicService的onStartCommand的调用，封装MadiaPlayer对歌曲的操作
 * @author: JiangJiaHui
 * @createDate: 2019/11/25
 * @Modified By：
 * @version: 1.0
 */


public class PlayerUtil {

    private Context mContext;
    private final Intent intent;
    public PlayerUtil(Context context) {
        mContext=context;
        intent = new Intent(mContext,MusicService.class);
    }

    /**
     * 歌曲播放
     * @param song
     */
    public void play(Song song){
        intent.putExtra("Song", song);
        intent.putExtra("MSG", AppConstant.PlayerMsg.PLAY_MSG);//播放
        mContext.startService(intent);
        Log.e("play_music",song.getTitle());
    }

    /**
     * 歌曲暂停
     */
    public void pause(){
        intent.putExtra("MSG", AppConstant.PlayerMsg.PAUSE_MSG);//停止播放
        mContext.startService(intent);
        AppConstant.getInstance().setPlayingState(AppConstant.PlayerMsg.PAUSE_MSG);
    }
    /**
     * 歌曲恢复播放
     */
    public void resume(){
        intent.putExtra("MSG", AppConstant.PlayerMsg.CONTINUE_MSG);//播放
        mContext.startService(intent);
        AppConstant.getInstance().setPlayingState(AppConstant.PlayerMsg.PLAY_MSG);
    }
    /**
     * 下一曲
     */
    public void next(){
        Song song=AppConstant.getInstance().getSwitchSong().getNextSong(true);
        play(song);
        AppConstant.getInstance().setPlayingSong(song);
    }

    /**
     * 播放模式
     */
    public void previous(){
        Song song=AppConstant.getInstance().getSwitchSong().getNextSong(false);
        play(song);
        AppConstant.getInstance().setPlayingSong(song);
    }
    /**
     * 歌曲暂停
     */
    public void setMode(int mode){
        AppConstant.getInstance().setMode(mode);
    }
    /**
     *  更新歌曲播放进度
     *  by:yxy
     */
    public void updateMusicPrg(int newCurrentTime) {
        intent.putExtra("NewMusicTime", newCurrentTime);
        intent.putExtra("MSG", AppConstant.PlayerMsg.CHANGE_PRG);
        mContext.startService(intent);
    }

    //开启摇摇切歌
    public void openSensor() {
        intent.putExtra("MSG", AppConstant.PlayerMsg.SENSOR_OPN);
        mContext.startService(intent);
        AppConstant.getInstance().setSensorState(true);
    }

    //关闭摇摇切歌
    public void closeSensor() {
        intent.putExtra("MSG", AppConstant.PlayerMsg.SENSOR_CLS);
        mContext.startService(intent);
        AppConstant.getInstance().setSensorState(false);
    }
}
