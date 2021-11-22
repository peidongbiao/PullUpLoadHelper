package com.pei.pulluploadhelper;

/**
 * Created by peidongbiao on 2017/9/1.
 */

public interface LoadingIndicator {

    void setEmpty();

    void setLoading();

    void setLoaded();

    void setComplete();
}