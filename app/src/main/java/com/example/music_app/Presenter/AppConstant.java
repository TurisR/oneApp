package com.example.music_app.Presenter;

import android.content.Context;

import com.example.music_app.mould.Model.Model;
import com.example.music_app.mould.Model.bean.Song;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppConstant {

    private int playingState=0;
    private int position=-1;
    private int mode=2;
    private PlayerUtil mPlayerUtil;
    private Song playingSong;
    private SwitchSong mSwitchSong;
    private static AppConstant mAppConstant=new AppConstant();

    private List<Song> CurrentSongList=new ArrayList<>();//当前播放音乐列表
    private List<Song> LocalSongList=new ArrayList<>();//本地音乐列表
    private List<String> updateList=new ArrayList<>();//最近更新名称
    private Map<String,List<Integer>> mapNumber=new HashMap<String,List<Integer>>();
    private List<String>  ListName=new ArrayList<>();//个人歌单名称


//    public Map<String, List<Integer>> getMapNumber() {
//        return mapNumber;
//    }



    public void setMapNumber(Map<String, List<Integer>> map) {
        this.mapNumber = map;
    }

    public void setCurrentSongList(List<Song> currentSongList) {
        CurrentSongList =currentSongList;
    }
    public void setCurrentSongList(Song currentSongList) {
        CurrentSongList .clear();
        CurrentSongList.add(currentSongList);
    }

    public List<String> getListName() {
        return ListName;
    }

    public void setListName(List<String> listName) {
        ListName =listName ;
    }

    public void addCurrentSong(Song song) {
        CurrentSongList.add(song);
    }


    public List getList(String name){
        if(name.equals(DataType.PERSONAL_ALBUM_NAME)){
            return ListName;
        }else if (name.equals(DataType.CURRENT_MUSIC)){
            return CurrentSongList;
        }else {
            return getListSong(name);
        }
    }

    public List<Song> getListSong(String name){
        List<Song> songList=new ArrayList<>();
        if(ListName.contains(name)){
            if(mapNumber.get(name)!=null){
                for(int i:mapNumber.get(name)){
                    if(i<LocalSongList.size()&&i>=0){
                        songList.add(LocalSongList.get(i));
                    }

                }
            }

        }
        return songList;
    }

    public void addListSong(String name,Song song){
        List<Integer> list=new ArrayList<>();
        if(!ListName.contains(name)){
            ListName.add(name);
            list.add(getPosition(song));
            mapNumber.put(name,list);
            return;
        }
        int num=getPosition(song);
        if(!mapNumber.get(name).contains(num)){
            mapNumber.get(name).add(num);
        }
        updateList.add(name);
    }

    public void addListSongByIndex(String name,int num){
        List<Integer> list=new ArrayList<>();
        if(!ListName.contains(name)){
            list.add(num);
            ListName.add(name);
            mapNumber.put(name,list);
            return;
        }
        mapNumber.get(name).add(num);
        updateList.add(name);
    }
    public void addListName(String name){
        ListName.add(name);
    }

    public List<String> getAlbumList() {
        List<String> stringList=new ArrayList<>();
        stringList.addAll(ListName);
        stringList.remove(DataType.PERSONAL_COLLECT);
        stringList.remove(DataType.RECENT_MUSIC);
        stringList.remove(DataType.RECENT_SEARCH);
        return stringList;
    }

    public void removeListSong(String name,Song song){
        mapNumber.get(name).remove(getPosition(song));
        updateList.add(name);
    }

    public void removeListSongIndex(String name,int position){
        mapNumber.get(name).remove(position);
        updateList.add(name);
    }


    public void removeListName(String string){
       if(ListName.contains(string)) {
          ListName.remove(string);
           //ListName.remove(num);
       }
    }

    public void removeListSongByIndex(String name,int position){
        if(mapNumber.get(name)==null){
            return;
        }
        mapNumber.get(name).remove(position);
        updateList.add(name);
    }


    public int getPosition(Song song){
        int i=0;
        if(song==null){
            return -1;
        }
        for (Song song1:LocalSongList){
            if (song.Equals(song1)){
                return i;
            }
            i++;
        }
        return -1;
    }


    public void setLocalSongList(List<Song> localSongList) {
        LocalSongList = localSongList;
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
    public int getMode() {
        return mode;
    }
    public int getPosition() {
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

    public List<String> getUpdateList() {
        return updateList;
    }

    public void setUpdateList(List<String> updateList) {
        this.updateList = updateList;
    }

    public void addLocalSongList(List<Song> checkedData) {
        if(checkedData!=null){
            LocalSongList.addAll(checkedData);
        }
    }

    public List<Integer> getListNumber(String name) {
        if(mapNumber.containsKey(name)){
            return mapNumber.get(name);
        }
       return null;
    }

    public List<Integer> getListNumberL() {
        return mapNumber.get(DataType.RECENT_MUSIC);
    }


    public boolean isExist(String string,Song song){
        if(mapNumber.get(string)==null){
            return false;
        }
        if(mapNumber.get(string).contains(getPosition(song))){
            return true;
        }
        return false;

    }

    public boolean isExist(List<Song> songList,Song song){
        for(Song song1:songList){
            if(song.Equals(song1)){
                return true;
            }
        }
        return false;

    }

    public void removeLocalList(int position) {
        LocalSongList.remove(position);
        for (String s:ListName){
            if(mapNumber.get(s).contains(position)){
                int k=mapNumber.get(s).indexOf(position);
                mapNumber.get(s).remove(k);
                updateList.add(s);
            }
        }
    }

    public void removeCurrentSong(int position) {
        CurrentSongList.remove(position);
    }

    public void addCurrentSongList(List<Song> checkedData) {
        CurrentSongList.addAll(checkedData);
    }
    public boolean addCurrentSongList(Song checkedData) {
        if(CurrentSongList.contains(checkedData)){
            return false;
        }
        CurrentSongList.add(checkedData);
        return true;
    }

    public List<Song> getAddSongList(String string){
        List<Song> songList=new ArrayList<>();
        songList.addAll(LocalSongList);
        songList.removeAll(getList(string));
        return songList;
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
