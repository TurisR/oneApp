package com.example.music_app.Presenter;

import android.Manifest;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.CountDownTimer;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.example.music_app.mould.Model.Model;
import com.example.music_app.mould.Model.bean.Song;

import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class MusicApplication extends Application {

    private static Context context;
    private Song song;
    private int position;
    private int size;
    private DataManager mDataManager;
    private static CountDownTimer timer;



    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        Model.getInstance().init(this, "user");
        AppConstant.getInstance().setPlayingState(AppConstant.PlayerMsg.PAUSE_MSG);
//        TimeClock(1000);
    }



    public static CountDownTimer TimeClock(final int clockTime) {
        if(timer != null){
            timer.cancel();
        }
        timer = new CountDownTimer(clockTime, 1000) {
            /**
             * 固定间隔被调用,就是每隔countDownInterval会回调一次方法onTick
             * @param millisUntilFinished
             */
            @Override
            public void onTick(long millisUntilFinished) {
                Intent intent = new Intent();
                intent.setAction(AppConstant.MessageTimeType.TIME_COUNT);
                intent.putExtra("remain_time",millisUntilFinished/1000);
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

            }

            /**
             * 倒计时完成时被调用
             */
            @Override
            public void onFinish() {
                Intent intent = new Intent();
                intent.setAction(AppConstant.MessageTimeType.TIME_STOP);
                AppConstant.getInstance().getPlayerUtil(context).pause();
                AppConstant.getInstance().setClockTime(null);
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            }
        };
        return timer;

    }


    @Override
    public void onTerminate() {
        // 程序终止的时候执行
        Log.d("application", "onTerminate");
        Toast.makeText(context,"Terminate",Toast.LENGTH_LONG).show();
        mDataManager.upDate();
        Model.getInstance().getDBManager().close();
        super.onTerminate();
    }




}
