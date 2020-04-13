package com.example.music_app.View.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.music_app.Presenter.AppConstant;
import com.example.music_app.Presenter.DataManager;
import com.example.music_app.Presenter.PlayerUtil;
import com.example.music_app.R;
import com.example.music_app.View.Adapter.ShowListAdapter;
import com.example.music_app.mould.Model.bean.Song;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:歌曲展示列表页面
 * @author:
 * @createDate: 2019/11/10
 * @Modified By：
 * @version: 1.0
 */
public class ShowListActivity extends Activity implements View.OnClickListener{

    private TextView music_text_sum;
    private ListView list_playing;
    private PlayerUtil mPlayerUtil;
    private LinearLayout layout;
    private ShowListAdapter adapter;
    private TextView mode_music;
    private ImageView play_mode;
    private int mMode;
    private TextView text_none_song;
    private View divider;
    private LinearLayout set_mode_layout,add_more;
    private TextView music_type;
    private String mMusicType;
    private List<Song> mMList=new ArrayList<>();

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_list);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        getWindow().getAttributes().gravity= Gravity.BOTTOM;
        Intent intent = getIntent();
        // 根据key获取value
        mMusicType = intent.getStringExtra("MUSIC_TYPE");
        initView();
        initEvent();
    }

    private void initEvent() {
        mPlayerUtil=new PlayerUtil(this);
        setOnListViewItemClickListener();
    }

    private void setOnListViewItemClickListener() {
        list_playing.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AppConstant.getInstance().setCurrentSongList(AppConstant.getInstance().getList(mMusicType));
                Song song = AppConstant.getInstance().getCurrentSongList().get(position);
                mPlayerUtil.play(song);
                AppConstant.getInstance().setPlayingSong(song);
                Log.e("点击",""+position);
                //AppConstant.getInstance().setPlayingSong(song);
                list_playing.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }
        });
    }
    private void initView() {
        music_text_sum=findViewById(R.id.music_sum);
        add_more=findViewById(R.id.add_more);
        music_type=findViewById(R.id.music_type);
        mode_music = findViewById(R.id.mode_music);
        play_mode=findViewById(R.id.play_mode);
        list_playing=findViewById(R.id.list_playing);
        text_none_song=findViewById(R.id.text_none_song);
        set_mode_layout=findViewById(R.id.set_mode_layout);
        divider=findViewById(R.id.divider);
        layout=(LinearLayout)findViewById(R.id.pop_layout);
        layout.setOnClickListener(this);
        add_more.setOnClickListener(this);
        findViewById(R.id.set_mode_layout).setOnClickListener(this);
        music_type.setText(mMusicType);
        if(mMusicType.equals(AppConstant.DataType.CURRENT_MUSIC)){
            set_mode_layout.setVisibility(View.VISIBLE);
        }
        mMList.addAll(AppConstant.getInstance().getList( mMusicType));
        music_text_sum.setText("共用"+ mMList.size() +"首音乐");
        if(mMList.size()==0){
            list_playing.setVisibility(View.GONE);
            text_none_song.setVisibility(View.VISIBLE);
            text_none_song.setOnClickListener(this);
            divider.setVisibility(View.GONE);
        }else {
            list_playing.setVisibility(View.VISIBLE);
            text_none_song.setVisibility(View.GONE);
            divider.setVisibility(View.VISIBLE);
            adapter=new ShowListAdapter(this,mMusicType);
            list_playing.setAdapter(adapter);
        }
        mode_select(AppConstant.getInstance().getMode());
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pop_layout:
                Toast.makeText(getApplicationContext(), "提示：点击窗口外部关闭窗口！", Toast.LENGTH_SHORT).show();
                break;
            case R.id.set_mode_layout:
                mMode = AppConstant.getInstance().getMode();
                mMode++;
                mPlayerUtil.setMode(mMode%4);
                mode_select(mMode%4);
                AppConstant.getInstance().setMode(mMode %4);
                break;
            case R.id.text_none_song:
                addFuncation();
                break;
            case R.id.add_more:
                addFuncation();
                break;
        }

    }

    private void addFuncation() {
        Intent intent =new Intent(this, AddListActivity.class);
        intent.putExtra("ADD_TYPE",mMusicType); // 传字符串, 更多传值方法
        startActivity(intent);
        overridePendingTransition(R.anim.push_bottom_in,R.anim.push_bottom_out);
        finish();
    }

    private void mode_select(int mode) {
        switch (mode){
            case 0:
                play_mode.setImageResource(R.drawable.single_loop);
                mode_music.setText("单曲");
                break;
            case 1:
                play_mode.setImageResource(R.drawable.loop);
                mode_music.setText("循环");
                break;
            case 2:
                play_mode.setImageResource(R.drawable.order);
                mode_music.setText("循序");
                break;
            case 3:
                play_mode.setImageResource(R.drawable.random);
                mode_music.setText("随机");
                break;

        }
    }

    @Override
    protected void onResume() {
        super.onResume();

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
    }

}
