package com.example.music_app.View.Fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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
    private PlayingListAdapter mPlayingListAdapter;
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
        set_recent.setNum("最近播放"+AppConstant.getInstance().getRecentSongList().size()+"首歌");
        personal_love.setTitle("个人收藏");
        personal_love.setNumVisible(true);
        personal_songList.setTitle("个人歌单");
        personal_songList.setNumVisible(true);
        mPlayingListAdapter = new PlayingListAdapter(getActivity(),AppConstant.getInstance().getRecentSongList());

    }

    private void initEvent() {


        set_recent.setOnWidgetListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set_recent.setVisible(!set_recent.getVisible());
                if (set_recent.getVisible()) {
                    if(AppConstant.getInstance().getRecentSongList().size()==0){
                        set_recent.setListVisible(true);
                    }else {
                        set_recent.setListVisible(false);
                        set_recent.setListView(mPlayingListAdapter);
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
                Toast.makeText(getActivity(), "最近播放"+position, Toast.LENGTH_SHORT).show();
                AppConstant.getInstance().setCurrentSongList(AppConstant.getInstance().getRecentSongList());
                (new PlayerUtil(getActivity())).play(position);
                 mPlayingListAdapter.notifyDataSetChanged();
                 AppConstant.getInstance().getPlayerUtil(getActivity()).setPlayList();
            }
        });


        personal_love.setOnWidgetListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "个人收藏", Toast.LENGTH_SHORT).show();
                personal_love.setVisible(!personal_love.getVisible());
                if (personal_love.getVisible()) {
                    if(AppConstant.getInstance().getPersonalSongAlbum().size()==0){
                        personal_love.setListVisible(true);
                    }else {
                        personal_love.setListVisible(false);
                    }
                    personal_love.setNumVisible(false);
                    personal_love.setNum("共有"+AppConstant.getInstance().getPersonalSongAlbum().size()+"个收藏");
                   // set_recent.setListView(mPlayingListAdapter);
                   // isShow = !isShow;
                }else {
                    personal_love.setNumVisible(true);
                }

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
                    mPlayingListAdapter = new PlayingListAdapter(getActivity(),AppConstant.getInstance().getRecentSongList());
                    set_recent.setListView(mPlayingListAdapter);
                    mPlayingListAdapter.notifyDataSetChanged();
                    personal_songList.setNum("共有"+AppConstant.getInstance().getPersonalSongAlbum().size()+"个歌单");
                    //Toast.makeText(getActivity(),"this++++",Toast.LENGTH_SHORT).show();
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





}
