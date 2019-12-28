package com.example.music_app.mould.Model.SQLite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.example.music_app.mould.Model.Table.SongTable;


public class DBHelper extends SQLiteOpenHelper {
    private Context mContext;
    private String name;
    private SongTable songTable;
    private String drop_sql;

    public DBHelper(Context context) {
        super(context, "user", null, 1);
        mContext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        songTable=new SongTable("本地歌曲");
        drop_sql="DROP TABLE IF EXISTS "+songTable.getTableName()+";";
        db.execSQL(drop_sql);
        db.execSQL(songTable.getCreateTab());

        songTable=new SongTable("最近播放");
        drop_sql="DROP TABLE IF EXISTS "+songTable.getTableName()+";";
        db.execSQL(drop_sql);
        db.execSQL(songTable.getCreateTab());

        songTable=new SongTable("个人收藏");
        drop_sql="DROP TABLE IF EXISTS "+songTable.getTableName()+";";
        db.execSQL(drop_sql);
        db.execSQL(songTable.getCreateTab());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public boolean dynamicCreateTable(String name){
        SQLiteDatabase db=getWritableDatabase();
        songTable=new SongTable(name);
        drop_sql="DROP TABLE IF EXISTS "+songTable.getTableName()+";";
        db.execSQL(drop_sql);
        db.execSQL(songTable.getCreateTab());
        return tableIsExist(name);
    };
    /*
     * 判断库是否存在****
     * */
    public boolean tableIsExist(String tablename){
        boolean result=false;
        if(tablename.equals("null")){
            return false;
        }
        SQLiteDatabase db=null;
        Cursor cursor=null;//游标
        try{
            db=this.getReadableDatabase();
            String sql="select name from sqlite_master where name ='"+tablename.trim()+"' and type='table'";
            cursor=db.rawQuery(sql,null);
            if(cursor.getCount()!=0){
                result=true;
            }else{
                result=false;
            }
        }catch (Exception e){}
        return result;
    }//
}
