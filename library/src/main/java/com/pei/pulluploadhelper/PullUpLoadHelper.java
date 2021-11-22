package com.pei.pulluploadhelper;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * RecyclerView 可以加载更多的helper。在设置完Adapter和LayoutManager后使用，只支持LinearLayoutManger和GridLayoutManager
 * Created by peidongbiao on 2017/9/5.
 */
public class PullUpLoadHelper extends RecyclerView.OnScrollListener implements PullUpLoad {

    protected Context mContext;

    protected RecyclerView mRecyclerView;

    protected LoadingIndicator mEndIndicator;

    protected @PullUpLoad.State int mEndLoadingState = STATE_INIT;

    protected HeaderFooterRecyclerAdapterWrapper mHeaderFooterRecyclerAdapterWrapper;

    protected LinearLayoutManager mLayoutManager;

    protected OnPullUpLoadListener mOnPullUpLoadListener;

    protected int mTotalItemCount;

    protected int mLastVisibleItem;

    public PullUpLoadHelper(RecyclerView recyclerView, OnPullUpLoadListener onPullUpLoadListener) {
        if (recyclerView == null) {
            throw new NullPointerException("RecyclerView is null!");
        }
        if (recyclerView.getAdapter() == null) {
            throw new NullPointerException("RecyclerView's adapter is null!");
        }
        if (recyclerView.getLayoutManager() == null) {
            throw new NullPointerException("RecyclerView's layoutManager is null!");
        }
        if (!(recyclerView.getLayoutManager() instanceof LinearLayoutManager)) {
            throw new IllegalArgumentException("Only supports linearLayoutManager!");
        }
        if (onPullUpLoadListener == null) {
            throw new NullPointerException("OnPullUpLoadListener is null!");
        }

        mContext = recyclerView.getContext();
        mRecyclerView = recyclerView;
        mHeaderFooterRecyclerAdapterWrapper = wrapAdapter(recyclerView.getAdapter());
        mRecyclerView.setAdapter(mHeaderFooterRecyclerAdapterWrapper);
        mLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        mOnPullUpLoadListener = onPullUpLoadListener;

        mEndLoadingState = recyclerView.getAdapter().getItemCount() == 0 ? PullUpLoad.STATE_EMPTY : PullUpLoad.STATE_LOADED;
        //默认footer
        this.setEndIndicator(new DefaultLoadingIndicator(mContext));
        mRecyclerView.addOnScrollListener(this);
    }

    @Override
    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
        mLastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
        mTotalItemCount = mLayoutManager.getItemCount();
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            if (isReachToEnd()) {
                onScrollToEnd();
            }
        }
    }

    protected boolean isReachToEnd() {
        return mLastVisibleItem == mTotalItemCount - 1 && mEndLoadingState != PullUpLoad.STATE_LOADING && mEndLoadingState != PullUpLoad.STATE_COMPLETE && mEndLoadingState != PullUpLoad.STATE_EMPTY;
    }

    protected void onScrollToEnd() {
        setLoading();
        mOnPullUpLoadListener.onLoad();
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        mLastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
        mTotalItemCount = mLayoutManager.getItemCount();
    }


    @Override
    public <T extends View & LoadingIndicator> void setEndIndicator(T endIndicator) {
        if (endIndicator == null) {
            throw new NullPointerException("endIndicator is null");
        }
        if (mEndIndicator != null) {
            mHeaderFooterRecyclerAdapterWrapper.removeFooterView((View) mEndIndicator);
        }
        this.mEndIndicator = endIndicator;
        mHeaderFooterRecyclerAdapterWrapper.addFootView((View) mEndIndicator);
    }

    @Override
    public void setEmpty() {
        mEndLoadingState = PullUpLoad.STATE_EMPTY;
        mEndIndicator.setEmpty();
    }

    @Override
    public void setLoading() {
        mEndLoadingState = PullUpLoad.STATE_LOADING;
        mEndIndicator.setLoading();
    }

    @Override
    public void setLoaded() {
        mEndLoadingState = PullUpLoad.STATE_LOADED;
        mEndIndicator.setLoaded();
    }

    @Override
    public void setComplete() {
        mEndLoadingState = PullUpLoad.STATE_COMPLETE;
        mEndIndicator.setComplete();
    }

    public int getHeaderCount() {
        return mHeaderFooterRecyclerAdapterWrapper.getHeaderCount();
    }

    public int getFooterCount() {
        return mHeaderFooterRecyclerAdapterWrapper.getFooterCount();
    }

    private HeaderFooterRecyclerAdapterWrapper wrapAdapter(RecyclerView.Adapter<?> adapter) {
        HeaderFooterRecyclerAdapterWrapper adapterWrapper;
        if (adapter instanceof HeaderFooterRecyclerAdapterWrapper) {
            adapterWrapper = (HeaderFooterRecyclerAdapterWrapper) adapter;
        } else {
            adapterWrapper = new HeaderFooterRecyclerAdapterWrapper(adapter);
        }
        return adapterWrapper;
    }

    @Override
    public void setOnLoadListener(OnPullUpLoadListener onLoadListener) {
    }
}