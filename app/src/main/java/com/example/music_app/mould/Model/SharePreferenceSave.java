package com.example.music_app.mould.Model;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
/**
 * @description:采用sharePreferce存储自定义歌单，通过存储本地歌单的索引，需要用到json
 * @author: JiangJiaHui
 * @createDate: 2019/12/9
 * @Modified By：
 * @version: 1.0
 */
public class SharePreferenceSave {
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public SharePreferenceSave(Context mContext) {
        preferences = mContext.getSharedPreferences("Data", Context.MODE_MULTI_PROCESS);
        editor = preferences.edit();
    }

    /**
     * 保存List
     * @param tag
     * @param dataList
     */
    public <T> void saveData(String tag, List<T> dataList) {
        if (null == dataList || dataList.size() <= 0)
            return;
        Gson gson = new Gson();
        //转换成json数据，再保存
        String strJson = gson.toJson(dataList);
        //editor.clear();
        editor.putString(tag, strJson);
        editor.commit();
    }



    /**
     * 获取List
     * @param tag
     * @return
     */
    public <T> List<T> getData(String tag) {
        List<T> dataList=new ArrayList<T>();
        String strJson = preferences.getString(tag, null);
        if (null == strJson) {
            return dataList;
        }
        Gson gson = new Gson();
        dataList = gson.fromJson(strJson, new TypeToken<List<T>>() {
        }.getType());
        return dataList;
    }

    public List getDataNumber(String tag) {
        List<Integer> dataList=new ArrayList<>();
        String strJson = preferences.getString(tag, null);
        if (null == strJson) {
            return dataList;
        }
        Gson gson = new Gson();
        dataList = gson.fromJson(strJson, new TypeToken<List<Integer>>() {}.getType());
        return dataList;
    }


    public int getMode(){
        return preferences.getInt("MODE",1);
    }

    public void setMode(int mode){
        //editor.clear();
        editor.putInt("MODE",mode);
        editor.commit();
    }



    public String getListName(String string){
        return preferences.getString(string,"hhhhh");
    }


}
