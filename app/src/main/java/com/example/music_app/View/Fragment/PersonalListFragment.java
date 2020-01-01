package com.example.music_app.View.Fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.music_app.Presenter.AppConstant;
import com.example.music_app.Presenter.PlayerUtil;
import com.example.music_app.R;
import com.example.music_app.View.Activity.AddListActivity;
import com.example.music_app.View.Activity.MainActivity;
import com.example.music_app.View.Activity.ShowListActivity;
import com.example.music_app.View.Adapter.ShowListAdapter;
import com.example.music_app.View.widget.CustomDialog;
import com.example.music_app.View.widget.widgetLayout;
import com.example.music_app.mould.Model.Model;
import com.example.music_app.mould.Model.SQLite.DBHelper;


/**
 * Created by yifeng on 16/8/3.
 *
 */
public class PersonalListFragment extends Fragment  {

    private static final String EXTRA_CONTENT = "content";
    private TextView recent_play_num;
    private int num = 0;
    private LinearLayout recent_play_layout;
    private widgetLayout set_recent;
    private boolean isShow = false;
    private ShowListAdapter mRecentListAdapter,mCollectListAdapter,mPersonalAlbumAdapter;
    private View contentView;
    private widgetLayout personal_love,personal_songList;
    private ScrollView myScrollView;

