package com.example.music_app.mould.Model;

import android.content.Context;

import com.example.music_app.mould.Model.SQLite.DBMananger;

import java.util.concurrent.ExecutorService;

public class Model {

    private Context mContext;
    private ExecutorService mExecutorService;
    private static Model model = new Model();
    private DBMananger mMananger;

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
        mMananger = new DBMananger(context, name);
    }

    public DBMananger getDBMananger() {
        return mMananger;
    }


}

