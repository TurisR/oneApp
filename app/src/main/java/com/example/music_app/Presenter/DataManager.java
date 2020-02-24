package com.example.music_app.Presenter;

import android.content.Context;
import android.util.Log;

import com.example.music_app.mould.Model.Model;
import com.example.music_app.mould.Model.SQLite.DBHelper;
import com.example.music_app.mould.Model.bean.Song;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataManager {

    private DataManageUtil mDataManageUtil;

    public DataManager(Context context) {
        mDataManageUtil = new DataManageUtil(context);
    }

    public void initData(){
        Map<String,List<Integer>> mapNumber=new HashMap<String,List<Integer>>();
        List<String> ListName=mDataManageUtil.getData("listName");

        AppConstant.getInstance().setMode(mDataManageUtil.getMode());
        AppConstant.getInstance().setLocalSongList(Model.getInstance().getDBManager().getSongDao("本地歌曲").getSonglist());
        AppConstant.getInstance().setListName(ListName);
        if(ListName.isEmpty()){
            Log.e("1","one");
            return;
        }
        Gson gson=new Gson();
        AppConstant appConstant=AppConstant.getInstance();
        for(String name:ListName){
            List<Integer> songNumber=mDataManageUtil.getDataNumber(name);
            mapNumber.put(name,songNumber);
            String str=gson.toJson(mapNumber.get(name));
            Log.e(name," "+str);
            //songNumber.clear();
        }
        AppConstant.getInstance().setMapNumber(mapNumber);
        for(String name:ListName){
            String str=gson.toJson(mapNumber.get(name));
            Log.e("2"+name, " "+str);
        }
       //
    }

    public void upDate() {
        mDataManageUtil.setMode(AppConstant.getInstance().getMode());
        mDataManageUtil.saveData("listName",AppConstant.getInstance().getListName());
         Model.getInstance().getDBManager().getSongDao("本地歌曲").updateSongList(AppConstant.getInstance().getLocalSongList());
        List<String> stringList=AppConstant.getInstance().getUpdateList();
        if (stringList.isEmpty()){
            return;
        }else {
            for(String name:stringList){
                mDataManageUtil.saveData(name,AppConstant.getInstance().getListNumber(name));
            }
        }

    }

    public String getListName(String listName) {
        return mDataManageUtil.getListName("listName");
    }
}

