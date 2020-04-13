package com.example.music_app.mould.Model.SQLite;

import android.content.Context;

import com.example.music_app.mould.Model.Dao.SongDao;
import com.example.music_app.mould.Model.Table.SongTable;
/**
 * @description:SQLite管理类单例模式
 * @author: JiangJiaHui
 * @createDate: 2019/12/9
 * @Modified By：
 * @version: 1.0
 */
public class DBManager {

    private final DBHelper mHelper;
    private SongDao mSongDao;
    private Context mContext;
    public DBManager(Context context) {
        mContext=context;
        mHelper=new DBHelper(mContext);
    }
    public SongDao getSongDao(String name){
        return new SongDao(mHelper,new SongTable(name));
    }

   public void close() {
      mHelper.close();
  }

  public DBHelper getHelper(){
        return mHelper;
  }

}
