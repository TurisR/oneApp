package com.example.music_app.mould.Model.SQLite;

import android.content.Context;

import com.example.music_app.mould.Model.Dao.SongDao;
import com.example.music_app.mould.Model.Table.SongTable;


public class DBManager {

    private SongDao mSongDao;
    private Context mContext;
    public DBManager(Context context) {
        mContext=context;
    }
    public SongDao getSongDao(String name){
        return new SongDao(new DBHelper(mContext,name),new SongTable(name));
    }


//    public void close() {
//        mHelper.close();
//    }
//
//    public boolean getExit(String str){
//        return mHelper.tableIsExist(str);
//    }

}
