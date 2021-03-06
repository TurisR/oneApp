package com.example.music_app.mould.Model.SQLite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.example.music_app.mould.Model.Table.SongTable;

import java.util.ArrayList;
import java.util.List;
/**
 * @description:SQLite管理类，对本地歌曲建表
 * @author: JiangJiaHui
 * @createDate: 2019/12/9
 * @Modified By：
 * @version: 1.0
 */

public class DBHelper extends SQLiteOpenHelper {
    private Context mContext;
    private String name;
    private SongTable songTable;
    private String drop_sql;
    private DBHelper mDBHelper;

    public DBHelper(Context context) {
        super(context, "user", null, 1);
        mContext = context;
        mDBHelper=this;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        songTable = new SongTable("本地歌曲");
        drop_sql = "DROP TABLE IF EXISTS " + songTable.getTableName() + ";";
        db.execSQL(drop_sql);
        db.execSQL(songTable.getCreateTab());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public boolean dynamicCreateTable(String name) {
        SQLiteDatabase db = getWritableDatabase();
        songTable = new SongTable(name);
        drop_sql = "DROP TABLE IF EXISTS " + songTable.getTableName() + ";";
        db.execSQL(drop_sql);
        db.execSQL(songTable.getCreateTab());
        return tableIsExist(name);
    }

    ;

    /*
     * 判断库是否存在****
     * */
    public boolean tableIsExist(String tablename) {
        boolean result = false;
        if (tablename.equals("null")) {
            return false;
        }
        SQLiteDatabase db = null;
        Cursor cursor = null;//游标
        try {
            db = this.getReadableDatabase();
            String sql = "select name from sqlite_master where name ='" + tablename + "' and type='table'";
            cursor = db.rawQuery(sql, null);
            if (cursor.getCount() != 0) {
                result = true;
            } else {
                result = false;
            }
        } catch (Exception e) {
        }
        return result;
    }//


    public List<String> getAllTableName() {
        List<String> stringList = new ArrayList<>();
        SQLiteDatabase db = null;
        String string;
        Cursor cursor = null;//游标
        try {
            db = this.getReadableDatabase();
            String sql = "select name from sqlite_master where type='table' order by name";
            cursor = db.rawQuery(sql, null);
            while (cursor.moveToNext()) {
                string=cursor.getString(0);
                if(!string.equals("个人收藏")&&!string.equals("最近播放")&&!string.equals("本地歌曲")&&!string.equals("android_metadata")){
                    stringList.add(string);
                }
            }
        }catch (Exception e) {

        };
        return stringList;
    }
    public Boolean deleteTable(String name){//删除表
        SQLiteDatabase db = getWritableDatabase();
        songTable = new SongTable(name);
        drop_sql = "DROP TABLE IF EXISTS " + songTable.getTableName() + ";";
        db.execSQL(drop_sql);
        if(!tableIsExist(name)){
            return true;
        }
        return false;
    }
}