package com.pei.pulluploadhelper;

import android.view.View;

/**
 * Created by peidongbiao on 2017/9/1.
 */

public interface PullUpLoad {
    <T extends View & PullUpLoadFooter> void setLoadFooter(T loadFooter);

    void setEmpty();

    void setLoading();

    void setLoaded();

    void setComplete();

    void setOnLoadListener(OnPullUpLoadListener onLoadListener);

    interface OnPullUpLoadListener {
        void onLoad();
    }

    /**
     * 列表状态
     */
    enum State{
        EMPTY,
        LOADING,
        LOADED,
        COMPLETE
    }
}