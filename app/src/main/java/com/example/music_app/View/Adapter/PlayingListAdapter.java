package com.example.music_app.View.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.music_app.Presenter.AppConstant;
import com.example.music_app.R;
import com.example.music_app.mould.Model.bean.Song;

import java.util.ArrayList;
import java.util.List;

public class PlayingListAdapter extends BaseAdapter {

    private final Context mContext;
    private List<Song> mSongList=new ArrayList<>();
    private List<String> albumName=new ArrayList<>();
    private ViewHolder holder;
    private boolean isTitle=false;


    public PlayingListAdapter(Context context,List<Song> songList) {
        mContext=context;
        //isShowCheckBox=isSelectedAll;
        if(!mSongList.isEmpty()){
            mSongList.clear();
        }
        mSongList.addAll(songList);

    }

    public PlayingListAdapter(Context context,List<String> strings,boolean isTitle) {
        mContext=context;
        this.isTitle=isTitle;
        //isShowCheckBox=isSelectedAll;
        if(!albumName.isEmpty()){
            albumName.clear();
        }
        albumName.addAll(strings);

    }



    @Override
    public int getCount() {
        return isTitle ? albumName.size():mSongList.size();
    }

    @Override
    public Object getItem(int position) {
        return isTitle ? albumName.get(position):mSongList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.item_playing_list, null);
            convertView.setTag(holder);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);
            holder.mTitle = convertView.findViewById(R.id.play_name);
            holder.delete=convertView.findViewById(R.id.delete);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (mSongList!=null&&AppConstant.getInstance().getPlayingSong()!=null){
            if(AppConstant.getInstance().getPlayingSong().Equals(mSongList.get(position))){
                holder.mTitle.setTextColor(mContext.getResources().getColor(R.color.blue));
                holder.mTitle.setSelected(true);
            }else{
                holder.mTitle.setTextColor(mContext.getResources().getColor(R.color.black));
                holder.mTitle.setSelected(false);
            }
        }

        holder.mTitle.setText(mSongList.get(position).getTitle());

        return convertView;
    }

    public class ViewHolder{
        public TextView mTitle;
        public CheckBox checkBox;
        public ImageView delete;
    }



}
