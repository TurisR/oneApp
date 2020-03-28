package com.example.music_app.View.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.example.music_app.Presenter.AppConstant;
import com.example.music_app.Presenter.MusicService;
import com.example.music_app.Presenter.PlayerUtil;
import com.example.music_app.R;

public class SensorActivity extends Activity implements View.OnClickListener{

    private Switch aSwitch;
    private PlayerUtil playerUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_switch);

        playerUtil = new PlayerUtil(this);

        intiButton();
        intiView();
    }

    private void intiView() {
            aSwitch.setChecked(AppConstant.getInstance().getSensorState());
    }

    private void intiButton() {
        findViewById(R.id.title_left_btn2).setOnClickListener(this);        //返回按键

        aSwitch = findViewById(R.id.sensor_switch_btn);                     //摇摇切歌开关
        //开关按键监听
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    if (!AppConstant.getInstance().getSensorState())
                    {
                        playerUtil.openSensor();
                        aSwitch.setChecked(true);
                        Toast.makeText(SensorActivity.this, "开启摇摇切歌", Toast.LENGTH_LONG).show();
                    }

                }
                else
                {
                    if (AppConstant.getInstance().getSensorState())
                    {
                        playerUtil.closeSensor();
                        aSwitch.setChecked(false);
                        Toast.makeText(SensorActivity.this, "关闭摇摇切歌", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_left_btn2:
                finish();
                break;
        }
    }
}
