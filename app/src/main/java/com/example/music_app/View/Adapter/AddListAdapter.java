package com.example.music_app.View.Adapter;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.music_app.Presenter.AppConstant;
import com.example.music_app.Presenter.DataManager;
import com.example.music_app.R;
import com.example.music_app.mould.Model.bean.Song;

import java.util.ArrayList;
import java.util.List;
/**
 * @description:添加歌曲的listAdapter
 * @author:JiangJiaHui
 * @createDate: 2019/11/10
 * @Modified By：
 * @version: 1.0
 */

public class AddListAdapter extends BaseAdapter {

    List<Song> mSongList = new ArrayList<>();
    List<String> albumName = new ArrayList<>();
    private boolean isTitle=false;
    private Context mContext;
    private String Name;
    ViewHolder holder;
    private boolean isShowCheckBox = false;//表示当前是否是多选状态。
    private SparseBooleanArray stateCheckedMap = new SparseBooleanArray();//用来存放CheckBox的选中状态，true为选中,false为没有选中

    public AddListAdapter(Context context, String name, SparseBooleanArray stateCheckedMap) {
        mContext=context;
        //isShowCheckBox=isSelectedAll;
        if(!mSongList.isEmpty()){
            mSongList.clear();
        }
        Name=name;
        switch (name){
            case AppConstant.DataType.PERSONAL_ALBUM_NAME:
                isTitle=true;
                albumName.addAll(AppConstant.getInstance().getAlbumList());
                break;
            default:
                mSongList.addAll(AppConstant.getInstance().getAddSongList(name));

        }
        this.stateCheckedMap = stateCheckedMap;
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
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.item_add_list, null);
            convertView.setTag(holder);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.select_add);
            holder.mTitle = convertView.findViewById(R.id.tv_title);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if(!isTitle){
            holder.mTitle.setText(mSongList.get(position).getTitle());
        }else {
            holder.mTitle.setText(albumName.get(position));
        }

        holder.checkBox.setChecked(stateCheckedMap.get(position));//设置CheckBox是否选中
        return convertView;
    }

    public class ViewHolder {
        public TextView mTitle;
        public CheckBox checkBox;
    }




}
