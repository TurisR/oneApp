package com.example.music_app.Presenter;

import android.Manifest;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.music_app.mould.Model.Model;
import com.example.music_app.mould.Model.SQLite.DBHelper;
import com.example.music_app.mould.Model.bean.Song;

public class MusicApplication extends Application {

    private static Context context;
    private Song song;
    private int position;
    private int size;

    //获取上下文
    public static Context getAppContext(){
        return context.getApplicationContext();
    }

    public static String[]permissionsREAD={
            Manifest.permission.READ_EXTERNAL_STORAGE, //权限存取读取
            Manifest.permission.WRITE_EXTERNAL_STORAGE };

    @Override
    public void onCreate() {
        super.onCreate();
        context=this;
        Model.getInstance().init(this,"user");
        AppConstant.getInstance().setPlayingState(AppConstant.PlayerMsg.PAUSE_MSG);
        //
        receiveAdDownload();
        Boolean aBoolean1= (new DBHelper(context)).tableIsExist("本地歌曲");
        Boolean aBoolean2= (new DBHelper(context)).tableIsExist("个人收藏");
        Boolean aBoolean3= (new DBHelper(context)).tableIsExist("最近播放");
        Toast.makeText(context,"音乐播放器 "+aBoolean1+" "+aBoolean2+" "+aBoolean3,Toast.LENGTH_SHORT).show();
    }


    private static boolean lacksPermission(Context mContexts, String permission) {

     return ContextCompat.checkSelfPermission(mContexts, permission) == PackageManager.PERMISSION_DENIED;

    }

    private void receiveAdDownload() {

        IntentFilter filter = new IntentFilter();
        // 指定BroadcastReceiver监听的Action
        filter.addAction(AppConstant.MessageType.MUSIC_NEXT);
        filter.addAction(AppConstant.MessageType.MUSIC_PREVIOUS);
        context.registerReceiver(mAdDownLoadReceiver, filter);
    }



    BroadcastReceiver mAdDownLoadReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()){
                case AppConstant.MessageType.MUSIC_NEXT:
                   // Toast.makeText(context,"+音乐播放器下一首+",Toast.LENGTH_SHORT).show();
                   song= AppConstant.getInstance().getSwitchSong().getNextSong(true);
                    break;
                case AppConstant.MessageType.MUSIC_PREVIOUS:
                    //Toast.makeText(context,"+音乐播放器上一首+",Toast.LENGTH_SHORT).show();
                   song= AppConstant.getInstance().getSwitchSong().getNextSong(true);
                    break;
            }
            AppConstant.getInstance().getPlayerUtil(context).play(song);
        }


    };





    @Override
    public void onTerminate() {
        // 程序终止的时候执行
        Log.d("application", "onTerminate");
        context.unregisterReceiver(mAdDownLoadReceiver);
        super.onTerminate();
    }









}
