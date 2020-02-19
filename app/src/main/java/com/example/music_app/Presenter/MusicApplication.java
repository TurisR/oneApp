package com.example.music_app.Presenter;

import android.Manifest;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.music_app.mould.Model.Model;
import com.example.music_app.mould.Model.bean.Song;

public class MusicApplication extends Application {

    private static Context context;
    private Song song;
    private int position;
    private int size;
    private DataManager mDataManager;
    private Data mData;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        Model.getInstance().init(this, "user");
        AppConstant.getInstance().setPlayingState(AppConstant.PlayerMsg.PAUSE_MSG);
        mData=new Data(context);
        mData.initData();
        mDataManager = new DataManager(context);
        mDataManager.initData();
    }



    @Override
    public void onTerminate() {
        // 程序终止的时候执行
        Log.d("application", "onTerminate");
        mDataManager.upDate();
        Model.getInstance().getDBManager().close();
        super.onTerminate();
    }


}
