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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.music_app.Presenter.AppConstant;
import com.example.music_app.Presenter.PlayerUtil;
import com.example.music_app.R;
import com.example.music_app.View.Adapter.PlayingListAdapter;
import com.example.music_app.mould.Model.Model;

public class PlayinglistActivity extends Activity implements View.OnClickListener{

    private TextView music_text_sum;
    private TextView delete_play;
    private ListView list_playing;
    private boolean isSelectedAll = true;//用来控制点击全选，全选和全不选相互切换
    private PlayerUtil mPlayerUtil;
    private LinearLayout layout;
    private PlayingListAdapter adapter;

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_list);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mPlayerUtil=new PlayerUtil(this);
        initView();
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
        delete_play = findViewById(R.id.delete_music);
        list_playing=findViewById(R.id.list_playing);
        layout=(LinearLayout)findViewById(R.id.pop_layout);
        WindowManager wm = (WindowManager) this.getSystemService(WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        layout.setOnClickListener(this);
        music_text_sum.setText("共用"+ AppConstant.getInstance().getSongList().size() +"首音乐");

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pop_layout:
                Toast.makeText(getApplicationContext(), "提示：点击窗口外部关闭窗口！", Toast.LENGTH_SHORT).show();
                break;
            case R.id.delete:
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
