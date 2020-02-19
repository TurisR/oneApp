package com.example.music_app.Presenter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.music_app.mould.Model.bean.Song;

import java.io.Serializable;

public class PlayerUtil {

    private Context mContext;
    private final Intent intent;
    public PlayerUtil(Context context) {
        mContext=context;
        intent = new Intent(mContext,MusicService.class);
    }
    public void play(Song song){
        intent.putExtra("Song", song);
        intent.putExtra("MSG", AppConstant.PlayerMsg.PLAY_MSG);//播放
        mContext.startService(intent);
        Log.e("play_music",song.getTitle());
    }
    public void pause(){
        intent.putExtra("MSG", AppConstant.PlayerMsg.PAUSE_MSG);//停止播放
        mContext.startService(intent);
        AppConstant.getInstance().setPlayingState(AppConstant.PlayerMsg.PAUSE_MSG);
    }
    public void resume(){
        intent.putExtra("MSG", AppConstant.PlayerMsg.CONTINUE_MSG);//播放
        mContext.startService(intent);
        AppConstant.getInstance().setPlayingState(AppConstant.PlayerMsg.PLAY_MSG);
    }

    public void next(){
        Song song=AppConstant.getInstance().getSwitchSong().getNextSong(true);
        play(song);
    }
    public void previous(){
        Song song=AppConstant.getInstance().getSwitchSong().getNextSong(false);
        play(song);
    }
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


}
