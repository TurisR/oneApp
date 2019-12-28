package com.example.music_app.View.Fragment;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.music_app.Presenter.AppConstant;
import com.example.music_app.R;
import com.example.music_app.View.Activity.MainActivity;
import com.example.music_app.View.Adapter.LocalMusicListAdapter;
import com.example.music_app.mould.Model.Model;
import com.example.music_app.mould.Model.bean.Song;

import java.util.ArrayList;
import java.util.List;



public class MusicListFragment extends Fragment {

    private LocalMusicListAdapter adapter = null;

    private  LocalBroadcastManager broadcastManager;

    private android.widget.ListView ListView;
    private List<Song> mSongList=new ArrayList<>();
    private int Position;

    Intent service;
    private TextView music_text_sum;



    /**
     * fragment与activity产生关联是  回调这个方法
     */
    @Override
    public void onAttach(Context context) {
        // TODO Auto-generated method stub
        super.onAttach(context );
        //当前fragment从activity重写了回调接口  得到接口的实例化对象
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        receiveAdDownload();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragement_song_list,null);
        initData();
        initView(v);
        ListViewEvent();
        return v;
    }


    private void initData() {
        if(mSongList.size()==0){
            mSongList.addAll(AppConstant.getInstance().getLocalSongList());
        }

    }

    /**
     * 注册广播接收器
     */
    private void receiveAdDownload() {
        getActivity().registerReceiver(mAdDownLoadReceiver, new IntentFilter(AppConstant.MessageType.UPDATE_ACTION));
    }

    private void ListViewEvent() {
        adapter = new LocalMusicListAdapter(getActivity(),mSongList);
        ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                View mView=view.findViewById(R.id.v_playing);
                AppConstant.getInstance().setCurrentSongList(AppConstant.getInstance().getLocalSongList());
            //    Toast.makeText(getActivity(),"播放"+AppConstant.getInstance().getCurrentSongList().get(position).getTitle(),Toast.LENGTH_LONG).show();
                AppConstant.getInstance().getPlayerUtil(getActivity()).play(AppConstant.getInstance().getCurrentSongList().get(position));

            }

        });
        ListView.setAdapter(adapter);
    }

    private void initView(View v) {
        ListView = (ListView) v.findViewById(R.id.list_item_music);
        music_text_sum=v.findViewById(R.id.music_text_sum);
        music_text_sum.setText("共用"+AppConstant.getInstance().getLocalSongList().size()+"首音乐");
    }




    BroadcastReceiver mAdDownLoadReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()){
                case AppConstant.MessageType.UPDATE_ACTION:
                    ListView.setAdapter(adapter);
                  //  Toast.makeText(getActivity(),"this",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    public void onStop() {
        super.onStop();
        //broadcastManager.unregisterReceiver(mAdDownLoadReceiver);
        getActivity().unregisterReceiver(mAdDownLoadReceiver);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}


