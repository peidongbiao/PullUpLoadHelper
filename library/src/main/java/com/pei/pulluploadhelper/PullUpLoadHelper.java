package com.pei.pulluploadhelper;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**RecyclerView 可以加载更多的helper。在设置完Adapter和LayoutManager后使用，只支持LinearLayoutManger和GridLayoutManager
 * Created by peidongbiao on 2017/9/5.
 */

public class PullUpLoadHelper extends RecyclerView.OnScrollListener implements PullUpLoad{

    private Context mContext;
    private RecyclerView mRecyclerView;
    private PullUpLoadFooter mLoadFooter;
    private PullUpLoad.State mState;
    private HeaderFooterRecyclerAdapterWrapper mHeaderFooterRecyclerAdapterWrapper;
    private LinearLayoutManager mLayoutManager;
    private OnPullUpLoadListener mOnPullUpLoadListener;

    private int mLastVisibleItem;
    private int mTotalItemCount;

    public PullUpLoadHelper(RecyclerView recyclerView,OnPullUpLoadListener onPullUpLoadListener) {
        if(recyclerView == null){
            throw new NullPointerException("RecyclerView is null!");
        }
        if(recyclerView.getAdapter() == null){
            throw new NullPointerException("RecyclerView's adapter is null!");
        }
        if(recyclerView.getLayoutManager() == null){
            throw new NullPointerException("RecyclerView's layoutManager is null!");
        }
        if(!(recyclerView.getLayoutManager() instanceof LinearLayoutManager)){
            throw new IllegalArgumentException("Only supports linearLayoutManager!");
        }
        if(onPullUpLoadListener == null){
            throw new NullPointerException("OnPullUpLoadListener is null!");
        }

        mContext = recyclerView.getContext();
        mRecyclerView = recyclerView;
        mHeaderFooterRecyclerAdapterWrapper = wrapAdapter(recyclerView.getAdapter());
        mRecyclerView.setAdapter(mHeaderFooterRecyclerAdapterWrapper);
        mLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        mOnPullUpLoadListener = onPullUpLoadListener;

        mState = State.EMPTY;
        //默认footer
        this.setLoadFooter(new DefaultPullLoadFooter(mContext));
        mRecyclerView.addOnScrollListener(this);
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        if(newState == RecyclerView.SCROLL_STATE_IDLE){
            if(mLastVisibleItem == mTotalItemCount - 1 && mState != PullUpLoad.State.LOADING && mState != PullUpLoad.State.COMPLETE && mState != PullUpLoad.State.EMPTY){
                setLoading();
                mOnPullUpLoadListener.onLoad();
            }
        }
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        mLastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
        mTotalItemCount = mLayoutManager.getItemCount();
    }


    @Override
    public <T extends View & PullUpLoadFooter> void setLoadFooter(T loadFooter) {
        if(loadFooter == null){
            throw new NullPointerException("loadFooter is null");
        }
        if(mLoadFooter != null){
            mHeaderFooterRecyclerAdapterWrapper.removeFooterView((View) mLoadFooter);
        }
        this.mLoadFooter = loadFooter;
        mHeaderFooterRecyclerAdapterWrapper.addFootView((View)mLoadFooter);
    }

    @Override
    public void setEmpty() {
        mState = PullUpLoad.State.EMPTY;
        mLoadFooter.setEmpty();
    }

    @Override
    public void setLoading(){
        mState = PullUpLoad.State.LOADING;
        mLoadFooter.setLoading();
    }

    @Override
    public void setLoaded(){
        mState = PullUpLoad.State.LOADED;
        mLoadFooter.setLoaded();
    }

    @Override
    public void setComplete(){
        mState = PullUpLoad.State.COMPLETE;
        mLoadFooter.setComplete();
    }

    private HeaderFooterRecyclerAdapterWrapper wrapAdapter(RecyclerView.Adapter adapter){
        HeaderFooterRecyclerAdapterWrapper adapterWrapper;
        if(adapter instanceof HeaderFooterRecyclerAdapterWrapper){
            adapterWrapper = (HeaderFooterRecyclerAdapterWrapper) adapter;
        }else {
            adapterWrapper = new HeaderFooterRecyclerAdapterWrapper(adapter);
        }
        return adapterWrapper;
    }

    @Override
    public void setOnLoadListener(OnPullUpLoadListener onLoadListener) {}
}