package com.example.music_app.View.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.music_app.Presenter.AppConstant;
import com.example.music_app.R;
import com.example.music_app.View.widget.CustomDialog;
import com.example.music_app.mould.Model.bean.Song;

import java.util.ArrayList;
import java.util.List;

public class ShowListAdapter extends BaseAdapter implements View.OnClickListener{

    private String Name;
    private Context mContext;
    private List<Song> mSongList=new ArrayList<>();
    private List<String> albumName=new ArrayList<>();
    private ViewHolder holder;
    private boolean isTitle=false;
    public InOnDeleteListener mInOnDeleteListener;


    public ShowListAdapter(Context context,String name) {
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
            case AppConstant.DataType.CURRENT_MUSIC:
                mSongList.addAll(AppConstant.getInstance().getCurrentSongList());
                break;
            default:
                mSongList.addAll(AppConstant.getInstance().getList(name));
                break;
        }
        Log.e("name",name);
    }

    public ShowListAdapter(Context context,List<Song> songList){
        mContext=context;
        //isShowCheckBox=isSelectedAll;
        if(!mSongList.isEmpty()){
            mSongList.clear();
        }
        Name=AppConstant.DataType.CURRENT_MUSIC;
        mSongList.addAll(songList);
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.item_show_list, null);
            convertView.setTag(holder);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);
            holder.mTitle = convertView.findViewById(R.id.play_name);
            holder.delete=convertView.findViewById(R.id.delete);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if(!Name.equals(AppConstant.DataType.RECENT_SEARCH)){
            if (!isTitle&&mSongList!=null&&AppConstant.getInstance().getPlayingSong()!=null){
                if(AppConstant.getInstance().getPlayingSong().Equals(mSongList.get(position))){
                    holder.mTitle.setTextColor(mContext.getResources().getColor(R.color.blue));
                    holder.mTitle.setSelected(true);
                    holder.delete.setVisibility(View.INVISIBLE);
                }else{
                    holder.mTitle.setTextColor(mContext.getResources().getColor(R.color.black));
                    holder.mTitle.setSelected(false);
                    holder.delete.setVisibility(View.VISIBLE);
                }
            }
        }


        if(!isTitle){
            holder.mTitle.setText(mSongList.get(position).getTitle());
        }else {
            holder.mTitle.setText(albumName.get(position));
        }

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String string;
                if(isTitle){
                    string="确认要删除“"+albumName.get(position)+"”歌单吗？";;
                }else {
                    string="确认要移除“"+mSongList.get(position).getTitle()+"”歌曲吗？";
                }
                if(!Name.equals(AppConstant.DataType.CURRENT_MUSIC)&&!Name.equals(AppConstant.DataType.RECENT_SEARCH)){
                    CustomDialog customDialog=new CustomDialog(mContext,R.style.CustomDialog);
                    customDialog.setType(0).setTitle("移 除").setContent(string).setCancel(new CustomDialog.InOnCancelListener() {
                        @Override
                        public void onCancel(CustomDialog customDialog) {
                            customDialog.dismiss();
                        }
                    }).setConfirm(new CustomDialog.InOnConfirmListener() {
                        @Override
                        public void onConfirm(CustomDialog customDialog) {
                            if(isTitle){
                                //AppConstant.getInstance().removeListSong(AppConstant.DataType.PERSONAL_ALBUM_NAME,mSongList.get(position));
                                AppConstant.getInstance().removeListName(albumName.get(position));
                                albumName.remove(position);
                                Toast.makeText(mContext, "删除成功", Toast.LENGTH_LONG).show();
                            }else{
                                AppConstant.getInstance().removeListSongByIndex(Name,position);
                                mSongList.remove(position);
                                Toast.makeText(mContext, "移除成功", Toast.LENGTH_LONG).show();
                            }
                            notifyDataSetChanged();
                            customDialog.dismiss();
                        }
                    }).show();
                }else {
                    AppConstant.getInstance().removeCurrentSong(position);
                    mSongList.remove(position);
                    notifyDataSetChanged();
                }
            }
        });
        return convertView;
    }


    @Override
    public void onClick(View v) {

    }


    public class ViewHolder{
        public TextView mTitle;
        public CheckBox checkBox;
        public LinearLayout delete;
    }

   public interface InOnDeleteListener{
        void delete(int i);
   };





}
