package com.android.mylibrary.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.wangjj.R;


public class LoadView extends RelativeLayout {

    private TextView cancel;

    public LoadView(Context context) {
        super(context);
        init(context);
    }

    public LoadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LoadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_loading, this);
    }

    public TextView getCancel() {
        return cancel;
    }

    public void setCancel(TextView cancel) {
        this.cancel = cancel;
    }
}