    public static PersonalListFragment newInstance(String content) {
        Bundle arguments = new Bundle();
        arguments.putString(EXTRA_CONTENT, content);
        PersonalListFragment personalListFragment = new PersonalListFragment();
        personalListFragment.setArguments(arguments);
        return personalListFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_personal_list, null);
        initView();
        return contentView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initEvent();

    }

    private void initView() {
        set_recent = contentView.findViewById(R.id.recent);
        personal_love = contentView.findViewById(R.id.personal_love);
        personal_songList = contentView.findViewById(R.id.personal_songList);
        myScrollView=contentView.findViewById(R.id.myScrollView);
        set_recent.setTitle("最近播放");
        personal_love.setTitle("个人收藏");
        personal_love.setNumVisible(true);
        personal_songList.setTitle("个人歌单");
        personal_songList.setNumVisible(true);


    }

    private void initEvent() {
        set_recent.setNum("最近播放"+AppConstant.getInstance().getRecentSongList().size()+"首歌");
        set_recent.setOnWidgetListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set_recent.setVisible(!set_recent.getVisible());
                if (set_recent.getVisible()) {
                    if(AppConstant.getInstance().getRecentSongList().size()==0){
                        set_recent.setListVisible(true);
                    }else {
                        set_recent.setListVisible(false);
                        mRecentListAdapter = new ShowListAdapter(getActivity(),AppConstant.DataType.RECENT_MUSIC);
                        set_recent.setListView(mRecentListAdapter);
                    }
                    set_recent.setNumVisible(true);
                   // set_recent.setMoreVisible(false);
                    set_recent.setMoreText("最近播放"+AppConstant.getInstance().getRecentSongList().size()+"首歌");
                }else {
                    set_recent.setNumVisible(false);
                    //set_recent.setListVisible(false);
                    set_recent.setNum("最近播放"+AppConstant.getInstance().getRecentSongList().size()+"首歌");
                }


            }
        });

        set_recent.setListListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AppConstant.getInstance().setCurrentSongList(AppConstant.getInstance().getRecentSongList(),AppConstant.DataType.RECENT_MUSIC);
                (new PlayerUtil(getActivity())).play(AppConstant.getInstance().getCurrentSongList().get(position));
                //Toast.makeText(getActivity()," click "+position,Toast.LENGTH_LONG).show();
              //  mRecentListAdapter.notifyDataSetChanged();
            }
        });


        personal_love.setOnWidgetListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(getActivity(), "个人收藏", Toast.LENGTH_SHORT).show();
                personal_love.setVisible(!personal_love.getVisible());
                if (personal_love.getVisible()) {
                    if(AppConstant.getInstance().getPersonCollectSongList().size()==0){
                        personal_love.setListVisible(true);
                    }else {
                        personal_love.setListVisible(false);
                        mCollectListAdapter = new ShowListAdapter(getActivity(),AppConstant.DataType.PERSONAL_COLLECT);
                        personal_love.setListView(mCollectListAdapter);
                    }
                    personal_love.setNumVisible(false);
                    personal_love.setNum("共有"+AppConstant.getInstance().getPersonCollectSongList().size()+"个收藏");

                }else {
                    personal_love.setNumVisible(true);
                }

            }
        });


        personal_love.setListListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AppConstant.getInstance().setCurrentSongList(AppConstant.getInstance().getPersonCollectSongList(),AppConstant.DataType.PERSONAL_COLLECT);
                (new PlayerUtil(getActivity())).play(AppConstant.getInstance().getCurrentSongList().get(position));
                mCollectListAdapter.notifyDataSetChanged();
            }
        });

        personal_love.setMoreListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                personal_love.setVisible(!personal_love.getVisible());
                personal_love.setNumVisible(true);
                Intent intent =new Intent(getActivity(), AddListActivity.class);
                intent.putExtra("ADD_TYPE",AppConstant.DataType.PERSONAL_COLLECT); // 传字符串, 更多传值方法
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_bottom_in,R.anim.push_bottom_out);
            }
        });


        personal_songList.setOnWidgetListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), "个人歌单", Toast.LENGTH_SHORT).show();
                personal_songList.setVisible(!personal_songList.getVisible());
                if (personal_songList.getVisible()) {

                    if(AppConstant.getInstance().getPersonalAlbumName().size()==0){
                        personal_songList.setListVisible(true);
                    }else {
                        personal_songList.setListVisible(false);
                        mPersonalAlbumAdapter=new ShowListAdapter(getActivity(),AppConstant.DataType.PERSONAL_ALBUM_NAME);
                        personal_songList.setListView(mPersonalAlbumAdapter);
                    }

                    personal_songList.setNumVisible(false);
                    personal_songList.setNum("共有"+AppConstant.getInstance().getPersonalAlbumName().size()+"个歌单");
                }else {
                    personal_songList.setNumVisible(true);
                }
            }
        });


        personal_songList.setMoreListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                personal_songList.setVisible(!personal_songList.getVisible());
                personal_songList.setNumVisible(true);
                CustomDialog customDialog;
                customDialog = new CustomDialog(getActivity(),R.style.CustomDialog);
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
                                    Toast.makeText(getActivity(), "新建歌单“"+ string+"”成功", Toast.LENGTH_LONG).show();
                                    AppConstant.getInstance().addPersonalSongAlbum(string);
                                    personal_songList.setNum("共有"+AppConstant.getInstance().getPersonalAlbumName().size()+"个歌单");
                                    personal_songList.setListVisible(false);
                                    mPersonalAlbumAdapter=new ShowListAdapter(getActivity(),AppConstant.DataType.PERSONAL_ALBUM_NAME);
                                    personal_songList.setListView(mPersonalAlbumAdapter);
                                    customDialog.dismiss();
                                }else {
                                    Toast.makeText(getActivity(), "新建歌单“"+ string+"”失败", Toast.LENGTH_LONG).show();
                                }
                            }else {
                                Toast.makeText(getActivity(), "歌单“"+ string+"”已经存在", Toast.LENGTH_LONG).show();
                            }
                        }else {
                            Toast.makeText(getActivity(), "输入为空", Toast.LENGTH_LONG).show();
                        }




                    }
                }).show();
            }
        });



        personal_songList.setListListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent =new Intent(getActivity(), ShowListActivity.class);
                intent.putExtra("MUSIC_TYPE",AppConstant.getInstance().getPersonalAlbumName().get(position)); // 传字符串, 更多传值方法
                startActivity(intent);
                //overridePendingTransition(R.anim.push_bottom_in,R.anim.push_bottom_out);
            }
        });


        resolve(set_recent.getListView());
        resolve(personal_love.getListView());
        resolve(personal_songList.getListView());


    }

    private void resolve(ListView listView) {
        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Log.d("MainActivity","onTouch");
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    //点击listview里面滚动停止时，scrollview拦截listview的触屏事件，就是scrollview该滚动了
                    myScrollView.requestDisallowInterceptTouchEvent(false);
                } else {
                    //当listview在滚动时，不拦截listview的滚动事件；就是listview可以滚动，
                    myScrollView.requestDisallowInterceptTouchEvent(true);
                }
                return false;
            }
        });
    }


    BroadcastReceiver mAdDownLoadReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()){
                case AppConstant.MessageType.UPDATE_ACTION:
                    if(mRecentListAdapter!=null){
                        mRecentListAdapter.notifyDataSetChanged();
                    }
                    if(mCollectListAdapter!=null){
                        mCollectListAdapter.notifyDataSetChanged();
                    }
                    personal_songList.setNum("共有"+AppConstant.getInstance().getPersonalAlbumName().size()+"个歌单");
                    set_recent.setNum("最近播放"+AppConstant.getInstance().getRecentSongList().size()+"首歌");
                    break;
            }
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        receiveAdDownload();
    }
    /**
     * 注册广播接收器
     */
    private void receiveAdDownload() {
        getActivity().registerReceiver(mAdDownLoadReceiver, new IntentFilter(AppConstant.MessageType.UPDATE_ACTION));
    }

    @Override
    public void onStop() {
        super.onStop();
        //broadcastManager.unregisterReceiver(mAdDownLoadReceiver);
        getActivity().unregisterReceiver(mAdDownLoadReceiver);
    }



    @Override
    public void onResume() {
        super.onResume();
        //Toast.makeText(getActivity(),"刷新1",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(!isVisibleToUser){
            if(personal_love!=null){
                personal_love.setVisible(false);
                set_recent.setVisible(false);
                set_recent.setNumVisible(false);
                personal_songList.setNumVisible(true);
                personal_songList.setVisible(false);
                personal_love.setNumVisible(true);
                Log.e("刷新","personalListFragment");
            }

        }


    }





}
