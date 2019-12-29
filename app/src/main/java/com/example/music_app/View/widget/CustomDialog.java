package com.example.music_app.View.widget;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.music_app.R;

public class CustomDialog extends Dialog implements View.OnClickListener{
    private TextView  mTitle,mContent,mCancel,mConfirm;
    private EditText mEditText;
    private String title,content;

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public int getType() {
        return type;
    }

    public String getEditText() {
        return mEditText.getText().toString();
    }

    private int type;
    public InOnCancelListener mInOnCancelListener;
    public InOnConfirmListener mInOnConfirmListener;

    public CustomDialog setTitle(String string1) {
        title = string1;
        return this;
    }

    public CustomDialog setContent(String string2) {
        content = string2;
        return this;
    }

    public CustomDialog setType(int type) {
        this.type = type;
        return this;
    }


    public CustomDialog setCancel(InOnCancelListener listener) {
        mInOnCancelListener=listener;
        return this;
    }

    public CustomDialog setConfirm(InOnConfirmListener listener) {
        mInOnConfirmListener=listener;
        return this;
    }

    public CustomDialog(Context context, int themeResId) {
        super(context, themeResId);



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_custom_dialog);
        WindowManager m=getWindow().getWindowManager();
        Display d=m.getDefaultDisplay();
        WindowManager.LayoutParams p=getWindow().getAttributes();
        Point size=new Point();
        d.getSize(size);
        p.width=(int)(size.x*0.9);
        getWindow().setAttributes(p);
        initView();
        iniEvent();
    }

    private void iniEvent() {
        mCancel.setOnClickListener(this);
        mConfirm.setOnClickListener(this);
        if(!TextUtils.isEmpty(title)){
            mTitle.setText(title);
        }
        if(type==1){
            mContent.setVisibility(View.GONE);
        }
        if(!TextUtils.isEmpty(content)&&type==0){
            mContent.setText(content);
            mEditText.setVisibility(View.GONE);
        }
    }

    private void initView() {
        mTitle=findViewById(R.id.dialog_title);
        mContent=findViewById(R.id.dialog_content);
        mEditText=findViewById(R.id.dialog_edit);
        mCancel= findViewById(R.id.dialog_cancel);
        mConfirm=findViewById(R.id.dialog_confirm);
    }

    public CustomDialog(Context context) {
        super(context);
        setContentView(R.layout.layout_custom_dialog);
        WindowManager m=getWindow().getWindowManager();
        Display d=m.getDefaultDisplay();
        WindowManager.LayoutParams p=getWindow().getAttributes();
        Point size=new Point();
        d.getSize(size);
        p.width=(int)(size.x*0.8);
        getWindow().setAttributes(p);
        initView();
        iniEvent();
    }

    @Override
    public void onClick(View v) {
           switch (v.getId()){
               case R.id.dialog_cancel:
                   mInOnCancelListener.onCancel(this);
                   break;
               case R.id.dialog_confirm:
                   mInOnConfirmListener.onConfirm(this);
                   break;
           }
    }


    public interface InOnCancelListener{
        void onCancel(CustomDialog customDialog);
    }

    public interface InOnConfirmListener{
        void onConfirm(CustomDialog customDialog);
    }

}
