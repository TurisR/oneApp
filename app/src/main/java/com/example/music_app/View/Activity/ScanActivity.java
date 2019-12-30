package com.example.music_app.View.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.music_app.Presenter.AudioScan;
import com.example.music_app.R;
import com.example.music_app.View.Adapter.ScanListAdapter;
import com.example.music_app.mould.Model.Model;
import com.example.music_app.mould.Model.bean.Song;

import java.util.ArrayList;
import java.util.List;

public class ScanActivity extends AppCompatActivity implements View.OnClickListener {


    private ListView lvData;
    private LinearLayout mLlEditBar;//控制下方那一行的显示与隐藏
    private ScanListAdapter adapter;
    private List<Song> mSongList= new ArrayList<>();
    private List<Song> mCheckedData = new ArrayList<>();//将选中数据放入里面
    private SparseBooleanArray stateCheckedMap = new SparseBooleanArray();//用来存放CheckBox的选中状态，true为选中,false为没有选中
    private boolean isSelectedAll = true;//用来控制点击全选，全选和全不选相互切换
    private Button start_scan_btn;
    private AudioScan mAudioScan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        initView();
        adapter = new ScanListAdapter(ScanActivity.this, mSongList, stateCheckedMap);
        lvData.setAdapter(adapter);
        setOnListViewItemClickListener();
        //setOnListViewItemLongClickListener();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_cancel:
                sure();
                break;
            case R.id.ll_delete:
                delete();
                break;
            case R.id.ll_inverse:
                inverse();
                break;
            case R.id.ll_select_all:
                selectAll();
                break;
            case R.id.start_scan_btn:
                scan();
                break;
            case R.id.title_left_btn:
                Intent intent=new Intent(this,MainActivity.class);
                startActivity(intent);
                finish();
                break;

        }
    }

    private void scan() {
        start_scan_btn.setVisibility(View.GONE);
        mAudioScan=new AudioScan();
        if(mAudioScan!=null){
            mSongList.addAll(mAudioScan.getAllSongs(this));
            Toast.makeText(ScanActivity.this, "扫描到"+mSongList.size()+"音乐", Toast.LENGTH_SHORT).show();
        }
        setStateCheckedMap(false);
        adapter.notifyDataSetChanged();//更新ListView
    }

    private void sure() {
        Model.getInstance().getDBManager().getSongDao("本地歌曲").saveSongList(mCheckedData);
        Toast.makeText(ScanActivity.this, "存"+mCheckedData.size()+"音乐", Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(this,MainActivity.class);
        finish();
        startActivity(intent);
       // Toast.makeText(ScanActivity.this, "存音乐"+Model.getInstance().getDBMananger().getExit("table_song"), Toast.LENGTH_LONG).show();
    }

    private void delete() {
        if (mCheckedData.size() == 0) {
            Toast.makeText(ScanActivity.this, "您还没有选中任何数据！", Toast.LENGTH_SHORT).show();
            return;
        }
        mSongList.removeAll(mCheckedData);//删除选中数据
        setStateCheckedMap(false);//将CheckBox的所有选中状态变成未选中
        mCheckedData.clear();//清空选中数据
        adapter.notifyDataSetChanged();
        Toast.makeText(ScanActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
    }

    /**
     * 反选就是stateCheckedMap的值为true时变为false,false时变成true
     * */
    private void inverse() {
        mCheckedData.clear();
        for (int i = 0; i < mSongList.size(); i++) {
            if (stateCheckedMap.get(i)) {
                stateCheckedMap.put(i, false);
            } else {
                stateCheckedMap.put(i, true);
                mCheckedData.add(mSongList.get(i));
            }
            lvData.setItemChecked(i, stateCheckedMap.get(i));//这个好行可以控制ListView复用的问题，不设置这个会出现点击一个会选中多个
        }
        adapter.notifyDataSetChanged();
    }

    private void selectAll() {
        mCheckedData.clear();//清空之前选中数据
        if (isSelectedAll) {
            setStateCheckedMap(true);//将CheckBox的所有选中状态变成选中
            isSelectedAll = false;
            mCheckedData.addAll(mSongList);//把所有的数据添加到选中列表中
        } else {
            setStateCheckedMap(false);//将CheckBox的所有选中状态变成未选中
            isSelectedAll = true;
        }
        adapter.notifyDataSetChanged();
    }

    private void setOnListViewItemClickListener() {
        lvData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                updateCheckBoxStatus(view, position);
            }
        });
    }

    private void updateCheckBoxStatus(View view, int position) {
        ScanListAdapter.ViewHolder holder = (ScanListAdapter.ViewHolder) view.getTag();
        holder.checkBox.toggle();//反转CheckBox的选中状态
        lvData.setItemChecked(position, holder.checkBox.isChecked());//长按ListView时选中按的那一项
        stateCheckedMap.put(position, holder.checkBox.isChecked());//存放CheckBox的选中状态
        if (holder.checkBox.isChecked()) {
            mCheckedData.add(mSongList.get(position));//CheckBox选中时，把这一项的数据加到选中数据列表
        } else {
            mCheckedData.remove(mSongList.get(position));//CheckBox未选中时，把这一项的数据从选中数据列表移除
        }
        adapter.notifyDataSetChanged();
    }

    private void initView() {
        lvData = (ListView) findViewById(R.id.lv);
        mLlEditBar = findViewById(R.id.ll_edit_bar);
        start_scan_btn=(Button)findViewById(R.id.start_scan_btn);
        start_scan_btn.setOnClickListener(this);
        findViewById(R.id.title_left_btn).setOnClickListener(this);
        findViewById(R.id.ll_cancel).setOnClickListener(this);
        findViewById(R.id.ll_delete).setOnClickListener(this);
        findViewById(R.id.ll_inverse).setOnClickListener(this);
        findViewById(R.id.ll_select_all).setOnClickListener(this);
        lvData.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        mLlEditBar.setVisibility(View.VISIBLE);//显示下方布局
    }

    /**
     * 设置所有CheckBox的选中状态
     * */
    private void setStateCheckedMap(boolean isSelectedAll) {
        for (int i = 0; i < mSongList.size(); i++) {
            stateCheckedMap.put(i, isSelectedAll);
            lvData.setItemChecked(i, isSelectedAll);
        }
    }
}
