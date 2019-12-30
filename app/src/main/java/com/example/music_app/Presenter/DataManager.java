package com.example.music_app.Presenter;

import android.content.Context;

import com.example.music_app.mould.Model.Model;
import com.example.music_app.mould.Model.SQLite.DBHelper;
import com.example.music_app.mould.Model.bean.Song;

import java.util.List;

public class DataManager {

    private Boolean mABoolean;
    private Context mContext;
    private DBHelper helper;

    public DataManager(Context context) {
        mContext=context;
        helper=Model.getInstance().getDBManager().getHelper();
    }



    public void invoke(String name) {

        mABoolean = helper.tableIsExist(name);
        switch (name){
            case AppConstant.DataType.LOCAL_MUSIC:
                if(mABoolean){
                    AppConstant.getInstance().setLocalSongList(Model.getInstance().getDBManager().getSongDao("本地歌曲").getSonglist());
                }
                break;

            case  AppConstant.DataType.PERSONAL_COLLECT:
                if(mABoolean){
                    AppConstant.getInstance().setPersonCollectSongList(Model.getInstance().getDBManager().getSongDao("个人收藏").getSonglist());
                }
                break;
            case  AppConstant.DataType.RECENT_MUSIC:
                if(mABoolean){
                    AppConstant.getInstance().setRecentSongList(Model.getInstance().getDBManager().getSongDao("最近播放").getSonglist());
                }
                break;
        }
    }

    public void initData(){
        invoke(AppConstant.DataType.LOCAL_MUSIC);
        invoke(AppConstant.DataType.PERSONAL_COLLECT);
        invoke(AppConstant.DataType.RECENT_MUSIC);

        if(helper.getAllTableName()!=null){
            AppConstant.getInstance().setPersonalSongAlbum(helper.getAllTableName());
        }
    }

    public void upDate(){
        for(String name:AppConstant.getInstance().getRecentUpdateSQL()){
            upDateSQL(name);
        }
    }

    private void upDateSQL(String name) {
        mABoolean = helper.tableIsExist(name);
        switch (name){
            case AppConstant.DataType.LOCAL_MUSIC:
                if(mABoolean){
                    Model.getInstance().getDBManager().getSongDao("本地歌曲").updateSongList(AppConstant.getInstance().getLocalSongList());
                }
                break;

            case  AppConstant.DataType.PERSONAL_COLLECT:
                if(mABoolean){
                    Model.getInstance().getDBManager().getSongDao("个人收藏").updateSongList(AppConstant.getInstance().getPersonCollectSongList());
                }
                break;
            case  AppConstant.DataType.RECENT_MUSIC:
                if(mABoolean){
                    Model.getInstance().getDBManager().getSongDao("最近播放").updateSongList(AppConstant.getInstance().getRecentSongList());
                }
                break;
        }
    }


    public List<Song> getPersonalAlbumSong(String name){
        mABoolean = helper.tableIsExist(name);
        if(!mABoolean){
            return null;
        }
        return Model.getInstance().getDBManager().getSongDao(name).getSonglist();
    }

    public void upDatePersonalAlbum(String name,List<Song> songList){
        mABoolean = helper.tableIsExist(name);
        if(mABoolean){
            Model.getInstance().getDBManager().getSongDao(name).updateSongList(songList);
        }

    }


    public void AddPersonalAlbumSong(String name,Song song){
        mABoolean = helper.tableIsExist(name);
        if(mABoolean){
            Model.getInstance().getDBManager().getSongDao(name).addSong(song);
        }

    }

    public boolean dropPersonalTable(String name){
      return helper.deleteTable(name);
    }

    public Boolean isExistTable(String name){
        return helper.tableIsExist(name);
    }

}

