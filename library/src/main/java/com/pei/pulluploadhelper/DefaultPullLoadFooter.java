package com.pei.pulluploadhelper;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;


/**默认加载更多footer实现
 * Created by peidongbiao on 2017/9/1.
 */

public class DefaultPullLoadFooter extends FrameLayout implements PullUpLoadFooter {

    protected View mView;
    protected ProgressBar mPbLoading;
    protected TextView mTvText;

    public DefaultPullLoadFooter(Context context) {
        this(context,null);
    }

    public DefaultPullLoadFooter(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public DefaultPullLoadFooter(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mView  = LayoutInflater.from(context).inflate(R.layout.defualt_pull_load_footer,this);
        mPbLoading = (ProgressBar) findViewById(R.id.around_pb_loading);
        mTvText = (TextView) findViewById(R.id.around_tv_text);
        //初始化时隐藏
        setVisibility(INVISIBLE);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if(layoutParams != null){
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            setLayoutParams(layoutParams);
        }
    }

    @Override
    public void setEmpty() {
        setVisibility(INVISIBLE);
    }

    @Override
    public void setLoading() {
        setVisibility(VISIBLE);
        mPbLoading.setVisibility(VISIBLE);
        mTvText.setText(R.string.loading);
    }

    @Override
    public void setLoaded() {
        setVisibility(VISIBLE);
        mPbLoading.setVisibility(GONE);
        mTvText.setText(R.string.loaded);
    }

    @Override
    public void setComplete() {
        setVisibility(VISIBLE);
        mPbLoading.setVisibility(GONE);
        mTvText.setText(R.string.no_more_data);
    }
}