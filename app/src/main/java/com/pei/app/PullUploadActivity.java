package com.pei.app;

import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.pei.pulluploadhelper.PullUpLoad;
import com.pei.pulluploadhelper.PullUpLoadHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class PullUploadActivity extends AppCompatActivity {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;

    private Handler mHandler;
    private Adapter mAdapter;
    private PullUpLoadHelper mPullUpLoadHelper;
    private int mPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pullup_load);
        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        mRecyclerView = findViewById(R.id.recycler_view);

        mHandler = new Handler();
        mAdapter = new Adapter(this,getData());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });

        mPullUpLoadHelper = new PullUpLoadHelper(mRecyclerView, new PullUpLoad.OnPullUpLoadListener() {
            @Override
            public void onLoad() {
                getNextPage();
            }
        });

        //refresh();
    }

    private void refresh(){
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
            }
        });
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPage = 1;
                List<Data> data = getData();
                mAdapter.refresh(data);
                mSwipeRefreshLayout.setRefreshing(false);
                mPullUpLoadHelper.setLoaded();
            }
        },1000);
    }

    private void getNextPage(){
        mPullUpLoadHelper.setLoading();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPage++;
                List<Data> data = getData();
                mAdapter.addItems(data);
                if(mPage == 5){
                    mPullUpLoadHelper.setComplete();
                }else {
                    mPullUpLoadHelper.setLoaded();
                }
            }
        },1000);
    }

    private List<Data> getData(){
        List<Data> list = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            Data data = new Data(0, String.format(Locale.getDefault(), "Text: %d", i));
            list.add(data);
        }
        return list;
    }
}
