package com.example.music_app.Presenter;

import android.content.Context;

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

    private String currentListName;


    private List<Song> RecentSongList=new ArrayList<>();
    private List<Song> RecentSearch=new ArrayList<>();
    private List<Song> PersonCollectSongList=new ArrayList<>();
    private List<String> PersonalSongAlbum=new ArrayList<>();
    private List<Song> CurrentSongList=new ArrayList<>();
    private List<Song> LocalSongList=new ArrayList<>();
    private List<String> RecentUpdateSQL=new ArrayList<>();//最近更新SQL的表


    public void setRecentSongList(List<Song> recentSongList) {
        RecentSongList = recentSongList;
    }
    public void setPersonCollectSongList(List<Song> personCollectSongList) {
        PersonCollectSongList = personCollectSongList;
    }
    public void setCurrentSongList(List<Song> currentSongList,String name) {
        CurrentSongList =currentSongList;
        currentListName=name;
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
    public List<String> getPersonalAlbumName() {
        return PersonalSongAlbum;
    }

    public List<String> getRecentUpdateSQL() {
        return RecentUpdateSQL;
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



    public boolean addCurrentSongList(Song song){
        if(!isExist(song, CurrentSongList)){
            CurrentSongList.add(song);
            return true;
        }
        return false;
    }
    public void addPersonCollectSong(Song song) {
        if(!isExist(song, PersonCollectSongList)){
            PersonCollectSongList.add(song);
            RecentUpdateSQL.add(DataType.PERSONAL_COLLECT);
        }
    }

    public Boolean addPersonCollectSongList(List<Song> songList) {
       if(songList!=null){
            PersonCollectSongList.addAll(songList);
            RecentUpdateSQL.add(DataType.PERSONAL_COLLECT);
            return true;
        }
        return false;
    }

    public boolean removeSong(int position,String name){
        if(name==null){
            return false;
        }

        switch (name){
            case DataType.CURRENT_MUSIC://当前音乐
                CurrentSongList.remove(position);
                return true;
            case DataType.LOCAL_MUSIC://本地歌曲
                LocalSongList.remove(position);
                if(name.equals(currentListName)){
                    CurrentSongList.remove(position);
                }
                RecentUpdateSQL.add(DataType.LOCAL_MUSIC);
                return true;
            case DataType.PERSONAL_ALBUM_NAME://个人歌单名
                String string=PersonalSongAlbum.get(position);
                PersonalSongAlbum.remove(position);
                return true;
            case DataType.PERSONAL_COLLECT://个人收藏
                PersonCollectSongList.remove(position);
                if(name.equals(currentListName)){
                    CurrentSongList.remove(position);
                }
                RecentUpdateSQL.add(DataType.PERSONAL_COLLECT);
                return true;
            case DataType.RECENT_MUSIC://最近搜索
                RecentSongList.remove(position);
                RecentUpdateSQL.add(DataType.RECENT_MUSIC);
                return true;

            case DataType.RECENT_SEARCH://最近播放
                RecentSearch.remove(position);
                if(name.equals(currentListName)){
                    CurrentSongList.remove(position);
                }
                RecentUpdateSQL.add(DataType.RECENT_SEARCH);
                return true;
        }
        return false;
    }



    public void addRecentSongList(Song song) {
        if(!isExist(song, RecentSongList)){
            RecentSongList.add(song);
            RecentUpdateSQL.add(DataType.RECENT_MUSIC);
        }
    }

    public void addPersonalSongAlbum(String album) {
        if(!isExistAlbum(album)){
            PersonalSongAlbum .add(album);
        }
    }

    public void addRecentUpdataSQL(String name) {
        boolean i=true;
        for (String string:RecentUpdateSQL){
            if (string.equals(name)){
                i=false;
            }
        }
        if(i){
            RecentUpdateSQL .add(name);
        }
    }

    public Boolean isExistAlbum(String album) {
        for (String string:PersonalSongAlbum){
            if (string.equals(album)){
                return true;
            }
        }
        return  false;

    }

    public boolean isExist(Song song,List<Song> songList) {

        if(songList==null){
            return false;
        }
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


    public class DataType{

        public static final String LOCAL_MUSIC= "本地歌曲";		//更新动作
        public static final String RECENT_MUSIC = "最近播放";			//播放器状态 播放|暂停
        public static final String PERSONAL_COLLECT = "个人收藏";		//当前音乐改变动作
        public static final String RECENT_SEARCH = "最近搜索";	//音乐时长改变动作
        public static final String CURRENT_MUSIC = "当前播放音乐";		//音乐下一曲
        public static final String PERSONAL_ALBUM_NAME = "个人歌单";
    }



}
