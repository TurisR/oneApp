package com.example.music_app.View.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.music_app.Presenter.AppConstant;
import com.example.music_app.R;
import com.example.music_app.mould.Model.bean.Song;

import java.util.ArrayList;
import java.util.List;

public class PlayingListAdapter extends BaseAdapter {

    private final Context mContext;
    private List<Song> mSongList=new ArrayList<>();
    private ViewHolder holder;
    private boolean isShowCheckBox = false;//表示当前是否是多选状态

    public PlayingListAdapter(Context context,boolean isSelectedAll) {
        mContext=context;
        //isShowCheckBox=isSelectedAll;
        if(!mSongList.isEmpty()){
            mSongList.clear();
        }
        mSongList.addAll(AppConstant.getInstance().getSongList());

    }

    @Override
    public int getCount() {
        return mSongList.size();
    }

    @Override
    public Object getItem(int position) {
        return mSongList.get(position);
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

        if(AppConstant.getInstance().getPosotion()==position){
            holder.mTitle.setTextColor(mContext.getResources().getColor(R.color.red));
        }else{
            holder.mTitle.setTextColor(mContext.getResources().getColor(R.color.black));
        }

        holder.mTitle.setText(mSongList.get(position).getTitle());
        showAndHideCheckBox();//控制CheckBox的那个的框显示与隐藏

        return convertView;
    }

    public class ViewHolder{
        public TextView mTitle;
        public CheckBox checkBox;
        public ImageView delete;
    }


    private void showAndHideCheckBox() {
        if (isShowCheckBox) {
            holder.checkBox.setVisibility(View.VISIBLE);
        } else {
            holder.checkBox.setVisibility(View.GONE);
        }
    }
    public boolean isShowCheckBox() {
        return isShowCheckBox;
    }

    public void setShowCheckBox(boolean showCheckBox) {
        isShowCheckBox = showCheckBox;
    }
}
