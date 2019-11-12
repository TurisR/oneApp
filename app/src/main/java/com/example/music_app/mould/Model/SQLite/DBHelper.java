package com.example.music_app.mould.Model.SQLite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.example.music_app.mould.Model.Table.SongTable;


public class DBHelper extends SQLiteOpenHelper {
    private Context mContext;
    public DBHelper(Context context,String name) {
        super(context, name, null, 1);
        mContext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SongTable.CREATE_TAB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
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
