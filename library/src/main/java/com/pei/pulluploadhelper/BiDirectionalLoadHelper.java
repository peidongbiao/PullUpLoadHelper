package com.pei.pulluploadhelper;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BiDirectionalLoadHelper extends PullUpLoadHelper implements BiDirectionalLoad {

    private OnPullDownLoadListener mOnPullDownLoadListener;
    private LoadingIndicator mStartIndicator;

    private int mStartLoadingState;
    private int mFirstVisibleItem;

    public BiDirectionalLoadHelper(RecyclerView recyclerView, OnPullUpLoadListener onPullUpLoadListener, OnPullDownLoadListener onPullDownLoadListener) {
        super(recyclerView, onPullUpLoadListener);
        this.mOnPullDownLoadListener = onPullDownLoadListener;

        setStartIndicator(new DefaultLoadingIndicator(mContext));
        mRecyclerView.scrollToPosition(mHeaderFooterRecyclerAdapterWrapper.getHeaderCount());
    }

    @Override
    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        mFirstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            if (isReachToTop()) {
                onScrollToTop();
            }
        }
    }

    protected boolean isReachToTop() {
        return mFirstVisibleItem == 0 && mStartLoadingState != BiDirectionalLoad.STATE_START_LOADING && mStartLoadingState != BiDirectionalLoad.STATE_START_COMPLETE && mStartLoadingState != PullUpLoad.STATE_EMPTY;
    }

    protected void onScrollToTop() {
        setLoading(DIRECTION_START);
        mOnPullDownLoadListener.onLoad();
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        mFirstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();
    }


    @Override
    public <T extends View & LoadingIndicator> void setStartIndicator(T startIndicator) {
        if (startIndicator == null) {
            throw new NullPointerException("endIndicator is null");
        }
        if (mStartIndicator != null) {
            mHeaderFooterRecyclerAdapterWrapper.removeHeaderView((View) mEndIndicator);
        }
        this.mStartIndicator = startIndicator;
        mHeaderFooterRecyclerAdapterWrapper.addHeaderView((View) mStartIndicator);
    }

    @Override
    public void setEmpty() {
        super.setEmpty();
        mStartLoadingState = STATE_EMPTY;
    }

    @Override
    public void setLoading() {
        setLoading(DIRECTION_END);
    }

    @Override
    public void setLoaded() {
        setLoaded(DIRECTION_END);
    }

    @Override
    public void setComplete() {
        setComplete(DIRECTION_END);
    }

    @Override
    public void setLoading(@Direction int direction) {
        if (direction == DIRECTION_START) {
            mStartLoadingState = STATE_START_LOADING;
            mStartIndicator.setLoading();
        } else {
            super.setLoading();
        }
    }

    @Override
    public void setLoaded(@Direction int direction) {
        if (direction == DIRECTION_START) {
            mStartLoadingState = STATE_START_LOADED;
            mStartIndicator.setLoaded();
        } else {
            super.setLoaded();
        }
    }

    @Override
    public void setComplete(@Direction int direction) {
        if (direction == DIRECTION_START) {
            mStartLoadingState = STATE_START_COMPLETE;
            mStartIndicator.setComplete();
        } else {
            super.setComplete();
        }
    }
}
