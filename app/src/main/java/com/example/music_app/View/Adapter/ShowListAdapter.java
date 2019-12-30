package com.example.music_app.View.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.music_app.Presenter.AppConstant;
import com.example.music_app.Presenter.DataManager;
import com.example.music_app.R;
import com.example.music_app.View.widget.CustomDialog;
import com.example.music_app.mould.Model.Model;
import com.example.music_app.mould.Model.SQLite.DBHelper;
import com.example.music_app.mould.Model.bean.Song;

import java.util.ArrayList;
import java.util.List;

public class ShowListAdapter extends BaseAdapter implements View.OnClickListener{

    private final String Name;
    private final Context mContext;
    private List<Song> mSongList=new ArrayList<>();
    private List<String> albumName=new ArrayList<>();
    private ViewHolder holder;
    private boolean isTitle=false;
    public InOnDeleteListener mInOnDeleteListener;


    public ShowListAdapter(Context context, String name) {
        mContext=context;
        //isShowCheckBox=isSelectedAll;
        if(!mSongList.isEmpty()){
            mSongList.clear();
        }
        Name=name;

        switch (name){
            case AppConstant.DataType.CURRENT_MUSIC:
                mSongList.addAll(AppConstant.getInstance().getCurrentSongList());
                break;
            case AppConstant.DataType.RECENT_MUSIC:
                mSongList.addAll(AppConstant.getInstance().getRecentSongList());
                break;
            case AppConstant.DataType.PERSONAL_COLLECT:
                mSongList.addAll(AppConstant.getInstance().getPersonCollectSongList());
                break;

            case AppConstant.DataType.PERSONAL_ALBUM_NAME:
                isTitle=true;
                albumName.addAll(AppConstant.getInstance().getPersonalAlbumName());
                break;

            default:
                DataManager dataManager=new DataManager(context);
                mSongList.addAll(dataManager.getPersonalAlbumSong(name));
                break;
        }




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
                final String string;
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
                            String name=albumName.get(position);
                            DataManager manager=new DataManager(mContext);
                            DBHelper helper= Model.getInstance().getDBManager().getHelper();
                            helper.deleteTable(name);
                            if(!helper.tableIsExist(name)){
                                Toast.makeText(mContext, "删除成功", Toast.LENGTH_LONG).show();
                                AppConstant.getInstance().removeSong(position,Name);
                                albumName.remove(position);
                            }else {
                                Toast.makeText(mContext, "删除失败", Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Song song=mSongList.get(position);
                            if(AppConstant.getInstance().removeSong(position,Name)){
                                Toast.makeText(mContext, "删除成功", Toast.LENGTH_LONG).show();
                                mSongList.remove(position);
                            }else {
                                Toast.makeText(mContext, "删除失败", Toast.LENGTH_LONG).show();
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


    public void setInOnDeleteListener(InOnDeleteListener listener){
        mInOnDeleteListener=listener;
    }





}
