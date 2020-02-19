package com.example.music_app.Presenter;

import android.content.Context;

import com.example.music_app.mould.Model.Model;
import com.example.music_app.mould.Model.bean.Song;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Data<T> {


    private DataManageUtil mDataManageUtil;
    private List<Song> LocalSongList=new ArrayList<>();
    private Map<String,List<Integer>> mapNumber=new HashMap<String,List<Integer>>();
    private List<String> ListName=new ArrayList<>();
    private List<String> removeListName=new ArrayList<>();
    private List<Integer> songNumber=new ArrayList<>();
    private List<Song> songList=new ArrayList<>();


    public Data(Context context) {
            mDataManageUtil=new DataManageUtil(context);
    }

    public List<Song> getLocalSongList() {
        return LocalSongList;
    }

    public void setLocalSongList(List<Song> localSongList) {
        LocalSongList = localSongList;
    }



    public void initData(){
        ListName=mDataManageUtil.getData("listName");
        AppConstant.getInstance().setRecentSongList(Model.getInstance().getDBManager().getSongDao("本地歌曲").getSonglist());
        if (ListName==null){
            return;
        }
        for(String name:ListName){
            songNumber=mDataManageUtil.getData(name);
            mapNumber.put(name,songNumber);
            songNumber.clear();
        }

    }



    public boolean addList(String name,int num){
        songNumber.clear();
        if (mapNumber.get(name)==null){
           return false;
        }

        if(!ListName.contains(name)){
            songNumber.add(num);
            mapNumber.put(name,songNumber);
            mDataManageUtil.saveData(name,songNumber);
        }

        if(!mapNumber.get(name).contains(num)){
            mapNumber.get(name).add(num);
            mDataManageUtil.saveData(name,mapNumber.get(name));
            return true;
        }
        return false;
    }


    public void removeSong(String name,int num,Context context){
        mDataManageUtil=new DataManageUtil(context);
        switch (name){
            case AppConstant.DataType.PERSONAL_ALBUM_NAME:
                mapNumber.remove(ListName.get(num));
                ListName.remove(num);
               break;

            case AppConstant.DataType.LOCAL_MUSIC:
                for(String str:ListName){
                    if(mapNumber.get(str).contains(num)){
                        mapNumber.get(str).remove(num);
                        removeListName.add(name);
                    }
                }
                LocalSongList.remove(num);
                break;

            default:
                try {
                    mapNumber.get(name).remove(num);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
        }
    }

    public List getList(String name){
        switch (name){
            case AppConstant.DataType.PERSONAL_ALBUM_NAME:
                return ListName;
            default:
                return getSongList(name);
        }

    }




    public List<Song> getSongList(String name){
        if(!ListName.contains(name)){
            return null;
        }
        if (!songList.isEmpty()){
            songList.clear();
        }
        for (int i:mapNumber.get(name)){
            songList.add(LocalSongList.get(i));
        }
        return songList;
    }


}
