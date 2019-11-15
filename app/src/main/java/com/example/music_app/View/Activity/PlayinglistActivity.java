package com.example.music_app.View.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.music_app.Presenter.AppConstant;
import com.example.music_app.Presenter.PlayerUtil;
import com.example.music_app.R;
import com.example.music_app.View.Adapter.PlayingListAdapter;


public class PlayinglistActivity extends Activity implements View.OnClickListener{

    private TextView music_text_sum;
    private ListView list_playing;
    private boolean isSelectedAll = true;//用来控制点击全选，全选和全不选相互切换
    private PlayerUtil mPlayerUtil;
    private LinearLayout layout;
    private PlayingListAdapter adapter;
    private TextView mode_music;
    private ImageView play_mode;
    private int mMode;

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_list);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        initView();
        initEvent();

    }

    private void initEvent() {
        mPlayerUtil=new PlayerUtil(this);
        adapter=new PlayingListAdapter(this,isSelectedAll);
        list_playing.setAdapter(adapter);
        setOnListViewItemClickListener();
    }

    private void setOnListViewItemClickListener() {
        list_playing.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               mPlayerUtil.play(position);
                adapter.notifyDataSetChanged();

            }
        });
    }
    private void initView() {
        music_text_sum=findViewById(R.id.music_sum);
        mode_music = findViewById(R.id.mode_music);
        play_mode=findViewById(R.id.play_mode);
        list_playing=findViewById(R.id.list_playing);
        layout=(LinearLayout)findViewById(R.id.pop_layout);
        layout.setOnClickListener(this);
        findViewById(R.id.mode).setOnClickListener(this);
        music_text_sum.setText("共用"+ AppConstant.getInstance().getSongList().size() +"首音乐");

        mode_select(AppConstant.getInstance().getMode());
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pop_layout:
                Toast.makeText(getApplicationContext(), "提示：点击窗口外部关闭窗口！", Toast.LENGTH_SHORT).show();
                break;
            case R.id.mode:
                mMode = AppConstant.getInstance().getMode();
                mMode++;
                mPlayerUtil.setMode(mMode%4);
                mode_select(mMode%4);
                AppConstant.getInstance().setMode(mMode %4);
                break;
        }

    }

    private void mode_select(int mode) {
        switch (mode){
            case 0:
                play_mode.setImageResource(R.drawable.single_loop);
                mode_music.setText("单曲播放");
                break;
            case 1:
                play_mode.setImageResource(R.drawable.loop);
                mode_music.setText("循环播放");
                break;
            case 2:
                play_mode.setImageResource(R.drawable.order);
                mode_music.setText("循序播放");
                break;
            case 3:
                play_mode.setImageResource(R.drawable.random);
                mode_music.setText("随机播放");
                break;

        }
    }

    //实现onTouchEvent触屏函数但点击屏幕时销毁本Activity
    @Override
    public boolean onTouchEvent(MotionEvent event){
        //startActivity(new Intent(this,MainActivity.class));
        finish();
        return true;
    }
}
