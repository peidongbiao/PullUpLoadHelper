package com.pei.pulluploadhelper;

import android.view.View;

import androidx.annotation.IntDef;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public interface BiDirectionalLoad extends PullUpLoad {

    int DIRECTION_START = 1;
    int DIRECTION_END = 2;
    @IntDef({DIRECTION_START, DIRECTION_END})
    @Retention(RetentionPolicy.CLASS)
    @Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD, ElementType.LOCAL_VARIABLE})
    @interface Direction{}

    int STATE_START_LOADING = 5;
    int STATE_START_LOADED = 6;
    int STATE_START_COMPLETE = 7;

    @IntDef({STATE_EMPTY, STATE_START_LOADING, STATE_START_LOADED, STATE_START_COMPLETE, STATE_LOADING, STATE_LOADED, STATE_COMPLETE})
    @Retention(RetentionPolicy.CLASS)
    @Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD, ElementType.LOCAL_VARIABLE})
    @interface State{}

    <T extends View & LoadingIndicator> void setStartIndicator(T loadFooter);

    void setEmpty();

    void setLoading(@Direction int direction);

    void setLoaded(@Direction int direction);

    void setComplete(@Direction int direction);

}
