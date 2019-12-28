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
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.music_app.Presenter.AppConstant;
import com.example.music_app.Presenter.PlayerUtil;
import com.example.music_app.R;
import com.example.music_app.View.Adapter.PlayingListAdapter;
import com.example.music_app.View.widget.widgetLayout;


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
    private PlayingListAdapter mRecentListAdapter,mCollectListAdapter;
    private View contentView;
    private widgetLayout personal_love,personal_songList;

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
        set_recent.setTitle("最近播放");
        personal_love.setTitle("个人收藏");
        personal_love.setNumVisible(true);
        personal_songList.setTitle("个人歌单");
        personal_songList.setNumVisible(true);
        mRecentListAdapter = new PlayingListAdapter(getActivity(),AppConstant.getInstance().getRecentSongList());
        mCollectListAdapter = new PlayingListAdapter(getActivity(),AppConstant.getInstance().getPersonCollectSongList());


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
                        mRecentListAdapter = new PlayingListAdapter(getActivity(),AppConstant.getInstance().getRecentSongList());
                        set_recent.setListView(mRecentListAdapter);
                    }
                    set_recent.setNumVisible(true);
                    set_recent.setMoreText("最近播放"+AppConstant.getInstance().getRecentSongList().size()+"首歌");
                }else {
                    set_recent.setNumVisible(false);
                    set_recent.setNum("最近播放"+AppConstant.getInstance().getRecentSongList().size()+"首歌");
                }


            }
        });

        set_recent.setListListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AppConstant.getInstance().setCurrentSongList(AppConstant.getInstance().getRecentSongList());
                (new PlayerUtil(getActivity())).play(AppConstant.getInstance().getCurrentSongList().get(position));
                Toast.makeText(getActivity()," click "+position,Toast.LENGTH_LONG).show();
              //  mRecentListAdapter.notifyDataSetChanged();
            }
        });


        personal_love.setOnWidgetListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "个人收藏", Toast.LENGTH_SHORT).show();
                personal_love.setVisible(!personal_love.getVisible());
                if (personal_love.getVisible()) {
                    if(AppConstant.getInstance().getPersonCollectSongList().size()==0){
                        personal_love.setListVisible(true);
                    }else {
                        personal_love.setListVisible(false);
                        mCollectListAdapter = new PlayingListAdapter(getActivity(),AppConstant.getInstance().getPersonCollectSongList());
                        personal_love.setListView(mCollectListAdapter);
                    }
                    personal_love.setNumVisible(false);
                    mCollectListAdapter.notifyDataSetChanged();
                    personal_love.setNum("共有"+AppConstant.getInstance().getPersonCollectSongList().size()+"个收藏");
                   // set_recent.setListView(mPlayingListAdapter);
                   // isShow = !isShow;
                }else {
                    personal_love.setNumVisible(true);
                }

            }
        });


        personal_love.setListListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AppConstant.getInstance().setCurrentSongList(AppConstant.getInstance().getPersonCollectSongList());
                (new PlayerUtil(getActivity())).play(AppConstant.getInstance().getCurrentSongList().get(position));
                mCollectListAdapter.notifyDataSetChanged();
            }
        });


        personal_songList.setOnWidgetListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "个人歌单", Toast.LENGTH_SHORT).show();
                personal_songList.setVisible(!personal_songList.getVisible());
                if (personal_songList.getVisible()) {
                    if(AppConstant.getInstance().getPersonalSongAlbum().size()==0){
                        personal_songList.setListVisible(true);
                    }else {
                        personal_songList.setListVisible(false);
                    }

                    personal_songList.setNumVisible(false);
                    personal_songList.setNum("共有"+AppConstant.getInstance().getPersonalSongAlbum().size()+"个歌单");
                }else {
                    personal_songList.setNumVisible(true);
                }

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
                    personal_songList.setNum("共有"+AppConstant.getInstance().getPersonalSongAlbum().size()+"个歌单");
                    //Toast.makeText(getActivity(),"this++++",Toast.LENGTH_SHORT).show();
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
        Log.e("shuaxing","iiiii");


    }





}