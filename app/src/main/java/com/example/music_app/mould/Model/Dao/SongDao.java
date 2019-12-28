package com.example.music_app.mould.Model.Dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.music_app.mould.Model.SQLite.DBHelper;


import com.example.music_app.mould.Model.Table.SongTable;
import com.example.music_app.mould.Model.bean.Song;

import java.util.ArrayList;
import java.util.List;

public class SongDao {
    private final DBHelper mhelper;
    private final SongTable mSongTable;
    public SongDao(DBHelper helper ,SongTable songTable) {
        this.mhelper = helper;
        mSongTable=songTable;
    }

    public void addSong(Song song){
        if (song==null){
            return;
        }
        SQLiteDatabase db=mhelper.getWritableDatabase();
        if(db==null){
            return;
        }
        ContentValues values=new ContentValues();
        values.put(SongTable.COL_DURATION, song.getDuration());
        values.put(SongTable.COL_TITLE, song.getTitle());
        values.put(SongTable.COL_FILE_URL, song.getFileUrl());
        values.put(SongTable.COL_SINGER, song.getSinger());
        db.replace(mSongTable.getTableName(),null,values);

    }

    public long SongNum(){
        SQLiteDatabase db=mhelper.getReadableDatabase();
        long num=0;
        String sql="select*from "+mSongTable.getTableName();
        Cursor cursor = db.rawQuery(sql, null);
        num=cursor.getCount();
        cursor.close();
        return num;
    }

   public void saveSongList(List<Song> data){
        if(data==null||data.size()<=0){
            return;
        }
        for (Song song:data){
            addSong(song);
        }

    }
    /*public void deleteSong(String time){
        if (time==null){
            return;
        }
        SQLiteDatabase db = mHelper.getReadableDatabase();
        db.delete(SongTable.TAB_NAME,Song.COL_TIME+"=?",new String[]{time});
    }*/


    public List<Song> getSonglist(){
        SQLiteDatabase db=mhelper.getReadableDatabase();
        String sql="select*from "+mSongTable.getTableName();
        Cursor cursor = db.rawQuery(sql, null);
        List<Song> datas=new ArrayList<>();
        while (cursor.moveToNext()){
            Song data=new Song();
            data.setTitle(cursor.getString(cursor.getColumnIndex(SongTable.COL_TITLE)));
            data.setSinger(cursor.getString(cursor.getColumnIndex(SongTable.COL_SINGER)));
            data.setDuration(cursor.getInt(cursor.getColumnIndex(SongTable.COL_DURATION)));
            data.setFileUrl(cursor.getString(cursor.getColumnIndex(SongTable.COL_FILE_URL)));
            datas.add(data);
        }
        cursor.close();
        return datas;
    }

}
