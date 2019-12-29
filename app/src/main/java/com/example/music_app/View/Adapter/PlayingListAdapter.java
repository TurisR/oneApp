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
import com.example.music_app.View.widget.CustomDialog;
import com.example.music_app.mould.Model.SQLite.DBHelper;
import com.example.music_app.mould.Model.bean.Song;

import java.util.ArrayList;
import java.util.List;

public class PlayingListAdapter extends BaseAdapter implements View.OnClickListener{

    private final Context mContext;
    private List<Song> mSongList=new ArrayList<>();
    private List<String> albumName=new ArrayList<>();
    private ViewHolder holder;
    private boolean isTitle=false;
    public InOnDeleteListener mInOnDeleteListener;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
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

        if(!isTitle){
            holder.mTitle.setText(mSongList.get(position).getTitle());
        }else {
            holder.mTitle.setText(albumName.get(position));
        }
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String string;
                if(isTitle){
                    string="确认要删除“"+albumName.get(position)+"”歌单吗？";;
                }else {
                    string="确认要删除“"+mSongList.get(position).getTitle()+"”歌曲吗？";
                }
                CustomDialog customDialog=new CustomDialog(mContext,R.style.CustomDialog);
                customDialog.setType(0).setTitle("删 除").setContent(string).setCancel(new CustomDialog.InOnCancelListener() {
                    @Override
                    public void onCancel(CustomDialog customDialog) {
                        customDialog.dismiss();
                    }
                }).setConfirm(new CustomDialog.InOnConfirmListener() {
                    @Override
                    public void onConfirm(CustomDialog customDialog) {
                        if(isTitle){
                            DBHelper helper=new DBHelper(mContext);
                            helper.deleteTable(albumName.get(position));
                            if(!helper.tableIsExist(albumName.get(position))){
                                Toast.makeText(mContext, "删除成功", Toast.LENGTH_LONG).show();
                                albumName.remove(position);
                            }else {
                                Toast.makeText(mContext, "删除失败", Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Song song=mSongList.get(position);
                            mSongList.remove(position);
                            if(AppConstant.getInstance().isExist(song,mSongList)){
                                Toast.makeText(mContext, "删除失败", Toast.LENGTH_LONG).show();
                            }else {
                                Toast.makeText(mContext, "删除成功", Toast.LENGTH_LONG).show();
                            }
                        }
                        notifyDataSetChanged();
                        customDialog.dismiss();

                    }
                }).show();
            }
        });
        return convertView;
    }



    //删除元素,需要告诉UI线程布局的变动
    public void remove(int position) {
        if(isTitle){
            albumName.remove(position);
        }else {
            mSongList.remove(position);
        }
        notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {

    }


    public class ViewHolder{
        public TextView mTitle;
        public CheckBox checkBox;
        public ImageView delete;
    }

   public interface InOnDeleteListener{
        void delete(int i);
   };


    public void setInOnDeleteListener(InOnDeleteListener listener){
        mInOnDeleteListener=listener;
    }





}
