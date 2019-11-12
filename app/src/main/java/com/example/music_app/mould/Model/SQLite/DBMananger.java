package com.example.music_app.mould.Model.SQLite;

import android.content.Context;

import com.example.music_app.mould.Model.Dao.SongDao;



public class DBMananger {

    private final DBHelper mHelper;

    private SongDao mSongDao;

    public DBMananger(Context context, String name) {
        mHelper = new DBHelper(context,name);
        mSongDao=new SongDao(mHelper);
    }
    public SongDao getSongDao(){
        return mSongDao;
    }
    public void close() {
        mHelper.close();
    }

    public boolean getExit(String str){
        return mHelper.tableIsExist(str);
    }

}
