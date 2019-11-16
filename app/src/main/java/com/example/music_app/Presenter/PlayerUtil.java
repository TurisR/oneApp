package com.example.music_app.Presenter;

import android.content.Context;
import android.content.Intent;

public class PlayerUtil {

    private Context mContext;
    private final Intent intent;
    public PlayerUtil(Context context) {
        mContext=context;
        intent = new Intent(mContext,MusicService.class);
    }


    public void play(int pos){
        intent.putExtra("listPosition", pos);
        intent.putExtra("MSG", AppConstant.PlayerMsg.PLAY_MSG);//播放
        mContext.startService(intent);
        AppConstant.getInstance().setPosotion(pos);
        AppConstant.getInstance().setPlayingState(AppConstant.PlayerMsg.PLAY_MSG);
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
        intent.putExtra("MSG", AppConstant.PlayerMsg.NEXT_MSG);//下一曲
        //intent.putExtra("listPosition", AppConstant.getInstance().getPosotion());
        mContext.startService(intent);
        AppConstant.getInstance().setPlayingState(AppConstant.PlayerMsg.PLAY_MSG);
    }
    public void previous(){
        intent.putExtra("MSG", AppConstant.PlayerMsg.PREVIOUS_MSG);//下一曲
        mContext.startService(intent);
        AppConstant.getInstance().setPlayingState(AppConstant.PlayerMsg.PLAY_MSG);
    }
    public void setMode(int mode){
        intent.putExtra("Mode",mode);
        intent.putExtra("MSG", AppConstant.PlayerMsg.CHANG_MODE);//下一曲
        AppConstant.getInstance().setMode(mode);
        mContext.startService(intent);
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
