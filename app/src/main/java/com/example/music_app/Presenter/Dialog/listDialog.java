package com.example.music_app.Presenter.Dialog;

import android.app.AlertDialog;
import android.content.Context;

public class listDialog extends AlertDialog {
    protected listDialog(Context context) {
        super(context);

    }

    protected listDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    protected listDialog(Context context, int themeResId) {
        super(context, themeResId);
    }



}
