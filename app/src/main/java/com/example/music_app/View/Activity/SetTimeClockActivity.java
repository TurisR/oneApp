package com.example.music_app.View.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.music_app.Presenter.AppConstant;
import com.example.music_app.Presenter.MusicApplication;
import com.example.music_app.R;
import com.example.music_app.View.widget.WheelView;

import java.util.ArrayList;
import java.util.List;

public class SetTimeClockActivity extends Activity implements View.OnClickListener{

    private List<String> timePick = new ArrayList<>();
    private String time ;
    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_timeclock);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        getWindow().getAttributes().gravity= Gravity.BOTTOM;
        Intent intent = getIntent();
        // 根据key获取value
        initData();
        initView();

    }

    private void initData() {
        if(AppConstant.getInstance().getClockTime()!= null){
            timePick.add("关闭定时");
        }
        timePick.add("1  分钟");
        timePick.add("5  分钟");
        timePick.add("10 分钟");
        timePick.add("15 分钟");
        timePick.add("20 分钟");
        timePick.add("25 分钟");
        timePick.add("30 分钟");

        WheelView wheelView = findViewById(R.id.wheel_view);
        wheelView.setOffset(1);// 对话框中当前项上面和下面的项数
        wheelView.setItems(timePick);// 设置数据源
        wheelView.setSeletion(0);// 默认选中第三项
        wheelView.setOnWheelViewListener(new WheelView.OnWheelViewListener(){
            @Override
            public void onSelected(int selectedIndex, String item) {
                time = item;
            }
        });


    }

    private void initView() {
        findViewById(R.id.setClock_cancel).setOnClickListener(this);
        findViewById(R.id.setClock_confirm).setOnClickListener(this);

    }


    //实现onTouchEvent触屏函数但点击屏幕时销毁本Activity
    @Override
    public boolean onTouchEvent(MotionEvent event){
        finish();
        return true;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.push_bottom_in, R.anim.push_bottom_out);


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

            }
        });
        thread.start();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.setClock_cancel:
                finish();
                break;
            case R.id.setClock_confirm:
                if(time.equals("关闭定时")){
                    AppConstant.getInstance().setClockTime(null);
                    MusicApplication.TimeClock(AppConstant.getInstance().getClockIntTime()).cancel();
                }else {
                    AppConstant.getInstance().setClockTime(time);
                    MusicApplication.TimeClock(AppConstant.getInstance().getClockIntTime()).start();
                }
                finish();
                break;
        }

    }
}
