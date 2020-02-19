package com.example.music_app.View.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.music_app.Presenter.AppConstant;
import com.example.music_app.Presenter.DataManager;
import com.example.music_app.R;
import com.example.music_app.View.Adapter.AddListAdapter;
import com.example.music_app.View.Adapter.ScanListAdapter;
import com.example.music_app.View.Adapter.ShowListAdapter;
import com.example.music_app.View.widget.CustomDialog;
import com.example.music_app.mould.Model.Model;
import com.example.music_app.mould.Model.SQLite.DBHelper;
import com.example.music_app.mould.Model.bean.Song;

import java.util.ArrayList;
import java.util.List;

public class AddListActivity extends Activity implements View.OnClickListener{

    private String addType;
    private AddListAdapter adapter;
    private TextView album_create,music_add_title;
    private CheckBox select_all;
    private Song receive_song;
    private boolean isAlbum=false;
    private List<Song> mSongList= new ArrayList<>();
    private List<String> mAlbumList= new ArrayList<>();
    private List<String> mSelectAlbumNameList= new ArrayList<>();
    private LinearLayout select_all_linearLayout;
    private ListView list_select;
    private List<Song> mCheckedData = new ArrayList<>();//将选中数据放入里面
    private boolean isSelectedAll = true;//用来控制点击全选，全选和全不选相互切换
    private SparseBooleanArray stateCheckedMap = new SparseBooleanArray();//用来存放CheckBox的选中状态，true为选中,false为没有选中
    private Intent mIntent;
    private TextView select_all_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activiy_add_list);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        getWindow().getAttributes().gravity= Gravity.BOTTOM;
        mIntent = getIntent();
        // 根据key获取value
        addType = mIntent.getStringExtra("ADD_TYPE");
        initView();

    }

    private void initView() {

        album_create=findViewById(R.id.album_create);
        select_all=(CheckBox) findViewById(R.id.select_all_checkbox);
        music_add_title= findViewById(R.id.music_add_title);
        select_all_text=findViewById(R.id.select_all_text);
        list_select=findViewById(R.id.list_select);
        select_all_linearLayout=findViewById(R.id.select_all_linearLayout);
        select_all_linearLayout.setOnClickListener(this);
        findViewById(R.id.add_confirm).setOnClickListener(this);

        music_add_title.setText("添加音乐到“"+addType+"”");
        adapter = new AddListAdapter(this,addType,stateCheckedMap);
        list_select.setAdapter(adapter);
        switch (addType){
            case AppConstant.DataType.CURRENT_MUSIC:
                album_create.setVisibility(View.INVISIBLE);
                mSongList.addAll(AppConstant.getInstance().getLocalSongList());
                break;

            case AppConstant.DataType.PERSONAL_COLLECT:
                album_create.setVisibility(View.INVISIBLE);
//                AppConstant appConstant=AppConstant.getInstance();
//                if(appConstant.getPersonCollectSongList()!=null&&appConstant.getPersonCollectSongList().size()>0){
//                    for (Song song:appConstant.getLocalSongList()){
//                        if(!appConstant.isExist(song,appConstant.getPersonCollectSongList())){
//                            mSongList.add(song);
//                        }
//                    }
//                }else {
//                    mSongList.addAll(AppConstant.getInstance().getLocalSongList());
//                }
                mSongList.addAll(AppConstant.getInstance().getLocalSongList());
                break;

            default:
                isAlbum=true;
                mAlbumList.addAll(AppConstant.getInstance().getPersonalAlbumName());
                receive_song=(Song)mIntent.getSerializableExtra("ALBUM_SONG");
                album_create.setOnClickListener(this);
                break;
        }

        setOnListViewItemClickListener();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.select_all_linearLayout:
               // isSelectedAll=true;
                if(isSelectedAll){
                    select_all.setChecked(true);
                    select_all_text.setText("取消");
                }else {
                select_all.setChecked(false);
                select_all_text.setText("全选");
               }

                selectAll();
                break;
            case R.id.add_confirm:
                //Toast.makeText(this, "添加了"+mCheckedData.size()+"首音乐", Toast.LENGTH_SHORT).show();
                confirm();
                Toast.makeText(this, "添加了"+mCheckedData.size()+"首音乐", Toast.LENGTH_SHORT).show();
                break;

            case R.id.album_create:
                //Toast.makeText(this, "添加了"+mCheckedData.size()+"首音乐", Toast.LENGTH_SHORT).show();
                CustomDialog customDialog;
                customDialog = new CustomDialog(this,R.style.CustomDialog);
                customDialog.setType(1).setTitle("新 建 歌 单").setCancel(new CustomDialog.InOnCancelListener() {
                    @Override
                    public void onCancel(CustomDialog customDialog) {
                        customDialog.dismiss();
                    }
                }).setConfirm(new CustomDialog.InOnConfirmListener() {
                    @Override
                    public void onConfirm(CustomDialog customDialog) {
                        String string=customDialog.getEditText();
                        if(string!=null){
                            DBHelper helper= Model.getInstance().getDBManager().getHelper();
                            if(!helper.tableIsExist(string)){
                                helper.dynamicCreateTable(string);
                                if(helper.tableIsExist(string)){
                                    Toast.makeText(AddListActivity.this, "新建歌单“"+ string+"”成功", Toast.LENGTH_LONG).show();
                                    AppConstant.getInstance().addPersonalSongAlbum(string);
                                    adapter = new AddListAdapter(AddListActivity.this,addType,stateCheckedMap);
                                    list_select.setAdapter(adapter);
                                    customDialog.dismiss();
                                }else {
                                    Toast.makeText(AddListActivity.this, "新建歌单“"+ string+"”失败", Toast.LENGTH_LONG).show();
                                }
                            }else {
                                Toast.makeText(AddListActivity.this, "歌单“"+ string+"”已经存在", Toast.LENGTH_LONG).show();
                            }
                        }else {
                            Toast.makeText(AddListActivity.this, "输入为空", Toast.LENGTH_LONG).show();
                        }
                    }
                }).show();

                break;



        }

    }

    private void confirm() {
        if(isAlbum){
            DataManager manager=new DataManager(this);
            for(String string:mSelectAlbumNameList){
                manager.AddPersonalAlbumSong(string,receive_song);
            }
        }else {
            if(addType.equals(AppConstant.DataType.CURRENT_MUSIC)){
                AppConstant.getInstance().setCurrentSongList(mCheckedData,AppConstant.DataType.CURRENT_MUSIC);
            }else if(addType.equals(AppConstant.DataType.PERSONAL_COLLECT)){
                Log.e("添加",""+AppConstant.getInstance().getPersonCollectSongList().size());
               Boolean b= AppConstant.getInstance().addPersonCollectSongList(mCheckedData);
                Log.e("添加",b+""+AppConstant.getInstance().getPersonCollectSongList().size());
            }
        }
        if(addType.equals(AppConstant.DataType.CURRENT_MUSIC)){
            Intent intent =new Intent(this, ShowListActivity.class);
            intent.putExtra("MUSIC_TYPE",AppConstant.DataType.CURRENT_MUSIC); // 传字符串, 更多传值方法
            startActivity(intent);
            overridePendingTransition(R.anim.push_bottom_in,R.anim.push_bottom_out);
        }
        finish();
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
        list_select.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    /**
     * 设置所有CheckBox的选中状态
     * */
    private void setStateCheckedMap(boolean isSelectedAll) {

        int num=0;
        if(isAlbum){
            num=mAlbumList.size();
        }else {
            num=mSongList.size();
        }

        for (int i = 0; i < num; i++) {
            stateCheckedMap.put(i, isSelectedAll);
            list_select.setItemChecked(i, isSelectedAll);
        }
    }



    private void setOnListViewItemClickListener() {
        list_select.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              updateCheckBoxStatus(view, position);
               // Toast.makeText(AddListActivity.this, "添加"+position+"首音乐", Toast.LENGTH_SHORT).show();
              //  Log.e("click"," "+position);
            }
        });
    }

    private void updateCheckBoxStatus(View view, int position) {
        AddListAdapter.ViewHolder holder = (AddListAdapter.ViewHolder) view.getTag();
        holder.checkBox.toggle();//反转CheckBox的选中状态
        stateCheckedMap.put(position, holder.checkBox.isChecked());//存放CheckBox的选中状态

        if(!isAlbum){
            if (holder.checkBox.isChecked()) {
                mCheckedData.add(mSongList.get(position));//CheckBox选中时，把这一项的数据加到选中数据列表
            } else {
                mCheckedData.remove(mSongList.get(position));//CheckBox未选中时，把这一项的数据从选中数据列表移除
            }
        }else {
            if (holder.checkBox.isChecked()) {
                mSelectAlbumNameList.add(mAlbumList.get(position));//CheckBox选中时，把这一项的数据加到选中数据列表
            } else {
                mSelectAlbumNameList.remove(mAlbumList.get(position));//CheckBox未选中时，把这一项的数据从选中数据列表移除
            }

            //manager.AddPersonalAlbumSong(recieve_song,);
        }

        adapter.notifyDataSetChanged();
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
