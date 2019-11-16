package com.example.music_app.Presenter;

import com.example.music_app.mould.Model.Model;
import com.example.music_app.mould.Model.bean.Song;

import java.util.ArrayList;
import java.util.List;

public class AppConstant {

    private int playingState=0;
    private int posotion=-1;

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    private int mode=2;


    public int getPosotion() {
        return posotion;
    }

    public void setPosotion(int posotion) {
        this.posotion = posotion;
        setPlayingSong(getSongList().get(posotion));
    }

    private List<Song> mSongList=new ArrayList<>();
    private Song playingSong;

    public Song getPlayingSong() {
        return playingSong;
    }

    public void setPlayingSong(Song playingSong) {
        this.playingSong = playingSong;
    }

    public List<Song> getSongList() {
        return Model.getInstance().getDBMananger().getSongDao().getSonglist();
    }

    private static AppConstant mAppConstant=new AppConstant();
    public int getPlayingState() {
        return playingState;
    }

    public void setPlayingState(int playingState) {
        this.playingState = playingState;
    }

    public static AppConstant getInstance() {
        return mAppConstant;
    }

    public class PlayerMsg{
        public static final int PLAY_MSG=1;//开始播放
        public static final int PAUSE_MSG=2;//暂停播放
        public static final int STOP_MSG=3;//停止播放
        public static final int CONTINUE_MSG=4;//继续播放
        public static final int PREVIOUS_MSG=5;//上一曲播放
        public static final int NEXT_MSG=6;//下一曲播放
        public static final int CHANG_MODE = 7 ;//模式改变

        public static final int CHANGE_PRG = 8; //歌曲进度改变 /by：yxy
    }

    public class Mode{
        public static final int SINGLE_LOOP=0;//单曲循环播放
        public static final int LOOP=1;//循环播放
        public static final int ORDER=2;//循序播放
        public static final int RANDOM=3;//随机播放
    }
}
