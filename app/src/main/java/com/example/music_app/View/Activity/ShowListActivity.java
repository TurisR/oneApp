package com.example.music_app.View.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
    private LinearLayout set_mode_layout;
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
                Song song = AppConstant.getInstance().getCurrentSongList().get(position);
                mPlayerUtil.play(song);
                AppConstant.getInstance().setPlayingSong(song);
                adapter.notifyDataSetChanged();
            }
        });
    }
    private void initView() {
        music_text_sum=findViewById(R.id.music_sum);
        music_type=findViewById(R.id.music_type);
        mode_music = findViewById(R.id.mode_music);
        play_mode=findViewById(R.id.play_mode);
        list_playing=findViewById(R.id.list_playing);
        text_none_song=findViewById(R.id.text_none_song);
        set_mode_layout=findViewById(R.id.set_mode_layout);
        divider=findViewById(R.id.divider);
        layout=(LinearLayout)findViewById(R.id.pop_layout);
        layout.setOnClickListener(this);
        findViewById(R.id.set_mode_layout).setOnClickListener(this);

        music_type.setText(mMusicType);

        switch (mMusicType){
            case AppConstant.DataType.CURRENT_MUSIC:
                mMList.addAll(AppConstant.getInstance().getCurrentSongList());
                set_mode_layout.setVisibility(View.VISIBLE);
                break;
            default:
                DataManager manager=new DataManager(this);
                mMList.addAll(manager.getPersonalAlbumSong(mMusicType));
               // set_mode_layout.setVerticalGravity(View.INVISIBLE);
                break;
        }
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
              //  Toast.makeText(this,"点击添加",Toast.LENGTH_LONG).show();
                Intent intent =new Intent(this, AddListActivity.class);
                intent.putExtra("ADD_TYPE",mMusicType); // 传字符串, 更多传值方法
                startActivity(intent);
                overridePendingTransition(R.anim.push_bottom_in,R.anim.push_bottom_out);
                finish();
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

    @Override
    protected void onResume() {
        super.onResume();

    }

    //实现onTouchEvent触屏函数但点击屏幕时销毁本Activity
    @Override
    public boolean onTouchEvent(MotionEvent event){
        //startActivity(new Intent(this,MainActivity.class));
        finish();
        return true;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.push_bottom_in, R.anim.push_bottom_out);
    }

}
