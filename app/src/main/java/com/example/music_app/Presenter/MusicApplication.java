package com.example.music_app.Presenter;

import android.app.Application;

import com.example.music_app.mould.Model.Model;

public class MusicApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Model.getInstance().init(this,"user");
        AppConstant.getInstance().setPlayingState(AppConstant.PlayerMsg.STOP_MSG);
    }

}
