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
        Log.e("play",song.getTitle());
        AppConstant.getInstance().setPlayingState(AppConstant.PlayerMsg.PLAY_MSG);
        AppConstant.getInstance().setPlayingSong(song);
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
        intent.putExtra("MSG", AppConstant.PlayerMsg.NEXT_MSG);//下一曲
        intent.putExtra("NextSong", song);
        //AppConstant.getInstance().setPlayingState(AppConstant.PlayerMsg.PLAY_MSG);
        mContext.startService(intent);
    }
    public void previous(){
        Song song=AppConstant.getInstance().getSwitchSong().getNextSong(false);
        intent.putExtra("MSG", AppConstant.PlayerMsg.PREVIOUS_MSG);//下一曲
        intent.putExtra("PreviousSong", song);
       // AppConstant.getInstance().setPlayingState(AppConstant.PlayerMsg.PLAY_MSG);
        mContext.startService(intent);
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
