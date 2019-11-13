package com.example.music_app.View.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.example.music_app.View.Adapter.LocalMusicListAdapter;
import com.example.music_app.mould.Model.Model;
import com.example.music_app.mould.Model.bean.Song;

import java.util.ArrayList;
import java.util.List;



public class MusicListFragement extends Fragment {

    private LocalMusicListAdapter adapter = null;


    private android.widget.ListView ListView;
    private List<Song> mSongList=new ArrayList<>();
    private int Position;

    Intent service;
    private TextView music_text_sum;


    //接口
    CallBackValue callBackValue;
    /**
     * fragment与activity产生关联是  回调这个方法
     */
    @Override
    public void onAttach(Context context) {
        // TODO Auto-generated method stub
        super.onAttach(context );
        //当前fragment从activity重写了回调接口  得到接口的实例化对象
        callBackValue =(CallBackValue) getActivity();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragement_song_list,null);
        initData();
        initView(v);
        ListViewEvent();
        UpdataUI();
        return v;
    }

    private void UpdataUI() {

    }

    private void initData() {
        if(mSongList.size()==0){
            mSongList.addAll(Model.getInstance().getDBMananger().getSongDao().getSonglist());
        }

    }

    private void ListViewEvent() {
        adapter = new LocalMusicListAdapter(getActivity(),mSongList);
        ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                callBackValue.SendMessageValue(mSongList.get(position),position);
                View mView=view.findViewById(R.id.v_playing);
                Position=position;
                adapter.changeSelected(position);
                Toast.makeText(getActivity(),"播放"+mSongList.get(position).getTitle(),Toast.LENGTH_LONG);
                callBackValue.playMusic(position,mSongList.get(position));
            }

        });
        ListView.setAdapter(adapter);
    }

    private void initView(View v) {
        ListView = (ListView) v.findViewById(R.id.list_item_music);
        music_text_sum=v.findViewById(R.id.music_text_sum);
        music_text_sum.setText("共用"+Model.getInstance().getDBMananger().getSongDao().SongNum()+"首音乐");
    }


    //定义一个回调接口
    public interface CallBackValue{
        public void SendMessageValue(Song song,int position);
        public void playMusic(int action,Song song);
    }


}


