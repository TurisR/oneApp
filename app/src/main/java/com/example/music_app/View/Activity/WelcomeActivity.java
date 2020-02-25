package com.example.music_app.View.Activity;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.example.music_app.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;



/**
 * Created by jinpei chen on 2017/5/25.
 */

public class WelcomeActivity extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
        * ****隐藏标题栏****
        * */
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);
        TimerTask t = new TimerTask() {
            @Override
            public void run() {
               //next();
                Intent in=new Intent(WelcomeActivity.this,MainActivity.class);
                startActivity(in);
                finish();
            }
        };
        //延时跳转
        Timer time=new Timer();
        time.schedule(t,1500);
    }//

}
