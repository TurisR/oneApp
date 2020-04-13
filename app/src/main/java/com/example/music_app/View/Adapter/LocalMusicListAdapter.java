package com.example.music_app.View.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.music_app.Presenter.AppConstant;
import com.example.music_app.R;
import com.example.music_app.View.Activity.AddListActivity;
import com.example.music_app.View.widget.CustomDialog;
import com.example.music_app.mould.Model.bean.Song;

import java.util.List;
/**
 * @description:本地歌曲的listAdapter
 * @author:JiangJiaHui
 * @createDate: 2019/11/10
 * @Modified By：
 * @version: 1.0
 */

public class LocalMusicListAdapter extends BaseAdapter {

    private Context context;
    private List<Song> list;
    private AlertDialog alertDialog1; //信息框
    public LocalMusicListAdapter(Context context, List<Song> list) {
        this.context = context;
        this.list = list;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null) {
            holder = new ViewHolder();
            //引入布局
            view = View.inflate(context, R.layout.item_music_list, null);
            //实例化对象
            holder.song = (TextView) view.findViewById(R.id.tv_title);
            holder.singer = (TextView) view.findViewById(R.id.tv_artist);
            holder.v_playing=(View)view.findViewById(R.id.v_playing);
            holder.iv_more=(ImageView) view.findViewById(R.id.iv_more);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        //给控件赋值
        holder.song.setText(list.get(i).getTitle().toString());
        holder.singer.setText(list.get(i).getSinger().toString());
        holder.iv_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showList(v,i);
            }
        });

        if(AppConstant.getInstance().getPlayingSong()!=null){
            if(AppConstant.getInstance().getPlayingSong().Equals(list.get(i))){
                holder.v_playing.setVisibility(View.VISIBLE);
                AppConstant.getInstance().setPlayingSong(list.get(i));
                holder.song.setTextColor(context.getResources().getColor(R.color.blue) );
                holder.singer.setTextColor(context.getResources().getColor(R.color.blue));

            }else{
                holder.v_playing.setVisibility(View.INVISIBLE);
                holder.song.setTextColor(context.getResources().getColor(R.color.black) );
                holder.singer.setTextColor(context.getResources().getColor(R.color.grey));
            }
        }


        return view;
    }
    class ViewHolder{
        TextView song;//歌曲名
        TextView singer;//歌手
        View v_playing;
        ImageView iv_more;
    }



    public void showList(View view, final int position){
        final String[] items = {"收藏", "删除", "加到歌单", "添加到播放列表"};
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setTitle(list.get(position).getTitle());
        final boolean bl=AppConstant.getInstance().isExist(AppConstant.DataType.PERSONAL_COLLECT,list.get(position));
        if(bl){
            items[0]="取消收藏";
        }
        alertBuilder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i){
                    case 0:
                        if(bl){
                            AppConstant.getInstance().removeListSongByIndex(AppConstant.DataType.PERSONAL_COLLECT,position);
                            Toast.makeText(context, "取消收藏", Toast.LENGTH_LONG).show();
                        }else {
                            AppConstant.getInstance().addListSongByIndex(AppConstant.DataType.PERSONAL_COLLECT,position);
                            Toast.makeText(context, "收藏成功", Toast.LENGTH_LONG).show();
                        }
                        break;
                    case 1:
                        if(AppConstant.getInstance().getPlayingSong()!=null&&AppConstant.getInstance().getPlayingSong().Equals(list.get(position))){
                            Toast.makeText(context, "歌曲正在播放中 暂时不能删除", Toast.LENGTH_LONG).show();
                        }else {
                            CustomDialog customDialog=new CustomDialog(context,R.style.CustomDialog);
                            customDialog.setType(0).setTitle("删 除").setContent("确认要删除“"+list.get(position).getTitle()+"”歌曲吗？").setCancel(new CustomDialog.InOnCancelListener() {
                                @Override
                                public void onCancel(CustomDialog customDialog) {
                                    customDialog.dismiss();
                                }
                            }).setConfirm(new CustomDialog.InOnConfirmListener() {
                                @Override
                                public void onConfirm(CustomDialog customDialog) {
                                    AppConstant.getInstance().removeLocalList(position);
                                    list.remove(position);
                                    notifyDataSetChanged();
                                    Toast.makeText(context, "删除成功", Toast.LENGTH_LONG).show();
                                    customDialog.dismiss();
                                }
                            }).show();
                        }

                        break;
                    case 2:
                        Intent intent =new Intent(context, AddListActivity.class);
                        intent.putExtra("ADD_TYPE",AppConstant.DataType.PERSONAL_ALBUM_NAME); // 传字符串, 更多传值方法
                        intent.putExtra("ALBUM_SONG",list.get(position));
                        context.startActivity(intent);
                         //overridePendingTransition(R.anim.push_bottom_in,R.anim.push_bottom_out);
                        break;
                    case 3:
                        if(AppConstant.getInstance().addCurrentSongList(list.get(position))){
                            Toast.makeText(context, "添加成功", Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(context, "已经添加", Toast.LENGTH_LONG).show();
                        }
                        break;
                }
            }
        });
        alertDialog1 = alertBuilder.create();
        alertDialog1.show();
    }


}
