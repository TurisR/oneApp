package com.example.music_app.mould.Model.Table;

/**
 * @description:本地歌曲表，采用以歌名作为主键
 * @author: JiangJiaHui
 * @createDate: 2019/12/9
 * @Modified By：
 * @version: 1.0
 */

public class SongTable {

    public String TABLE_NAME="table_song";
    public String CREATE_TAB;
    public static final String COL_TITLE="title";
    public static final String COL_SINGER="singer";
    public static final String COL_DURATION="duration";
    public static final String COL_FILE_URL="fileUrl";

    public SongTable(String name) {
        this.TABLE_NAME = name;
    }

    public String getCreateTab(){
        return "create table if not exists "
                +TABLE_NAME+" ("
                +COL_TITLE+" text primary key,"
                +COL_SINGER+" text,"
                +COL_DURATION+" integer,"
                +COL_FILE_URL+" text);";
    }

    public String getTableName() {
         return TABLE_NAME;
    }


}


