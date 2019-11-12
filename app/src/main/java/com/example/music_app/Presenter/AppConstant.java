package com.example.music_app.Presenter;

import com.example.music_app.mould.Model.Model;
import com.example.music_app.mould.Model.bean.Song;

import java.util.ArrayList;
import java.util.List;

public class AppConstant {

    private int playingState=0;
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

    public class PlayerMag{
        public static final int PLAY_MAG=1;//开始播放
        public static final int PAUSE=2;//暂停播放
        public static final int NEXT=3;//暂停播放
    }
}
