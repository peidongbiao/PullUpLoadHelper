package com.pei.pulluploadhelper;

import android.view.View;

import androidx.annotation.IntDef;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by peidongbiao on 2017/9/1.
 */

public interface PullUpLoad {

    int STATE_EMPTY = 0;
    int STATE_LOADING = 1;
    int STATE_LOADED = 2;
    int STATE_COMPLETE = 3;

    @IntDef({STATE_EMPTY, STATE_LOADING, STATE_LOADED, STATE_COMPLETE})
    @Retention(RetentionPolicy.CLASS)
    @Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD, ElementType.LOCAL_VARIABLE})
    @interface State{}

    <T extends View & LoadingIndicator> void setLoadFooter(T loadFooter);

    void setEmpty();

    void setLoading();

    void setLoaded();

    void setComplete();

    void setOnLoadListener(OnPullUpLoadListener onLoadListener);

    interface OnPullUpLoadListener {
        void onLoad();
    }
}