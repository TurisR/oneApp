package com.example.music_app.View.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.music_app.R;
import com.example.music_app.mould.Model.bean.Song;

import java.util.List;

public class LocalMusicListAdapter extends BaseAdapter {

    private Context context;
    private List<Song> list;
    private AlertDialog alertDialog1; //信息框

    private int mSelect=-1;


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

        if(i==mSelect){
            holder.v_playing.setVisibility(View.VISIBLE);
        }else {
            holder.v_playing.setVisibility(View.INVISIBLE);
        }

        return view;
    }
    class ViewHolder{
        TextView song;//歌曲名
        TextView singer;//歌手
        View v_playing;
        ImageView iv_more;
    }

    public void changeSelected(int positon){ //刷新方法
        if(positon != mSelect){
            mSelect = positon;
            notifyDataSetChanged();
        }
    }


    public void showList(View view,int position){
        final String[] items = {"列表1", "列表2", "列表3", "列表4"};
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setTitle(list.get(position).getTitle());
        alertBuilder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(context, items[i], Toast.LENGTH_SHORT).show();
                //alertDialog1.dismiss();
            }
        });
        alertDialog1 = alertBuilder.create();
        alertDialog1.show();
    }


}
