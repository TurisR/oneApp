package com.example.music_app.mould.Model;

import android.content.Context;

import com.example.music_app.mould.Model.SQLite.DBManager;

import java.util.concurrent.ExecutorService;

public class Model {

    private Context mContext;
    private ExecutorService mExecutorService;
    private static Model model = new Model();
    private DBManager mManager;

    private Model() {

    }

    public static Model getInstance() {
        return model;
    }

    public ExecutorService getGlobalTreadPool() {
        return mExecutorService;
    }

    public void init(Context context, String name) {
        mContext = context;
        mManager = new DBManager(context);
    }

    public DBManager getDBManager() {
        return mManager;
    }


}

