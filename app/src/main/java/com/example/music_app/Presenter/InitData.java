package com.example.music_app.Presenter;

import android.content.Context;

import com.example.music_app.mould.Model.Model;
import com.example.music_app.mould.Model.SQLite.DBHelper;

public class InitData {

    private Boolean mABoolean1;
    private Boolean mABoolean2;
    private Boolean mABoolean3;
    private Context mContext;
    private DBHelper helper;

    public InitData(Context context) {
        mContext=context;
        helper=new DBHelper(mContext);
    }

    public Boolean getABoolean1() {
        return mABoolean1;
    }

    public Boolean getABoolean2() {
        return mABoolean2;
    }

    public Boolean getABoolean3() {
        return mABoolean3;
    }

    public InitData invoke() {

        mABoolean1 = helper.tableIsExist("本地歌曲");
        mABoolean2 = helper.tableIsExist("个人收藏");
        mABoolean3 = helper.tableIsExist("最近播放");

        if(helper.getTableName()!=null){
            AppConstant.getInstance().setPersonalSongAlbum(helper.getTableName());
        }
        if(mABoolean1){
            AppConstant.getInstance().setLocalSongList(Model.getInstance().getDBManager().getSongDao("本地歌曲").getSonglist());
        }

        if(mABoolean2){
            AppConstant.getInstance().setPersonCollectSongList(Model.getInstance().getDBManager().getSongDao("个人收藏").getSonglist());
        }

        if(mABoolean3){
            AppConstant.getInstance().setRecentSongList(Model.getInstance().getDBManager().getSongDao("最近播放").getSonglist());
        }
        return this;
    }
}

