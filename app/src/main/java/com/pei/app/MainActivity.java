package com.pei.app;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pei.pulluploadhelper.PullUpLoad;
import com.pei.pulluploadhelper.PullUpLoadHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;

    private Handler mHandler;
    private Adapter mAdapter;
    private PullUpLoadHelper mPullUpLoadHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        mRecyclerView = findViewById(R.id.recycler_view);

        mHandler = new Handler();
        mAdapter = new Adapter(this);

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
                nextPage();
            }
        });

        refresh();
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
                List<String> data = getData();
                mAdapter.refresh(data);
                mSwipeRefreshLayout.setRefreshing(false);
                mPullUpLoadHelper.setLoaded();
            }
        },1000);
    }

    private void nextPage(){
        mPullUpLoadHelper.setLoading();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                List<String> data = getData();
                mAdapter.addItems(data);
                mPullUpLoadHelper.setLoaded();
            }
        },1000);
    }

    private List<String> getData(){
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            list.add(String.format(Locale.getDefault(),"Text: %d",i));
        }
        return list;
    }

    static class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        private Context mContext;
        private List<String> mData;

        public Adapter(Context context) {
            this(context,null);
        }

        public Adapter(Context context, List<String> data) {
            mContext = context;
            mData = data == null ? new ArrayList<String>() : data;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_recycler_view,parent,false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ViewHolder viewHolder = (ViewHolder) holder;
            String str = mData.get(position);
            viewHolder.mTextView.setText(str);
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        public void refresh(List<String> data){
            this.mData.clear();
            this.mData.addAll(data);
            this.notifyDataSetChanged();
        }

        private void addItems(List<String> data){
            int start = mData.size();
            mData.addAll(data);
            this.notifyItemRangeInserted(start,data.size());
        }

        class ViewHolder extends RecyclerView.ViewHolder{
            private TextView mTextView;
            public ViewHolder(View itemView) {
                super(itemView);
                mTextView = itemView.findViewById(R.id.text);
            }
        }
    }
}
