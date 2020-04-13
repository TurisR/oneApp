package com.example.music_app.Presenter;

import android.content.Context;
import android.util.Log;

import com.example.music_app.mould.Model.SharePreferenceSave;
import com.example.music_app.mould.Model.Model;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @description:数据管理类，从Model类获取SQLite数据初始化、更新
 * @author: JiangJiaHui
 * @createDate: 2019/12/9
 * @Modified By：
 * @version: 1.0
 */
public class DataManager {

    private SharePreferenceSave mSharePreferenceSave;

    public DataManager(Context context) {
        mSharePreferenceSave = new SharePreferenceSave(context);
    }

    public void initData(){
        Map<String,List<Integer>> mapNumber=new HashMap<String,List<Integer>>();
        List<String> ListName= mSharePreferenceSave.getData("listName");

        AppConstant.getInstance().setMode(mSharePreferenceSave.getMode());
        AppConstant.getInstance().setLocalSongList(Model.getInstance().getDBManager().getSongDao("本地歌曲").getSonglist());
        AppConstant.getInstance().setListName(ListName);
        if(ListName.isEmpty()){
            Log.e("1","one");
            return;
        }
        Gson gson=new Gson();
        AppConstant appConstant=AppConstant.getInstance();
        for(String name:ListName){
            List<Integer> songNumber= mSharePreferenceSave.getDataNumber(name);
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
        mSharePreferenceSave.setMode(AppConstant.getInstance().getMode());
        mSharePreferenceSave.saveData("listName",AppConstant.getInstance().getListName());
         Model.getInstance().getDBManager().getSongDao("本地歌曲").updateSongList(AppConstant.getInstance().getLocalSongList());
        List<String> stringList=AppConstant.getInstance().getUpdateList();
        if (stringList.isEmpty()){
            return;
        }else {
            for(String name:stringList){
                mSharePreferenceSave.saveData(name,AppConstant.getInstance().getListNumber(name));
            }
        }

    }

    public String getListName(String listName) {
        return mSharePreferenceSave.getListName("listName");
    }
}

