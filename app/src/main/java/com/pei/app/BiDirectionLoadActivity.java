package com.pei.app;

import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pei.pulluploadhelper.BiDirectionalLoadHelper;
import com.pei.pulluploadhelper.PullUpLoad;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class BiDirectionLoadActivity extends AppCompatActivity {
    private static final int PAGE_SIZE = 30;

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;

    private Handler mHandler;
    private Adapter mAdapter;
    private BiDirectionalLoadHelper mBiDirectionalLoadHelper;

    private int mPrePage;
    private int mNextPage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bidirectional_load);

        mRecyclerView = findViewById(R.id.recycler_view);

        mHandler = new Handler();
        mAdapter = new Adapter(this, getNextData(false, 0));
        mNextPage = 1;
        mAdapter.setHasStableIds(true);

        mLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        //mLinearLayoutManager.setStackFromEnd(true);

        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mBiDirectionalLoadHelper = new BiDirectionalLoadHelper(mRecyclerView, new PullUpLoad.OnLoadListener() {
            @Override
            public void onLoad() {
                loadNextPage();
            }
        }, new PullUpLoad.OnLoadListener() {
            @Override
            public void onLoad() {
                loadPrePage();
            }
        });
    }


    private void loadNextPage() {
        mBiDirectionalLoadHelper.setLoading();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                List<Data> data = getNextData(false, mNextPage);
                mNextPage++;
                mAdapter.addItems(data);

                if (mNextPage >= 5) {
                    mBiDirectionalLoadHelper.setComplete();
                } else {
                    mBiDirectionalLoadHelper.setLoaded();
                }
            }
        }, 1000);
    }


    private void loadPrePage() {
        mBiDirectionalLoadHelper.setLoading(BiDirectionalLoadHelper.DIRECTION_START);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                List<Data> data = getNextData(true, mPrePage);
                mPrePage++;

                mAdapter.addItems(0, data);
                //mLinearLayoutManager.scrollToPosition(1);
                mRecyclerView.scrollToPosition(mBiDirectionalLoadHelper.getHeaderCount() + data.size());

                if (mPrePage >= 5) {
                    mBiDirectionalLoadHelper.setComplete(BiDirectionalLoadHelper.DIRECTION_START);
                } else {
                    mBiDirectionalLoadHelper.setLoaded(BiDirectionalLoadHelper.DIRECTION_START);
                }
            }
        }, 1000);
    }

    private List<Data> getNextData(boolean top, int page) {
        List<Data> list = new ArrayList<>();
        for (int i = 0; i < PAGE_SIZE; i++) {
            int id = (top ? -1 : 1) * (i + page * PAGE_SIZE);
            Data data = new Data(id, String.format(Locale.getDefault(), (top ? "top " : "") + "Text: %d", id));
            list.add(data);
        }
        if (top) {
            Collections.reverse(list);
        }
        return list;
    }
}
