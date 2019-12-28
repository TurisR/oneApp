package com.example.music_app.Presenter;

import android.content.Context;

import com.example.music_app.View.Adapter.PlayingListAdapter;
import com.example.music_app.mould.Model.Model;
import com.example.music_app.mould.Model.bean.Song;

import java.util.ArrayList;
import java.util.List;

public class AppConstant {

    private int playingState=0;
    private int position=-1;
    private int mode=2;

    private PlayerUtil mPlayerUtil;
    private Song playingSong;
    private SwitchSong mSwitchSong;
    private static AppConstant mAppConstant=new AppConstant();

    private List<Song> RecentSongList=new ArrayList<>();
    private List<Song> PersonCollectSongList=new ArrayList<>();
    private List<String> PersonalSongAlbum=new ArrayList<>();
    private List<Song> CurrentSongList=new ArrayList<>();
    private List<Song> LocalSongList=new ArrayList<>();


    public void setRecentSongList(List<Song> recentSongList) {
        RecentSongList = recentSongList;
    }
    public void setPersonCollectSongList(List<Song> personCollectSongList) {
        PersonCollectSongList = personCollectSongList;
    }
    public void setCurrentSongList(List<Song> currentSongList) {
        CurrentSongList =currentSongList;
    }
    public void setLocalSongList(List<Song> localSongList) {
        LocalSongList = localSongList;
    }
    public void setPersonalSongAlbum(List<String> personalSongAlbum) {
        PersonalSongAlbum = personalSongAlbum;
    }
    public void setMode(int mode) {
        this.mode = mode;
    }

    public void setPosition(int position) {
        this.position = position;
        setPlayingSong(getCurrentSongList().get(position));
    }




    public List<Song> getLocalSongList() {
     return LocalSongList;
    }
    public List<Song> getCurrentSongList() {
        return CurrentSongList;
    }
    public List<Song> getPersonCollectSongList() {
        return PersonCollectSongList;
    }
    public List<Song> getRecentSongList() {
        return RecentSongList;
    }
    public List<String> getPersonalSongAlbum() {
        return PersonalSongAlbum;
    }
    public int getMode() {
        return mode;
    }
    public int getPosition() {
        int i=0;
        for (Song song:getCurrentSongList()){
            if(song.Equals(getPlayingSong())) {
                position=i;
            }
            i++;
        }
        return position;
    }


    public SwitchSong getSwitchSong() {
        mSwitchSong=new SwitchSong();
        return mSwitchSong;
    }
    public PlayerUtil getPlayerUtil(Context context){
        mPlayerUtil=new PlayerUtil(context);
        return mPlayerUtil;
    }

    public void addPersonCollectSongList(Song song) {
        if(isExist(song, PersonCollectSongList)){
            PersonCollectSongList.add(song);
            Model.getInstance().getDBManager().getSongDao("个人收藏").addSong(song);
        }

    }
    public void addRecentSongList(Song song) {
        if(isExist(song, RecentSongList)){
            RecentSongList.add(song);
            Model.getInstance().getDBManager().getSongDao("最近播放").addSong(song);
        }
    }

    public void addPersonalSongAlbum(String album) {
        PersonalSongAlbum .add(album);
    }


    public boolean isExist(Song song,List<Song> songList) {
        for (Song song1:songList){
            if (song1.Equals(song)){
                return true;
            }
        }
        return  false;
    }


    public List<Song> getSongList(String name) {
        return Model.getInstance().getDBManager().getSongDao(name).getSonglist();

    }

    public Song getPlayingSong() {
        return playingSong;
    }
    public int getPlayingState() {
        return playingState;
    }
    public void setPlayingSong(Song playingSong) {
        this.playingSong = playingSong;
    }
    public void setPlayingState(int playingState) {
        this.playingState = playingState;
    }
    public static AppConstant getInstance() {
        return mAppConstant;
    }


    //信息
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

    public class MessageType{
        //一系列动作
        public static final String UPDATE_ACTION= "com.example.music_app.UPDATE_ACTION";		//更新动作
        public static final String MUSIC_STATE = "com.example.music_app.MUSIC_STATE";			//播放器状态 播放|暂停
        public static final String MUSIC_CURRENT = "com.example.music_app.MUSIC_CURRENT";		//当前音乐改变动作
        public static final String MUSIC_DURATION = "com.action.MUSIC_DURATION";	//音乐时长改变动作
        public static final String MUSIC_NEXT = "com.action.MUSIC_NEXT";		//音乐下一曲
        public static final String MUSIC_PREVIOUS = "com.action.MUSIC_PREVIOUS";	//音乐上一曲
    }




}
