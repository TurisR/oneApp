package com.example.music_app.View.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.music_app.R;

/**
 * @description:自定义控件
 * @author:JiangJiaHui
 * @createDate: 2019/11/8
 * @Modified By：
 * @version: 1.0
 */
public class widgetLayout extends LinearLayout {
    private  RelativeLayout set_more;
    private  TextView set_num;
    private  LinearLayout set_layout;
    private  Context mContext;
    private  View mView;
    private  TextView set_name;
    private  TextView set_more_text;
    private  ImageView set_image;


    private  ListView set_list;
    private String setTitle;


    public widgetLayout(Context context) {
        super(context);
    }

    public widgetLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mContext = context;
        //LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      //  inflater.inflate(R.layout.widget_setlayout, this, true);

        mView= LayoutInflater.from(context).inflate(R.layout.widget_setlayout,this);
        set_name = (TextView) mView.findViewById(R.id.set_name);
        set_num = (TextView) mView.findViewById(R.id.set_num);
        set_more_text = (TextView) mView.findViewById(R.id.set_more_text);
        set_image = (ImageView) mView.findViewById(R.id.set_image);
        set_list = (ListView) mView.findViewById(R.id.set_list);
        set_layout = (LinearLayout) mView.findViewById(R.id.set_layout);
        set_more = (RelativeLayout) mView.findViewById(R.id.set_more);

        TypedArray at=context.obtainStyledAttributes(attrs,R.styleable.CustomTextView);
        at.recycle();

    }


    public widgetLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }


    public void setTitle(String title){
        set_name.setText(title);
    }
    public void setNum(String str){
        set_num.setText(str);
    }
    public void setNumVisible(boolean bl){
        if(bl){
            set_num.setVisibility(INVISIBLE);
        }else {
            set_num.setVisibility(VISIBLE);
        }
    }

    public void setMoreVisible(boolean bl){
        if(bl){
            set_more.setVisibility(INVISIBLE);
        }else {
            set_more.setVisibility(GONE);
        }
    }

    public void setVisible(boolean bl){
        if(bl){
            set_list.setVisibility(VISIBLE);
            set_more.setVisibility(VISIBLE);
            set_image.animate().rotation(45).setDuration(200).start();
        }else{
            set_list.setVisibility(GONE);
            set_more.setVisibility(GONE);
            set_image.animate().rotation(-45).setDuration(200).start();
        }
    }





    public void setListVisible(boolean bl){
        if(bl){
            set_list.setVisibility(GONE);
        }else {
            set_list.setVisibility(VISIBLE);
        }
    }



    public void setMoreText(String str){
        set_more_text.setText(str);
    }

    public void setListView(ListAdapter mada){
        set_list.setAdapter(mada);
    }
    public boolean getVisible() {
        if(set_more.getVisibility()==VISIBLE){
            return true;
        }
        return false;
    }

    public void setOnWidgetListener(OnClickListener listener){
        set_layout.setOnClickListener(listener);

    }
    public void setListListener( AdapterView.OnItemClickListener lt){
        set_list.setOnItemClickListener(lt);
    }

    public ListView getListView(){
        return set_list;
    }


    public void setMoreListener(OnClickListener listener){
        set_more.setOnClickListener(listener);
    }



}
