package com.example.music_app.mould.Model.Table;

import com.example.music_app.mould.Model.Model;

public class SongTable {
    /*public static final String TABLE_NAME="table_song";
   // public static final String COL_TYPE="music_type";
    public static final String COL_TITLE="title";
    public static final String COL_SINGER="singer";
   // public static final String COL_ALBUM="album";
   // public static final String COL_YEAR="year";
    public static final String COL_DURATION="duration";
  //  public static final String COL_FILENAME="fileName";
    public static final String COL_FILEURL="fileUrl";
   // public static final String COL_FIlESIZE="fileSize";

    public static final String CREATE_TAB="create table "
            +TABLE_NAME+" ("
            +COL_TITLE+" text primary key,"
            +COL_SINGER+" text,"
            +COL_DURATION+" text,"
            +COL_FILEURL+" text);";*/

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


