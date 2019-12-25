package com.example.music_app.Presenter;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.music_app.mould.Model.Model;

public class MusicApplication extends Application {

    public static String[]permissionsREAD={
            Manifest.permission.READ_EXTERNAL_STORAGE, //权限存取读取
            Manifest.permission.WRITE_EXTERNAL_STORAGE };


    @Override
    public void onCreate() {
        super.onCreate();
        Model.getInstance().init(this,"user");
        AppConstant.getInstance().setPlayingState(AppConstant.PlayerMsg.PAUSE_MSG);
        //boolean permission = (PackageManager.PERMISSION_GRANTED == getPackageManager().checkPermission("android.permission.READ_EXTERNAL_STORAGE", "packageName"));
    }


    private static boolean lacksPermission(Context mContexts, String permission) {

     return ContextCompat.checkSelfPermission(mContexts, permission) == PackageManager.PERMISSION_DENIED;

    }





}
