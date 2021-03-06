package com.example.music_app.Presenter;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.example.music_app.mould.Model.bean.Song;

import java.util.ArrayList;
/**
 * @description:歌曲扫描类，利用contentProvider扫描本地文件获取数据
 * @author: JiangJiaHui
 * @createDate: 2019/11/10
 * @Modified By：
 * @version: 1.0
 */

public class AudioScan {
    /**
     * 获取sd卡所有的音乐文件
     *
     * @return
     * @throws Exception
     */
    public static ArrayList<Song> getAllSongs(Context context) {
        ArrayList<Song> songs = null;
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[] { MediaStore.Audio.Media._ID,
                        MediaStore.Audio.Media.DISPLAY_NAME,
                        MediaStore.Audio.Media.TITLE,
                        MediaStore.Audio.Media.DURATION,
                        MediaStore.Audio.Media.ARTIST,
                        MediaStore.Audio.Media.ALBUM,
                        MediaStore.Audio.Media.YEAR,
                        MediaStore.Audio.Media.MIME_TYPE,
                        MediaStore.Audio.Media.SIZE,
                        MediaStore.Audio.Media.DATA },
                MediaStore.Audio.Media.MIME_TYPE + "=? or "
                        + MediaStore.Audio.Media.MIME_TYPE + "=?",
                new String[] { "audio/mpeg", "audio/x-ms-wma" }, null);

        songs = new ArrayList<Song>();

        if (cursor.moveToFirst()) {

            Song song = null;

            do {
                song = new Song();
                // 文件名
                song.setFileName(cursor.getString(1));
                // 时长
                song.setDuration(cursor.getInt(3));
                // 歌手名
                String string =cursor.getString(4);
                if(string.equals("<unknown>")){
                    string = cursor.getString(2);
                    if(string.indexOf('-')==-1){
                        song.setTitle(string);
                        song.setSinger("未知");
                    }else {
                        song.setTitle(string.substring(string.indexOf('-')+1).trim());
                        song.setSinger(string.substring(0,string.indexOf('-')).trim());
                    }


                }else {
                    song.setTitle(cursor.getString(2).trim());
                    song.setSinger(cursor.getString(4).trim());
                }




                // 专辑名
                song.setAlbum(cursor.getString(5));
                // 年代
                if (cursor.getString(6) != null) {
                    song.setYear(cursor.getString(6));
                } else {
                    song.setYear("未知");
                }
                // 歌曲格式
                if ("audio/mpeg".equals(cursor.getString(7).trim())) {
                    song.setType("mp3");
                } else if ("audio/x-ms-wma".equals(cursor.getString(7).trim())) {
                    song.setType("wma");
                }
                // 文件大小
                if (cursor.getString(8) != null) {
                    float size = cursor.getInt(8) / 1024f / 1024f;
                    song.setSize((size + "").substring(0, 4) + "M");
                } else {
                    song.setSize("未知");
                }
                // 文件路径
                if (cursor.getString(9) != null) {
                    song.setFileUrl(cursor.getString(9));
                }
//                if(!AppConstant.getInstance().isExist(song,AppConstant.getInstance().getLocalSongList())){
                songs.add(song);
//                }
            } while (cursor.moveToNext());

            cursor.close();

        }




        return songs;
    }

    /**
     * 定义一个方法用来格式化获取到的时间
     */
    public static String formatTime(int time) {
        if (time / 1000 % 60 < 10) {
            return time / 1000 / 60 + ":0" + time / 1000 % 60;

        } else {
            return time / 1000 / 60 + ":" + time / 1000 % 60;
        }

    }

}
